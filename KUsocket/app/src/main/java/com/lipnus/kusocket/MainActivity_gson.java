package com.lipnus.kusocket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity_gson extends AppCompatActivity {

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
                String urlStr = "http://10.16.33.250:7002/";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null){
                    //설정
                    conn.setConnectTimeout(10000); //연결을 10초간 시도
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //글자들을 모으기 위해서
                    StringBuffer buffer = new StringBuffer();

                    //서버로 연결
                    int resCode = conn.getResponseCode();

                    //if(resCode == HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );

                        while(true){
                            String line = reader.readLine(); //한줄씩 읽어들임
                            if(line == null){
                                break;
                            }

                            buffer.append(line + "\n");
                        }

                        printLog(buffer.toString());
                        reader.close();
                        conn.disconnect();
                    //}
                }


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

                        //JSON 파싱(gson 이용)
                        Gson gson = new Gson();
                        Customer customer = gson.fromJson(data, Customer.class);
                        println("이름: " + customer.name);
                        println("나이: " + customer.age);
                        println("전화번호: " + customer.mobile);
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
