package com.big_whiteweather.service;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.big_whiteweather.receiver.AutoUpdateReceiver;
import com.big_whiteweather.util.HttpCallbackListener;
import com.big_whiteweather.util.HttpUtil;
import com.big_whiteweather.util.MyApplication;
import com.big_whiteweather.util.Utility;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends IntentService {
	// IntentService 是安卓中专门提供的类 可以帮助开启线程还有服务在运行结束后会自动停止
	public AutoUpdateService() {
		super("AutoUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		updateWeather();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000; // 8小时秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent intent2 = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

	}

	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		RequestQueue mQueue = MyApplication.getRequestQueue();
		StringRequest stringRequest = new StringRequest(address,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Utility.handleWeatherResponse(getApplicationContext(),
								response);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
		mQueue.add(stringRequest);
	}
	/*
	 * HttpUtil.SendHttpRequest(address, new HttpCallbackListener() {
	 * 
	 * @Override public void onFinish(String response) {
	 * Utility.handleWeatherResponse(AutoUpdateService.this, response); }
	 * 
	 * @Override public void onError(Exception e) { e.printStackTrace(); } }); }
	 */
	/*
	 * @Override public int onStartCommand(Intent intent, int flags, int
	 * startId) { new Thread(new Runnable() {
	 * 
	 * @Override public void run() { updateWeather();
	 * 
	 * } }).start(); AlarmManager manager =(AlarmManager)
	 * getSystemService(ALARM_SERVICE); int anHour = 8 * 60 * 60 * 1000; //8小时秒数
	 * long triggerAtTime = SystemClock.elapsedRealtime() + anHour; Intent
	 * intent2 = new Intent(this, AutoUpdateReceiver.class); PendingIntent pi =
	 * PendingIntent.getBroadcast(this, 0, intent2, 0);
	 * manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
	 * return super.onStartCommand(intent, flags, startId); }
	 * 
	 * @Override public IBinder onBind(Intent intent) { // TODO Auto-generated
	 * method stub return null; }
	 *//**
	 * 更新天气信息
	 */
	/*
	 * private void updateWeather() { SharedPreferences prefs =
	 * PreferenceManager.getDefaultSharedPreferences(this); String weatherCode =
	 * prefs.getString("weather_code", ""); String address =
	 * "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
	 * HttpUtil.SendHttpRequest(address, new HttpCallbackListener() {
	 * 
	 * @Override public void onFinish(String response) {
	 * Utility.handleWeatherResponse(AutoUpdateService.this, response); }
	 * 
	 * @Override public void onError(Exception e) { e.printStackTrace(); } }); }
	 */
}
