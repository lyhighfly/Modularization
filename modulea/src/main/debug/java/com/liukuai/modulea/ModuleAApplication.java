package com.liukuai.modulea;

import com.liukuai.base_lib.BaseApplication;
import com.liukuai.modulea.app.ModuleApp;

/**
 * Created by liuyu on 18/6/30.
 */

public class ModuleAApplication extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        new ModuleApp().onCreate(this);
    }
}
