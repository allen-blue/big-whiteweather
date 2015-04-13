package com.big_whiteweather.db;

import java.util.ArrayList;
import java.util.List;

import com.big_whiteweather.model.City;
import com.big_whiteweather.model.Country;
import com.big_whiteweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BigWhiteWeatherDB {
	/**
	 * ���ݿ���
	 
	 */
	public static final String DB_NAME = "big_white_weather";
	
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	private static BigWhiteWeatherDB bigWhiteWeatherDB;
	private SQLiteDatabase db;
	/**
	 * �����췽��˽�л�
	 */
	private BigWhiteWeatherDB(Context context){
		//
		BigWhiteWeatherOpenHelper dbHelper = new BigWhiteWeatherOpenHelper(context, DB_NAME, null, 1);
		db =dbHelper.getWritableDatabase();
		
		
	}
	
	/**
	 * ��ȡBigWhiteWeather��ʵ��
	 */
	public synchronized static BigWhiteWeatherDB getInstance(Context context){
		if(bigWhiteWeatherDB == null){
			bigWhiteWeatherDB = new BigWhiteWeatherDB(context);
		}
		return bigWhiteWeatherDB;
	}
	
	/**
	 * ��Provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province){
		if(province !=null){
			ContentValues values =new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
	   // province.setProvinceName(province.getProvinceName());
		//...
		//    province.save();
		}
		
	}
	
	/*
	 * �����ݿ��ȡȫ�����е�ʡ��
	 */
	public List<Province> LoadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor != null && cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	
	/**
	 * ��Cityʵ���洢�����ݿ�
	 */
	public void saveCity(City city){
		if(city !=null){
			ContentValues values =new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
		
	}
	
	/*
	 * �����ݿ��ȡĳʡ�����еĳ�����Ϣ��
	 */
	public List<City> LoadCitys(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[] {String.valueOf(provinceId)}, null, null, null);
		if(cursor != null && cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	/**
	 * ��Countryʵ���洢�����ݿ⡣
	 */
	public void saveCountry(Country country){
		if(country !=null){
			ContentValues values =new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}
		
	}
	
	/*
	 * �����ݿ��ȡĳ�����������ص���Ϣ��
	 */
	public List<Country> LoadCountries(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id = ?", new String[] {String.valueOf(cityId)}, null, null, null);
		if(cursor != null && cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				list.add(country);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	


}
