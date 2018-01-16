package lk.cn.com.newbanner;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * View相关工具类
 * Created by bm on 2016/8/4.
 */
public class ViewUtil {

    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED); 无效
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);


    }

    public static void hideIM(View v) {
        try {
            InputMethodManager im = (InputMethodManager) v.getContext().getSystemService(Activity
                    .INPUT_METHOD_SERVICE);
            IBinder windowToken = v.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {

        }
    }

    public static boolean isSoftShowing(Window window) {
        //获取当前屏幕内容的高度
        int screenHeight = window.getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) APPContext.getInstance().getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) APPContext.getInstance().getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, APPContext
                .getInstance().getResources().getDisplayMetrics());
    }

    public static void toast(String s) {
        Toast.makeText(APPContext.getInstance(), s, Toast.LENGTH_SHORT).show();
    }

    //    /**
    //     * 获取状态栏高度
    //     */
    //    public static int getStatusBarHeight() {
    //        int result = 0;
    //        int resourceId = AppContext.getInstance().getResources().getIdentifier
    //                ("status_bar_height", "dimen", "android");
    //        if (resourceId > 0) {
    //            result = AppContext.getInstance().getResources().getDimensionPixelSize
    // (resourceId);
    //        }
    //        return result;
    //    }

}
