package com.lipnus.windowservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 서비스 클래스 정의
 * 
 * @author Mike
 */
public class MyService extends Service  {
	/**
	 * 디버깅을 위한 태그
	 */
	public static final String TAG = "MyService";
	
	/**
	 * 반복 횟수
	 */
	private int count = 0;
	
	/**
	 * 서비스 객체 생성 시 자동 호출됩니다.
	 */
	public void onCreate() {
		super.onCreate();

		Log.d("SSSS", "실행sssOK");
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
