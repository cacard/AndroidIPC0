package cacard.androidipc0;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
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

public class ClientActivity extends Activity {

    MyIInterfaceCopy mService;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(ClientActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();

            /**
             * 如何将service转化成IIterface使用呢？
             */

            /**
             * 1，最直接的想法是instanceof，然后cast。
             * 先看看是不是Stub类型。然而并不是。毕竟Stub是Server端创建Binder实体时用的。
             * 那是不是Proxy类型呢？Sorry，Proxy是private的，无法判断。
             */
            if (service instanceof MyIInterfaceCopy.Stub) {
                try {
                    int r = ((SimpleAidl.Stub) service).add(1, 2);
                    Toast.makeText(ClientActivity.this, "result:" + r, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 2，通过Stub的asInterface转化。OK的。
             */
            MyIInterfaceCopy clazz = MyIInterfaceCopy.Stub.asInterface(service);
            mService = clazz;
            try {
                int r = clazz.add(1, 2);
                Toast.makeText(ClientActivity.this, "result:" + r + "/ thread:" + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            /**
             * 死亡连接。我去！
             */
            try {
                clazz.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        // 在哪个线程呢？
                        Global.logGlobal("binderDied @Thread:" + Thread.currentThread().getName());
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            /**
             * 再次asInterface，会是不同的BinderProxy吗？
             * 是的，查看asInterface，你也会发现，Client调用这个，总是会new一个Proxy出来！
             */
            MyIInterfaceCopy clazz2 = MyIInterfaceCopy.Stub.asInterface(service);

            /**
             * 能将Interface拿到对应的Binder实体吗？
             * NO，实体肯定拿不到，而是代理：BinderProxy
             */
            IBinder binder = clazz.asBinder();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Global.logGlobal("onServiceDisconnected @Thread:" + Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        // add
        TextView textView = new TextView(this);
        textView.setPadding(30, 30, 30, 30);
        textView.setBackgroundColor(Color.RED);
        textView.setText("startBind");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBind();
            }
        });

        // unbind
        TextView tvUnbind = new TextView(this);
        tvUnbind.setText("unbindService");
        tvUnbind.setPadding(30, 30, 30, 30);
        tvUnbind.setBackgroundColor(Color.BLUE);
        tvUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnection != null) {
                    try {
                        /**
                         * 直接调用unbind并不会引发onServiceDisconnected()!!!
                         */
                        ClientActivity.this.unbindService(mConnection);

                        /**
                         * Stop，也不会引发onServiceDisconnected()!!!
                         */
                        ClientActivity.this.stopService(new Intent(ClientActivity.this, MainService.class));


                        /**
                         * 看文档
                         * Called when a connection to the Service has been lost.  This typically
                         * happens when the process hosting the service has crashed or been killed.
                         * This does <em>not</em> remove the ServiceConnection itself -- this
                         * binding to the service will remain active, and you will receive a call
                         * to {@link #onServiceConnected} when the Service is next running.
                         *
                         * 通常是service所在进程Crash了！
                         */

                        /**
                         * 那就来个特殊操作，让Service Crash
                         */
                        if (mService != null) {
                            mService.add(-1, -1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ll.addView(textView);
        ll.addView(tvUnbind);

        setContentView(ll);
    }

    private void startBind() {
        bindService(new Intent(this, MainService.class), mConnection, BIND_AUTO_CREATE);
    }


}
