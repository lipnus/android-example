package com.lipnus.brochure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Toast;

public class Page2Activity_3 extends FragmentActivity {

    int timeValue = 0;
    int count;


    //현재액티비티
    public static Activity page2Activity_3;

    //객체
    ScrollView sC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_3);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page2Activity_3 = Page2Activity_3.this;

        //객체
        sC = (ScrollView) findViewById(R.id.scrollView);
        sC.post(new Runnable() {
            @Override
            public void run() {
                sC.fullScroll(View.FOCUS_DOWN);
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void onClick_next(View v){
        Toast.makeText(this, "페이지를 넘겨주세요!!", Toast.LENGTH_LONG).show();
        Intent iT = new Intent(this, Page3Activity.class);
        startActivity(iT);

        finish();
        return;
    }


}