package com.lipnus.oronaminc;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    InsertData task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.oronaminc);
        mp.start();

        //AyacTask객체를 만들고 실행시킨다
        task = new InsertData();
        task.execute("-1","-1","-1");

        //"-1","-1","-1"이 들어가면 방목록을 받아오고, 그 이외의 값이 들어가면 값이 정확한지 체크
        //("닉네임","들어갈방이름","방비밀번호")이 String값으로 들어간다.
    }


    //접속신호
    public void onClick(View v){
        task.cancel(true);

        task = new InsertData();
        task.execute("닉네임","들어갈방이름","방비밀번호");
    }




    //ConnectServer에서 받은값을 처리
    public void Setting(String JsonStr) {

        if(JsonStr.equals("Enter")){
            task.cancel(true);
            Log.i("AAAA", "접속OK");



            return;

        }else if(JsonStr.equals("RoomNameError")){
            Log.i("AAAA", "비밀번호를 확인해주세요");
            //방 접속실패

            return;

        }

        StringBuffer sb = new StringBuffer();
        String str = JsonStr;


        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String roomname = jObject.getString("roomname");

                //리스트 추가
                Log.d("AAAA", "*방목록: "+roomname);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**=============================================================================================
     *
     * AyncTask를 이용하여 웹에 접속
     *
     * =============================================================================================
     */
    class InsertData extends AsyncTask<String, String, String> {

        @Override //시작
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override //끝
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }


        protected void onProgressUpdate(String ... values) {

            //받은 데이터를 Setting에 넘겨준다.
            Setting(values[0]);
            Log.d("AAAA", "넘겨준값: :"+ values[0]);
        }


        @Override //메인루프
        protected String doInBackground(String... params) {
            while (isCancelled() == false) {

                try{
                    Thread.sleep(1000);

                    String nickname = (String)params[0];
                    String roomname = (String)params[1];
                    String roompassword = (String)params[2];

                    String link;


                    //접속할 주소
                    link = "http://lipnus.ivyro.net/app/enterroom.php";

                    String data  = URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");
                    data += "&" + URLEncoder.encode("roomname", "UTF-8") + "=" + URLEncoder.encode(roomname, "UTF-8");
                    data += "&" + URLEncoder.encode("roompassword", "UTF-8") + "=" + URLEncoder.encode(roompassword, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();



                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    publishProgress( sb.toString() );
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }

            return "end";
        }
    }//InsertData(AyncTask)
    //==========================================================================================

}
