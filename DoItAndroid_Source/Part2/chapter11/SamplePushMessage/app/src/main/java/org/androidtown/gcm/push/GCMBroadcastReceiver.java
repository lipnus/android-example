package org.androidtown.gcm.push;

import java.net.URLDecoder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * 푸시 메시지를 받는 Receiver 정의
 * 
 * @author Mike
 *
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
	private static final String TAG = "GCMBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {		//상대방이 메시지 보낼때  intent의 부가적인 정보로 사용
		String action = intent.getAction();	
		Log.d(TAG, "action : " + action);
		
		if (action != null) {
			if (action.equals("com.google.android.c2dm.intent.RECEIVE")) { // 푸시 메시지 수신 시
				String from = intent.getStringExtra("from");
				String command = intent.getStringExtra("command");		// 서버에서 보낸 command 라는 키의 value 값 
				String type = intent.getStringExtra("type");		// 서버에서 보낸 type 라는 키의 value 값 
				String rawData = intent.getStringExtra("data");		// 서버에서 보낸 data 라는 키의 value 값
				String data = "";
				try {
					data = URLDecoder.decode(rawData, "UTF-8");
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				Log.v(TAG, "from : " + from + ", command : " + command + ", type : " + type + ", data : " + data);
				
				// 액티비티로 전달
				sendToActivity(context, from, command, type, data);
				
			} else {
				Log.d(TAG, "Unknown action : " + action);
			}
		} else {
			Log.d(TAG, "action is null.");
		}
		
	}

	/**
	 * 메인 액티비티로 수신된 푸시 메시지의 데이터 전달
	 * 
	 * @param context
	 * @param command
	 * @param type
	 * @param data
	 */
	private void sendToActivity(Context context, String from, String command, String type, String data) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("from", from);
		intent.putExtra("command", command);
		intent.putExtra("type", type);
		intent.putExtra("data", data);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

		context.startActivity(intent);
	}

}
