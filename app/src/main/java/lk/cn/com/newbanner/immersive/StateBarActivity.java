package lk.cn.com.newbanner.immersive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import lk.cn.com.newbanner.R;

public class StateBarActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statebar);

        StatusBarUtil.immersive(this, Color.argb(40, 0, 0, 0));
        fixTop();
    }

    public void fixTop() {
        StatusBarUtil.setHeight(this, findViewById(R.id.fl_title));
        StatusBarUtil.setMargin(this, findViewById(R.id.title));
    }
}