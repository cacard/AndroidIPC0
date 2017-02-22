package cacard.androidipc0;

/**
 * Created by cunqingli on 2017/2/21.
 * 这个文件是从自动生成的（SimpleAidl.java）那里拷贝而来，并且重命名为SimpleAidlCopy，这样就可以任意修改了。
 * -------------------------------------
 * 整体
 * - SimpleAidlCopy这个interface继承自IInterface，分为两部分：
 * 1) 操作定义；
 * 2) 一个static-member-class，Stub，继承自Binder，并且实现了SimpleAidlCopy接口。其实这个Stub完全可以拿到外面去。
 * -------------------------------------
 * 重要概念
 * 【IInterface】
 * - Binder-Interface（就是Server/Client都可以相互遵守的契约）
 * - IInterface只定义了一个操作asBinder，即：Binder-Interface转化成Binder，需要调用这个操作。而不是使用强制转换。
 * 【IBinder】
 * - 代表具有跨进程的能力。所有Binder实体必须实现IBinder。
 * 【Binder本地对象】
 * - 指Binder真身，仅在Server中有一个，其它地方有很多它的引用。
 * 【BinderProxy】
 * - 是对Binder真身的代理
 */
public interface SimpleAidlCopy extends android.os.IInterface {

    /**
     * 接口中的操作定义，供Server/Client共同使用。
     */
    public int add(int x, int y) throws android.os.RemoteException;

    /**
     * Local-side IPC implementation stub class.
     * ------------------------------------------
     * todo 分析一下Binder的注释
     * ------------------------------------------
     * 重要类型。Server的onBind时，直接返回的就是这个类的实现。
     * 继承自Binder，说明是一个【Binder本地对象】
     * Stub是Server端作为Binder真身的“存根”。通过它，能创建一个【Binder真身】，运行在Server中。
     * Stub实现了Binder-Interface，且继承自Binder。
     */
    public static abstract class Stub extends android.os.Binder implements cacard.androidipc0.SimpleAidlCopy {

        private static final java.lang.String DESCRIPTOR = "cacard.androidipc0.SimpleAidlCopy";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * asInterface(android.os.IBinder obj):
         * Cast an IBinder object into an cacard.androidipc0.SimpleAidlCopy interface,
         * generating a proxy if needed.
         * 上面自带的注释是说，将obj转化成interface，或者新建一个proxy。
         * 其实，前者是在Server中发生的，后者是在Client中发生的。
         * ------------------------------------------
         * Client在什么时候调用的？
         * 进程B（Client）在拿到Server传过来的IBinder时：
         * SimpleAidlCopy clazz = SimpleAidlCopy.Stub.asInterface(service);
         * 其实参数service是个BinderProxy类型。
         * ------------------------------------------
         * 将一个Binder转化成当前Binder-Interface，为啥不直接转换呢？
         * 比如像这样：if(obj instanceof SimpleAidlCopy) { return (SimpleAidlCopy)obj; }
         * Debug发现，obj其实是个android.os.BinderProxy类型!，所以不能强制转换。
         ** ------------------------------------------
         * from http://weishu.me/2016/01/12/binder-index-for-newer/
         * 函数的参数IBinder类型的obj，这个对象是驱动给我们的，
         * 如果是Binder本地对象，那么它就是Binder类型，如果是Binder代理对象，那就是BinderProxy类型；
         * 然后，正如上面自动生成的文档所说，它会试着查找Binder本地对象，
         * 如果找到，说明Client和Server都在同一个进程，这个参数直接就是本地对象，直接强制类型转换然后返回，
         * 如果找不到，说明是远程对象（处于另外一个进程）那么就需要创建一个Binde代理对象，让这个Binder代理实现对于远程对象的访问。
         * 一般来说，如果是与一个远程Service对象进行通信，那么这里返回的一定是一个Binder代理对象，这个IBinder参数的实际上是BinderProxy;
         */
        public static cacard.androidipc0.SimpleAidlCopy asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }

            //尝试查找Binder本地对象。如果是不同进程，这个obj其实BinderProxy，其queryLocalInterface总是返回null。
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof cacard.androidipc0.SimpleAidlCopy))) {
                return ((cacard.androidipc0.SimpleAidlCopy) iin);
            }

            //如果是在Client进程，总是会走这句。
            return new cacard.androidipc0.SimpleAidlCopy.Stub.Proxy(obj);
        }

        /**
         * 将一个IInterface转化成Binder实体
         * 1）如果在Client进程中，拿到的是BinderProxy；
         * 2）如果在Server进程中，拿到的是Binder真身；
         */
        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_add: {
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0;
                    _arg0 = data.readInt(); //从Client读数据
                    int _arg1;
                    _arg1 = data.readInt();
                    int _result = this.add(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result); //向Client回复
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        /**
         * Proxy供Client使用
         * 即，Clien端的Proxy是Server端Binder真是的引用。而且Client只能通过Proxy指向Binder真身。
         */
        private static class Proxy implements cacard.androidipc0.SimpleAidlCopy {
            private android.os.IBinder mRemote;

            /**
             * remote是个BinderProxy类型
             */
            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            /**
             * Proxy这个方法里面是写数据（向Server写数据），然后等待reply。
             */
            @Override
            public int add(int x, int y) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }
    
}
