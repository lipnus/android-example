package com.lipnus.mysocket;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    EditText editText2;

    //스레드에서 ui로 접근하는 방법
    Handler handler = new Handler();

    //사용자가 입력하는 서버정보
    String host;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
     }


     public void request(){
         host = editText.getText().toString().trim();
         String portStr = editText2.getText().toString().trim();

         //숫자로 바꿔준다
         try{
             port = Integer.parseInt(portStr);
         }catch (Exception e){

         }

         RequestThread thread = new RequestThread();
         thread.start();
     }

     public void println(final String data){ //data가 run안쪽에서 접근이 가능하지 않기때문에 final사용
         handler.post(new Runnable(){
             public void run(){
                 textView.append(data + "\n");
             }
         });
     }

     class RequestThread extends Thread{
        public void run(){

            Log.d("SSSS", "RequestThread()   host: "+host + "  port: "+port);

            try {
                Socket socket = new Socket(host, port);
                println("서버에 연결됨: " + host + ", " + port);

                String output = "안녕!"; //서버로 보내는 데이터
                ObjectOutputStream outstream = new ObjectOutputStream( socket.getOutputStream() );
                outstream.writeObject(output);
                outstream.flush();
                println("서버로 보냄: " + output);

                ObjectInputStream instream = new ObjectInputStream( socket.getInputStream() );
                String input = (String)instream.readObject();
                println("서버로부터 받은 데이터: " + input);

            }catch ( Exception e) {
                Log.d("SSSS", "RequestThread() Error");
                e.printStackTrace();
            }
        }
     }
}
