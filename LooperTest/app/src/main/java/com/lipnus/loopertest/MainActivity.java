package com.lipnus.loopertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView01, textView02;
    EditText editText01, editText02;

    /**
     * 메인 스레드의 핸들러
     */
    MainHandler mainHandler;

    /**
     * 새로 만든 스레드
     */
    ProcessThread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new MainHandler();
        thread1 = new ProcessThread();

        textView01 = (TextView) findViewById(R.id.textView01);
        textView02 = (TextView) findViewById(R.id.textView02);
        editText01 = (EditText) findViewById(R.id.editText01);
        editText02 = (EditText) findViewById(R.id.editText02);

        // 버튼 이벤트 처리
        Button processBtn = (Button) findViewById(R.id.processBtn);
        processBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inStr = editText01.getText().toString();

                Message msgToSend = Message.obtain();
                msgToSend.obj = inStr;

                thread1.handler.sendMessage(msgToSend);
            }
        });

        thread1.start();
    }


    class ProcessThread extends Thread{
        ProcessHandler handler; //스레드 내에 선언된 핸든럴 객체

        public ProcessThread(){
            handler = new ProcessHandler();
        }

        public void run(){
            Looper.prepare();
            Looper.loop();
        }
    }

    class ProcessHandler extends Handler{
        public void handleMessage(Message msg){
            Message resultMsg = Message.obtain();
            resultMsg.obj = msg.obj + "FUCKING!!!!";

            mainHandler.sendMessage(resultMsg);
        }
    }

    class MainHandler extends Handler{
        public void handleMessage(Message msg){
            String str = (String)msg.obj;
            editText02.setText(str);
        }
    }
}
