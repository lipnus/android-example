package com.lipnus.test_push;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FCM을 log에 표시
        FirebaseInstanceId.getInstance().getToken();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_Token", token);


    }
}
