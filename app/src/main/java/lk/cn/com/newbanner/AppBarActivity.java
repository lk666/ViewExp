package lk.cn.com.newbanner;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Toolbar使用
 */
public class AppBarActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
private Toolbar toolbar;
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

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppBarActivity.this, "点击了导航图标", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        toolbar.inflateMenu(R.menu.menu_app_bar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    Toast.makeText(AppBarActivity.this , "search" , Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_notification) {
                    Toast.makeText(AppBarActivity.this , "notifications" , Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item1) {
                    Toast.makeText(AppBarActivity.this , "item_01" , Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item2) {
                    Toast.makeText(AppBarActivity.this , "R.string.item_02" , Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

        findViewById(R.id.tv0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppBarActivity.this, "点击了自定义View", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("onOffsetChanged", "" + verticalOffset);

            }
        });
    }

}
