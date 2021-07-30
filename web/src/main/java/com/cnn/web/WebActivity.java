package com.cnn.web;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnn.web.databinding.ActivityWebBinding;
import com.just.agentweb.AgentWeb;

@Route(path = "/web/web")
public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_web);

        AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) binding.root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://oslanka.github.io/statichtml.github.io/index.html");
    }
}