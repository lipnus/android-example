package com.lipnus.brochure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Page3Activity_2 extends FragmentActivity {

    int timeValue = 0;
    int count;

    //음악플레이어
    MediaPlayer mp = new MediaPlayer();

    //현재액티비티
    public static Activity page3Activity_2;

    private Context mContext = this; //aync에서 mediaplayer를 컨트롤할때 쓸라고


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3_2);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page3Activity_2 = Page3Activity_2.this;

        mp.release();
        mp = MediaPlayer.create(this, R.raw.madcolor);
        mp.start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void onClick_next(View v){

        mp.release();
        Intent iT = new Intent(this, Page4Activity.class);
        startActivity(iT);

        Toast.makeText(this, "페이지를 넘겨주세요!!", Toast.LENGTH_LONG).show();

        finish();
        return;
    }
}