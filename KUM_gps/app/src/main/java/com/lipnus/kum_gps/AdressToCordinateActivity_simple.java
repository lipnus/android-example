package com.lipnus.kum_gps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class AdressToCordinateActivity_simple extends AppCompatActivity {

    TextView tv;
    EditText et3;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_to_cordinate_simple);

        //    지오코딩(GeoCoding) : 주소,지명 => 위도,경도 좌표로 변환
        //    위치정보를 얻기위한 권한을 획득, AndroidManifest.xml
        //    ACCESS_FINE_LOCATION : 현재 나의 위치를 얻기 위해서 필요함
        //    INTERNET : 구글서버에 접근하기위해서 필요함

        tv = (TextView) findViewById(R.id.textView4); // 결과창
        et3 = (EditText)findViewById(R.id.editText3);
        geocoder = new Geocoder(this);

    } // end of onCreate




    //주소를 위도와 경도로 바꿈
    public void onClick(View v){

        List<Address> list = null;
        String str = et3.getText().toString();
        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                tv.setText("해당되는 주소 정보는 없습니다");
            } else {
                tv.setText("위도: "+ list.get(0).getLatitude() + "\n경도: " + list.get(0).getLongitude());
            }
        }
    } //end of onClick


    //두 좌표 사이의 거리 구하기
    public void onClick2(View v){
        String distance = calcDistance(37.5902947f, 127.0214863, 37.5870906f, 127.0294484);
        tv.setText("거리: " + distance);
    }


    //두 좌표 사이의 거리
    public static String calcDistance(double lat1, double lon1, double lat2, double lon2){
        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;
        EARTH_R = 6371000.0;
        Rad = Math.PI/180;
        radLat1 = Rad * lat1;
        radLat2 = Rad * lat2;
        radDist = Rad * (lon1 - lon2);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);
        double rslt = Math.round(Math.round(ret) / 1000);
        String result = rslt + " km";

        if(rslt == 0) result = Math.round(ret) +" m";

        return result;
    }


} // end of class
