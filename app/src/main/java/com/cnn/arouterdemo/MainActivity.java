package com.cnn.arouterdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cnn.arouterdemo.databinding.ActivityMainBinding;
import com.cnn.crouter.CRouter;

public class MainActivity extends AppCompatActivity {
    private static final int result_code = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                1.显式跳转
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));

                //2.隐式 跳转
//                Intent intent = new Intent();
//                intent.setClassName(MainActivity.this, "com.cnn.loginplugin.ui.login.LoginActivity");
//                startActivity(intent);

                //3.ARouter
                ARouter.getInstance().build("/login/login").navigation();

            }
        });


        binding.btLoginWithParms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/login/login")
                        .withString("username", "admin")
                        .withString("password", "123456").navigation();
            }
        });


        binding.btLoginWithResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/login/login")
                        .withString("username", "admin")
                        .withString("password", "123456").navigation(MainActivity.this, result_code);
            }
        });

        binding.btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/setting/setting").navigation();

            }
        });

        binding.btWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/web/web").navigation();

            }
        });

        binding.btSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRouter.getInstance().navigation("/csetting/csetting");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case result_code:
                    Toast.makeText(this, "回调成功", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}