package org.androidtown.basic.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 서비스 객체를 만들고 시작하는 방법을 알 수 있습니다.
 *
 * 서비스는 애플리케이션 구성요소이므로 매니페스트에 등록하는 것을 잊지말아야 합니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onButton1Clicked(View v) {
        // 인텐트 객체를 만듭니다.
        Intent myIntent = new Intent(this, WindowService.class);

        // 서비스를 시작합니다.
        startService(myIntent);


    }

    public void onClick(View v){
        // 인텐트 객체를 만듭니다.
        Intent myIntent = new Intent(this, MyService.class);

        // 서비스를 시작합니다.
        stopService(myIntent);
    }



}
