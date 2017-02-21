package cacard.androidipc0;

import android.os.RemoteException;

/**
 * Created by cunqingli on 2017/2/21.
 * 对Stub的实现
 */

public class StubImpl extends SimpleAidlCopy.Stub {
    @Override
    public int add(int x, int y) throws RemoteException {
        return x + y;
    }
}
