package com.example.wisdomexchange.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.wisdomexchange.base.BaseActivity;


/**
 * @Author: PengLiang
 * @Time: 2020/11/21
 * @Description: 启动页
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        // 休眠500毫秒然后进入主页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}