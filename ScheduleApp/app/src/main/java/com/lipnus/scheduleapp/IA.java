package com.lipnus.scheduleapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Iterator;

/**
 * Created by Sunpil on 2016-07-14.
 */
public class IA extends AppCompatActivity {

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

    //상단의 프로그래스바 있는부분
    TextView devPerTv;
    TextView iaPerTv;
    ProgressBar devProgress;
    ProgressBar iaIngProgress;
    ProgressBar iaEndProgress;

    //depth3의 색을 저장하고 있는 리스트
    ArrayList<Integer> d3Color;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ia);

        //depth3의 색을 저장하고 있는 리스트
        d3Color = new ArrayList<>();

        //프로그래스바 있는 부분의 객체
        devPerTv = (TextView) findViewById(R.id.ia_devlopPercent_Tv);
        iaPerTv = (TextView) findViewById(R.id.ia_Percent_Tv);
        devProgress = (ProgressBar) findViewById(R.id.ia_devlopPercent_Progress);
        iaIngProgress = (ProgressBar) findViewById(R.id.ia_Percent_Ing_Progress);
        iaEndProgress = (ProgressBar) findViewById(R.id.ia_Percent_End_Progress);


        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todaybutton);
        wbsBtn.setBackgroundResource(R.drawable.wbsbutton);
        iaBtn.setBackgroundResource(R.drawable.iaclick);
        testBtn.setBackgroundResource(R.drawable.testbutton);
        moreBtn.setBackgroundResource(R.drawable.morebutton);
        //===========================================================


        //동적으로 생성되는 값들이 들어가는 LinearLayout
        depth1_Linear = (LinearLayout) findViewById(R.id.depth1_Linear);
        depth2_Linear = (LinearLayout) findViewById(R.id.depth2_Linear);
        depth3_Linear = (LinearLayout) findViewById(R.id.depth3_Linear);


        //connectServer(CustomApplication.userID, "ia");
        iaSetting(null);

        //상단의 프로그래스바있는부분 값 설정
        devPerTv.setText(CustomApplication.developPercent + "%");
        iaPerTv.setText(""+CustomApplication.iaEndPercent + "%");
        devProgress.setProgress(CustomApplication.developPercent);
        iaIngProgress.setProgress(CustomApplication.iaIngPercent+CustomApplication.iaEndPercent);
        iaEndProgress.setProgress(CustomApplication.iaEndPercent);



    }


    //==============================================================================================
    //동적으로 표를 만들어준다.
    // whichRow = 1:depth1 2:depth2 3:depth3 에 표생성
    // height = 제일작은칸을 기준(1)으로 칸의 세로길이
    // color = 1:흰색(테스트이전) 2:하늘색(오류수정중) 3:진한파랑(테스트완료)
    // endDay는 해당칸의 개발완료예정일(YYYYMMDD)
    //==============================================================================================
    public void makeBox(int whichRow, int height, int color, String strData, String endDay){

        TextView tV = new TextView(this);

        //입력받은 height값을 DP단위로 교체(50dp가 제일 작은칸의 크기)
        height = (50 * height) + (height-1)*4; //뒷부분은 각 칸들 사이의 여백때문에 띄워준것
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

        //내용+완료예정일
        String printText = endDay.substring(0, 4) +"."+ endDay.substring(4, 6) +"."+ endDay.substring(6, 8);
        tV.setText(strData+"\n"+printText);

        //박스의 색설정
        switch (color){
            case 1:
                tV.setBackgroundColor(Color.parseColor("#ebebeb"));
                tV.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                tV.setBackgroundColor(Color.parseColor("#6dcff6"));
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
        }


    }









    public void iaSetting(String JsonStr) {

        StringBuffer sb = new StringBuffer();
        String str = CustomApplication.iaJson;

        //depth1과 depth2의 색은 depth3에 의해 결정되는데, 어디까지 탐색했는지 저장해놓음
        int d1Index = 0;
        int d2Index = 0;



        //원래는 서버에서 색을 다 정해줘야 되는데, depth3의 색만 받고 1,2는 그에따라 바뀌도록 앱에서 조작해주는걸로 해주기로 함
        //depth3이 가장 마지막에 입력되므로 먼저 한바퀴 돌려서 색만 저장해놓는다.
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int whichRow = jObject.getInt("whichRow");
                int color = jObject.getInt("color");

                //리스트에 depth3의 색을 저장
                if(whichRow==3){ d3Color.add(color); }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //색이 잘 저장되었는지 로그로 표시
        Iterator it = d3Color.iterator();
        while(it.hasNext()){
            Log.d("COLORTEST", ""+it.next() );
        }



        //전체적인 데이터들을 입력
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            Log.d("AAAA", "여기2");
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



                //데이터를 표시
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

        //Log.d("COLORTEST", "탐색시작");

        for(int i=index; i<index+height; i++){
            //Log.d( "COLORTEST", i +"번째: "+ d3Color.get(i) );

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
