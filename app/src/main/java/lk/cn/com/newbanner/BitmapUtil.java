package lk.cn.com.newbanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

/**
 * Created by lk on 2018/1/23.
 */

public class BitmapUtil {
    /**
     * 获取尽量不大于某个尺寸的图片（像素）
     */
    public static Bitmap getRequareSizeBitmap(Context context, @DrawableRes int src, int w, int h) {
        if (context == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), src, options);
        options.inSampleSize = getInSampleSize(options, w, h);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(context.getResources(), src, options);
    }

    /**
     * 获取缩放比例，有一条边小于指定尺寸就返回
     */
    private static int getInSampleSize(BitmapFactory.Options options, int w, int h) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int sampleSize = 1;

        // 设置inSampleSize为2的幂是因为解码器最终还是会对非2的幂的数进行向下处理，获取到最靠近2的幂的数
        if (height > h || width > w) {
            do {
                sampleSize *= 2;
            } while ((height / sampleSize > h) && (width / sampleSize > w));
        }
        return sampleSize;
    }
}
