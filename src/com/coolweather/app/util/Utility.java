package com.coolweather.app.util;

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
}
