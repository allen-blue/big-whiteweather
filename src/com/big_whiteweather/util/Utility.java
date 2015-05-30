package com.big_whiteweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.big_whiteweather.db.BigWhiteWeatherDB;
import com.big_whiteweather.model.City;
import com.big_whiteweather.model.Country;
import com.big_whiteweather.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {
	/**
	 * ����������������ص�ʡ������
	 */
   public synchronized static boolean handleProvincesResponse(BigWhiteWeatherDB bigWhiteWeatherDB , String response){
	   if(!TextUtils.isEmpty(response)){
		   String[] allProvinces = response.split(",");
		   if(allProvinces != null && allProvinces.length > 0){
		   for(String p : allProvinces){
			   String [] array = p.split("\\|");
			   Province province = new Province();
			   province.setProvinceCode(array[0]);
			   province.setProvinceName(array[1]);
			   //������������ݴ洢������
			   bigWhiteWeatherDB.saveProvince(province);
		   }
		   return true;
	   }
	   } 
	return false;	   
   }
   
   
   
   /**
    * ����������������ص��м�����
    */
   public  static boolean handleCitiesResponse(BigWhiteWeatherDB bigWhiteWeatherDB , String response , int provinceId){
	   if(!TextUtils.isEmpty(response)){
		   String[] allCities = response.split(",");
		   if(allCities != null && allCities.length > 0){
		   for(String c : allCities){
			   String [] array = c.split("\\|");
			   City city = new City();
			   city.setCityCode(array[0]);
			   city.setCityName(array[1]);
			   city.setProvinceId(provinceId);
			   //������������ݴ洢������
			   bigWhiteWeatherDB.saveCity(city);
		   }
		   return true;
	   }
	   } 

	return false;	   
   }
   
   
   /**
    * ����������������ص��ؼ�����
    */
   public  static boolean handleCountriesResponse(BigWhiteWeatherDB bigWhiteWeatherDB , String response , int cityId){
	   if(!TextUtils.isEmpty(response)){
		   String[] allCountries = response.split(",");
		   if(allCountries != null && allCountries.length > 0){
		   for(String c : allCountries){
			   String [] array = c.split("\\|");
			   Country country = new Country();
			   country.setCountryCode(array[0]);
			   country.setCountryName(array[1]);
			   country.setCityId(cityId);
			   //������������ݴ洢������
			   bigWhiteWeatherDB.saveCountry(country);
		   }
		   return true;
	   }
	   } 
	return false;	   
   }
   /**
    * �������������ص�json���ݣ����������������ݱ��浽����
    */
   public static void handleWeatherResponse(Context context , String response){
	   try{
		   JSONObject jsonObject = new JSONObject(response);
		   JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
		   String cityName = weatherInfo.getString("city");
		   String weatherCode = weatherInfo.getString("cityid");
		   String temp1 = weatherInfo.getString("temp1");
		   String temp2 = weatherInfo.getString("temp2");
		   String weatherDesp = weatherInfo.getString("weather");
		   String publishTime = weatherInfo.getString("ptime");
		   saveWeatherInfo(context , cityName , weatherCode , temp1 , temp2 , weatherDesp , publishTime);
	   }
	   catch(JSONException e){
		   e.printStackTrace();
	   }
   }

/**
 * �����������ص���������Ԥ����Ϣ�洢��SharePreferences
 * @param context
 * @param cityName
 * @param weatherCode
 * @param temp1
 * @param temp2
 * @param weatherDesp
 * @param publishTime
 */

private static void saveWeatherInfo(Context context, String cityName,
		String weatherCode, String temp1, String temp2, String weatherDesp,
		String publishTime) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
	editor.putBoolean("city_selected", true);
	editor.putString("city_name", cityName);
	editor.putString("weather_code", weatherCode);
	editor.putString("temp1", temp2);
	editor.putString("temp2", temp1);
	editor.putString("weather_desp", weatherDesp);
	editor.putString("publish_time", publishTime);
	editor.putString("current_date", sdf.format(new Date()));
	editor.commit();
	
}
}
