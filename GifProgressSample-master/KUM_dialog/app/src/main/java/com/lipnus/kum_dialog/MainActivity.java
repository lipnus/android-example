package com.lipnus.kum_dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        customDialog = new CustomDialog(this);

        // 위에서 테두리를 둥글게 했지만 다이얼로그 자체가 네모라 사각형 여백이 보입니다. 아래 코드로 다이얼로그 배경을 투명처리합니다.
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        customDialog.show(); // 보여주기
        customDialog.setCancelable(false);//끌 수 없다
//        customDialog.setCanceledOnTouchOutside(true);//밖을 클릭했을 때 닫을 수도 없다
        customDialog.show(); // 보여주기
//        mProgressDialog.dismiss(); // 없애기

    }
}
