package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	/*Province�������*/
	private static final String CREATE_PROVINCE = "creat table province("
	+"id integer primary key autoincrement"
	+"province_name text"
	+"province_code text)";
	/*City�������*/
	private static final String CREATE_CITY = "creat table city("
	+"id integer primary key autoincrement"
	+"city_name text"
	+"city_code text)";
	/*County�������*/
	private static final String CREATE_COUNTY = "creat table county("
	+"id integer primary key autoincrement"
	+"county_name text"
	+"county_code text)";
	//���캯�����������ɶ��󣬵���WritableDatabase()�򿪴������ݿ�
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	//�������ݿ�ʱ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}
	//�������ݿ�ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
