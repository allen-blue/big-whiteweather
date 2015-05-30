package com.big_whiteweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.big_whiteweather.db.BigWhiteWeatherDB;
import com.big_whiteweather.model.City;
import com.big_whiteweather.model.Country;
import com.big_whiteweather.model.Province;
import com.big_whiteweather.util.HttpCallbackListener;
import com.big_whiteweather.util.HttpUtil;
import com.big_whiteweather.util.Utility;
import com.big_whiteweather.R;









import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	
	
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private BigWhiteWeatherDB bigWhiteWeatherDB;
	private boolean isFromWeatherActivity;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * 省列表
	 */
	private List<Province> provinceList; 
	/**
	 * 市列表
	 */
	private List<City> cityList; 
	/**
	 * 县列表
	 */
	private List<Country> countryList;
	/**
	 * 当前选中的省份
	 */
	private Province selectedProvince;
	/**
	 * 选中的市
	 */
	private City selectedCity;
	/**
	 * 当前级别
	 */
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activtiy", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//已经选择了城市不是从weatheractivity中跳转过来，才会直接跳转到weatheractivity
		if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) this.findViewById(R.id.list_view);
		titleText = (TextView) this.findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		bigWhiteWeatherDB = BigWhiteWeatherDB.getInstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int index, long arg3) {
				
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					queryCities();
				}
				else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(index);
					queryCountries();
					}
				else if(currentLevel == LEVEL_COUNTRY){
					String countryCode = countryList.get(index).getCountryCode();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("country_code", countryCode);
					startActivity(intent);
					finish();
				}
			}			
		});
		queryProvinces();		
	}
/**
 * 查询全国所有的省，优先从数据库中查询。如果没有再去服务器中查
 */
	private void queryProvinces() {
	   provinceList = bigWhiteWeatherDB.LoadProvinces();
	   if(provinceList.size() > 0){
		   dataList.clear();
		   for(Province province : provinceList){
			   dataList.add(province.getProvinceName());
		   }
		   adapter.notifyDataSetChanged();
		   listView.setSelection(0);
		   titleText.setText("中国");
		   currentLevel = LEVEL_PROVINCE;
	   }else{
		   queryFromSever(null, "province");
	   }
		
	}
	private void queryCities() {
		cityList = bigWhiteWeatherDB.LoadCitys(selectedProvince.getId());
		   if(cityList.size() > 0){
			   dataList.clear();
			   for(City city : cityList){
				   dataList.add(city.getCityName());
			   }
			   adapter.notifyDataSetChanged();
			   listView.setSelection(0);
			   titleText.setText(selectedProvince.getProvinceName());
			   currentLevel = LEVEL_CITY;
	}else{
		queryFromSever(selectedProvince.getProvinceCode(), "city");
	}
	}
	private void queryCountries() {
		countryList = bigWhiteWeatherDB.LoadCountries(selectedCity.getId());
		   if(countryList.size() > 0){
			   dataList.clear();
			   for(Country country : countryList){
				   dataList.add(country.getCountryName());
			   }
			   adapter.notifyDataSetChanged();
			   listView.setSelection(0);
			   titleText.setText(selectedCity.getCityName());
			   currentLevel = LEVEL_COUNTRY;	
	}else{
		queryFromSever(selectedCity.getCityCode(), "country");
		
	}
}
	/**
	 * 根据查询类型及代码从服务器中查询
	 */
	private void queryFromSever(final String code, final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}
		else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
	    HttpUtil.SendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(bigWhiteWeatherDB, response);
				}
				else if("city".equals(type)){
					result = Utility.handleCitiesResponse(bigWhiteWeatherDB, response, selectedProvince.getId());
				}
				else if("country".equals(type)){
					result = Utility.handleCountriesResponse(bigWhiteWeatherDB, response, selectedCity.getId());
				}
				if(result){
					//通runonUIThread()方法回到主线程
					runOnUiThread(new  Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("country".equals(type)){
								queryCountries();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				//通过runonUiThread方法回到主线程逻辑
				runOnUiThread(new  Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_LONG).show();
					}
					
				});
				
			}
		});
	}
	/**
	 * 显示进度对话框	
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载。。。。");
			progressDialog.setCanceledOnTouchOutside(false);
			//progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);显示进度条
			//progressDialog.setMax(100);  
		}
		progressDialog.show();
	}
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		
		}
	}
	/**
	 * 通过BACK键判断返回的 的是什么 还是退出
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel ==LEVEL_CITY){
			queryProvinces();
		}
		else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
		//super.onBackPressed();
	}
}
