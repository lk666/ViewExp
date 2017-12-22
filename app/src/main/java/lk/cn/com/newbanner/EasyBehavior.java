package lk.cn.com.newbanner;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 简单的behavior，view初始化的时候即会执行onDependentViewChanged
 */
public class EasyBehavior extends CoordinatorLayout.Behavior<TextView> {
    //    必须重写带双参的构造器，因为从xml反射需要调用。
    public EasyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        // 判断parent下被依赖的子view
        return dependency != null && dependency.getId() == R.id.btn;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View
            dependency) {
        // 被观察View变化的时候回调用的方法
        child.setX(dependency.getX() + 200);
        child.setY(dependency.getY() + 200);
        child.setText(dependency.getX() + "," + dependency.getY());
        return true;
    }
}
