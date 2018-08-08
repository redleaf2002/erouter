package com.leaf.erouter;

import com.leaf.erouter.annotation.EasyRouter;
import com.leaf.erouter.api.ERouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@EasyRouter(path = "/test/SkipActivity")
public class SkipActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        editText = findViewById(R.id.edit);

        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    content = "post event from skipactivity";
                }
                ERouter.getInstance().post(new EventInfoBean(content));
                Toast.makeText(SkipActivity.this, "Successfully post to MainActivity", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
