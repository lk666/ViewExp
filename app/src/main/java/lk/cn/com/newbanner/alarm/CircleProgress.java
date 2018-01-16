package lk.cn.com.newbanner.alarm;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import lk.cn.com.newbanner.R;

/**
 * 椭圆形进度条
 * Created by lk on 2018/1/5.
 * ------------------------------------------
 * AttributeSet: 获取布局文件中定义的所有属性的key和value
 * TypedArray：用于获取AttributeSet中真实value（如使用引用值时）的包装类，如相当于attrs.getAttributeResourceValue(...)
 * ------------------------------------------
 * declare-styleable中使用attr name="android:width"，即可
 */

public class CircleProgress extends View {
    private int thiness = 20;
    private int color = Color.blue(120);
    private int innerColor = Color.WHITE;
    private Paint paint;
    private Paint paintInner;

    public CircleProgress(Context context) {
        super(context);
        initAttr(null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        thiness = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_10);
        color = Color.blue(120);
        innerColor = Color.WHITE;
        angle = 0;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgress);
            if (ta != null) {
                thiness = ta.getDimensionPixelOffset(R.styleable.CircleProgress_thiness, thiness);
                color = ta.getColor(R.styleable.CircleProgress_color, color);
                innerColor = ta.getColor(R.styleable.CircleProgress_inner_color, innerColor);
                angle = ta.getFloat(R.styleable.CircleProgress_progress, 0f) * 3.6f;
                angle = angle > 360 ? 360 : angle;
                ta.recycle();
            }
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        paintInner = new Paint();
        paintInner.setAntiAlias(true);
        paintInner.setColor(innerColor);
    }

    private float angle = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth() - getPaddingRight() - getPaddingLeft();
        if (width < 0 || getHeight() < getPaddingBottom()
                + getPaddingTop()) {
            return;
        }

        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        canvas.drawArc(rectF, -90, angle, true, paint);

        if (thiness + thiness > width) {
            return;
        }

        RectF rectFInner = new RectF(getPaddingLeft() + thiness, getPaddingTop() + thiness,
                getWidth() - getPaddingRight() - thiness, getHeight() - getPaddingBottom() -
                thiness);
        canvas.drawArc(rectFInner, 0, 360, true, paintInner);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouch(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouch(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouch(event.getX(), event.getY());
                break;
        }

        return true;
    }

    private void onTouch(float x, float y) {
        float x0 = (getWidth() + getPaddingLeft() - getPaddingRight()) * 0.5f;
        float y0 = (getHeight() + getPaddingTop() - getPaddingBottom()) * 0.5f;
        float detalX = x - x0;
        float detalY = y0 - y;
        float sin = (float) (detalX / Math.sqrt(detalX * detalX + detalY * detalY));
        angle = (float) (Math.asin(sin) * 180 / Math.PI);
        if (y > y0) {
            angle = 180 - angle;
        } else {
            if (x < x0) {
                angle = 360 + angle;
            }
        }
        invalidate();
    }

    public float getProgress() {
        return angle / 360f;
    }

    private OnClickListener listener;

    public void setOnInnerClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }
}
