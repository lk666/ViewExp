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

        vx = 0.8f;
        vx0 = 0.8f;
        vy = 2.5f;
        g = 0.01f;
    }

    private float vx = 0.3f;
    private float vx0 = 0.3f;
    private float vy = 3f;
    private float g = 0.01f;
    private long lastTime;

    private boolean isStop = true;

    public void start() {
        isStop = false;
        lastTime = System.currentTimeMillis();
        invalidate();
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

    // View绘制流程 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0603/2983.html
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(lastX, lastY, radius, ballPaint);

        if (!isStop) {
            long detalTime = System.currentTimeMillis() - lastTime;

            // x方向只是匀速往复运动而已
            int x = (int) (lastX + vx * detalTime);
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

            lastTime = System.currentTimeMillis();
            invalidate();
        }
    }

    /**
     * 递归算出最终的y坐标
     *
     * @param detalTime 经过的时间
     */

    private int getCurY(long detalTime) {
        int bottom = getHeight();
        // 先边界检测一遍，防止转屏刚好卡在这个时间
        if (lastY <= radius) {
            vy = Math.abs(vy);
            lastY = radius;
        } else if (lastY + radius >= bottom) {
            lastY = bottom - radius;
            if (vy == 0) {
                return lastY;
            }
            vy = -Math.abs(vy);
        }

        // 原速度方向上可行进距离
        int remain;

        // 初始向下运动
        if (vy >= 0) {
            remain = bottom - radius - lastY;

            // 这段时间的位移量
            int detalY = (int) ((vy + 0.5 * g * detalTime) * detalTime);

            // 到了底部
            if (detalY > remain) {
                // 到达底部的速度
                float vBottom = (float) Math.sqrt(2 * remain * g + vy * vy);
                detalTime = detalTime - (long) ((vBottom - vy) / g);

                vy = vBottom;
                lastY = bottom - radius;
                return getCurY(detalTime);
            } else {
                // 没到底部
                vy = Math.abs(vy) + g * detalTime;
                return lastY + detalY;
            }
        } else {
            // 向上运动
            // 可向上运动的距离
            remain = lastY - radius;

            // 速度为0时的时间
            long time0 = (long) Math.abs(vy / g);
            // 速度为0时的位移量
            int detalY0 = (int) (vy * vy / 2 / g);

            // 在时间内速度变为了0
            if (time0 <= detalTime) {
                // 碰壁了
                if (remain < detalY0) {
                    // 碰壁速度
                    float vTop = (float) Math.sqrt(vy * vy - 2 * remain * g);
                    detalTime = detalTime - (long) ((Math.abs(vy) - vTop) / g);

                    vy = vTop;
                    lastY = radius;
                    return getCurY(detalTime);
                } else {
                    // 没碰壁就向下运动了
                    detalTime = detalTime - time0;
                    vy = 0;
                    // 速度为0时的位移量
                    lastY = lastY - detalY0;
                    return getCurY(detalTime);
                }
            } else {
                // 没变为0速度就结束了运动

                // 这段时间的位移量
                int detalY = (int) ((Math.abs(vy) - 0.5 * g * detalTime) * detalTime);

                // 碰壁了
                if (detalY > remain) {
                    // 碰壁速度
                    float vTop = (float) Math.sqrt(vy * vy - 2 * remain * g);
                    detalTime = detalTime - (long) (((Math.abs(vy) - vTop) / g));

                    vy = vTop;
                    lastY = radius;
                    return getCurY(detalTime);
                } else {
                    // 此段时间没碰壁
                    vy = -(Math.abs(vy) - g * detalTime);
                    return lastY - detalY;
                }
            }
        }
    }
}
