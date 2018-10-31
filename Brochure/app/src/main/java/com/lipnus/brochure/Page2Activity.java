package com.lipnus.brochure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Page2Activity extends FragmentActivity {

    //현재액티비티
    public static Activity page2Activity;
    ChangeAyncTask task;

    //AyncTask
    int timeValue = 0;
    int count=0;

    //색이 변하는 이미지뷰
    ImageView iV;

    //에디트텍스트
    EditText editText;


    //음악플레이어
    MediaPlayer mp = new MediaPlayer();

    //정답을 맞췄는지 여부
    boolean answerOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재 액티비티를 변수에 넣음
        page2Activity = Page2Activity.this;

        try {
            //이전액티비티 종료
            //==========================================================================================
            //음악나오는 비트찍는액티비티 종료
            Page1Activity p1a1 = (Page1Activity)Page1Activity.page1Activity;
            p1a1.finish();

            //이전액티비티 종료
            Page1Activity_2 p1a2 = (Page1Activity_2)Page1Activity_2.page1Activity_2;
            p1a2.finish();

            //계단모양 격자 액티비티
            Page1Activity_3 p1a3 = (Page1Activity_3)Page1Activity_3.page1Activity_3;
            p1a3.finish();
            //==========================================================================================
        } catch (Exception e) {
            e.printStackTrace();
        }

        //aynctask실행
        task = new ChangeAyncTask();
        task.execute(100);


        //에디트텍스트
        editText = (EditText)findViewById(R.id.editText);

        //이미지뷰
        iV = (ImageView) findViewById(R.id.changeColoriV);


    }

    public void onClick(View v){
        iV.setImageResource(R.drawable.munk);
    }

    public void onClick2(View v){
        String inPutText = editText.getText().toString();

        if(inPutText.equals("322")){//정답

            Toast.makeText(this, "♬ RESPECT(LOCO feat.GRAY, DJ Pumkin)", Toast.LENGTH_LONG).show();

            //끝. aync에서 체크하고 종료
            answerOk=true;
            task.cancel(true);

            //그림제거
            iV.setBackgroundResource(0);
            if (mp != null) {mp.pause();}
            mp.release();

            Intent iT = new Intent(this, Page2Activity_1Music.class);
            startActivity(iT);

            finish();
            return;
        }
        else{//오답답
           iV.setImageResource(R.drawable.x);
            Toast.makeText(getApplicationContext(), "땡", Toast.LENGTH_LONG).show();

            mp.release();
            mp = MediaPlayer.create(this, R.raw.wrong);
            mp.start();
        }

    }


    //Wow타이밍 맞춰서 띄워준다.
    public class ChangeAyncTask extends AsyncTask<Integer , Integer , Integer> {

        protected void onPreExecute() {
            timeValue = 0;
        }


        //계속해서 실행되는 부분
        protected Integer doInBackground(Integer ... values) {

            //캔슬되지 않는동안은 계속실행
            while (isCancelled() == false) {

                publishProgress(timeValue);
                count++;

                try {
                    Thread.sleep(800);
                } catch (InterruptedException ex) {}
            }
            return timeValue;
        }

        //실행 중간중간 업데이트하는부분(publicProgress에 의해 호출)
        protected void onProgressUpdate(Integer ... values) {

            Log.d("answerC", "count:"+count);
            iV.setImageResource(0);

            if(answerOk==true){
                //답을 맞췄으면 끈다.
                task.cancel(true);
            }

            //간혹 숫자가 저절로 넘어가는 경우 바로잡아줌
            if(count>9){count=0;}

            switch( count ){
                case 1:{ iV.setBackgroundColor(Color.rgb(166,203,126)); }break;
                case 2:{ iV.setBackgroundResource(R.drawable.munk);} break;
                case 3:{ iV.setBackgroundColor(Color.rgb(192,238,255)); }break;
                case 4:{ iV.setBackgroundResource(R.drawable.munk2);} break;
                case 5:{ iV.setBackgroundColor(Color.rgb(158,126,203)); }break;
                case 6:{ iV.setBackgroundResource(R.drawable.munk3);} break;
                case 7:{ iV.setBackgroundColor(Color.rgb(221,144,111)); }break;
                case 8:{ iV.setBackgroundResource(R.drawable.munk4);} break;
                case 9:{
                    count=0;
                    iV.setBackgroundColor(Color.rgb(182,112,154));
                }break;
            }

        }

        //aynctast가 종료되었을때
        protected void onPostExecute(Integer result) {

        }

        //aynctast가 정지되었을때
        protected void onCancelled() {

        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
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
}
