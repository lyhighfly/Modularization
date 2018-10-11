package com.liukuai.moduleb.app;

import android.app.Application;
import android.util.Log;

import com.liukuai.base_lib.AppShadow;


public class ModuleApp implements AppShadow {


    private static Application hostApp;
    @Override
    public void onCreate(Application application) {
        hostApp = application;
        Log.e("InitModule", application.getClass().getName()+" invoke  Module ["+this.getClass().getName()+"] App onCreate()");
    }

    @Override
    public void onDestory() {

    }

    public static Application getHostApp() {
        return hostApp;
    }
}
