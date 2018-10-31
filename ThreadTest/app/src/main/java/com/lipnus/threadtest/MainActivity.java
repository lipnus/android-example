package com.lipnus.threadtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int value = 0;
    private boolean running = false;

    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.textView01);

        // 버튼 이벤트 처리
        Button showBtn = (Button) findViewById(R.id.showBtn);


        //버튼을 누르면 텍스트뷰에 현재 value에 할당된 정수값 표시
        showBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tV.setText("스레드에서 받은 값: "+value);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        running = true;

        //액티비티가 화면에 보이면 새로 정의한 스레드 시작
        //onCreate말고 여기서 하는 이유가 있었던 것 같은데...  (onCreate()->onStart()->onResume 순서대로임)
        Thread th = new BackgroundThread();
        th.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //액티비티가 멈추면 스레드 중지
        running = false;
        value = 0;
    }

    //Thread 클래스를 상속하여 새로운 스레드 정의
    class BackgroundThread extends Thread{
        public void run(){
            while(running){
                try{
                    Thread.sleep(1000);
                    value++;
                }catch(InterruptedException e){
                    Log.e("Fuck", "Eception in thread.", e);
                }
            }
        }
    }
}
