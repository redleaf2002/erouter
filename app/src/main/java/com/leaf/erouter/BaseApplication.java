package com.leaf.erouter;

import com.leaf.erouter.api.ERouter;

import android.app.Application;

/**
 * Created by suhong01 on 2018/7/5.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ERouter.getInstance().init(getApplicationContext());
    }
}
