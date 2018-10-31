package com.lipnus.kuthread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tV;
    //mainThread안에 속하는 핸들러
    Handler handler = new Handler();

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.textView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        //온클릭리스너로 버튼 실행(테스트버튼)
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();
            }
        });

    }

    public void onClick1(View v){
        CountThread thread = new CountThread();
        thread.start();
    }

    public void onClick2(View v){
        CountThread2 thread2 = new CountThread2();
        thread2.start();
    }

    public void println(final String data){
        handler.post(new Runnable(){
           public void run(){
               tV.append(data + "\n");
           }
        });
    }


    public void startProgress() {
        ProgressTask task = new ProgressTask();
        task.execute(0);
    }

    class ProgressTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행률을 0으로
            progressBar.setProgress(0);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            for(int i=0; i<100; i++){

                try{
                    Thread.sleep(100);
                    publishProgress(i);
                }catch(Exception e){}
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Toast.makeText(getApplicationContext(), "작업이 끝났습니다", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0].intValue());
        }


    }


    class CountThread2 extends Thread{
        public void run(){
            for(int i=0; i<100; i++){
                println("#" + i + "호출됨(두번째 Thread)");

                try { Thread.sleep(500);}
                catch(Exception e){}
            }
        }
    }

    class CountThread extends Thread{
        public void run(){
            for(int i=0; i<100; i++){
                println("#" + i + "호출됨");

                try { Thread.sleep(500);}
                catch(Exception e){}
            }
        }
    }
}
