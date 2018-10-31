package com.example.saint.oronamincvote;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.w3c.dom.Text;

/**
 * Created by saint on 2016-08-06.
 */
public class VoteActivity extends AppCompatActivity {

    private TextView roomNameTxt;
    private String mNickName;
    private String realroomName;
    private InsertData task;

    private boolean is_start;
    private boolean only_one;
    private boolean time_out;

    private int numOfCard;

    private ImageView mButton01;
    private ImageView mButton02;
    private ImageView mButton03;

    private boolean select01;
    private boolean select02;
    private boolean select03;
    MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.i("Tag","onCreate");
        roomNameTxt = (TextView)findViewById(R.id.enteredRoomName);
        realroomName = getIntent().getStringExtra("roomName").toString();
        //Log.i("Tag",getRoomName);
        String totalText = "Room : "+realroomName;
        roomNameTxt.setText(totalText);

        mNickName = getIntent().getStringExtra("nickName");

        is_start = false;
        only_one = false;
        time_out = false;

        mButton01 = (ImageView) findViewById(R.id.button01);
        mButton02 = (ImageView) findViewById(R.id.button02);
        mButton03 = (ImageView) findViewById(R.id.button03);

        select01 = false;
        select02 = false;
        select03 = false;

        task = new InsertData();
        task.execute(mNickName,realroomName,"0");


        /*
        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this,R.raw.oronaminc);
        mp.start();
        */
    }

    //ConnectServer에서 받은값을 처리
    public void Setting(String JsonStr) {

        String str = JsonStr;

        //Log.d("AAAA", "세팅: " + str);
        Log.i("Tag","세팅들어옴"+JsonStr);

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int time = jObject.getInt("time");
                int num = jObject.getInt("num");

                //리스트 추가
                Log.i("Tag", "시간: " + String.valueOf(time) + " 투표할 대상의 수: " + String.valueOf(num));

                if(time != 0){
                    //투표시작!
                    is_start = true;
                    start_voting(num);

                }else if(time == 0){
                    if(is_start == true){
                        time_out = true;
                        finish();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void start_voting(int num){
        if(only_one == false){
            only_one = true;
            LinearLayout wating_layout = (LinearLayout)findViewById(R.id.voteWatingLayout);
            LinearLayout voting_layout = (LinearLayout)findViewById(R.id.voteLayout);
            wating_layout.setVisibility(View.INVISIBLE);
            voting_layout.setVisibility(View.VISIBLE);

            ImageView mbutton01 = (ImageView)findViewById(R.id.button01);
            ImageView mbutton02 = (ImageView)findViewById(R.id.button02);
            ImageView mbutton03 = (ImageView)findViewById(R.id.button03);

            numOfCard = num;

            if(num == 1){
                mbutton02.setVisibility(View.GONE);
                mbutton03.setVisibility(View.GONE);
            }else if(num == 2){
                mbutton03.setVisibility(View.GONE);
            }
            mp = new MediaPlayer();
            mp = MediaPlayer.create(this,R.raw.oronaminc);
            mp.start();

        }
    }

    public void voting(View v){
        switch (v.getId()){
            case R.id.button01:

                if(numOfCard == 3){

                    mButton01.setImageResource(R.drawable.gray);
                    mButton02.setImageResource(R.drawable.black);
                    mButton03.setImageResource(R.drawable.black);

                }else if(numOfCard == 2){

                    mButton01.setImageResource(R.drawable.gray);
                    mButton02.setImageResource(R.drawable.black);

                }else if(numOfCard == 1){
                    if(select01 == true){
                        select01 = false;
                        mButton01.setImageResource(R.drawable.black);
                        deliver_selecting(0);
                        break;
                    }else{
                        select01 = true;
                        mButton01.setImageResource(R.drawable.gray);
                    }
                }
                deliver_selecting(1);
                break;
            case R.id.button02:

                if(numOfCard == 3){
                    mButton01.setImageResource(R.drawable.black);
                    mButton02.setImageResource(R.drawable.blue);
                    mButton03.setImageResource(R.drawable.black);

                }else if(numOfCard == 2){
                    mButton01.setImageResource(R.drawable.black);
                    mButton02.setImageResource(R.drawable.blue);
                }
                deliver_selecting(2);
                break;
            case R.id.button03:

                if(numOfCard == 3){
                    mButton01.setImageResource(R.drawable.black);
                    mButton02.setImageResource(R.drawable.black);
                    mButton03.setImageResource(R.drawable.purple);
                }
                deliver_selecting(3);
                break;
        }
    }

    private  void deliver_selecting(int num){
        task.cancel(true);
        task = new InsertData();
        System.out.printf("%d\n",num);
        String param01 = mNickName;
        String param02 = realroomName;
        String param03 = Integer.toString(num);
        Log.i("Tag",param01+"  "+param02+"  "+param03);
        task.execute(param01,param02,param03);
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
            //Log.d("AAAA", "넘겨준값: :"+ values[0]);
        }


        @Override //메인루프
        protected String doInBackground(String... params) {
            while (isCancelled() == false) {
                Log.i("Tag","async 들어옴");
                try{
                    Thread.sleep(1000);

                    String nickname = (String)params[0];
                    String roomname = (String)params[1];
                    String vote = (String)params[2];
                    String link;


                    //접속할 주소
                    link = "http://lipnus.ivyro.net/app/vote.php";

                    String data  = URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");
                    data += "&" + URLEncoder.encode("roomname", "UTF-8") + "=" + URLEncoder.encode(roomname, "UTF-8");
                    data += "&" + URLEncoder.encode("vote", "UTF-8") + "=" + URLEncoder.encode(vote, "UTF-8");

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

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("Tag", "onStop ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Tag", "onDestroy ");
        mp.stop();
        if(time_out == true){
            Toast.makeText(getApplicationContext(),"시간이 종료되었습니다",Toast.LENGTH_SHORT).show();
        }
        if(task != null){
            task.cancel(true);
            task = null;
        }

    }
}
