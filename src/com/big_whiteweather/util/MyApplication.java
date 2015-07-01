package com.big_whiteweather.util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

public class MyApplication extends Application {

	private static Context context;
	private static RequestQueue mQueue;
	private static ImageLoader mImLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		mQueue = Volley.newRequestQueue(context);
		mImLoader = new ImageLoader(mQueue, new ImageCache() {
			
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public static Context getContext() {
		return context;
	}
	public static RequestQueue getRequestQueue(){
		return mQueue;
	}
	public static ImageLoader getImageLoader(){
		return mImLoader;
	}
}
