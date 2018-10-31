package com.lipnus.brochure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Page2Activity_2 extends FragmentActivity {

    int timeValue = 0;
    int count;


    //객체들
    ImageView birthNumber;

    //애니매이션
    Animation out;

    //현재액티비티
    public static Activity page2Activity_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_2);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page2Activity_2 = Page2Activity_2.this;

        //객체들
        birthNumber = (ImageView) findViewById(R.id.birthYear);

        //애니매이션
        out = AnimationUtils.loadAnimation(this, R.anim.out);
        birthNumber.setAnimation(out);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void onClick_next(View v){
        Intent iT = new Intent(this, Page2Activity_3.class);
        startActivity(iT);

        finish();
        return;
    }
}