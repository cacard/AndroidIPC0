package cacard.androidipc0;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by cunqingli on 2017/2/21.
 *
 * 运行在主进程。B进程在connected后，想拿到一个Binder。
 * 本Server的onBind就返回一个Binder新实例。
 */

public class MainService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new StubImpl();
    }
}
