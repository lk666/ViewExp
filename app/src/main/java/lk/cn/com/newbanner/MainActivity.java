package lk.cn.com.newbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import lk.cn.com.newbanner.alarm.AlarmActivity;
import lk.cn.com.newbanner.behavior.BehaviorActivity;
import lk.cn.com.newbanner.immersive.ThemeActivity;
import lk.cn.com.newbanner.overdraw.OverDrawActivity;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;

    private ArrayList<ActivityItem> list = new ArrayList<>();

    {
        list.add(new ActivityItem("banner切换动画及新的自定义banner指示器", BannerActivity.class));
        list.add(new ActivityItem("伸缩toolbar、fab使用示例", AppBarActivity.class));
        list.add(new ActivityItem("Behavior使用示例", BehaviorActivity.class));
        list.add(new ActivityItem("Ripple涟漪效果", RippleActivity.class));
        list.add(new ActivityItem("定时器", AlarmActivity.class));
        list.add(new ActivityItem("主题、沉浸式与状态栏、底部条", ThemeActivity.class));
        list.add(new ActivityItem("过度绘制", OverDrawActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);

        rv.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        ItemAdapter adapter = new ItemAdapter();
        adapter.bindToRecyclerView(rv);

        adapter.replaceData(list);
    }

}
