package com.lipnus.kum_volleysystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //volley를 하나의 클래스로 만들어서 다른 액티비티들에서 편하게 접근하여 사용할 수 있도록 만든 코드
    //아래의 사이트를 참조하였음
    //http://stackoverflow.com/questions/35628142/how-to-make-separate-class-for-volley-library-and-call-all-method-of-volley-from
    //다른 곳으로 옮길 때 manifests와 gradle을 확인할것


    //콜백함수
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.volleyTv);


        //콜백함수
        initVolleyCallback();


        //전송에 필요한 것은 아래의 다섯줄이 끝
        String url = "http://kumchurk.ivyro.net/test2/connect_check.php";
        Map<String, String> params = new HashMap<>();
        params.put("request", "볼리를 쉽게 쓰자");

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);



    }

    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                tV.setText(response);
                Log.d("VOVO", "결과: "+ response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }
}
