package lk.cn.com.newbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;

    private ArrayList<ActivityItem> list = new ArrayList<>();

    {
        list.add(new ActivityItem("banner切换动画及新的自定义banner指示器", BannerActivity.class));
        list.add(new ActivityItem("metal design简单实例", AppBarActivity.class));
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
