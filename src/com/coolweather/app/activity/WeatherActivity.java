package com.coolweather.app.activity;



import java.util.ArrayList;
import java.util.List;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import com.coolweather.app.R;
import com.coolweather.app.fragment.FirstWeatherFragment;
import com.coolweather.app.fragment.SecondWeatherFragment;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.WeatherPagerAdapter;
import com.coolweather.app.util.WeatherPagerAdapter.OnReloadListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherActivity extends FragmentActivity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	private LinearLayout gif;
	private RelativeLayout viewPagerLayout;
	private TextView cityNameText;//显示城市名称
	private TextView publicText;//显示发布时间
	private TextView weatherDespText;//显示天气描述信息
	private TextView temp1Text;//显示气温1
	private TextView temp2Text;//显示气温2
	private TextView currentDateText;//显示当前日期
	private TextView currentT;//当前温度
	private TextView fl;//风力
	private TextView fx;//风向
	private TextView type;//云彩类型
	private Button switchCity;//切换城市按钮
	private Button refreshWeather;//更新天气按钮
	
	private ViewPager mViewPager;
	private List<Fragment> fragments;
	private WeatherPagerAdapter mWeatherPagerAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout); 
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new FirstWeatherFragment());
		fragments.add(new SecondWeatherFragment());
		mViewPager = (ViewPager) this.findViewById(R.id.viewpager);
		mWeatherPagerAdapter = new WeatherPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mWeatherPagerAdapter);
		//初始化各控件
		initView();
		//广告条
		View bannerView = BannerManager.getInstance(this).getBannerView(new 
				BannerViewListener() {
									@Override
									public void onRequestSuccess() {
									}
				
									@Override
									public void onSwitchBanner() {
									}
				
									@Override
									public void onRequestFailed() {
									}
								});
						LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.ll_banner);
						bannerLayout.addView(bannerView);
	}
	
	private void FragmentUpdate() {
		
		mWeatherPagerAdapter.setOnReloadListener(new OnReloadListener()
		{
			@Override
			public void onReload()
			{
				fragments = null;
				List<Fragment> list = new ArrayList<Fragment>();
				list.add(new FirstWeatherFragment());
				list.add(new SecondWeatherFragment());
				mWeatherPagerAdapter.setPagerItems(list);
			}
		});
		mViewPager.setAdapter(mWeatherPagerAdapter);
		
	}

	public WeatherPagerAdapter getAdapter()
	{
		return mWeatherPagerAdapter;
	}
	
	private void initView() {
		switchCity = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		cityNameText = (TextView)findViewById(R.id.city_name);
		currentT = (TextView)findViewById(R.id.current_T);
		
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		gif = (LinearLayout)findViewById(R.id.gif);
		viewPagerLayout = (RelativeLayout)findViewById(R.id.r_viewpager);
		publicText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDateText = (TextView)findViewById(R.id.current_date);
		fl = (TextView)findViewById(R.id.fl);
		fx = (TextView)findViewById(R.id.fx);
		type = (TextView)findViewById(R.id.type);
		
		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//如果有县级代号就去查询天气
			publicText.setText("同步中....");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			gif.setVisibility(View.INVISIBLE);
			viewPagerLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
			FragmentUpdate();
		}
	}
	//查询县级代号所对应的天气代号
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	//查询天气代号所对应的天气
	private void queryWeatherInfo(String weatherCode) {
		//String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+weatherCode;//+".html";
		queryFromServer(address,"weatherCode");
	}
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRquest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
						String array[] = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
							SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
							editor.putString("weather_code", weatherCode);
							editor.commit();
						}
					}
				}else if("weatherCode".equals(type)){
					//处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					//Log.d("MainActivity", response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
							FragmentUpdate();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publicText.setText("同步失败");
					}
				});
			}
		});
	}

	//从SharedPreferences文件中读取存储的天气信息，并显示到界面上
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city", ""));
		cityNameText.setVisibility(View.VISIBLE);
		
		temp1Text.setText("温度范围： "+prefs.getString("high", ""));
		temp2Text.setText(prefs.getString("low", ""));
		weatherDespText.setText(prefs.getString("ganmao", ""));
		publicText.setText("今天"+prefs.getString("date", ""));
		currentDateText.setText(prefs.getString("current_date", ""));
		currentT.setText("当前温度： "+prefs.getString("wendu", "")+"℃");
		fl.setText("风力： "+prefs.getString("fengli", ""));
		fx.setText("风向： "+prefs.getString("fengxiang", ""));
		type.setText("SKY ： "+prefs.getString("type", ""));
		
		viewPagerLayout.setVisibility(View.VISIBLE);
		gif.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		
		if(prefs.getString("type", "").equals("晴")){
			gif.setBackgroundResource(R.drawable.qing);
		}else if(prefs.getString("type", "").indexOf("雨") != -1){
			gif.setBackgroundResource(R.drawable.yu);
		}else if(prefs.getString("type", "").indexOf("雪") != -1){
			gif.setBackgroundResource(R.drawable.xue);
		}else if(prefs.getString("type", "").indexOf("雾") != -1){
			gif.setBackgroundResource(R.drawable.wu);
		}else if(prefs.getString("type", "").equals("多云") || prefs.getString("type", "").equals("阴")){
			gif.setBackgroundResource(R.drawable.duoyun);
		}else if(prefs.getString("type", "").indexOf("风") != -1){
			gif.setBackgroundResource(R.drawable.feng);
		}else if(prefs.getString("type", "").indexOf("霾") != -1){
			gif.setBackgroundResource(R.drawable.mai);
		}
		
		//启动后台自动更新天气服务，每隔8小时更新一次
		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
	
	//切换城市和天气
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publicText.setText("同步中....");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}
}
