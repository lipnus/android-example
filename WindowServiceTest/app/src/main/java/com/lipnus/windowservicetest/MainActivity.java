package com.lipnus.windowservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){

        // 인텐트 객체를 만듭니다.
        Intent myIntent = new Intent(this, WindowService.class);

        // 서비스를 시작합니다.
        startService(myIntent);

        // 인텐트 객체를 만듭니다.
        Intent smyIntent = new Intent(this, MyService.class);

        // 서비스를 시작합니다.
        startService(smyIntent);
    }
}
