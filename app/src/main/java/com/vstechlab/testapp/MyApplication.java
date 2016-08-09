package com.vstechlab.testapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by admin on 8/8/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
