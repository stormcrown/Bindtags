package com.blozi.bindtags.application;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.bumptech.glide.module.AppGlideModule;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 骆长涛 on 2018/3/8  19:50
 *
 */

public class Global extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.i("Global","内存不足");

        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("Global","内存不足2");
        super.onTrimMemory(level);
    }

    /** 当前门店 */
    public static final Map<String,String> curryentStore = new HashMap<String,String>();
    public static int currentStoreIndex =0;
    /** 顶级门店 */
    public static final Map<String,String> mainStore = new HashMap<String,String>();
    /** 子门店 */
    public static List<Map<String,String>> childrenStores = new ArrayList<Map<String, String>>();
    /***/
    public static final String STOREID = "storeId";
    public static final String STORE_NAME = "storeName";
    public static Activity currentActivity =null;
    public static Activity mainActivity =null;

}
