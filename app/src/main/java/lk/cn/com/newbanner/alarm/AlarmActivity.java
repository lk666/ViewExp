package lk.cn.com.newbanner.alarm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import lk.cn.com.newbanner.R;
import lk.cn.com.newbanner.ViewUtil;

public class AlarmActivity extends FragmentActivity {
    CircleProgress pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        pb = findViewById(R.id.pb);

        pb.setOnInnerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtil.toast("" + pb.getProgress());
            }
        });
    }
}
