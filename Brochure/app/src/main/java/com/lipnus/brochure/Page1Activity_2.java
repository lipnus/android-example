package com.lipnus.brochure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Page1Activity_2 extends FragmentActivity {

    int timeValue = 0;
    int count;
    WowAyncTask wowtask;

    //현재액티비티
    public static Activity page1Activity_2;

    private Context mContext = this; //aync에서 mediaplayer를 컨트롤할때 쓸라고

    TextView countTv;
    TextView countTv2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1_2);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //카운트다운 텍스트뷰
        countTv = (TextView)findViewById(R.id.count);
        countTv2 = (TextView)findViewById(R.id.count2);

        //현재 액티비티를 변수에 넣음
        page1Activity_2 = Page1Activity_2.this;

        //Wow타이밍
        count=20;

        Log.d("SSIBAL", "실행");
        //ayncTask
        wowtask = new WowAyncTask();
        wowtask.execute(100);

    }

    public void onClick(View v){
        //기다리기 누르면 다음으로 넘어감
        wowtask.cancel(true);
        Intent iT = new Intent(mContext, Page1Activity_3.class);
        startActivity(iT);
    }

    //Wow타이밍 맞춰서 띄워준다.
    public class WowAyncTask extends AsyncTask<Integer , Integer , Integer> {

        protected void onPreExecute() {
            timeValue = 0;
            Log.d("SSIBAL", "?");
        }


        //계속해서 실행되는 부분
        protected Integer doInBackground(Integer ... values) {

            //캔슬되지 않는동안은 계속실행
            while (isCancelled() == false) {

                    count--;
                    publishProgress(timeValue);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            }
            return timeValue;
        }

        //실행 중간중간 업데이트하는부분(publicProgress에 의해 호출)
        protected void onProgressUpdate(Integer ... values) {

            countTv.setText(""+count);
            countTv2.setText(""+count);

            if(count==0){
                Toast.makeText(getApplicationContext(), "WOW!!!", Toast.LENGTH_LONG).show();



                Intent iT = new Intent(mContext, Page1Activity_3.class);
                startActivity(iT);
                wowtask.cancel(true);

            }


        }

        //aynctast가 종료되었을때
        protected void onPostExecute(Integer result) {

        }

        //aynctast가 정지되었을때
        protected void onCancelled() {

        }
    }
}