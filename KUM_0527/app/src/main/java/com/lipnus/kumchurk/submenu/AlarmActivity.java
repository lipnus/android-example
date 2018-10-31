package com.lipnus.kumchurk.submenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;
import com.lipnus.kumchurk.data.Alarm_JSON;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {

    //알람의 리스트뷰 어댑터
    ListView listview;
    AlarmListViewAdapter adapter;


    //volley와 리스너 (volly는 독립적인 클래스로 구현)
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    //알람정보 데이터
    Alarm_JSON alJSON;

    //현재 표시된 알람의 개수
    int alCount = 0;

     //상단, 서브메뉴 컨트롤
    SubMenuControl smc;

    //다른 곳에서 여기를 finish()할 수 있도록 함.
    public static Activity ALActiviry;

    boolean lastItemVisibleFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재의 뷰(서브메뉴에 넘김)
        View thisView = this.getWindow().getDecorView();

        //서브메뉴 생성
        smc = new SubMenuControl(this, thisView, "ALARM");

        //다른 곳에서 이 액티비티를 조작할 수 있게 함
        ALActiviry = AlarmActivity.this;

        //Volley 콜백함수
        initVolleyCallback();

        //리스트뷰 설정
        initList();

        //서버에서 데이터를 받아온다
        connect();

    }


    public void initList(){

        //리스트뷰 Adapter 생성
        adapter = new AlarmListViewAdapter();

        //알람를 표시할 리스트뷰와 컨트롤을 위한 어댑터
        listview = (ListView) findViewById(R.id.al_listview);
        listview.setAdapter(adapter);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    Log.d("LILI", "인생바닥침");

                    addAlarm();//10개추가
                }
            }

        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    } //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )


    //취소버튼
    @Override
    public void onBackPressed() {
        smc.backPress();
    }

    //상단바의 중간부분 타이틀을 터치하면 스크롤 위로 끌어올림
    public void onClicK_topmenu_title(View v){
        listview.smoothScrollToPosition( 0 );
    }



    public void connect(){

        String url = "http://kumchurk.ivyro.net/app/download_alarm.php";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //데이터 다운로드 volley
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("ALAL", response);
                jsonToJava(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "(기본)에러: "+ error);
            }
        };
    } //데이터 다운로드 callback
    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        Gson gson = new Gson();
        alJSON = gson.fromJson(jsonStr, Alarm_JSON.class);
        addAlarm();

    } //다운받은 Json데이터들을 객체화



    //무한스크롤을 이용하여 5개씩 보여준다
    public void addAlarm(){

        //알람이 1개이상 존재하는 경우
        if(alJSON.alarmData != null && alJSON.alarmData.size() > 0){

            //더이상 추가할 것이 없음
            if(alCount == alJSON.alarmData.size()){
                Toast.makeText(getApplicationContext(), "아쉽지만 마지막이예요", Toast.LENGTH_SHORT).show();
            }

            //15개가 되지 않을때 남은 것들 추가
            else if( (alJSON.alarmData.size() - alCount) < 15 ){
                for(int i=alCount; i<alJSON.alarmData.size(); i++){
                    adapter.addItem( alJSON.alarmData.get(i) );
                }
                alCount = alJSON.alarmData.size();
            }

            //15개씩 추가
            else{
                for(int i=alCount; i<alCount+15; i++){
                    adapter.addItem( alJSON.alarmData.get(i) );
                }
                alCount=alCount+15;
            }
            adapter.notifyDataSetChanged(); //리스트 새로고침
        }

    }

}
