package com.cnn.baselib;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cnn.crouter.CRouter;

/**
 * Created by caining on 7/25/21 08:24
 * E-Mail Address：cainingning@360.cn
 */
public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        String scheme = uri.getScheme();
        if ("arouter".equals(scheme)) {
            ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    finish();
                }
            });
        }else if ("crouter".equals(scheme)){
            CRouter.getInstance().navigation(uri.getPath());
        }
        finish();
    }
}
