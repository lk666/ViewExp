package lk.cn.com.newbanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.viewpagerindicator.RoundLinePageIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vp;
    private Button btn;
    private TextView tv;
    private int type = 0;
    private RoundLinePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp =  findViewById(R.id.vp);
        // 超出vp部分可响应事件
        findViewById(R.id.fl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vp.onTouchEvent(event);
                return false;
            }
        });

        vp.setPageMargin(30);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new Adapter());
        vp.setPageTransformer(false, new PageAnimation());

        btn =  findViewById(R.id.btn);
        tv =  findViewById(R.id.tv);
        tv.setText("PageAnimation");
        btn.setOnClickListener(this);


        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(vp);
    }

    private Class list[] = {PageAnimation.class,
            // com.ToxicBakery.viewpager.transforms:view-pager-transforms
            AccordionTransformer.class,
            BackgroundToForegroundTransformer.class, CubeInTransformer.class,
            CubeOutTransformer.class, DefaultTransformer.class,
            DepthPageTransformer.class, FlipHorizontalTransformer.class,
            FlipVerticalTransformer.class, ForegroundToBackgroundTransformer.class,
            RotateDownTransformer.class, RotateUpTransformer.class,
            ScaleInOutTransformer.class, StackTransformer.class, TabletTransformer.class,
            ZoomInTransformer.class, ZoomOutTranformer.class, ZoomOutSlideTransformer.class};

    @Override
    public void onClick(View v) {
        type = (type + 1) % list.length;
        Class c = list[type];
        try {
            ViewPager.PageTransformer tr = (ViewPager.PageTransformer) c.newInstance();
            vp.setPageTransformer(false, tr);
            tv.setText(c.getSimpleName());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    class Adapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            TextView tv = new TextView(MainActivity.this);
            tv.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv.setText(position + "");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(30);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.argb(255, 50 * position, 50, 50));

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "点击文本" + position, Toast.LENGTH_SHORT)
                            .show();
                }
            });

            container.addView(tv);
            return tv;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 自定义切换动画
     */
    class PageAnimation implements ViewPager.PageTransformer {
        final float SCALE_MAX = 0.75f;
        final float ALPHA_MAX = 0.5f;
        final float MAX_Z = 12;

        public PageAnimation() {

        }

        @Override
        public void transformPage(View page, float position) {
            float scale = (position < 0)
                    ? ((1 - SCALE_MAX) * position + 1)
                    : ((SCALE_MAX - 1) * position + 1);
            float alpha = (position < 0)
                    ? ((1 - ALPHA_MAX) * position + 1)
                    : ((ALPHA_MAX - 1) * position + 1);
            //为了滑动过程中，page间距不变，这里做了处理
            if (position < 0) {
                ViewCompat.setPivotX(page, page.getWidth());
                ViewCompat.setPivotY(page, page.getHeight() / 2);
            } else {
                ViewCompat.setPivotX(page, 0);
                ViewCompat.setPivotY(page, page.getHeight() / 2);
            }
            ViewCompat.setScaleX(page, scale);
            ViewCompat.setScaleY(page, scale);
            ViewCompat.setAlpha(page, Math.abs(alpha));

            // 阴影
            ViewCompat.setElevation(page, MAX_Z * Math.abs(alpha));
        }
    }
}
