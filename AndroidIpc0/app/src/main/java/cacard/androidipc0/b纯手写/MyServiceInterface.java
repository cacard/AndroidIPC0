package cacard.androidipc0.b纯手写;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by cunqingli on 2017/2/27.
 * <p>
 * 思路：
 * 1）定义Server/Client共用的契约（接口，包含方法调用，继承自IInterface）
 * 2）定义共用契约的操作。
 * 3）定义Stub。继承自Binder，实现了共用契约（重要的是这里的实现是指“数据操作”，而不是逻辑a+b！）。
 * ....重点：
 * ........继承自Binder，因为Stub是在Server端的的Binder真身；
 * ........implements自契约，但重点是不实现操作定义！因为这个Stub类是定义成abstract的。具体操作的实现，由Stub的实现类去完成！
 * ........由此，剩下两个重点了：asInterface()和onTransact()
 * ........总结一下：
 * ............a）是static abstract的，extends自Binder， implements自契约；
 * ............b）重点实现asInterface/onTransact()
 * ............c）Prox单说；
 * ............d）定义DESCRIPTOR
 * ............e)构造函数时，要调用attachInterface。
 * 4）在Stub中定义Proxy。（不继承自Binder），但实现了共用契约（而且接口具体的实现是对Parcel的操作！而不是a+b这样的逻辑。）。而且是private static的，表示只有Stub使用它！
 */

public interface MyServiceInterface extends IInterface {

    /**
     * ******************************************************************************
     * 接口
     */

    //定义操作
    public int operation(int a, int b);

    /**
     * *******************************************************************************
     * Stub
     */

    //运行在Server端的Binder实体类
    public static abstract class Stub extends Binder implements MyServiceInterface {

        // 描述符：契约包路径
        private static final String DESCRIPTOR = "cacard.androidipc0.MyServiceInterface";

        // 那就是自身了
        @Override
        public IBinder asBinder() {
            return this;
        }

        /**
         * 构造函数，这个也很重要。
         * 看看attachInterface的文档：
         * Convenience method for associating a specific interface with the Binder.
         * After calling, queryLocalInterface() will be implemented for you
         * to return the given owner IInterface when the corresponding
         * descriptor is requested.
         * 将一个契约与Binder实体相关联。
         */
        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /**
         * 重点1来了，定义asInterface。
         * 为啥要定义这个呢？这个接口的目的是啥？
         * 简单来说，就是拿到Binder绑定的契约；
         * 两种情况：
         * 1）如果是Server想拿到契约，那就是Server端的实现（就是Stub的实现类了）；
         * 2）如果是Client想拿到契约，那就是Client端的实现（就是Proxy了）；
         * <p>
         * todo obj是啥？
         */
        public static MyServiceInterface anInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }

            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof cacard.androidipc0.SimpleAidlCopy))) {
                return ((MyServiceInterface) iin);
            }

            return new Proxy(obj); //这个obj为啥传给Proxy，也值得思考。
        }

        private static final int TRANSCATION_Add = FIRST_CALL_TRANSACTION + 0; //按照First_Call位移

        /**
         * 重点2来了，定义onTranscate()
         * 首先是个各个参数的含义：
         * code:操作码；
         * data:接收大的数据；需要读取；
         * reply:响应的数据，学要写入；
         * flags:特殊标志位
         */
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            switch (code) {
                // 接收消息方的Descriptor
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true; //表示写入成功?
                }
                case TRANSCATION_Add: {
                    // todo 这个是做什么的？
                    data.enforceInterface(DESCRIPTOR);

                    // 读取
                    int a = data.readInt();
                    int b = data.readInt();
                    int result = this.operation(a, b); // operation()具体是哪里实现的呢？在契约中定义，在Binder实体中实现。

                    // 意思是走到这里没发生Exception
                    reply.writeNoException();

                    // 把计算结果写入响应数据中
                    reply.writeInt(result);

                    return true;
                }
            }

            return super.onTransact(code, data, reply, flags);
        }

        /**
         * ***************************************************************************
         * Proxy
         */

        private static class Proxy implements MyServiceInterface {

            private IBinder mRemote;

            // 构造函数，因为该类是代理，所以应该有个“代理谁”
            public Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            /**
             * 契约操作实现。
             * <p>
             * 虽然是契约操作的实现，但并不是逻辑实现。因为逻辑实现最终是在Binder实体中实现的。
             *
             * @param a
             * @param b
             * @return
             */
            @Override
            public int operation(int a, int b) {

                Parcel parcelData = null;
                Parcel parcelReply = null;
                int result;

                try {
                    parcelData = Parcel.obtain();
                    parcelReply = Parcel.obtain();

                    parcelData.writeInt(a);
                    parcelData.writeInt(b);

                    //让remote实体执行一个操作！
                    boolean bb = mRemote.transact(TRANSCATION_Add, parcelData, parcelReply, 0);

                    parcelReply.readException(); //如果远程发生了Exception，这里可以读取到并抛出。

                    //如果上面和远程没发生异常，可走到这里
                    result = parcelReply.readInt();
                } catch (Exception e) {
                    parcelData.recycle();
                    parcelReply.recycle();
                }

                return 0;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

        }
    }
}
