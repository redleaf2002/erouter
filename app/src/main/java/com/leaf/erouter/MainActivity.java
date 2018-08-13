package com.leaf.erouter;

import com.leaf.erouter.annotation.EasyRegister;
import com.leaf.erouter.api.ERouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView mReceiverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ERouter.getInstance().register(this);
        initView();
    }

    private void initView() {
        Button skipBnt = findViewById(R.id.skip);
        Button skipOtherBnt = findViewById(R.id.skip_other_module);
        Button postBnt = findViewById(R.id.post);
        mReceiverView = findViewById(R.id.receiver);

        skipBnt.setOnClickListener(this);
        skipOtherBnt.setOnClickListener(this);
        postBnt.setOnClickListener(this);
    }

    @EasyRegister()
    public void onEventInfoBean(final EventInfoBean event) {
        if (event == null) {
            return;
        }
        Log.i(TAG, "MainActivity EventInfoBean " + event.name);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mReceiverView.setText(event.name);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ERouter.getInstance().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                ERouter.getInstance().build(MainActivity.this).setIntent(null).skipTo
                        ("/test/SkipActivity");
                break;

            case R.id.skip_other_module:
                ERouter.getInstance().build(MainActivity.this).setIntent(null).skipTo
                        ("/test/HomeActivity");
                break;
            case R.id.post:
                ERouter.getInstance().post(new EventInfoBean("event"));
                break;
        }
    }
}
