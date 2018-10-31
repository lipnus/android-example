package com.lipnus.pd4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BaseActivity extends AppCompatActivity {

    //네비게이션 버튼들
    ImageView naveMenu1, naveMenu2, naveMenu3, naveMenu4;
    ImageView naviGradiation;

    //상단 타이틀 이름
    TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //상단제목
        titleTv = (TextView) findViewById(R.id.title_tv);

        //네비게이션 버튼 객체들 등록
        naveMenu1 = (ImageView) findViewById(R.id.navi_menu1);
        naveMenu2 = (ImageView) findViewById(R.id.navi_menu2);
        naveMenu3 = (ImageView) findViewById(R.id.navi_menu3);
        naveMenu4 = (ImageView) findViewById(R.id.navi_menu4);
        naviGradiation = (ImageView) findViewById(R.id.navi_gardation_iv);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int pxWidth  = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        float dipWidth  = displayMetrics.widthPixels  / displayMetrics.density;
        float dipHeight = displayMetrics.heightPixels / displayMetrics.density;


        Log.d("SSSS", pxWidth + " " + pxHeight + " " + dipWidth+ " " +dipHeight );

        setting_navi();


    }

    //네비게이션 그림 세팅
    public void setting_navi(){

        Glide.with(this)
                .load(R.drawable.order2)
                .into(naveMenu1);

        Glide.with(this)
                .load(R.drawable.stamp2)
                .into(naveMenu2);

        Glide.with(this)
                .load(R.drawable.translation2)
                .into(naveMenu3);

        Glide.with(this)
                .load(R.drawable.mailbox2)
                .into(naveMenu4);

        Glide.with(this)
                .load(R.drawable.gradation)
                .into(naviGradiation);

    }

    //네비게이션 메뉴
    public void onClick_navimnu(View v){

        Intent iT = null;

        switch ( v.getId() ){
            case R.id.navi_menu1:
                iT = new Intent(this, OrderActivity.class);
                break;

            case R.id.navi_menu2:
                iT = new Intent(this, StampActivity.class);
                break;

            case R.id.navi_menu3:
                iT = new Intent(this, TranslationActivity.class);
                break;

            case R.id.navi_menu4:
                iT = new Intent(this, MessageActivity.class);
                break;
        }

        startActivity(iT);
    }
}
