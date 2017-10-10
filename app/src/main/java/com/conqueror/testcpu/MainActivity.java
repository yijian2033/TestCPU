package com.conqueror.testcpu;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button clickBt;
    private boolean isTest = true;//true表示开始测试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickBt = (Button) findViewById(R.id.bt);

        boolean testKey = PreferenceUtils.getBoolean(getApplicationContext(), "TestKey", true);

        clickBt.setText(testKey ? "开始测试" : "停止测试");
        clickBt.setBackgroundColor(testKey ? Color.RED : Color.GREEN);
        initListener();
    }

    private void initListener() {
        clickBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTest) {
                    clickBt.setText("停止测试");
                    clickBt.setBackgroundColor(Color.GREEN);
                    isTest = false;
                    startService(new Intent(MainActivity.this, RecordService.class));
                } else {
                    clickBt.setText("开始测试");
                    clickBt.setBackgroundColor(Color.RED);
                    isTest = true;
                    stopService(new Intent(MainActivity.this, RecordService.class));
                }

                PreferenceUtils.putBoolean(getApplicationContext(), "TestKey", isTest);

            }
        });
    }

}
