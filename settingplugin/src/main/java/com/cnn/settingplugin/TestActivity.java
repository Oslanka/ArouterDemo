package com.cnn.settingplugin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cnn.testrouter_annotation.CRoute;

@CRoute(path = "/test/test")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}