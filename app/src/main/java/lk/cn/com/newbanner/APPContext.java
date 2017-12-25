package lk.cn.com.newbanner;

import android.app.Application;

public class APPContext extends Application {
    private static APPContext instance;

    public static APPContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
