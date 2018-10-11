package com.liukuai.base_lib;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by liuyu on 18/7/1.
 */

public class BaseApplication extends Application{

    static BaseApplication mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }

    public static Application getApp(){
        return mApp;
    }
}
