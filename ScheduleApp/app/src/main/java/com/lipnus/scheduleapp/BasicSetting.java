package com.lipnus.scheduleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class BasicSetting extends AppCompatActivity {

    //배경화면을 담당할 FrameLayout
    FrameLayout backgroundFrameLayout;

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //웹으로부터 받는 데이터
    //==============================================================================================
    int todayWork; //오늘 무엇을 할지 [ 기획:1 / 디자인:2 / 개발:3 / 테스트:4 ]
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbs);


        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todaybutton);
        wbsBtn.setBackgroundResource(R.drawable.wbsclick);
        iaBtn.setBackgroundResource(R.drawable.iabutton);
        testBtn.setBackgroundResource(R.drawable.testbutton);
        moreBtn.setBackgroundResource(R.drawable.morebutton);
        //===========================================================

        //배경화면 설정
        backgroundFrameLayout = (FrameLayout)findViewById(R.id.backgroundFrameLayout);
        backgroundFrameLayout.setBackgroundResource( CustomApplication.checkTodayWork() );


    }

    //==============================================================================================
    //하단 메뉴
    //==============================================================================================
    public void onClick_menu(View v){

        CustomApplication CusApp = (CustomApplication) getApplication();
        CusApp.controlBottomMenu(v.getId());
        overridePendingTransition(R.anim.noting, R.anim.noting);
    }
}
