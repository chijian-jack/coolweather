package chijian.coolweather.app.start;


import cn.waps.AppConnect;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import chijian.coolweather.app.R;
import chijian.coolweather.app.activity.WeatherActivity;


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
        //���չ��
        AppConnect.getInstance("f90429cf9150e25d77295588b3c0d8f9","QQ",this);
        setContentView(R.layout.activity_splash);  
        init(); 
      //����ͳ��
		MobclickAgent.setScenarioType(this, EScenarioType.E_UM_GAME);
    }
    
  //����ͳ��
  	public void onResume() {
  		super.onResume();
  		MobclickAgent.onResume(this);
  		}
  		public void onPause() {
  		super.onPause();
  		MobclickAgent.onPause(this);
  		}
  	//����ͳ��
    
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

