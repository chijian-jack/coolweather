package chijian.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import chijian.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	//数据库名字
	private static final String DB_NAME = "cool_weather"; 
	//数据库版本
	private static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	//构造方法私有化
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	//获取CoolWeatherDB的实例,这种实例化的方法叫单例模式，这个类叫单例类
	//一般用于比较大比较复杂的对象，每次调用都返回相同的对象，避免系统资源浪费，节省内存空间
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	//将Province实例存储到数据库
	public void saveProvince(Province province){
		if(province != null){
		ContentValues values = new ContentValues();
		values.put("province_name", province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
		}
	}
	//从数据库读取全国省份信息
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	//将City实例存储到数据库
	public void saveCity(City city){
		if(city != null){
		ContentValues values = new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
		}
	}
	//从数据库读取某省下所有城市的信息
	public List<City> loadCity(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String [] {String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	//将County实例存储到数据库
	public void saveCounty(County county){
		if(county != null){
		ContentValues values = new ContentValues();
		values.put("county_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		values.put("city_id", county.getCityId());
		db.insert("County", null, values);
		}
	}
	//从数据库读取某城市所有的县信息
	public List<County> loadCounty(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String [] {String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCityId(cityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
