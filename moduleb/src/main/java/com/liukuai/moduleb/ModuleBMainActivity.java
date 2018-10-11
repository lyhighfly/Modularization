package com.liukuai.moduleb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/module_b/release_main_activity")
public class ModuleBMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduleb_activity_main);
                Intent intent = getIntent();
        String param = intent.getStringExtra("trans");
        if(!TextUtils.isEmpty(param))
            ((TextView)findViewById(R.id.moduleb_text)).setText("MainApp pass :"+param);
        findViewById(R.id.moduleb_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(200);
                finish();
            }
        });
    }
}
