package com.lipnus.kum_connect_image_db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class VolleyClassTest extends AppCompatActivity {

    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_class_test);

        tV = (TextView) findViewById(R.id.volleyTv);


        initVolleyCallback();
        String url = "http://kumchurk.ivyro.net/test2/connect_check.php";
        Map<String, String> params = new HashMap<>();
        params.put("request", "밥은먹고다니냐");
        volley = new VolleyConnect(mResultCallback, this, url, params);

//        이 방법이 소스가 더 짧긴한데...
//        나중에 헷갈릴것 같아서 그냥 필요한 엑티비티마다 볼리 붙여서 만드는게 나을듯..


    }

    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                tV.setText(response);
                Log.d("VOVO", "결과: "+ response);
            }

            @Override
            public void notifyError(VolleyError error) {
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }
}
