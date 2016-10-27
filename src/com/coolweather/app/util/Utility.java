package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	private static int count;
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
					//Log.d("Utility", response);
					//判断服务器返回的JSON数据是否可用
					String desc = jsonObject.getString("desc");
					int status = jsonObject.getInt("status");
					if("OK".equals(desc) && status == 1000 ){
					//解析当前温度和感冒指数
					JSONObject data = jsonObject.getJSONObject("data");
					String wendu = data.getString("wendu");
					String ganmao = data.getString("ganmao");
					//五天的天气
					JSONArray jsonArray = data.getJSONArray("forecast");
					for(int i = 0;i<jsonArray.length();i++){
						JSONObject json = jsonArray.getJSONObject(i);
						String fengxiang = json.getString("fengxiang");
						String fengli = json.getString("fengli");
						String high = json.getString("high");
						String type = json.getString("type");
						String low = json.getString("low");
						String date = json.getString("date");count++;
						saveWeatherInfo1(context,fengxiang,fengli,high,type,low,date);
					}
					//昨天温度
					JSONObject y = data.getJSONObject("yesterday");
					String fl = y.getString("fl");//风力
					String fx = y.getString("fx");//风向
					String high1 = y.getString("high");//最高温度
					String type1 = y.getString("type");//天气状况，多云还是等等
					String low1 = y.getString("low");//最低温度
					String date1 = y.getString("date");//日期
					String city = data.getString("city");//城市名称
					saveWeatherInfo2(context,wendu,ganmao,fl,fx,high1,type1,low1,date1,city);
					}else{
						Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	
	//将服务器返回的所有信息，存储到SharedPreferences
	private static void saveWeatherInfo1(Context context, String fengxiang, String fengli, String high,
			String type,String low,String date) {
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		if(count== 1){
		editor.putString("fengxiang", fengxiang);
		editor.putString("fengli", fengli);
		editor.putString("high", high);
		editor.putString("type", type);
		editor.putString("low", low);
		editor.putString("date", date);
		}else if(count== 2){
			editor.putString("fengxiang_1", fengxiang);
			editor.putString("fengli_1", fengli);
			editor.putString("high_1", high);
			editor.putString("type_1", type);
			editor.putString("low_1", low);
			editor.putString("date_1", date);
			}else if(count== 3){
				editor.putString("fengxiang_2", fengxiang);
				editor.putString("fengli_2", fengli);
				editor.putString("high_2", high);
				editor.putString("type_2", type);
				editor.putString("low_2", low);
				editor.putString("date_2", date);
				}else if(count== 4){
					editor.putString("fengxiang_3", fengxiang);
					editor.putString("fengli_3", fengli);
					editor.putString("high_3", high);
					editor.putString("type_3", type);
					editor.putString("low_3", low);
					editor.putString("date_3", date);
					}else if(count== 5){
						editor.putString("fengxiang_4", fengxiang);
						editor.putString("fengli_4", fengli);
						editor.putString("high_4", high);
						editor.putString("type_4", type);
						editor.putString("low_4", low);
						editor.putString("date_4", date);
						}
		editor.commit();
	}
	
	private static void saveWeatherInfo2(Context context, String wendu,
			String ganmao,String fl, String fx,
			String high1, String type1, String low1, String date1, String city) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		editor.putBoolean("city_selected", true);
		editor.putString("wendu", wendu);
		editor.putString("ganmao", ganmao);
		editor.putString("fl", fl);
		editor.putString("fx", fx);
		editor.putString("high1", high1);
		editor.putString("type1", type1);
		editor.putString("low1", low1);
		editor.putString("date1", date1);
		editor.putString("high1", high1);
		editor.putString("city", city);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}

}
