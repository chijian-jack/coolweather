package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	/*Province建表语句*/
	private static final String CREATE_PROVINCE = "creat table province("
	+"id integer primary key autoincrement"
	+"province_name text"
	+"province_code text)";
	/*City建表语句*/
	private static final String CREATE_CITY = "creat table city("
	+"id integer primary key autoincrement"
	+"city_name text"
	+"city_code text)";
	/*County建表语句*/
	private static final String CREATE_COUNTY = "creat table county("
	+"id integer primary key autoincrement"
	+"county_name text"
	+"county_code text)";
	//构造函数，用于生成对象，调用WritableDatabase()打开创建数据库
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	//创建数据库时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}
	//更新数据库时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
