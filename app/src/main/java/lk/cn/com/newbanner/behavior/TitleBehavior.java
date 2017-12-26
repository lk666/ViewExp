package lk.cn.com.newbanner.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import lk.cn.com.newbanner.R;

/**
 * 顶部title高度变化的behavior，view初始化的时候即会执行onDependentViewChanged
 */
public class TitleBehavior extends CoordinatorLayout.Behavior<TextView> {
    //    必须重写带双参的构造器，因为从xml反射需要调用。
    public TitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        // 判断parent下被依赖的子view
        return dependency != null && dependency.getId() == R.id.tb;
    }

    int initTBY = 0;
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View
            dependency) {
        //记录开始的Y坐标 也就是toolbar起始Y坐标
        if (initTBY == 0) {
            initTBY = (int) dependency.getY();
        }

        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY() / initTBY;

        //改变child的坐标(从消失，到可见)
        child.setY(-percent * child.getHeight());
        return true;
    }
}
