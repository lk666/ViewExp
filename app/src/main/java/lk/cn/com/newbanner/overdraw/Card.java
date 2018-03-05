package lk.cn.com.newbanner.overdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import lk.cn.com.newbanner.R;

public class Card extends View {
    private int picWidth;
    private int space;
    private Bitmap bm;
    private Paint paint;

    public Card(Context context) {
        super(context);
        init();
    }

    public Card(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Card(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        picWidth = getResources().getDimensionPixelOffset(R.dimen.dp150);
        space = getResources().getDimensionPixelOffset(R.dimen.dp50);
        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ysl);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = (getWidth() - picWidth) / space;
        for (int i = 0; i < n; i++) {
            int left = space * i;
            canvas.save();
            int right = left + (i < n - 1 ? space : picWidth);
            // 只裁剪出需要绘制的区域
            canvas.clipRect(left, 0, right, picWidth);
            drawPic(canvas, left, picWidth);
            canvas.restore();
        }
    }

    private void drawPic(Canvas canvas, int marginLeft, int size) {
        Rect recDes = new Rect(marginLeft, 0, marginLeft + size, size);
        canvas.drawBitmap(bm, new Rect(0, 0, getWidth(), getHeight()),
                recDes, paint);
    }
}
