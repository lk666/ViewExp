package lk.cn.com.newbanner;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Toolbar使用
 */
public class AppBarActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,
                        // 最多两行文字
                        "最多两行文字最多两行文字最多两行文字最多两行文字最多两行文字最多两行文字最多两行文字", Snackbar.LENGTH_SHORT)
                        // 文本内容右侧的可点击区域，会挤占左侧文本
                        .setAction("可点击区域，会挤占左侧文本", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AppBarActivity.this, "点击了可点击区域", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }).show();
            }
        });

        appbar = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("onOffsetChanged", "" + verticalOffset);
            }
        });
    }
}
