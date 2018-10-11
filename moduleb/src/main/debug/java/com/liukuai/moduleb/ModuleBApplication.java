package com.liukuai.moduleb;

import com.liukuai.base_lib.BaseApplication;
import com.liukuai.moduleb.app.ModuleApp;

/**
 * Created by liuyu on 18/6/30.
 */

public class ModuleBApplication extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        new ModuleApp().onCreate(this);
    }
}
