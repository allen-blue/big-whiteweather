package com.big_whiteweather.util;

public interface HttpCallbackListener {
	void onFinish(String response);
	
	void onError(Exception e);

}
