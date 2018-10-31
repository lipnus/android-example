package com.lipnus.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    Button button1 = null;
    Button button2 = null;
    Button button3 = null;
    TextView tV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button3 = (Button) findViewById(R.id.btn3);
        tV = (TextView) findViewById(R.id.tv2);

        //수신된 번들데이터 추출
        //==========================================================================================
        Intent iT = getIntent();
        Bundle bundleData = iT.getBundleExtra("LOGIN_DATA");

        if(bundleData==null){
            tV.setText("받은 값이 없다");
            return;
        }
        tV.setText("아이디: " + bundleData.getString("STR_ID") + "\n비밀번호: "+bundleData.getString("STR_PW"));
        //==========================================================================================
    }

    public void onClick_Select(View v){

        Intent resultIntent = new Intent();



        switch( v.getId() ){
            case R.id.btn1:{
                resultIntent.putExtra("RETURN", "고객관리");
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            case R.id.btn2:{
                resultIntent.putExtra("RETURN", "매출관리");
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            case R.id.btn3:{
                resultIntent.putExtra("RETURN", "상품관리");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
}
