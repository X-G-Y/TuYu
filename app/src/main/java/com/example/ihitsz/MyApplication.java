package com.example.ihitsz;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    private static final String VALUE = "Harvey";

    private String value;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setValue(VALUE); // 初始化全局变量
        Stetho.initializeWithDefaults(this);
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
