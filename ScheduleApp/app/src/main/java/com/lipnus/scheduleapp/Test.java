package com.lipnus.scheduleapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sunpil on 2016-07-14.
 */
public class Test extends AppCompatActivity {

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //동적으로 생성되는 값들이 들어가는 LinearLayout
    LinearLayout depth1_Linear;
    LinearLayout depth2_Linear;
    LinearLayout depth3_Linear;
    LinearLayout content_Linear;
    LinearLayout android_Linear;
    LinearLayout ios_Linear;
    LinearLayout ie_Linear;
    LinearLayout chrome_Linear;
    LinearLayout firefox_Linear;

    //상단부의 프로그래스바 부분
    TextView percentTv;
    ProgressBar ingPercent_Progress;
    ProgressBar endPercent_Progress;

    //depth3의 색을 저장하고 있는 리스트
    ArrayList<Integer> d3Color;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;

    //os제목 나열
    TextView osNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //depth3의 색을 저장하고 있는 리스트
        d3Color = new ArrayList<>();


        //상단부의 프로그래스바부분
        percentTv = (TextView)findViewById(R.id.TEST_PERCENT_TV);
        ingPercent_Progress = (ProgressBar) findViewById(R.id.TEST_PERCENT_ING_PROGRESS);
        endPercent_Progress = (ProgressBar) findViewById(R.id.TEST_PERCENT_END_PROGRESS);

        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todaybutton);
        wbsBtn.setBackgroundResource(R.drawable.wbsbutton);
        iaBtn.setBackgroundResource(R.drawable.iabutton);
        testBtn.setBackgroundResource(R.drawable.testclick);
        moreBtn.setBackgroundResource(R.drawable.morebutton);
        //===========================================================


        //동적으로 생성되는 값들이 들어가는 LinearLayout
        depth1_Linear = (LinearLayout) findViewById(R.id.depth1_Linear);
        depth2_Linear = (LinearLayout) findViewById(R.id.depth2_Linear);
        depth3_Linear = (LinearLayout) findViewById(R.id.depth3_Linear);
        content_Linear = (LinearLayout) findViewById(R.id.content_Linear);
        android_Linear = (LinearLayout) findViewById(R.id.android_Linear);
        ios_Linear = (LinearLayout) findViewById(R.id.ios_Linear);
        ie_Linear = (LinearLayout) findViewById(R.id.ie_Linear);
        chrome_Linear = (LinearLayout) findViewById(R.id.chrome_Linear);
        firefox_Linear = (LinearLayout) findViewById(R.id.firefox_Linear);


        //os제목을 표시
        osNameTv = (TextView) findViewById(R.id.test_osname_informationTv);
        osNameTv.setText("( ");
        for(int i=0; i<5; i++){
            if(CustomApplication.osname[i].equals("no") == false){
                osNameTv.append( (i+1) + ":" + CustomApplication.osname[i] + "  " );
            }
        }
        osNameTv.append(")");

        //Today에서 받은 데이터 재이용
        testSetting(null);

        //상단의 프로그래스바 부분
        percentTv.setText(CustomApplication.testEndPercent+"%");
        ingPercent_Progress.setProgress(CustomApplication.testEndPercent + CustomApplication.testIngPercent);
        endPercent_Progress.setProgress(CustomApplication.testEndPercent);


    }

    //==============================================================================================
    //동적으로 표를 만들어준다.
    // whichRow = 1:depth1 2:depth2 3:depth3 4:내용 5:Android 6:IOS 7:IE 8:Chrome 9:Firefox 에 칸 생성
    // height = 제일작은칸을 기준(1)으로 칸의 세로길이
    // color = 1:흰색(테스트이전) 2:하늘색(오류수정중) 3:진한파랑(테스트완료)
    // endDay = 해당 칸의 완료예정일(여기서는 쓰이지 않음)
    //==============================================================================================
    public void makeBox(int whichRow, int height, int color, String strData, String endDay){

        TextView tV = new TextView(this);

        //입력받은 height값을 DP단위로 교체(25dp가 제일 작은칸의 크기)
        height = (25 * height) + (height-1)*4;
        int dp_height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());

        tV.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, dp_height));

        // 각 칸의 아래에 빈공간 Margin은 4dp로 준다.
        int dp_margin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams( tV.getLayoutParams());
        margin.setMargins(0, 0, 0, dp_margin);
        tV.setLayoutParams(new LinearLayout.LayoutParams(margin));

        tV.setGravity(Gravity.CENTER);
        tV.setPadding(20, 10, 10, 10);
        tV.setAlpha((float) 0.8);
        tV.setTextSize(13);
        tV.setText(strData);

        //박스의 색설정
        switch (color){
            case 1:
                tV.setBackgroundColor(Color.parseColor("#ebebeb"));
                tV.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                tV.setBackgroundColor(Color.parseColor("#f49ac1"));
                tV.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                tV.setBackgroundColor(Color.parseColor("#082233"));
                tV.setTextColor(Color.parseColor("#ffffff"));
                break;
        }

        //박스가 어디 위치할지 설정
        switch(whichRow){
            case 1: depth1_Linear.addView(tV); break;
            case 2: depth2_Linear.addView(tV); break;
            case 3: depth3_Linear.addView(tV); break;
            case 4: content_Linear.addView(tV); break;
            case 5: android_Linear.addView(tV); break;
            case 6: ios_Linear.addView(tV); break;
            case 7: ie_Linear.addView(tV); break;
            case 8: chrome_Linear.addView(tV); break;
            case 9: firefox_Linear.addView(tV); break;
        }


    }


    //ConnectServer에서 받은값으로 makeBox호출
    public void testSetting(String JsonStr) {

        StringBuffer sb = new StringBuffer();
        String str = CustomApplication.testJson;
        //String str = JsonStr;

        //depth1,2가 depth3의 어디까지 탐색했는지 저장(depth 4,5,6은 무조건 한칸이니까 같은 Index 탐색하면 됨)
        int d1Index = 0;
        int d2Index = 0;



        //원래는 서버에서 색을 다 정해줘야 되는데, depth3의 색만 받고 1,2는 그에따라 바뀌도록 앱에서 조작해주는걸로 해주기로 함
        //depth3이 가장 마지막에 입력되므로 먼저 한바퀴 돌려서 색만 저장해놓는다.
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int whichRow = jObject.getInt("whichRow");
                int height = jObject.getInt("height");
                int color = jObject.getInt("color");

                //리스트에 depth3의 색을 저장
                //찾기쉽게 한칸단위로 쪼개서 넣는다. 3칸짜리면 1칸짜리 3개로 나누어서.
                if(whichRow==3){
                    for(int j=0; j<height; j++)
                    d3Color.add(color);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //전체적인 데이터들을 입력
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int whichRow = jObject.getInt("whichRow");
                int height = jObject.getInt("height");
                int color = jObject.getInt("color");
                String strData = jObject.getString("strData");
                String endDay = jObject.getString("endDay");

                //depth1,2는 depth3에 따라 색이 바뀐다.
                if(whichRow == 1){
                    color = settingColor(height, d1Index);
                    d1Index += height;
                }
                else if(whichRow == 2){
                    color = settingColor(height, d2Index);
                    d2Index += height;
                }


                //해당하는 데이터가 있으면 칸을 열어준다.
                if(whichRow==5 && android_Linear.getVisibility()==View.GONE){
                    android_Linear.setVisibility(View.VISIBLE);
                }
                else if(whichRow==6 && ios_Linear.getVisibility()==View.GONE){
                    ios_Linear.setVisibility(View.VISIBLE);
                }
                else if(whichRow==7 && ie_Linear.getVisibility()==View.GONE){
                    ie_Linear.setVisibility(View.VISIBLE);
                }
                else if(whichRow==8 && chrome_Linear.getVisibility()==View.GONE){
                    chrome_Linear.setVisibility(View.VISIBLE);
                }
                else if(whichRow==9 && firefox_Linear.getVisibility()==View.GONE){
                    firefox_Linear.setVisibility(View.VISIBLE);
                }

                //데이터 표시
                makeBox(whichRow, height, color, strData, endDay);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    //==============================================================================================
    //depth3의 색에따라 depth1,2의 색이 바뀌게 해주는 매소드(색값을 리턴)
    //index는 위에서 몇번째 박스인지(0부터시작)
    //==============================================================================================
    public int settingColor(int height, int index){

        //기본리턴 색은 '테스트완료'이고, 검사하면서 조건불충족시 그에 맞게 바뀜
        int returnColor=3;


        for(int i=index; i<index+height; i++){

            //하나라도 오류수정중이면 오류수정중!
            if(d3Color.get(i) == 2){returnColor = 2; }

            //테스트이전
            if(d3Color.get(i) == 1 && returnColor != 2){returnColor = 1; }

        }

        return returnColor;
    }



    //==============================================================================================
    //뒤로버튼 눌렀을때 앱종료
    //==============================================================================================
    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            //3초안에 한번 더 누르면 종료
            ActivityCompat.finishAffinity(this);
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
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
