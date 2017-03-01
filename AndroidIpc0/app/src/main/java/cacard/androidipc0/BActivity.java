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
 * 运行在B进程的Activity，尝试与主进程交互。
 */

public class BActivity extends Activity {

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(BActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();

            /**
             * 如何将service转化成IIterface使用呢？
             */

            /**
             * 1，最直接的想法是instanceof，然后cast。
             * 先看看是不是Stub类型。然而并不是。毕竟Stub是Server端创建Binder实体时用的。
             * 那是不是Proxy类型呢？Sorry，Proxy是private的，无法判断。
             */
            if (service instanceof SimpleAidlCopy.Stub) {
                try {
                    int r = ((SimpleAidl.Stub) service).add(1, 2);
                    Toast.makeText(BActivity.this, "result:" + r, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 2，通过Stub的asInterface转化。OK的。
             */
            SimpleAidlCopy clazz = SimpleAidlCopy.Stub.asInterface(service);
            try {
                int r = clazz.add(1, 2);
                Toast.makeText(BActivity.this, "result:" + r + "/ thread:" + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            /**
             * 再次asInterface，会是不同的BinderProxy吗？
             * 是的，查看asInterface，你也会发现，Client调用这个，总是会new一个Proxy出来！
             */
            SimpleAidlCopy clazz2 = SimpleAidlCopy.Stub.asInterface(service);

            /**
             * 能将Interface拿到对应的Binder实体吗？
             * NO，实体肯定拿不到，而是代理：BinderProxy
             */
            IBinder binder = clazz.asBinder();
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
