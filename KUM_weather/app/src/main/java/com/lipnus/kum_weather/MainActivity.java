package com.lipnus.kum_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //volley와 리스너
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;
    Weather weatherData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVolleyCallback();
        connect();

        Intent iT = new Intent(this, SecondActivity.class);
        startActivity(iT);

    }

    //volley 접속 - (1)
    public void connect(){
        Log.d("VOVO", "접속...");
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=37.56826&lon=126.977829&APPID=2aafb65d33ba4042d1b90190605c6829";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("", "");



        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    //volley 콜백 - (2)
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("VOVO", response);
                jsonToJava(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }


    //다운받은 Json데이터들을 객체화 - (3)
    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        Gson gson = new Gson();

        weatherData = gson.fromJson(jsonStr, Weather.class);

        Log.d("VOVO", "온도: " + weatherData.main.temp );



    }
}
