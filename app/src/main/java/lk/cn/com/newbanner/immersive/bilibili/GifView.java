package lk.cn.com.newbanner.immersive.bilibili;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import lk.cn.com.newbanner.BitmapUtil;
import lk.cn.com.newbanner.R;

/**
 * 务必保证ballSize小于整个GifView尺寸
 * Created by lk on 2018/1/23.
 */

public class GifView extends View {
    public GifView(Context context) {
        super(context);
        init();
    }

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Paint ballPaint;
    private int radius = 50;

    private void init() {
        //  画笔Paint相关知识 http://wuxiaolong.me/2016/08/20/Paint/
        ballPaint = new Paint();
        ballPaint.setAntiAlias(true);

        int ballSize = 100;
        Bitmap bm = BitmapUtil.getRequareSizeBitmap(getContext(), R.mipmap.ysl, ballSize, ballSize);
        BitmapShader bs = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        LinearGradient linearGradient = new LinearGradient(0, 0, ballSize, ballSize,
                Color.TRANSPARENT, Color.argb(180, 255, 255, 255), Shader.TileMode.REPEAT);

        // bs在下层，linearGradient在上层
        ballPaint.setShader(new ComposeShader(bs, linearGradient, PorterDuff.Mode.ADD));

        radius = ballSize / 2;
        lastX = radius;
        lastY = radius + 300;
        y0 = lastY;

        vx = 0.8f;
        vx0 = 0.8f;
        vy0 = 3f;
        directY = vy0 < 0 ? -1 : 1;
        g = 0.01f;
    }

    private double vx = 0.3f;
    private double vx0 = 0.3f;
    private double vy0 = -1.5f;
    private double g = 0.01f;

    private boolean isStop = true;

    public void startIf() {
        if (!isStop) {
            reDraw();
        }
    }

    private long tNon, // 首次到顶/底的时间
            tHalf; // 半个周期的时间
    private double vTop, // 顶部速度
            vBottom; //底部速度
    private int hTop; //顶部坐标
    private int y0; // 起始坐标
    private int directY; // 1表示向下，-1向上；

    private void reDraw() {
        // 重新设置位置
        // x坐标使用原先的逻辑

        h = getHeight() - radius;

        // 重置速度
        vy0 = Math.sqrt(2 * g * (lastY - y0) + vy0 * vy0) * directY;

        // 重置y坐标
        if (lastY <= radius) {
            vy0 = Math.abs(vy0);
            lastY = radius;
        } else if (lastY >= h) {
            lastY = h;
            vy0 = -Math.abs(vy0);
        }
        directY = vy0 < 0 ? -1 : 1;
        y0 = lastY;

        // y坐标系相关参数
        double v02 = vy0 * vy0;
        double _2g = 2 * g;
        double _2gy0 = _2g * y0;
        vBottom = Math.sqrt(v02 + _2g * (h - y0));
        vTop = _2gy0 <= v02 ? Math.sqrt(v02 - 2 * g * (y0 - radius)) : 0;
        tNon = (long) (vy0 >= 0 ? (vBottom - vy0) / g : (-vy0 - vTop) / g);
        hTop = (int) (vTop == 0 ? (h - Math.sqrt(v02 / _2g)) : 0);
        th = (long) ((vBottom - vTop) / g);

        isStop = false;
        lastTime = System.currentTimeMillis();
        stime = lastTime;
        invalidate();
    }

    public void start() {
        reDraw();
    }

    public void stop() {
        isStop = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    // 从左上方出发，初始向右下方弹，遇到墙壁回弹
    private int lastX;
    private int lastY;
    private int h;
    private long th = 0;
    private long stime = 0;
    private long lastTime = 0;

    // View绘制流程 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0603/2983.html
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("Draw", stime + "  " + lastY);
        canvas.drawCircle(lastX, lastY, radius, ballPaint);

        if (!isStop) {
            long time = System.currentTimeMillis();
            long detalTime = time - stime;
            long xTime = time - lastTime;
            lastTime = time;
            // x方向只是匀速往复运动而已
            int x = (int) (lastX + vx * xTime);
            int right = getWidth();
            if (x < radius) {
                vx = vx0;
                x = radius;
            } else if (x + radius > right) {
                vx = -vx0;
                x = right - radius;
            }

            lastY = getCurY(detalTime);
            lastX = x;

            invalidate();
        }
    }

    /**
     *最终的y坐标
     *
     * @param detalTime 经过的时间
     */

    private int getCurY(long detalTime) {
        if (detalTime < tNon) {
            return (int) (y0 + 0.5 * g * detalTime * detalTime + vy0 * detalTime);
        }

        detalTime -= tNon;
        long k = detalTime / th;
        long t12 = detalTime - k * th;
        k = k % 2;

        if (vy0 >= 0) {
            if (k == 0) {
                // 向上
                directY = -1;
                return (int) (h - vBottom * t12 + 0.5 * g * t12 * t12);
            } else {
                directY = 1;
                // 向下
                return (int) (hTop + vTop * t12 + 0.5 * g * t12 * t12);
            }
        } else {
            if (k == 0) {
                // 向下
                directY = 1;
                return (int) (hTop + vTop * t12 + 0.5 * g * t12 * t12);
            } else {
                directY = -1;
                // 向上
                return (int) (h - vBottom * t12 + 0.5 * g * t12 * t12);
            }
        }
    }
}
