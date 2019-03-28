package com.lkl.ansuote.hdqlibrary.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.lkl.ansuote.hdqlibrary.BuildConfig;

/**
 * Application 基类
 * @author huangdongqiang
 * @date 2018/3/30
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppStatusTracker.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Utils.init(this);
    }

}
