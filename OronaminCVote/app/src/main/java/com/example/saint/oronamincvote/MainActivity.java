package com.example.saint.oronamincvote;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
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

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;

    private EditText mNickNameEdt;

    InsertData task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        listView = (ListView)findViewById(R.id.RoomList);
        adapter = new ListViewAdapter(roomHandler);
        listView.setAdapter(adapter);


        mNickNameEdt = (EditText)findViewById(R.id.nickNameEdt);




        mNickNameEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow( mNickNameEdt.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    protected void onResume(){
        super.onResume();

        task = new InsertData();
        task.execute("-1","-1","-1");

        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static final int CLICKITEM = 1;
    private final Handler roomHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case CLICKITEM:
                    final String roomName = (String)msg.obj;
                    roomNameForIntent = roomName;
                    final String nickName = mNickNameEdt.getText().toString();
                    if(nickName.contains(" ") || nickName.length() == 0){
                        Toast.makeText(getApplicationContext(),"NickName을 확인해주세요",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
                    d.setTitle("비밀번호를 입력해주세요 ♥");
                    final RelativeLayout r = (RelativeLayout)View.inflate(MainActivity.this,R.layout.password,null);
                    d.setView(r);
                    DialogInterface.OnClickListener l = new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int which){
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    EditText e = (EditText)r.findViewById(R.id.passwordEdt);
                                    String password = e.getText().toString();
                                    task.cancel(true);
                                    task = new InsertData();
                                    task.execute(nickName,roomName,password);
                                    break;
                            }

                        }
                    };
                    d.setPositiveButton("Enter",l);
                    d.show();

                    break;
            }
        }
    };

    private String roomNameForIntent;
    //ConnectServer에서 받은값을 처리
    public void Setting(String JsonStr) {

        if(JsonStr.equals("Enter")){
            task.cancel(true);

            Log.i("AAAA", "접속OK");
            Intent intent = new Intent(MainActivity.this,VoteActivity.class);
            intent.putExtra("roomName",roomNameForIntent);
            if(mNickNameEdt.getText().toString()!= null) {
                intent.putExtra("nickName", mNickNameEdt.getText().toString());
            }
            startActivity(intent);
            return;

        }else if(JsonStr.equals("Error")){
            Log.i("AAAA", "비밀번호를 확인해주세요");

            Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
            //방 접속실패
            task.cancel(true);
            task = new InsertData();
            task.execute("-1","-1","-1");
            return;
        }

        StringBuffer sb = new StringBuffer();
        String str = JsonStr;


        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성

            int size = jarray.length();
            String arr[] = new String[size];

            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String roomname = jObject.getString("roomname");

                arr[i] = roomname;


            }
            adapter.listViewItemList.clear();
            for(int i=0 ; i<jarray.length() ; i++){
                adapter.addItem(arr[i]);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


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

                    Thread.sleep(1000);
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }

            return "end";
        }
    }//InsertData(AyncTask)

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
        if(task != null){
            task.cancel(true);
            task = null;
        }

    }

    public void makingRoom(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lipnus.ivyro.net"));
        startActivity(intent);
    }

}
