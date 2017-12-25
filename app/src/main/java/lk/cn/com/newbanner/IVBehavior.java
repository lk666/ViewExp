package lk.cn.com.newbanner;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 图标变化的behavior，view初始化的时候即会执行onDependentViewChanged
 */
public class IVBehavior extends CoordinatorLayout.Behavior<ImageView> {
    //    必须重写带双参的构造器，因为从xml反射需要调用。
    public IVBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 左右边距15
        MAX_LEFT_DETAL = ViewUtil.getScreenWidth() - ViewUtil.dp2px(130);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        // 判断parent下被依赖的子view
        return dependency != null && dependency.getId() == R.id.tb;
    }

    int initTBY = 0;
    int MAX_LEFT_DETAL;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View
            dependency) {
        //记录开始的Y坐标 也就是toolbar起始Y坐标
        if (initTBY == 0) {
            initTBY = (int) dependency.getY();
        }

        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY() / initTBY;

        int width = ViewUtil.dp2px(40 + 60 * percent);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = width;
        lp.height = width;
        child.setLayoutParams(lp);

        child.setY(ViewUtil.dp2px(10 + 150 * percent));
        child.setX(ViewUtil.dp2px(15) + MAX_LEFT_DETAL * percent);

        return true;
    }
}
