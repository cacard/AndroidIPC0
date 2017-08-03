package cacard.androidipc0;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cunqingli on 2017/2/21.
 * 运行在主进程。B进程在connected后，想拿到一个Binder。
 * 本Server的onBind就返回一个Binder新实例。
 */

public class MainService extends Service {

    private static final String TAG = "MainService";

    @Override
    public void onCreate() {
        super.onCreate();
        Global.logGlobal("MainService onCreate()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        // @ server's main thread
        Global.logGlobal("[Server][onBind] thread:" + Thread.currentThread().getName());

        /**
         * Server传给Client一个new Stub();
         */
        MyIInterfaceCopy.Stub stub = new MyIInterfaceCopy.Stub() {
            @Override
            public int add(int x, int y) throws RemoteException {
                // @ thread Binder_2
                Global.logGlobal("[Server][StubImpl.add()] thread:" + Thread.currentThread().getName());

                // Crash操作，没用！外面有catch
                if (x == -1 && y == -1) {

                    MainService.this.crashAtService(); //让Service自身Crash

                    int a = 0;
                    int c = 10 / a;
                }

                return x + y;
            }
        };

        /**
         * 目前是在Server进程中，看看asBinder拿到的是啥？
         * 就是Binder真身，即SimpleAidlCopy.Stub
         */
        IBinder binder = stub.asBinder();
        if (binder instanceof MyIInterfaceCopy.Stub) {
            Log.i(TAG, "@Server, so asBinder() return RealBinder.");
        }

        return stub;
    }

    private void crashAtService() {
        int a = 0;
        int b = 1 / a;
    }
}
