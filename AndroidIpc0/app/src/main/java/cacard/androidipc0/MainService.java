package cacard.androidipc0;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cunqingli on 2017/2/21.
 * <p>
 * 运行在主进程。B进程在connected后，想拿到一个Binder。
 * 本Server的onBind就返回一个Binder新实例。
 */

public class MainService extends Service {

    private static final String TAG = "MainService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /**
         * Server传给Client一个new Stub();
         */
        SimpleAidlCopy.Stub stub = new SimpleAidlCopy.Stub() {
            @Override
            public int add(int x, int y) throws RemoteException {
                return x + y;
            }
        };

        /**
         * 目前是在Server进程中，看看asBinder拿到的是啥？就是Binder真身，即SimpleAidlCopy.Stub
         */
        IBinder binder = stub.asBinder();
        if (binder instanceof SimpleAidlCopy.Stub) {
            Log.i(TAG, "@Server, so asBinder() return RealBinder.");
        }

        return stub;
    }
}
