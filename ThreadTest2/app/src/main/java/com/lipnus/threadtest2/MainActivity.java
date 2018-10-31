package com.lipnus.threadtest2;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    ProgressBar bar;
    TextView tV;
    boolean isRunning = false;

    //메인스레드의 UI에 접근하기 위한 핸들러
    ProgressHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar) findViewById(R.id.progress);
        tV = (TextView) findViewById(R.id.textView01);

        handler = new ProgressHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread thd = new Thread(new Runnable(){
            public void run() {
                try{
                    for(int i=0; i<20 && isRunning; i++){
                        Thread.sleep(1000);

                        //스레드 안에서 작업 상태나 결과를 핸들러의 sendMessage함수로 전송
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                }catch(InterruptedException e){}


            }
        });

        isRunning = true;
        thd.start();
    }

    public void onStop() {
        super.onStop();

        isRunning = false;
    }

    //Handler클래스를 상속하여 새로운 핸들러 클래스 정의
    public class ProgressHandler extends android.os.Handler {
        public void handleMessage(Message msg){ //handleMessage()메소드 안에서 전달된 정보를 이용해 UI업데이트

            bar.incrementProgressBy(5);

            if (bar.getProgress() == bar.getMax()) {
                tV.setText("Done");
            } else {
                tV.setText("Working ..." + bar.getProgress());
            }
        }
    }
}
