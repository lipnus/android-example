package com.lipnus.scheduleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;

public class WBS_detail extends AppCompatActivity {

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //리스트뷰와 리스트뷰 어뎁터
    ListView listview ;
    ListViewAdapter_wbsDetail adapter;

    //YYYY.MM.DD.XX산출물 부분 tV
    TextView titleTv;

    //path를 저장하는 Hashmap
    HashMap<Integer, String > filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbs_detail);


        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todaybutton);
        wbsBtn.setBackgroundResource(R.drawable.wbsclick);
        iaBtn.setBackgroundResource(R.drawable.iabutton);
        testBtn.setBackgroundResource(R.drawable.testbutton);
        moreBtn.setBackgroundResource(R.drawable.morebutton);
        //===========================================================


        //리스트뷰 생성
        //==================================================================================

        // Adapter 생성
        adapter = new ListViewAdapter_wbsDetail() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //==================================================================================


        //YYYY.MM.DD.XX산출물(CustomApplication에 임시로 저장된 값들을 가져와서 출력)
        //==================================================================================
        titleTv = (TextView)findViewById(R.id.wbsDetail_InformTv);
        titleTv.setText(CustomApplication.wbs_Year +"."+ CustomApplication.wbs_Month+"."+
        CustomApplication.wbs_Date+" "+CustomApplication.wbs_Type +"산출물");
        //==================================================================================

        //파일path를 담는 해쉬맵
        filepath = new HashMap<Integer, String>();

        connectServer(CustomApplication.userID, CustomApplication.wbs_Type, Integer.toString(CustomApplication.wbs_YMD));
        Log.d("AAAA", ""+CustomApplication.userID+" "+CustomApplication.wbs_Type+" "+CustomApplication.wbs_YMD);

        /*
        adapter.addItem("파일명.pptx", "수고했다") ;
        */

        // 리스트뷰 클릭 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
                String url = filepath.get(position).toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "position: "+ position +" path: "+ url, Toast.LENGTH_LONG).show();
            }
        }) ;




    }



    //ConnectServer에서 받은값으로 makeBox호출
    public void wbsDetailSetting(String JsonStr) {

        StringBuffer sb = new StringBuffer();

        //filename, comment, path

        String str = JsonStr;

        /*
        String str = "[{'filename':'최종.pptx','comment':'마지막','path':'http://lipnus.ivyro.net/aaaa.txt'},"+
                "{'filename':'최종최종.pptx','comment':'다시','path':'http://lipnus.ivyro.net/aaaa.txt'},"+
                "{'filename':'진짜최종.pptx','comment':'끝이다 진짜','path':'http://lipnus.ivyro.net/aaaa.txt'}]";
        */

        Log.d("AAAA", str);

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            Log.d("AAAA", "webDetailSetting try 안쪽");
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String filename = jObject.getString("filename");
                String comment = jObject.getString("comment");
                String path = jObject.getString("path");

                sb.append(""+filename+" "+comment+" "+path);
                Log.d("AAAA", sb.toString());

                //파일경로를 담는 해쉬맵추가
                filepath.put(i,path);

                //리스트 추가
                adapter.addItem(filename, comment);

                //리스트뷰를 업데이트(이렇게 스레드로 안하면 간혹 안먹힐 때가 있음)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                Log.d("FFFF", "webDetailSetting 리스트추가");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View v){
        //한번씩 리스트뷰가 안뜰때가 있음 제목을 탭하면 재호출
        connectServer(CustomApplication.userID, CustomApplication.wbs_Type, Integer.toString(CustomApplication.wbs_YMD));
        Log.d("FFFF", "재호출");
    }


    /**=============================================================================================
     *
     * AyncTask를 이용하여 웹에 접속
     *
     * =============================================================================================
     */
    private void connectServer(String id, String type, String date){


        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override //시작
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WBS_detail.this, "Loading..", null, true, true);
            }

            @Override //끝
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();



                //받은 json데이터를 이용해 표를 작성
                wbsDetailSetting(s);

            }

            @Override //메인루프
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String type = (String)params[1];
                    String date = (String)params[2];

                    //접속할 주소
                    String link = CustomApplication.serverPath;
                    link = link + "app/getwbsdetail.php";


                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

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
                        Log.d("AAAA", "line:"+line);
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        //AyacTask객체를 만들고 실행시킨다
        InsertData task = new InsertData();
        task.execute(id,type, date);
    }//connecttoserver클래스




    //상단의 < 모양을 눌렀을때 WBS로 돌아간다
    public void onClick_backToWBS(View v){
        finish();
        Intent iT = new Intent(this, WBS.class);
        iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(iT);
    }

    //==============================================================================================
    //뒤로버튼 눌렀을때 WBS로
    //==============================================================================================
    @Override
    public void onBackPressed() {
        finish();
        Intent iT = new Intent(this, WBS.class);
        iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(iT);
    }

    //==============================================================================================
    //하단 메뉴
    //==============================================================================================
    public void onClick_menu(View v){

        CustomApplication CusApp = (CustomApplication) getApplication();
        CusApp.controlBottomMenu(v.getId());
        overridePendingTransition(R.anim.noting, R.anim.noting);
    }
}
