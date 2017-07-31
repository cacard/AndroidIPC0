package cacard.androidipc0;

/**
 * Created by cunqingli on 2017/2/21.
 *
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
 * - Stub以及Proxy中的mRemote（类型是ProxyBinder）都实现了Binder
 * 【Binder本地对象】
 * - 指Binder真身，仅在Server中有一个，其它地方有很多它的引用。
 * 【BinderProxy】
 * - 是对Binder真身的代理
 */
public interface MyIInterfaceCopy extends android.os.IInterface {

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
     * Stub还包含了一个static的Proxy
     * Stub实现了Binder-Interface，且继承自Binder。
     */
    public static abstract class Stub extends android.os.Binder implements MyIInterfaceCopy {

        /**
         * Binder实体的标识符。
         * 为什么使用标识符？驱动中就是用这个查找的？
         */
        private static final java.lang.String DESCRIPTOR = "cacard.androidipc0.MyIInterfaceCopy";

        /**
         * Construct the stub at attach it to the interface.
         * 在一个Binder实体创建时，会吧自己与标识符绑定
         */
        public Stub() {
            // 两个参数分别对应mOwner和mDescriptor
            // 这俩家伙仅在queryLocalInterface时使用，仅仅是用来做这个操作的？
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * asInterface(android.os.IBinder obj):
         * Cast an IBinder object into an cacard.androidipc0.MyIInterfaceCopy interface,
         * generating a proxy if needed.
         * 上面自带的注释是说，将obj转化成interface，或者新建一个proxy。
         * 其实，前者是在Server中发生的，后者是在Client中发生的。
         * ------------------------------------------
         * 参数obj是一个BinderProxy类型（在Binder中定义，是private的）
         * 而Proxy里面的mRemote就是这个BinderProxy类型，最后调用的是它的transcate()方法。
         * ------------------------------------------
         * Client在什么时候调用的？
         * 进程B（Client）在拿到Server传过来的IBinder时：
         * MyIInterfaceCopy clazz = MyIInterfaceCopy.Stub.asInterface(service);
         * 其实参数service是个BinderProxy类型。
         * ------------------------------------------
         * 将一个Binder转化成当前Binder-Interface，为啥不直接转换呢？
         * 比如像这样：if(obj instanceof MyIInterfaceCopy) { return (MyIInterfaceCopy)obj; }
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
        public static MyIInterfaceCopy asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }

            // 尝试查找Binder本地对象。
            // 如果是不同进程，这个obj其实BinderProxy，其queryLocalInterface总是返回null。
            // ？？BinderProxy有queryLocalInterface吗？BinderProxy仅仅是一个BinderInterface啊
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof MyIInterfaceCopy))) {
                return ((MyIInterfaceCopy) iin);
            }

            //如果是在Client进程，总是会走这句。
            return new MyIInterfaceCopy.Stub.Proxy(obj);
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

        /**
         * Server的Binder线程池里面执行的操作，执行后向Binder驱动写入结果数据。
         */
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
         * Proxy供Client使用，其实是个BinderProxy的代理。
         */
        private static class Proxy implements MyIInterfaceCopy {

            /**
             * BinderProxy类型
             */
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
             * 在Client进程里面进行操作。
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
                    // 走向ProxyBinder，最后走向natvie，最后在Client的Binder线程池里面执行
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


/**
 * IBinder文档
 *
 * Base interface for a remotable object, the core part of a lightweight
 * remote procedure call mechanism designed for high performance when
 * performing in-process and cross-process calls.  This
 * interface describes the abstract protocol for interacting with a
 * remotable object.  Do not implement this interface directly, instead
 * extend from {@link android.os.Binder}.
 * 该接口定义了一组抽象，代表了一个可跨进程的对象。是进程内和进程外通信过程的核心部件。
 *
 * <p>The key IBinder API is {@link #transact transact()} matched by
 * {@link android.os.Binder#onTransact Binder.onTransact()}.  These
 * methods allow you to send a call to an IBinder object and receive a
 * call coming in to a Binder object, respectively.  This transaction API
 * is synchronous, such that a call to {@link #transact transact()} does not
 * return until the target has returned from
 * {@link android.os.Binder#onTransact Binder.onTransact()}; this is the
 * expected behavior when calling an object that exists in the local
 * process, and the underlying inter-process communication (IPC) mechanism
 * ensures that these same semantics apply when going across processes.
 * 进程内和进程外的调用都是同步的。
 *
 * <p>The data sent through transact() is a {@link android.os.Parcel}, a generic buffer
 * of data that also maintains some meta-data about its contents.  The meta
 * data is used to manage IBinder object references in the buffer, so that those
 * references can be maintained as the buffer moves across processes.  This
 * mechanism ensures that when an IBinder is written into a Parcel and sent to
 * another process, if that other process sends a reference to that same IBinder
 * back to the original process, then the original process will receive the
 * same IBinder object back.  These semantics allow IBinder/Binder objects to
 * be used as a unique identity (to serve as a token or for other purposes)
 * that can be managed across processes.
 * 进程间传递的Parcel是有一份缓存来维护的。
 *
 * <p>The system maintains a pool of transaction threads in each process that
 * it runs in.  These threads are used to dispatch all
 * IPCs coming in from other processes.  For example, when an IPC is made from
 * process A to process B, the calling thread in A blocks in transact() as
 * it sends the transaction to process B.  The next available pool thread in
 * B receives the incoming transaction, calls Binder.onTransact() on the target
 * object, and replies with the result Parcel.  Upon receiving its result, the
 * thread in process A returns to allow its execution to continue.  In effect,
 * other processes appear to use as additional threads that you did not create
 * executing in your own process.
 * 每个进程有特定的线程池做交互。
 *
 * <p>The Binder system also supports recursion across processes.  For example
 * if process A performs a transaction to process B, and process B while
 * handling that transaction calls transact() on an IBinder that is implemented
 * in A, then the thread in A that is currently waiting for the original
 * transaction to finish will take care of calling Binder.onTransact() on the
 * object being called by B.  This ensures that the recursion semantics when
 * calling remote binder object are the same as when calling local objects.
 * 支持递归交互。
 *
 * <p>When working with remote objects, you often want to find out when they
 * are no longer valid.  There are three ways this can be determined:
 * 什么时候远程不可用
 * <ul>
 * <li> The {@link #transact transact()} method will throw a
 * {@link android.os.RemoteException} exception if you try to call it on an IBinder
 * whose process no longer exists.
 * <li> The {@link #pingBinder()} method can be called, and will return false
 * if the remote process no longer exists.
 * <li> The {@link #linkToDeath linkToDeath()} method can be used to register
 * a {@link android.os.IBinder.DeathRecipient} with the IBinder, which will be called when its
 * containing process goes away.
 * </ul>
 *
 * @see android.os.Binder
 */

/**
 * Binder文档
 *
 * Base class for a remotable object, the core part of a lightweight
 * remote procedure call mechanism defined by {@link android.os.IBinder}.
 * This class is an implementation of IBinder that provides
 * standard local implementation of such an object.
 *
 * <p>Most developers will not implement this class directly, instead using the
 * <a href="{@docRoot}guide/components/aidl.html">aidl</a> tool to describe the desired
 * interface, having it generate the appropriate Binder subclass.  You can,
 * however, derive directly from Binder to implement your own custom RPC
 * protocol or simply instantiate a raw Binder object directly to use as a
 * token that can be shared across processes.
 * 一般的开发过程中无需直接实现Binder类，而是通过aidl定义接口。
 *
 * <p>This class is just a basic IPC primitive; it has no impact on an application's
 * lifecycle, and is valid only as long as the process that created it continues to run.
 * To use this correctly, you must be doing so within the context of a top-level
 * application component (a {@link android.app.Service}, {@link android.app.Activity},
 * or {@link android.content.ContentProvider}) that lets the system know your process
 * should remain running.</p>
 *
 * <p>You must keep in mind the situations in which your process
 * could go away, and thus require that you later re-create a new Binder and re-attach
 * it when the process starts again.  For example, if you are using this within an
 * {@link android.app.Activity}, your activity's process may be killed any time the
 * activity is not started; if the activity is later re-created you will need to
 * create a new Binder and hand it back to the correct place again; you need to be
 * aware that your process may be started for another reason (for example to receive
 * a broadcast) that will not involve re-creating the activity and thus run its code
 * to create a new Binder.</p>
 *
 * @see android.os.IBinder
 */
