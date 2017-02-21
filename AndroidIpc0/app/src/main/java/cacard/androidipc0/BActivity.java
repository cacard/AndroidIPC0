package cacard.androidipc0;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cunqingli on 2017/2/21.
 * <p>
 * 运行在B进程的Activity，尝试与主进程交互。
 */

public class BActivity extends Activity {

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(BActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();

            //连接成功之后，拿到一个IBinder，IBinder就是一个可跨进程的可操作的物件。
            //如何使用这个IBinder呢?

            //这样? NO!
            if (service instanceof SimpleAidlCopy.Stub) {
                try {
                    int r = ((SimpleAidl.Stub) service).add(1, 2);
                    Toast.makeText(BActivity.this, "result:" + r, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            // 这样?OK
            // todo 重点分析
            SimpleAidlCopy clazz = SimpleAidlCopy.Stub.asInterface(service);
            try {
                int r = clazz.add(1, 2);
                Toast.makeText(BActivity.this, "result:" + r, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        TextView textView = new TextView(this);
        textView.setText("execute add()");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBind();
            }
        });
        ll.addView(textView);
        setContentView(ll);
    }

    private void startBind() {
        bindService(new Intent(this, MainService.class), mConnection, BIND_AUTO_CREATE);
    }
}
