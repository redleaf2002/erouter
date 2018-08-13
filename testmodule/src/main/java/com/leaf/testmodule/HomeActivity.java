package com.leaf.testmodule;

import com.leaf.erouter.annotation.EasyRouter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by suhong01 on 2018/8/13.
 */

@EasyRouter(path = "/test/HomeActivity")
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
