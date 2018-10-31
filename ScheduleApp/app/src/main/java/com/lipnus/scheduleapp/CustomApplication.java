package com.lipnus.scheduleapp;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Sunpil on 2016-07-10.
 * 여기서 전체 액티비티를 통제한다.
 */



public class CustomApplication extends Application{

    static int wbsDate[];
    static int which_wbs_Deatil;


    //WBS에서 산출물을 눌렀을때 해당 클릭지점의 정보를 web_detail로 넘길때 사용하는 변수들
    //액티비티 간의 상호작용이므로 여기서 값을 관리함
    static String wbs_Type;
    static int wbs_YMD;
    static String wbs_Year;
    static String wbs_Month;
    static String wbs_Date;

    //서버주소(각 AyncTask에서 이용)
    static String serverPath="http://sss.trams.co.kr/";

    //로그인에 성공하면 아이디를 여기다가 저장해놓음
    static String userID;

    //Today부분에서 서버와 접속한다. 그때 wbs, ia, test에 관한 자료들도 다 받은뒤 아래 3개변수에 넣고 각 wbs,ia,test에서 이것들을 이용
    static String wbsJson;
    static String iaJson;
    static String testJson;

    //개발, ia, test의 진행률
    static int developPercent;
    static int iaEndPercent;
    static int iaIngPercent;
    static int testEndPercent;
    static int testIngPercent;

    //wbs에서 쓸 데이터들
    static String wbs_workDay;
    static int wbs_workDayPercent;
    static String wbs_Today;
    static String wbs_comment1;
    static String wbs_comment2;

    static int backgroundPath;

    //os이름을 저장
    static String[] osname = new String[5];

    @Override
    public void onCreate() {
        super.onCreate();
    }



    ////WBS에서 산출물을 눌렀을때 해당 클릭지점의 정보를 일시적으로 담아놓음
    public static void setWbsDetail(String s, int year_month_date){
        wbs_Type = s;
        wbs_YMD = year_month_date;

        String str = Integer.toString(year_month_date);

        //붙어있는 날짜를 다 떼어낸다.
        wbs_Year = str.substring(0, 4);
        wbs_Month = str.substring(4, 6);
        wbs_Date = str.substring(6, 8);


        //substring(인자1, 인자2)
        //인자 1은 시작지점 index, 인자 2는 끝지점 index
    }




    /**
    오늘 할일 [ 기획 / 디자인 / 개발 / 테스트 ]
     drawable경로를 받환해주고, 각 액티비티는 그걸 받아서 배경화면 설정
     */
    public static int checkTodayWork(){

        if(backgroundPath == 0){
            return R.drawable.bg_design;
        }
        else{
            return backgroundPath;
        }

    }

    //wbs상 오늘의 스케줄에 따라 배경설정
    public static void setcheckTodayWork(String type) {

        if (type.equals("test")) {
            backgroundPath = R.drawable.bg_test;
        } else if (type.equals("develop")) {
            backgroundPath = R.drawable.bg_development;
        } else if (type.equals("design")) {
            backgroundPath = R.drawable.bg_design;
        } else if (type.equals("plan")) {
            backgroundPath = R.drawable.bg_planning;
        }
    }

    /**
     하단메뉴 통제
     */
    public void controlBottomMenu(int idValue){

        //인텐트를 선언
        Intent iT;

        switch(idValue){
            case R.id.todayBtn:
                iT = new Intent(this, Today.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.wbsBtn:
                iT = new Intent(this, WBS.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.iaBtn:
                iT = new Intent(this, IA.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.testBtn:
                iT = new Intent(this, Test.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.moreBtn:
                iT = new Intent(this, More.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;
        }



    }
}
