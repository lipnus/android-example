package com.lipnus.kum_gson_structure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lipnus.kum_gson_structure.com.lipnus.data.MenuInfo_JSON;
import com.lipnus.kum_gson_structure.com.lipnus.data.Menu_List;
import com.lipnus.kum_gson_structure.com.lipnus.data.Menu_Recommend;
import com.lipnus.kum_gson_structure.com.lipnus.data.Menu_Review;
import com.lipnus.kum_gson_structure.com.lipnus.data.Res_Info;
import com.lipnus.kum_gson_structure.com.lipnus.volley.IVolleyResult;
import com.lipnus.kum_gson_structure.com.lipnus.volley.VolleyConnect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //콜백함수
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.Tv);

        //Volley 콜백함수
        initVolleyCallback();

    }

    public void onClick(View v){
        makeJson();
    }
    public void onClick_downJSON(View v){
        Connect();
    }
    public void onClick_write(View v){
        Intent i = new Intent(this, WriteActivity.class);
        startActivity(i);
    }
    public void onClick_date(View v){

        //오늘
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMddHHmm", Locale.KOREA );
        java.util.Date currentTime = new java.util.Date ( );
        String todayDate = mSimpleDateFormat.format(currentTime);

        tV.setText("지금: " + todayDate);
    }


    void jsonToJava(String jsonStr){
        Gson gson = new Gson();

        MenuInfo_JSON menuInfo = gson.fromJson(jsonStr, MenuInfo_JSON.class);

        List<Menu_Review> mR = menuInfo.menuReview;
        tV.setText("추출된 아이디: " + mR.get(0).user_id);
        tV.append("\n\n\n"+jsonStr);
    }

    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
//                tV.setText(response);
                Log.d("VOVO", "결과: "+ response);

                jsonToJava(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }

    public void Connect(){
        //전송에 필요한 것은 아래의 다섯줄이 끝
        String url = "http://kumchurk.ivyro.net/app/download_menupage.php";
        Map<String, String> params = new HashMap<>();

        params.put("menu_name", "목살스테이크샐러드");
        params.put("res_name", "매스플레이트");

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    //json String을 만든다
    public void makeJson(){
        Gson gson = new Gson();

        //위에서부터 순서대로임
        List<Menu_Review> menu_review = new ArrayList<Menu_Review>();
        List<Res_Info> res_info = new ArrayList<Res_Info>();
        List<Menu_List> menu_list = new ArrayList<Menu_List>();
        List<Menu_Recommend> menu_recommend = new ArrayList<Menu_Recommend>();

        menu_review.add(new Menu_Review(1,"", "", 2, 3,"a4",5,"a6","a7","a8","a9","a10"));
        res_info.add( new Res_Info("b1","b2","b3","b4","b5", 6, 7, 8, "b9","b10","b11"));
        menu_list.add( new Menu_List("c1", "c2", 3, 4, 5, "c6", "c7", 8,9,10,"c11"));
        menu_recommend.add( new Menu_Recommend("d1", "d2", 3, 4, 5, "d6", "d7", 8,9,10, "d11") );

        String result = gson.toJson( new MenuInfo_JSON(menu_review, res_info, menu_list, menu_recommend) );

        tV.setText(result);
        Log.d("JSJS", ""+result);
    }
}
