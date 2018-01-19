package lk.cn.com.newbanner.immersive;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import lk.cn.com.newbanner.R;

public class ImmersiveActivity extends FragmentActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);

        StatusBarUtil.immersive(this, Color.argb(40, 0, 0, 0));
        fixTop();

         appBarLayout = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);

        ((Switch) findViewById(R.id.sw)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                if (isChecked) {
                    appBarLayout.setExpanded(true,true);
                    lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                } else {
                    appBarLayout.setExpanded(true,true);
                    lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout
                            .LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
            }
        });
    }

    public void fixTop() {
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.toolbar));
    }
}