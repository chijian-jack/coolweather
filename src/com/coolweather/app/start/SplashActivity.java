package com.coolweather.app.start;


import net.youmi.android.AdManager;

import com.coolweather.app.R;
import com.coolweather.app.activity.WeatherActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�����  
	boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final String SHAREDPREFERENCES_NAME = "first_pref"; 
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        AdManager.getInstance(this).init("0b778ce34b95a128", "5a3cc870fa4f9538", false, true);
        setContentView(R.layout.activity_splash);  
        init(); 
    }
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case GO_HOME:
                goHome();
                break;
            case GO_GUIDE:
                goGuide();
                break;
            }
            super.handleMessage(msg);
        }
    };
    
    private void init() {
        // ��ȡSharedPreferences����Ҫ������
        // ʹ��SharedPreferences����¼�����ʹ�ô���
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        // ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
        if (!isFirstIn) {
            // ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DISPLAY_LENGHT);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DISPLAY_LENGHT);
        }

    }
    
    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, WeatherActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

}

