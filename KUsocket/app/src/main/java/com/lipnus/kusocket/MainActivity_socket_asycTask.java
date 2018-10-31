package com.lipnus.kusocket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity_socket_asycTask extends AppCompatActivity {

    TextView textView;
    EditText editText;
    EditText editText2;


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
        }catch (Exception e){}

        //ayncTask를 이용하여 소켓접속
        SocketTask task = new SocketTask();
        task.execute("");
    }

    public void println( String data){ //data가 run안쪽에서 접근이 가능하지 않기때문에 final사용
        textView.append(data + "\n");
    }

    //타입은 순서대로 doInBackground, progressUpdate, onProgressUpdate, onPostExcute
    class SocketTask extends AsyncTask<String, Bundle, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                Socket socket = new Socket(host, port);
                printLog("서버에 연결됨: " + host + ", " + port);


                String output = "안녕!"; //서버로 보내는 데이터
                ObjectOutputStream outstream = new ObjectOutputStream( socket.getOutputStream() );
                outstream.writeObject(output);
                outstream.flush();
                printLog("서버로 보냄: " + output);

                ObjectInputStream instream = new ObjectInputStream( socket.getInputStream() );
                String input = (String)instream.readObject();
                printLog("서버로부터 받은 데이터: " + input);

            }catch ( Exception e) {
                Log.d("SSSS", "SocketTask Error");
                e.printStackTrace();
            }

            return 0;
        }

        private void printLog(String str){
            Bundle bundle = new Bundle();
            bundle.putString("command", "println");
            bundle.putString("data", str);
            publishProgress(bundle);
        }

        @Override
        protected void onProgressUpdate(Bundle... values) {
            super.onProgressUpdate(values);
            if(values != null && values.length > 0){
                String command = values[0].getString("command");
                if(command != null){
                    if(command.equals("println")){
                        String data = values[0].getString("data");
                        println(data);
                    }
                }

            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

        }




    }
}
