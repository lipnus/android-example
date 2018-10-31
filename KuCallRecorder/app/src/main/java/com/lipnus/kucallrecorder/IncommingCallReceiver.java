package com.lipnus.kucallrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class IncommingCallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() 호출");

        if(intent != null){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE); //전화상태
            if(state != null){
                if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    Log.d(TAG, "전화가 왔어");

                }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    Log.d(TAG, "전화를 받았어");

                    String mobile = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    Intent recordIntent = new Intent(context, RecordingService.class);
                    recordIntent.putExtra("command", "startRecording");
                    recordIntent.putExtra("mobile", mobile);
                    context.startService(recordIntent);

                }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    Log.d(TAG, "전화 끊었어");

                    Intent recordIntent = new Intent(context, RecordingService.class);
                    recordIntent.putExtra("command", "stopRecording");
                    context.startService(recordIntent);
                }
            }
        }
    }
}
