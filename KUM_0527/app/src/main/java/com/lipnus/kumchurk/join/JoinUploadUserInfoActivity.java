package com.lipnus.kumchurk.join;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.IntroActivity;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;

import java.util.HashMap;
import java.util.Map;

public class JoinUploadUserInfoActivity extends AppCompatActivity {

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //volley, 리스너
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_userinfo);

        Log.d("URUR", "[JoinLastActivity]"
                + GlobalApplication.getUser_id() + ", "
                + GlobalApplication.getUser_nickname() + ", "
                + GlobalApplication.getUser_image() + ", "
                + GlobalApplication.getUser_thumbnail() + ", " );

        //volley callback
        initVolleyCallback();

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        //connect (사용자정보를 서버로 전송)
        connect();
    }


    public void connect(){
        Log.d("VOVO", "접속...");
        String url = "http://kumchurk.ivyro.net/app/upload_userinfo.php";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id());
        params.put("user_nickname", GlobalApplication.getUser_nickname());
        params.put("user_image", GlobalApplication.getUser_image());
        params.put("user_thumbnail", GlobalApplication.getUser_thumbnail());

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }//volley 접속(회원정보 업로드)
    public void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                //전송의 결과를 받는 부분
                Log.d("MAINPAGE", response);

                Intent iT = new Intent(getApplicationContext(), IntroActivity.class);
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
    }//volley 콜백(회원정보 업로드)


}
