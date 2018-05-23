package lk.cn.com.newbanner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Constraintlayout:
 * 不要使用match_parent属性,如此，在Constraintlayout中进行2次测量只有是子view有且宽或者高中的一个值为0dp时。
 * 对于Constraintlayout的性能主要体现在 部分view需要两次测量和解线性方程组上，所以当布局层级较深或者约束规则较多时会影响到性能，而Constraintlayout主要就是为了解决嵌套层级过深的扁平化控件，所以对于布局层级过深这一种场景基本不存在，千万不要用了Constraintlayout还是嵌套了多层布局。
 * 不要添加一些无用约束，约束尽量保证以最少的数量准确的描述出view的大小和位置，从而可以减少整个solveLinearSystem的时间
 */
public class LayoutActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
    }
}
