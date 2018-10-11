package com.liukuai.modulea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by liuyu on 18/6/30.
 */

@Route(path = "/module/module_a/debug_main_activity")
public class MuduleADebugMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulea_activity_module_debug_main);
        findViewById(R.id.modulea_button_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/module/module_a/debug_second_activity").navigation();
            }
        });
        findViewById(R.id.modulea_button_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/module_a/release_main_activity").navigation();
            }
        });
//        Intent intent = getIntent();
//        String param = intent.getStringExtra("trans");
//        if(!TextUtils.isEmpty(param))
//            ((TextView)findViewById(R.id.module_text)).setText(param);
    }
}
