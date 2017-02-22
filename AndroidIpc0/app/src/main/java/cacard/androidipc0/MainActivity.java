package cacard.androidipc0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 主要目的
 * - 通过定义aidl文件，查看生成的Binder结构，并熟悉每个环节；
 * - 实现最简单的两个进程之间的交互；
 * 试验过程
 * 1，通过定义aidl文件和其生成的类，配合Service，实现最简单的跨进程通信；
 * 2，拷贝出aidl生成的类型，对各个细节进行详细调研；
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        LinearLayout ll = new LinearLayout(this);
        TextView tv = new TextView(this);
        tv.setText("go to BActivity");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BActivity.class));
            }
        });
        ll.addView(tv);

        setContentView(ll);
    }
}
