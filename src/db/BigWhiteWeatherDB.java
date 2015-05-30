package db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.City;
import model.Country;
import model.Province;

public class BigWhiteWeatherDB {
	/**
	 * 数据库名
	 
	 */
	public static final String DB_NAME = "big_white_weather";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static BigWhiteWeatherDB bigWhiteWeatherDB;
	private SQLiteDatabase db;
	/**
	 * 将构造方法私有化
	 */
	private BigWhiteWeatherDB(Context context){
		BigWhiteWeatherOpenHelper dbHelper = new BigWhiteWeatherOpenHelper(context, DB_NAME, null, 1, null);
		db =dbHelper.getWritableDatabase();
		
	}
	
	/**
	 * 获取BigWhiteWeather的实例
	 */
	public synchronized static BigWhiteWeatherDB getInstance(Context context){
		if(bigWhiteWeatherDB == null){
			bigWhiteWeatherDB = new BigWhiteWeatherDB(context);
		}
		return bigWhiteWeatherDB;
	}
	
	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province){
		if(province !=null){
			ContentValues values =new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
		
	}
	
	/*
	 * 从数据库读取全国所有的省份
	 */
	public List<Province> LoadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getColumnIndex("id"));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	
	/**
	 * 将City实例存储到数据库
	 */
	public void saveCity(City city){
		if(city !=null){
			ContentValues values =new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			db.insert("City", null, values);
		}
		
	}
	
	/*
	 * 从数据库读取某省下所有的城市信息。
	 */
	public List<City> LoadCitys(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getColumnIndex("id"));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	/**
	 * 将Country实例存储到数据库。
	 */
	public void saveCountry(Country country){
		if(country !=null){
			ContentValues values =new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			db.insert("Country", null, values);
		}
		
	}
	
	/*
	 * 从数据库读取某城市下所有县的信息。
	 */
	public List<Country> LoadCountries(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setId(cursor.getColumnIndex("id"));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				list.add(country);
			}while(cursor.moveToNext());
			
		}
		return list;
	}
	
	
	


}
