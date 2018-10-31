package com.lipnus.scheduleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WBS extends AppCompatActivity {

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;

    //wbs표와 전체 이미지의 스크롤
    private HorizontalScrollView wbsScroll, mainScroll;

    //WBS데이터가 입력되는 LinearLayout
    LinearLayout wbsInput;

    //뒤쪽의 배경레이어의 크기를 앞쪽의 wbsTable과 맞춘다
    LinearLayout bgLinear;
    LinearLayout wbsLinear;

    //분홍포인터
    TextView pinkPoint_up;
    TextView pinkPoint_down;

    //임시테스트
    TextView tV;

    //월 표시
    TextView monthTv;

    //상단부분 데이터표시
    TextView workDayTv;
    TextView percentTv;
    ProgressBar wbsProgress;
    TextView circleTodayTv;
    TextView endProjectTv;
    TextView leftDayTv;

    //스크롤값의 움직임(onCreate에서 스크롤 조작불가해서 Thread로 해야되는데 스레드 안에 변수를 집어넣으려고)
    int scrollmoving;

    //각 세로줄마다의 주차와 날짜
    int[] count_week = new int[3000];
    int[] count_date = new int[3000];
    int index=0;

    //오늘날짜
    String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbs);

        //상단의 데이터 표시해주는 부분
        workDayTv = (TextView) findViewById(R.id.wbs_workDay);
        percentTv = (TextView) findViewById(R.id.wbs_percentTv);
        wbsProgress = (ProgressBar) findViewById(R.id.wbs_Progress);
        circleTodayTv = (TextView) findViewById(R.id.wbs_circle_Today);
        endProjectTv = (TextView) findViewById(R.id.wbs_endProjectTv);
        leftDayTv = (TextView) findViewById(R.id.wbs_leftDayTv);

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


        //wbs표 부분을 스크롤하면 전체 이미지가 스크롤되도록 함
        wbsScroll = (HorizontalScrollView)findViewById(R.id.wbsScroll);
        wbsScroll.setOnTouchListener(mListener);
        mainScroll = (HorizontalScrollView)findViewById(R.id.mainScroll);
        mainScroll.setOnTouchListener(mListener);

        /*
        //뒷 배경의 크기조절(뒷 배경은 늘이거나 줄이지 않고 일정하게 두는게 더 나은듯..)
        bgLinear = (LinearLayout)findViewById(R.id.bgLayout);
        wbsLinear = (LinearLayout)findViewById(R.id.frontWBSLinear);
        int width = wbsLinear.getWidth()+100;
        */

        //wbs가 입력되는 부분
        wbsInput = (LinearLayout) findViewById(R.id.wbsTableLinear);

        //서버에 접속하여 데이터를 받아온다(
        //connectServer(CustomApplication.userID, "wbs");

        //Today받은 데이터를 재이용
        wbsSetting(null);


        //상단부분 데이터표시
        //==========================================================================================
        workDayTv.setText(CustomApplication.wbs_workDay);
        percentTv.setText(CustomApplication.wbs_workDayPercent + "%");
        wbsProgress.setProgress(CustomApplication.wbs_workDayPercent);
        circleTodayTv.setText(CustomApplication.wbs_Today);
        endProjectTv.setText(CustomApplication.wbs_comment1);
        leftDayTv.setText(CustomApplication.wbs_comment2);
        //==========================================================================================




        //날짜를 표시
        monthTv = (TextView)findViewById(R.id.showmonthTv);

        //분홍포인터
        pinkPoint_up = (TextView) findViewById(R.id.pinkpoint_up);
        pinkPoint_down = (TextView)findViewById(R.id.pinkpoint_down);


        //임시로 스크롤위치 표시
        tV = (TextView)findViewById(R.id.fuck);

        //오늘날짜를 저장
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
        java.util.Date currentTime = new java.util.Date ( );
        todayDate = mSimpleDateFormat.format ( currentTime );
        Log.d("BBBB", "오늘: "+todayDate);



        Log.d("BBBB", ""+count_date[3] );

        //만약 wbs에 오늘이 있다면 오늘로 스크롤(첫째날이 오늘이면 스크롤할 필요가 없음)
        for(int i=1; count_date[i]!=0; i++){
            Log.d("BBBB", ""+count_date[i] );
            //오늘이 존재
            if(Integer.toString(count_date[i]).equals(todayDate)){
                Log.d("BBBB", "오늘이 있따");
                scrollmoving = pxFromDp(35*i);

                wbsScroll.post(new Runnable() {
                    public void run() {
                        wbsScroll.scrollTo( scrollmoving , 0);

                        //옮긴 후 분홍포인터에 현재 중간에 위치한 날짜의 데이터를 표시
                        showPinkText();
                    }
                });
                break;
            }
        }

    }



    public void onClick(View v){
        //makeTable(1,1,1,1,20160909,1,"no");
    }


    //==============================================================================================
    //동적으로 표를 만들어준다.
    //plan,design, develop,test는 각 영역의 산출물의 개수(산출물이 없으면 -1)
    //date는 YYYYMMDD 형식의 날짜
    //week는 몇주차인지(숫자만)
    //eventday:그냥 보통날이면 "no", 공휴일이면 해당 휴일의 값(ex "현충일, 광복절, 휴일 등)
    //==============================================================================================
    public void makeTable(int plan, int design, int develop, int test, int date, int week, String eventday){

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.wbstable, null);
        wbsInput.addView(convertView);

        //사용되어지는 객체들========================================================
        Button plan_btn = (Button)findViewById(R.id.plan_btn);
        Button design_btn = (Button)findViewById(R.id.design_btn);
        Button develop_btn = (Button)findViewById(R.id.develop_btn);
        Button test_btn = (Button)findViewById(R.id.test_btn);
        TextView holiday_Tv = (TextView)findViewById(R.id.holiday_Tv);
        TextView date_Tv = (TextView)findViewById(R.id.wbs_date_Text);
        TextView week_Tv = (TextView)findViewById(R.id.wbs_week_Text);
        TextView month_Tv = (TextView)findViewById(R.id.wbs_month_Text);

        //세로로 아이디가 겹치는 것 방지(세로줄끼리 날짜는 같을 수가 없으니까)
        plan_btn.setId(date);
        design_btn.setId(date);
        develop_btn.setId(date);
        test_btn.setId(date);
        holiday_Tv.setId(date);
        date_Tv.setId(date);
        week_Tv.setId(date);
        month_Tv.setId(date);

        //========================================================================


        //========================================================================
        //각 세로줄 마다의 주차와 날짜를 배열에 저장
        count_week[index] = week;
        count_date[index] = date;
        index++;

        //오늘이 있으면 스크롤을 오늘로 맞춤
        if(Integer.toString(date).equals(todayDate)) {
            Log.d("BBBB", "오늘이 wbs에 있어서 스크롤을 거기로 옮김");
            scrollmoving = pxFromDp(35 * (index-1) );

            wbsScroll.post(new Runnable() {
                public void run() {
                    wbsScroll.scrollTo(scrollmoving, 0);
                }
            });
        }
        //========================================================================

        //기획
        if(plan == -1){
            plan_btn.setVisibility(View.INVISIBLE);
        }
        else{
            plan_btn.setText(Integer.toString(plan));

            if(plan != 0){ //산출물이 0이면 다운받을게 없으니까 wbs_detail로 갈 필요가 없고 그렇지 않을경우
                plan_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplication.setWbsDetail("기획",v.getId());

                        Intent iT = new Intent(getApplicationContext(), WBS_detail.class);
                        startActivity(iT);

                    }
                });
            }
        }//기획(if)

        //디자인
        if(design == -1){
            design_btn.setVisibility(View.INVISIBLE);
        }
        else{
            design_btn.setText(Integer.toString(design));

            if(design != 0){ //산출물이 0이면 다운받을게 없으니까 wbs_detail로 갈 필요가 없고 그렇지 않을경우
                design_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplication.setWbsDetail("디자인",v.getId());

                        Intent iT = new Intent(getApplicationContext(), WBS_detail.class);
                        startActivity(iT);

                    }
                });
            }
        }//디자인(if)

        //개발
        if(develop == -1){
            develop_btn.setVisibility(View.INVISIBLE);
        }
        else{
            develop_btn.setText(Integer.toString(develop));

            if(develop != 0){ //산출물이 0이면 다운받을게 없으니까 wbs_detail로 갈 필요가 없고 그렇지 않을경우
                develop_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplication.setWbsDetail("개발",v.getId());

                        Intent iT = new Intent(getApplicationContext(), WBS_detail.class);
                        startActivity(iT);

                    }
                });
            }
        }//개발(if)


        //테스트
        if(test == -1){
            test_btn.setVisibility(View.INVISIBLE);
        }
        else{
            test_btn.setText(Integer.toString(test));

            if(test != 0){ //산출물이 0이면 다운받을게 없으니까 wbs_detail로 갈 필요가 없고 그렇지 않을경우
                test_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplication.setWbsDetail("테스트",v.getId());

                        Intent iT = new Intent(getApplicationContext(), WBS_detail.class);
                        startActivity(iT);

                    }
                });
            }
        }//테스트(if)


        //날짜표기
        String dateStr;
        dateStr = Integer.toString(date).substring(6,8);
        if(dateStr.substring(0,1).equals("0")){ //04일같이 앞자리가 0이면 제거
            dateStr = dateStr.substring(1,2);
        }
        date_Tv.setText(dateStr);

        //앞뒤로 빈공간 여백을 위해서 넣을때 임의로 넣은 날짜는 19890322. 이 날짜가 들어오면 아예 표시를 안함
        if(date == 19890322){
            date_Tv.setText("");
        }


        //휴일일때의 처리
        if( eventday.equals("no") == false ){
            plan_btn.setVisibility(View.GONE);
            design_btn.setVisibility(View.GONE);
            develop_btn.setVisibility(View.GONE);
            test_btn.setVisibility(View.GONE);

            holiday_Tv.setVisibility(View.VISIBLE);
            holiday_Tv.setText(eventday);
        }

    }




    //ConnectServer에서 받은값으로 makeBox호출
    public void wbsSetting(String JsonStr) {




        Log.d("AAAA", "wbsSetting()");
        StringBuffer sb = new StringBuffer();

        String str = CustomApplication.wbsJson;

        /*
        String str = "[{'plan':1,'design':2,'develop':3,'test':4,'date':20160726,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160727,'week':2,'eventday':'노는날이다'},"+
                "{'plan':1,'design':2,'develop':-1,'test':4,'date':20160728,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':-1,'test':4,'date':20160729,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':-1,'test':4,'date':20160730,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':-1,'test':4,'date':20160731,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':-1,'develop':3,'test':4,'date':20160801,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':-1,'develop':3,'test':4,'date':20160802,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':-1,'develop':3,'test':4,'date':20160803,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160804,'week':2,'eventday':'휴일'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160805,'week':2,'eventday':'휴일'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160806,'week':2,'eventday':'휴일'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160807,'week':2,'eventday':'휴일'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160808,'week':2,'eventday':'휴일'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160809,'week':2,'eventday':'노는날이다'},"+
                "{'plan':1,'design':2,'develop':-1,'test':4,'date':20160810,'week':2,'eventday':'no'},"+
                "{'plan':1,'design':2,'develop':3,'test':4,'date':20160818,'week':2,'eventday':'no'}]";
        */


        //int plan, int design, int develop, int test, int date, int week, String eventday

        //스크롤이 끝까지 가게 하기위해 앞쪽에 3칸 채워줌
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int plan = jObject.getInt("plan");
                int design = jObject.getInt("design");
                int develop = jObject.getInt("develop");
                int test = jObject.getInt("test");
                int date = jObject.getInt("date");
                int week = jObject.getInt("week");
                String eventday = jObject.getString("eventday");

                sb.append(plan+" "+design+" "+develop+" "+test+" "+date+" "+week+" "+eventday);
                Log.d("AAAA", "*"+sb);

                makeTable(plan, design, develop, test, date, week, eventday);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //스크롤이 끝까지 가게 하기위해 뒤쪽에 5칸 채워줌
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
        makeTable(-1, -1, -1, -1, 19890322, 1, "no");
    }











    //==============================================================================================
    //1.
    //중간의 분홍색 포인터에 값을 표신
    //
    //2.
    //스크롤 뷰 터치 이벤트 리스너.' wbs의 이벤트를 전체스크롤창으로 동기화'시킨다.
    //(스크롤뷰가 화면의 일부이고 그걸 스크롤하면 전체 배경이 움직이게 해야되는걸 만족하기 위해 이런걸 해야됨...)
    //==============================================================================================
    private View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {

            //1.
            showPinkText();

            //2.
            int id =  v.getId();                    //이벤트 들어온 뷰의 아이디값
            //wbs스크롤 뷰가 터치 되면 뒤쪽 배경 스크롤 뷰에 이벤트 전달
            if(id == R.id.wbsScroll) mainScroll.dispatchTouchEvent(event);    //터치 이벤트 전달

            return false;
        }
    };


    //분홍포인터에 일,주차, 월 값들을 출력한다
    public void showPinkText(){

        int count;
        int screen_width, startpoint;

        //스크린크기
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //스크린크기
        screen_width = dm.widthPixels;
        screen_width = dpFromPx(screen_width);

        startpoint = screen_width/2 - 60; //60은 왼쪽의 동그라미들이 있는 곳의 공간dp
        count = ( startpoint+dpFromPx(wbsScroll.getScrollX()) )/35; //현재 가운데점이 왼쪽으로부터 몇번째 날짜의 데이터인지 표시

        //포인터가 위치한 곳의 날짜
        String today="";

        //포인터가 위치한 곳의 월
        if(count_date[count]!=0){
            today = Integer.toString(count_date[count]).substring(6,8);
            if(today.substring(0,1).equals("0")){ //04일같이 앞자리가 0이면 제거
                today = today.substring(1,2);
            }
        }

        //분홍포인터의 날짜표시
        pinkPoint_up.setText(today);

        //분홍포인터의 몇주차표시
        String str = Integer.toString(count_week[count]) + "w";
        pinkPoint_down.setText(str);

        //아래쪽에 월을 영어로 표시
        monthTv.setText(showMonth( count_date[count] ));

        //현재의 포인터 스크롤을 dp와 px단위로 표시(테스트용)
        //tV.setText(count +"\ndp:"+dpFromPx(wbsScroll.getScrollX()) +"  px"+wbsScroll.getScrollX());
        Log.d("CCCC", "dp:"+dpFromPx(wbsScroll.getScrollX()) +"  px"+wbsScroll.getScrollX());
    }



    //각 월에 맞는 영어단어를 반환
    public String showMonth(int date){

        //배열이 비어있는 경우(날짜 뒤쪽으로 넘어간 경우)
        if (date==0){return " ";}

        String s = Integer.toString(date).substring(4,6);


        if(s.equals("01")) return "January";
        else if(s.equals("02")) return "Febuary";
        else if(s.equals("03")) return "March";
        else if(s.equals("04")) return "April";
        else if(s.equals("05")) return "May";
        else if(s.equals("06")) return "Jun";
        else if(s.equals("07")) return "July";
        else if(s.equals("08")) return "August";
        else if(s.equals("09")) return "September";
        else if(s.equals("10")) return "October";
        else if(s.equals("11")) return "November";
        else if(s.equals("12")) return "December";
        else return "";
    }




    private int pxFromDp(int dp) {

        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,this.getApplicationContext().getResources().getDisplayMetrics());
        return px;
    }

    private int dpFromPx(float px)
    {
        return (int)(px / this.getApplicationContext().getResources().getDisplayMetrics().density);
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




    //임시로 확인하기 위해 wbs_detail창 띄우는버튼
    public void onClick_showDetail(View v){

        Intent iT = new Intent(this, WBS_detail.class);
        startActivity(iT);

    }
}
