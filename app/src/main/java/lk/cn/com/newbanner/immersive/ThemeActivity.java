package lk.cn.com.newbanner.immersive;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import lk.cn.com.newbanner.ActivityItem;
import lk.cn.com.newbanner.ItemAdapter;
import lk.cn.com.newbanner.R;
import lk.cn.com.newbanner.RippleActivity;
import lk.cn.com.newbanner.behavior.BehaviorActivity;

public class ThemeActivity extends FragmentActivity {

    private RecyclerView rv;

    private ArrayList<ActivityItem> list = new ArrayList<>();

    {
        list.add(new ActivityItem("主题设置", AppThemeActivity.class));
        list.add(new ActivityItem("透明状态栏", StateBarActivity.class));
        list.add(new ActivityItem("全屏沉浸式", BehaviorActivity.class));
        list.add(new ActivityItem("底部导航栏", RippleActivity.class));
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