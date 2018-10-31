package com.lipnus.webconnecttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTest extends AppCompatActivity {

    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.txtView);





        StringBuffer sb = new StringBuffer();

        String str =
                "[{'name':'배트맨','age':43,'address':'고담'},"+
                        "{'name':'슈퍼맨','age':36,'address':'뉴욕'},"+
                        "{'name':'앤트맨','age':25,'address':'LA'}]";

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성

            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String address = jObject.getString("address");
                String name = jObject.getString("name");
                int age = jObject.getInt("age");

                sb.append(
                        "주소:" + address +
                                "이름:" + name +
                                "나이:" + age + "\n"
                );
            }
            tV.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getJson(String JsonStr) {

        StringBuffer sb = new StringBuffer();

        String str =
                "[{'name':'배트맨','age':43,'address':'고담'},"+
                        "{'name':'슈퍼맨','age':36,'address':'뉴욕'},"+
                        "{'name':'앤트맨','age':25,'address':'LA'}]";

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String address = jObject.getString("address");
                String name = jObject.getString("name");
                int age = jObject.getInt("age");

                sb.append(
                        "주소:" + address +
                                "이름:" + name +
                                "나이:" + age + "\n"
                );
            }
            tV.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

