package com.lipnus.kumchurk.join;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.MainActivity;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;
import com.lipnus.kumchurk.data.main.Main_Data;
import com.lipnus.kumchurk.kum_class.Select_menu;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

public class JoinLastActivity extends AppCompatActivity {

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //volley, 리스너
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_last);

        //volley callback
        initVolleyCallback();

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        //서버로부터 데이터를 받아온다
        connect();
    }




    //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }





    //volley 접속(회원정보 업로드)
    public void connect(){
        Log.d("VOVO", "접속...");
        String url = "http://kumchurk.ivyro.net/app/upanddown_beforemain2.php";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id());
        params.put("search_code", "");



        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    //volley 콜백(회원정보 업로드)
    public void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                //전송의 결과를 받는 부분
                Log.d("MAINPAGE", response);
                jsonToJava(response);

                Intent iT = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(iT);
                finish();
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                connect();
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }

    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        Gson gson = new Gson();

        //==============================================================================
        // = 전체목록은 Maindata에 저장되고, 분류된 목록은 MainData2에 저장 =
        //==============================================================================

        //서버에서 받은 정보
        Main_Data mainData =  gson.fromJson(jsonStr, Main_Data.class);

        //받은 메뉴정보를 정렬
        Select_menu selectMenu = new Select_menu(mainData); //GlobalApplication.Maindata에 업로드도 함
        selectMenu.makeMainList(); //GlobalApplication.Maindata2에 업로드도 함

        //앱의 개인정보를 서버와 동기화
        synchronizeUserInfo();
    }

    //아이디를 서버로 보내서 정보를 받아옴(서버와 앱의 유저정보 동기화)
    public void synchronizeUserInfo(){

//        앱의 사용자정보를 서버와 일치시킴
//        1. 서버에서 받아온 정보를 Application에 넣음(다른 데이터들에 끼워서 함께 받았는데 userinfo정보만 뽑아서 저장)
//        2. Application의 정보를 Preference에 넣음
//
//        이게 있으면, 서버에서 회원정보를 수정하면 그대로 앱에 반영됨
//

        //1.
        GlobalApplication.setUser_nickname( GlobalApplication.getMainData().userInfo.get(0).getUser_nickname() );
        GlobalApplication.setUser_sex( GlobalApplication.getMainData().userInfo.get(0).getUser_sex() );
        GlobalApplication.setUser_grade( GlobalApplication.getMainData().userInfo.get(0).getUser_grade() );
        GlobalApplication.setUser_thumbnail( GlobalApplication.getMainData().userInfo.get(0).getUser_thumbnail() );
        GlobalApplication.setUser_image( GlobalApplication.getMainData().userInfo.get(0).getUser_image() );


        //2.
        editor.putString("user_nickname", GlobalApplication.getUser_nickname() );
        editor.putString("user_sex", GlobalApplication.getUser_sex() );
        editor.putString("user_grade", GlobalApplication.getUser_grade() );
        editor.putString("user_image", GlobalApplication.getUser_image() );
        editor.putString("user_thumbnail", GlobalApplication.getUser_thumbnail() );
        editor.commit();

    }
}
