package lk.cn.com.newbanner.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import lk.cn.com.newbanner.R;

/**
 * 圆角阴影按钮，background设置无效
 * Created by lk on 2018/9/11.
 */

public class ShadowButton extends android.support.v7.widget.AppCompatTextView {

    private RectF mRectF = new RectF();
    private Paint mPaint;

    /**
     * 阴影的颜色, 需要带透明，color_state
     */
    private ColorStateList mShadowColor;
    /**
     * 背景的颜色, 需要带透明，color_state
     */
    private ColorStateList mBg;
    /**
     * 背景当前颜色
     */
    private int mCurShadowColor;
    /**
     * 阴影当前颜色
     */
    private int mCurBg;

    /**
     * 阴影的大小范围 radius越大越模糊，越小越清晰
     */
    private float mShadowRadius = 10f;

    /**
     * 阴影的宽度，比如底部的阴影，那就是底部阴影的高度
     */
    private int mShadowWidth = 30;

    /**
     * 阴影 x 轴的偏移量, 计算padding时需要计算在内
     */
    private int mShadowDx = 0;

    /**
     * 阴影 y 轴的偏移量，计算padding时需要计算在内，比如想底部的阴影多一些，这个设置值就可以了
     */
    private int mShadowDy = 20;

    /**
     * 圆角
     */
    private float mRadius = 20f;

    public ShadowButton(Context context) {
        super(context);
    }

    public ShadowButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ShadowButton);

        Resources res = getResources();
        mShadowColor = res.getColorStateList(R.color.shadow_botton);
        mBg = res.getColorStateList(R.color.shadow_botton_bg);

        // 使用此法以不去设置没有在xml中定义的
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ShadowButton_shadow_color:
                    mShadowColor = typedArray.getColorStateList(attr);
                    if (mShadowColor == null) {
                        mShadowColor = res.getColorStateList(R.color.shadow_botton);
                    }
                    break;
                case R.styleable.ShadowButton_bg_color:
                    mBg = typedArray.getColorStateList(attr);
                    if (mBg == null) {
                        mBg = res.getColorStateList(R.color.shadow_botton_bg);
                    }
                    break;
                case R.styleable.ShadowButton_shadow_radius:
                    mShadowRadius = typedArray.getFloat(attr, 10f);
                    break;
                case R.styleable.ShadowButton_shadow_width:
                    mShadowWidth = typedArray.getDimensionPixelOffset(attr, 30);
                    break;
                case R.styleable.ShadowButton_shadow_dx:
                    mShadowDx = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ShadowButton_shadow_dy:
                    mShadowRadius = typedArray.getDimensionPixelOffset(attr, 20);
                    break;
                case R.styleable.ShadowButton_shadow_corner_radius:
                    mRadius = typedArray.getFloat(attr, 4f);
                    break;
            }
        }

        //   方法2：直接使用    String text = typedArray.getString(R.styleable.TabSelector_text);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //// 获取设置的bg
        //        Drawable background = getBackground();
        //        //background包括color和Drawable,这里分开取值
        //        if (background instanceof ColorDrawable) {
        //            ColorDrawable colorDrawable = (ColorDrawable) background;
        //            int color = colorDrawable.getColor();
        //            mPaint.setColor(color);
        //            setBackground(null);
        //        } else {
        //            BitmapDrawable bgBitmap = ((BitmapDrawable) background).getBitmap();
        //            mPaint.set
        //            setBackground(null);
        //        }

        mCurBg = mBg.getDefaultColor();
        mPaint.setColor(mCurBg);

        mCurShadowColor = mShadowColor.getDefaultColor();
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mCurShadowColor);
        mPaint.setStyle(Paint.Style.FILL);

        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateState();
    }

    private void updateState() {
        int[] state = getDrawableState();
        int curS = mShadowColor.getColorForState(state, mShadowColor.getDefaultColor());
        int curB = mBg.getColorForState(state, mBg.getDefaultColor());

        if (curB != mCurBg || curS != mCurShadowColor) {
            mCurBg = curB;
            mCurShadowColor = curS;
            invalidate();
        }
    }

    /**
     * 决定View在ViewGroup中的位置 , 此处left ，top...是相对于父视图
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        resetShadowPadding();
    }

    /**
     * 为 ShadowLayout 设置 Padding 以为显示阴影留出空间
     */
    private void resetShadowPadding() {
        int rectLeft = 0;
        int rectTop = 0;
        int rectRight = 0;
        int rectBottom = 0;
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;


        rectTop = mShadowWidth + mShadowDx;
        paddingTop = mShadowWidth + mShadowDx;

        rectLeft = mShadowWidth + mShadowDx;
        paddingLeft = mShadowWidth + mShadowDx;

        rectRight = this.getWidth() - mShadowWidth - mShadowDx;
        paddingRight = mShadowWidth + mShadowDx;

        rectBottom = this.getHeight() - mShadowWidth - mShadowDy;
        paddingBottom = mShadowWidth + mShadowDy;

        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mCurBg);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mCurShadowColor);

        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        super.onDraw(canvas);
    }
}
