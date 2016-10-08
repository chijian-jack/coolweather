package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	private ListView listView;
	private TextView titleText;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private CoolWeatherDB coolWeatherDB;
	private int currentLevel;//��ǰѡ�еļ���
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private Province selectedProvince;//ѡ�е�ʡ��
	private List<Province> provinceList;//ʡ�б�
	private City selectedCity;
	private List<City> cityList;
	//private County selectedCounty;
	private List<County> countyList;
	private ProgressDialog progressDialog;
	private boolean isFromWeatherActivity;//�Ƿ��WeatherActivity�е�ת����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//�Ѿ�ѡ���˳����Ҳ��Ǵ�WeatherActivity����ת�����ĲŻ���ת��WeatherActivity
		if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listView = (ListView)findViewById(R.id.list_view);
		titleText = (TextView)findViewById(R.id.title_text);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCity();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounty();
				}else if(currentLevel == LEVEL_COUNTY){
					String countyCode = countyList.get(position).getCountyCode();
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}
			}
			
		});
		queryProvince();//����ʡ������
	}
	//��ѯȫ������ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ���ݿ��ѯ
	private void queryProvince() {
		provinceList = coolWeatherDB.loadProvince();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	
		//��ѯȫ�����г��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ���ݿ��ѯ
		private void queryCity() {
			cityList = coolWeatherDB.loadCity(selectedProvince.getId());
			if(cityList.size() > 0){
				dataList.clear();
				for(City city : cityList){
					dataList.add(city.getCityName());
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText(selectedProvince.getProvinceName());
				currentLevel = LEVEL_CITY;
			}else{
				queryFromServer(selectedProvince.getProvinceCode(),"city");
			}
		}
		
	//��ѯȫ�������أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ���ݿ��ѯ
			private void queryCounty() {
				countyList = coolWeatherDB.loadCounty(selectedCity.getId());
				if(countyList.size() > 0){
					dataList.clear();
					for(County county : countyList){
						dataList.add(county.getCountyName());
					}
					adapter.notifyDataSetChanged();
					listView.setSelection(0);
					titleText.setText(selectedCity.getCityName());
					currentLevel = LEVEL_COUNTY;
				}else{
					queryFromServer(selectedCity.getCityCode(),"county");
				}
			}
	//���ߴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
		private void queryFromServer(final String code, final String type) {
			String address;
			if(!TextUtils.isEmpty(code)){
				address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
			}else{
				address = "http://www.weather.com.cn/data/list3/city.xml";
			}
			showProgressDialog();
			HttpUtil.sendHttpRquest(address, new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					boolean result = false;
					if("province".equals(type)){
						result = Utility.handleProvinceResponse(coolWeatherDB, response);
					}else if("city".equals(type)){
						result = Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
					}else if("county".equals(type)){
						result = Utility.handleCountyResponse(coolWeatherDB, response, selectedCity.getId());
					}
					if(result){
						//ͨ��runonUiThread���������ص����̴߳����߼�
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								closeProgressDialog();
								if("province".equals(type)){
									queryProvince();
								}else if("city".equals(type)){
									queryCity();
								}else if("county".equals(type)){
									queryCounty();
								}
							}
						});
					}
				}
				
				@Override
				public void onError(Exception e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		}
		//��ʾ���ȶԻ���
		private void showProgressDialog() {
			if(progressDialog == null){
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("���ڼ���....");
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.show();
		}
		//�رս��ĶԻ���
		private void closeProgressDialog() {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
		}
		//��ȡBack���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б���ʡ�б�����ֱ���˳�
		@Override
		public void onBackPressed() {
			if(currentLevel == LEVEL_COUNTY){
				queryCity();
			}else if(currentLevel == LEVEL_CITY){
				queryProvince();
			}else{
					if(isFromWeatherActivity){
						Intent intent = new Intent(this,WeatherActivity.class);
						startActivity(intent);
					}
					finish();
			}
		}
		
}