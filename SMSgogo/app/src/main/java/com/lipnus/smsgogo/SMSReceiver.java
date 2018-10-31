package com.lipnus.smsgogo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    //리시버가 메시지를 받았을때 실행되는 메소드
    @Override
    public void onReceive(Context context, Intent intent){

        Toast.makeText(context, "리시버작동", Toast.LENGTH_LONG).show();
        if(intent.getAction().equals("android.provider.Telephoney.SMS_RECEIVED")){
            Toast.makeText(context, "리시버작동!!", Toast.LENGTH_LONG).show();
        }

        //Intent iT = new Intent(context, DisplayActivity.class);
        //context.startActivity(iT);
    }
}
