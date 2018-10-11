package com.liukuai.modulazition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

import public_services.ModuleAFragmentService;
import public_services.ModuleBFragmentService;

/**
* 作为Module运行时的入口
* @author liuyu
* created at 2018/10/11---下午3:39
*/
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                ARouter.getInstance().build("/module_a/release_main_activity")
                        .withString("trans", "call from MainActivity").navigation();
                break;
            case R.id.button3:
                ARouter.getInstance().build("/module_b/release_main_activity")
                        .withString("trans", "call from MainActivity").navigation(this, 200);
                break;
            case R.id.button2:
                //两种调用方式都可以
                ModuleAFragmentService service_a = (ModuleAFragmentService) ARouter.getInstance()
                        .build( "/module_a/fragment_service").navigation();
//                ModuleAFragmentService service = (ModuleAFragmentService) ARouter.getInstance()
//                        .navigation(ModuleAFragmentService.class);
                FragmentManager fragmentManagera = getSupportFragmentManager();
                fragmentManagera.beginTransaction().replace(R.id.fragment_container,
                        service_a.getModuleAFragment("————Call From MainActivity"))
                        .commitAllowingStateLoss();
                break;
            case R.id.button4:
                ModuleBFragmentService service_b = (ModuleBFragmentService) ARouter.getInstance()
                        .build( "/module_b/fragment_service").navigation();
                FragmentManager fragmentManagerb = getSupportFragmentManager();
                fragmentManagerb.beginTransaction().replace(R.id.fragment_container,
                        service_b.getModuleBFragment("————Call From MainActivity"))
                        .commitAllowingStateLoss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200 && resultCode == 200){
            ((TextView)findViewById(R.id.textView2)).setText("ModuleB Finish work, onActivityResult");
        }
    }
}
