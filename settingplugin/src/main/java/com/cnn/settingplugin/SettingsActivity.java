package com.cnn.settingplugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cnn.settingplugin.databinding.SettingsActivityBinding;
import com.cnn.testrouter_annotation.CRoute;

@CRoute(path = "/csetting/csetting")
@Route(path = "/setting/setting")
public class SettingsActivity extends AppCompatActivity {
    private static final int result_code = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       SettingsActivityBinding  binding= DataBindingUtil.setContentView(this,R.layout.settings_activity);
       binding.settings.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ARouter.getInstance().build("/login/login").navigation();
           }
       });
       binding.btResult.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               ARouter.getInstance().build("/login/login")
//                       .withString("username", "admin")
//                       .withString("password", "123456").navigation(SettingsActivity.this, result_code);


               Intent intent = new Intent();
               intent.setClassName(SettingsActivity.this, "com.cnn.loginplugin.ui.login.LoginActivity");
               ActivityCompat.startActivityForResult(SettingsActivity.this, intent, result_code, null);
           }
       });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case result_code:
                    Toast.makeText(this, "设置界面回调成功", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}