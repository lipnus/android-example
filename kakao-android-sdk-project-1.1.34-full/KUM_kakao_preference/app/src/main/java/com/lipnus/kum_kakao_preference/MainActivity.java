package com.lipnus.kum_kakao_preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){

    }

    public void onClick2(View v){

    }
}
