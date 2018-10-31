package com.lipnus.brochure;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

//여기서는 음악만 틀어준다. 존나 병신같은 짓인데 어쩔 수 없다..ㅜㅜ
public class Page2Activity_1Music extends FragmentActivity {


    //현재액티비티
    public static Activity page2Activity_1Music;

    //음악플레이어
    MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page2Activity_1Music = Page2Activity_1Music.this;

        //음악재생
        if (mp != null) {mp.pause();}
        mp.release();
        mp = MediaPlayer.create(this, R.raw.respect);
        mp.start();

        //토스
        Intent iT = new Intent(this, Page2Activity_2.class);
        startActivity(iT);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }
}