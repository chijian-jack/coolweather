package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	//解析和处理服务器返回的省级数据
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB 
			coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){//如果response为空，返回true
			String [] allProvince = response.split(",");//将返回的数据用，隔开
			if(allProvince != null && allProvince.length>0){
				for(String p : allProvince){
					String array[] = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}  
	
	//解析和处理服务器返回的市级数据
	public synchronized static boolean handleCityResponse(CoolWeatherDB 
			coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){//如果response为空，返回true
			String [] allCity = response.split(",");//将返回的数据用，隔开
			if(allCity != null && allCity.length>0){
				for(String c : allCity){
					String array[] = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}  
		
	//解析和处理服务器返回的县级数据
			public synchronized static boolean handleCountyResponse(CoolWeatherDB 
					coolWeatherDB,String response,int cityId){
				if(!TextUtils.isEmpty(response)){//如果response为空，返回true
					String [] allCounty = response.split(",");//将返回的数据用，隔开
					if(allCounty != null && allCounty.length>0){
						for(String c : allCounty){
							String array[] = c.split("\\|");
							County county = new County();
							county.setCountyCode(array[0]);
							county.setCountyName(array[1]);
							county.setCityId(cityId);
							coolWeatherDB.saveCounty(county);
						}
						return true;
					}
				}
				return false;
			} 
	//解析服务器返回的JSON数据，并存储到本地
			public static void  handleWeatherResponse(Context context,String response){
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
					String cityName = weatherInfo.getString("city");
					String weatherCode = weatherInfo.getString("cityid");
					String temp1 = weatherInfo.getString("temp1");
					String temp2 = weatherInfo.getString("temp2");
					String weatherDesp = weatherInfo.getString("weather");
					String publishTime = weatherInfo.getString("ptime");
					saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	//将服务器返回的所有信息，存储到SharedPreferences
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
