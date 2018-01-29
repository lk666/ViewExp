package lk.cn.com.newbanner.immersive.bilibili;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import lk.cn.com.newbanner.R;
import lk.cn.com.newbanner.immersive.StatusBarUtil;

public class ImmersiveActivity extends FragmentActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    GifView gif;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);

        StatusBarUtil.immersive(this, Color.argb(40, 0, 0, 0));
        fixTop();

        appBarLayout = findViewById(R.id.appbar);
        gif = findViewById(R.id.gif);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams)
                        collapsingToolbarLayout.getLayoutParams();
                if (isChecked) {
                    appBarLayout.setExpanded(true, true);
                    lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                    gif.start();
                } else {
                    appBarLayout.setExpanded(true, true);
                    lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout
                            .LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                    gif.stop();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            if (gif != null) {
                gif.startIf();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sw.isChecked()) {
            gif.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sw.isChecked()) {
            gif.stop();
        }
    }

    public void fixTop() {
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.toolbar));
    }
}