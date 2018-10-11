package com.liukuai.modulazition;

import com.liukuai.base_lib.BaseApplication;


/**
 * Created by liuyu on 18/6/30.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //全局使用MainAppShadow，不管是独立运行，还是作为module运行，都需要初始化AppShadow
        new MainAppShadow().onCreate(this);
    }
}
