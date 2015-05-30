package com.big_whiteweather.activity;
import com.big_whiteweather.R;
import com.big_whiteweather.receiver.AutoUpdateService;
import com.big_whiteweather.util.HttpCallbackListener;
import com.big_whiteweather.util.HttpUtil;
import com.big_whiteweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	
	private TextView cityNameText;
	
	private TextView publishText;
	
	private TextView weatherDespText;
	
	private TextView temp1Text;
	
	private TextView temp2Text;
	
	private TextView currentDateText;
	
	private Button switchCity;
	
	private Button refreshWeather;
	
	//private RelativeLayout backGround ;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	 setContentView(R.layout.weather_layout);
	// android.app.ActionBar actionBar = getActionBar();
//	 actionBar.setDisplayHomeAsUpEnabled(true);
	 weatherInfoLayout = (LinearLayout) this.findViewById(R.id.weather_info_layout);
	 cityNameText = (TextView) this.findViewById(R.id.city_name);
	 publishText = (TextView) this.findViewById(R.id.publish_text);
	 weatherDespText = (TextView) this.findViewById(R.id.weather_desp);
	 temp1Text = (TextView) this.findViewById(R.id.temp1);
	 temp2Text = (TextView) this.findViewById(R.id.temp2);
	 currentDateText = (TextView) this.findViewById(R.id.current_date);
	 switchCity = (Button) this.findViewById(R.id.switch_city);
	 refreshWeather = (Button) this.findViewById(R.id.refresh_weather);
	// backGround = (RelativeLayout) this.findViewById(R.id.weather_background);
	 String countryCode = getIntent().getStringExtra("country_code");
	 switchCity.setOnClickListener(this);
	 refreshWeather.setOnClickListener(this);
	 
	 if(!TextUtils.isEmpty(countryCode)){
		 //有县级代号时就去查询天气
		 publishText.setText("同步中...");
		 weatherInfoLayout.setVisibility(View.INVISIBLE);
		 cityNameText.setVisibility(View.INVISIBLE);
		 queryWeatherCode(countryCode);
	 }
	 else{
		 //没有县级代号直接显示本地天气
		 showWeather();
	 }
}


 private void queryWeatherCode(String countryCode) {
	String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
	queryFromServer(address , "countryCode");
	
}
private  void queryWeatherInfo(String weatherCode){
	String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
	queryFromServer(address, "weatherCode");
}

private void queryFromServer(final String address, final String type) {
	HttpUtil.SendHttpRequest(address, new HttpCallbackListener() {
		
		@Override
		public void onFinish(final String response) {
			if("countryCode".equals(type)){
				if(!TextUtils.isEmpty(response)){
					//从服务器返回的数据中解析到出天气代码
					String[] array = response.split("\\|");
					if(array !=null && array.length ==2){
						String weatherCode = array[1];
						queryWeatherInfo(weatherCode);
					}
				}
			}else if("weatherCode".equals(type)){
				//处理服务器返回的天气信息
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new  Runnable() {
					@Override
					public void run() {
						showWeather();
					}
				});
			}
			
		}
		
		@Override
		public void onError(Exception e) {
		runOnUiThread(new  Runnable() {
			public void run() {
				publishText.setText("同步失败");
			}
		});
			
		}
	});
	
}

/**
  * 
  */
 
private void showWeather() {
	SharedPreferences spfs = PreferenceManager.getDefaultSharedPreferences(this);
	cityNameText.setText(spfs.getString("city_name", ""));
	temp1Text.setText(spfs.getString("temp1", ""));
	temp2Text.setText(spfs.getString("temp2", ""));
	weatherDespText.setText(spfs.getString("weather_desp", ""));
	publishText.setText("今天"+spfs.getString("publish_time", "")+"发布");
	currentDateText.setText(spfs.getString("current_date", ""));
	weatherInfoLayout.setVisibility(View.VISIBLE);
	cityNameText.setVisibility(View.VISIBLE);
	Intent intent = new Intent(this, AutoUpdateService.class);
	startService(intent);
}

/**@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}


@Override
public boolean onOptionsItemSelected(MenuItem item) {
	
	int id = item.getItemId();
	if (id == R.id.home) {
		return true;
	}
	if (id == R.id.refresh_weather){
		return true;
	}
	return super.onOptionsItemSelected(item);
}
*/
@Override
public void onClick(View v) {
	switch(v.getId()){
	case R.id.switch_city:
		Intent intent = new Intent(this, ChooseAreaActivity.class);
		intent.putExtra("from_weather_activtiy", true);
		startActivity(intent);
		finish();
		break;
	case R.id.refresh_weather:
		publishText.setText("同步中...");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		if(!TextUtils.isEmpty(weatherCode)){
			queryWeatherInfo(weatherCode);
		}
	    break;
	    default:
	    	break;
	}
	
}
 
 
}
