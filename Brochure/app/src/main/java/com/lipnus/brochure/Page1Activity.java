package com.lipnus.brochure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class Page1Activity extends FragmentActivity {


    //현재 액티비티
    public static Activity page1Activity;

    MediaPlayer mp = new MediaPlayer();



    //비트순서를 체크
    int answerCount = 0;

    //aync에서 사용하는 것들
    BackgroundAyncTask task;
    int timeValue=0;
    private Context mContext = this; //aync에서 mediaplayer를 컨트롤할때 쓸라고


    //애니매이션
    Animation move1;
    Animation move2;
    Animation move3;

    //버튼
    Button aa0;    Button aa1;    Button aa2;    Button aa3;

    Button a0;    Button a1;    Button a2;    Button a3;
    Button b0;    Button b1;    Button b2;    Button b3;
    Button c0;    Button c1;    Button c2;    Button c3;
    Button d0;    Button d1;    Button d2;    Button d3;
    Button e0;    Button e1;    Button e2;    Button e3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        Log.d("answerC", "시작");

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page1Activity = Page1Activity.this;



        //ayncTask
        task = new BackgroundAyncTask();
        task.execute(100);

        //애니매이션
        move1 = AnimationUtils.loadAnimation(this, R.anim.move1);
        move2 = AnimationUtils.loadAnimation(this, R.anim.move2);
        move3 = AnimationUtils.loadAnimation(this, R.anim.move3);

        //애니매이션 리스너에 등록
        MovingListener ML = new MovingListener();
        move2.setAnimationListener(ML);

        //버튼들 등록
        aa0 = (Button)findViewById(R.id.AA0);
        aa1 = (Button)findViewById(R.id.AA1);
        aa2 = (Button)findViewById(R.id.AA2);
        aa3 = (Button)findViewById(R.id.AA3);

        a0 = (Button)findViewById(R.id.A0);
        a1 = (Button)findViewById(R.id.A1);
        a2 = (Button)findViewById(R.id.A2);
        a3 = (Button)findViewById(R.id.A3);

        b0 = (Button)findViewById(R.id.B0);
        b1 = (Button)findViewById(R.id.B1);
        b2 = (Button)findViewById(R.id.B2);
        b3 = (Button)findViewById(R.id.B3);

        c0 = (Button)findViewById(R.id.C0);
        c1 = (Button)findViewById(R.id.C1);
        c2 = (Button)findViewById(R.id.C2);
        c3 = (Button)findViewById(R.id.C3);

        d0 = (Button)findViewById(R.id.D0);
        d1 = (Button)findViewById(R.id.D1);
        d2 = (Button)findViewById(R.id.D2);
        d3 = (Button)findViewById(R.id.D3);

        e0 = (Button)findViewById(R.id.E0);
        e1 = (Button)findViewById(R.id.E1);
        e2 = (Button)findViewById(R.id.E2);
        e3 = (Button)findViewById(R.id.E3);

    }


    public void onClick_beat(View v){

        //재생중인 상태이면 정지
        if (mp != null) {mp.pause();}

        //메모리를 릴리즈해준다(이거 안하면 일정 횟수 이상 실행하면 재생안됨)
        mp.release();

        //Drop the Beat!!

        //첫줄
        switch( v.getId() ){
            case R.id.A0:{
                mp = MediaPlayer.create(this, R.raw.a4);
                mp.start();

                break;
            }
            case R.id.A1:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a2);
                mp.start();

                break;
            }
            case R.id.A2:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a3);
                mp.start();
                break;
            }
            case R.id.A3:{//★4★

                mp = MediaPlayer.create(this, R.raw.b4);
                mp.start();

                if(answerCount==4){answerCount=5;} //aync에서 캐치되서 음악실행됨}
                else{answerCount=0;}

                break;
            }


            //두번째줄
            case R.id.B0:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a1);
                mp.start();
                break;
            }
            case R.id.B1:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a6);
                mp.start();
                break;
            }
            case R.id.B2:{//★2★
                if(answerCount==2){answerCount=3;}
                else{ answerCount=0;}

                mp = MediaPlayer.create(this, R.raw.b2);
                mp.start();
                break;
            }
            case R.id.B3:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a6);
                mp.start();
                break;
            }


            //세번째줄
            case R.id.C0:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.d1);
                mp.start();
                break;
            }
            case R.id.C1:{//★1★
                if(answerCount==0){
                    answerCount=1;
                }else if(answerCount==1 || answerCount==2){
                    answerCount=2;
                }else{
                    answerCount=0;
                }

                mp = MediaPlayer.create(this, R.raw.b1);
                mp.start();
                break;
            }
            case R.id.C2:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.d3);
                mp.start();
                break;
            }
            case R.id.C3:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.d2);
                mp.start();
                break;
            }



            //네번째줄줄
           case R.id.D0:{
               answerCount=0;
                mp = MediaPlayer.create(this, R.raw.d5);
                mp.start();
                break;
            }
            case R.id.D1:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a1);
                mp.start();
                break;
            }
            case R.id.D2:{ //★3★
                if(answerCount==3){answerCount=4;}
                else{answerCount=0;}

                mp = MediaPlayer.create(this, R.raw.b3);
                mp.start();
                break;
            }
            case R.id.D3:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a8);
                mp.start();
                break;
            }






            //다섯번째줄
            case R.id.E0:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a1);
                mp.start();
                break;
            }
            case R.id.E1:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.c2);
                mp.start();
                break;
            }
            case R.id.E2:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a7);
                mp.start();
                break;
            }
            case R.id.E3:{
                answerCount=0;
                mp = MediaPlayer.create(this, R.raw.a3);
                mp.start();
                break;
            }

        }

        Log.d("answerC", "answerCount: " + answerCount);
        if (answerCount>0){
            String Square = null;

            switch (answerCount){
                case 0: Square = "□□□□□"; break;
                case 1: Square = "■□□□□"; break;
                case 2: Square = "■■□□□";  break;
                case 3: Square = "■■■□□"; break;
                case 4: Square = "■■■■□"; break;
                case 5: Square = "■■■■■"; break;
            }
            Toast.makeText(getApplicationContext(), ""+Square, Toast.LENGTH_SHORT).show();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mp != null) {
            try {
                mp.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //단추들이 애니며이션을 하면서 없어짐
    public void startMoving(){

        //제일 윗줄만 걍 따로..
        aa0.startAnimation(move1);
        aa1.startAnimation(move2);
        aa2.startAnimation(move3);
        aa3.startAnimation(move2);

        a0.startAnimation(move1);
        b0.startAnimation(move2);
        c0.startAnimation(move3);
        d0.startAnimation(move2);
        e0.startAnimation(move1);

        a1.startAnimation(move2);
        b1.startAnimation(move1);
        c1.startAnimation(move2);
        d1.startAnimation(move1);
        e1.startAnimation(move3);

        a2.startAnimation(move1);
        b2.startAnimation(move2);
        c2.startAnimation(move1);
        d2.startAnimation(move2);
        e2.startAnimation(move1);

        a3.startAnimation(move2);
        b3.startAnimation(move3);
        c3.startAnimation(move2);
        d3.startAnimation(move1);
        e3.startAnimation(move2);

    }


    //계속 감시하고 있다가 조건이 충족되면 비트실행!
    public class BackgroundAyncTask extends AsyncTask<Integer , Integer , Integer> {

        protected void onPreExecute() {
            timeValue = 0;
        }


        //계속해서 실행되는 부분
        protected Integer doInBackground(Integer ... values) {

            //캔슬되지 않는동안은 계속실행
            while (isCancelled() == false) {
                timeValue++;
                if (timeValue >= 1) {

                    if(answerCount==5) {
                        Log.d("answerC", "캐치2");
                        answerCount=0;
                        publishProgress(timeValue);
                    }
                    timeValue = 0;
                }

                try {
                    Thread.sleep(1100);
                } catch (InterruptedException ex) {}
            }
            return timeValue;
        }

        //실행 중간중간 업데이트하는부분(publicProgress에 의해 호출)
        protected void onProgressUpdate(Integer ... values) {
             Log.d("answerC", "드롭더비트!");
            Toast.makeText(getApplicationContext(), "♬ BORN HATER\n    (EPICHIGH feat. BINZINO, B.I)", Toast.LENGTH_LONG).show();



            if (mp != null) {mp.pause();}
                mp.release();
                mp = MediaPlayer.create(mContext, R.raw.bornhater);
                mp.start();

                startMoving();

                //스스로를 종료
                task.cancel(true);
        }

        //aynctast가 종료되었을때
        protected void onPostExecute(Integer result) {

        }

        //aynctast가 정지되었을때
        protected void onCancelled() {

        }
    }


    /**
     애니매이션의 움직임을 감지하는 리스너이다.
     */
    private class MovingListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            Intent iT = new Intent(mContext, Page1Activity_2.class);
            startActivity(iT);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
