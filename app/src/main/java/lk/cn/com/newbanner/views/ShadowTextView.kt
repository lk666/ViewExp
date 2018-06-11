package lk.cn.com.newbanner.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.TextView


/**
 * 绘制自定义颜色边框阴影(右下)
 * Created by lk on 2018/6/7.
 */
class ShadowTextView : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var mRectF = RectF()

    /**
     * 阴影的颜色, 需要带透明
     */
    private val mShadowColor = Color.argb(128, 0, 172, 224)

    /**
     * 阴影的大小范围 radius越大越模糊，越小越清晰
     */
    private val mShadowRadius = 10f

    /**
     * 阴影的宽度，比如底部的阴影，那就是底部阴影的高度
     */
    private val mShadowWidth = 15f

    /**
     * 阴影 x 轴的偏移量, 计算padding时需要计算在内
     */
    private val mShadowDx = 0f

    /**
     * 阴影 y 轴的偏移量，计算padding时需要计算在内，比如想底部的阴影多一些，这个设置值就可以了
     */
    private val mShadowDy = 10f

    private val paint by lazy {
        val p = Paint()
        p.isAntiAlias = false
        p.color = Color.argb(255, 253, 73, 124)
        p.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
        p
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
//        this.setWillNotDraw(false)     // viewGroup调用此方法后，才会执行 onDraw(Canvas) 方法
    }

    /**
     * 决定View在ViewGroup中的位置 , 此处left ，top...是相对于父视图
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        resetShadowPadding()
    }

    /**
     * 为 ShadowLayout 设置 Padding 以为显示阴影留出空间
     */
    private fun resetShadowPadding() {
        var rectLeft = 0f
        var rectTop = 0f
        var rectRight = 0f
        var rectBottom = 0f
        var paddingLeft = 0
        var paddingTop = 0
        var paddingRight = 0
        var paddingBottom = 0


        rectTop = mShadowWidth + mShadowDx
        paddingTop = (mShadowWidth + mShadowDx).toInt()

        rectLeft = mShadowWidth + mShadowDx
        paddingLeft = (mShadowWidth + mShadowDx).toInt()

        rectRight = this.width - mShadowWidth - mShadowDx
        paddingRight = (mShadowWidth + mShadowDx).toInt()

        rectBottom = this.height - mShadowWidth - mShadowDy
        paddingBottom = (mShadowWidth + mShadowDy).toInt()

        mRectF.left = rectLeft
        mRectF.top = rectTop
        mRectF.right = rectRight
        mRectF.bottom = rectBottom
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    /**
     * 如何绘制这个View, 真正绘制阴影的方法
     */
    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(mRectF, 20f, 20f, paint)
        super.onDraw(canvas)
    }

}