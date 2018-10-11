package com.liukuai.modulea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Created by liuyu on 18/6/30.
 */

@Route(path = "/module_a/release_main_activity")
public class MuduleAMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulea_activity_module_main);
//        Intent intent = getIntent();
//        String param = intent.getStringExtra("trans");
//        if(!TextUtils.isEmpty(param))
//            ((TextView)findViewById(R.id.module_text)).setText(param);
    }
}
