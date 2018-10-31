package com.lipnus.scheduleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Today extends AppCompatActivity {


    //배경화면을 담당할 FrameLayout
    FrameLayout backgroundFrameLayout;

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //서버에서 받은 데이터들을 정렬해서 Today에 표시해줌
    //==============================================================
    int startday=20160801; //wbs상의 프로젝트 시작날짜
    int endday=20160901; //wbs상의 프로젝트 끝나는날
    int todayDate=00000000; //오늘

    String latest_ia;
    String latest_test;
    String latest_filename;
    String latest_comment;
    int latest_filedate;
    String latest_filetype;

    int depth3_endDayPass=0;

    //ia(단위테스트)
    int ia_all=0;
    int ia_testFinish=0;
    int ia_testIng=0;
    int ia_endPercent;
    int ia_IngPercent;

    //test(통합테스트)
    int test_all=0;
    int test_testFinish=0;
    int test_testIng=0;
    int test_endPercent=0;
    int test_IngPercent=0;
    int test_all_holiday=0; //전체 휴일
    int test_today_holiday=0; //프로젝트 시작일부터 오늘까지의 휴일

    //==============================================================

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;

    //메인의 객체들
    TextView calendarTv;
    TextView hiIamTrams;
    TextView busDayTv;
    ProgressBar busProgress;
    TextView busPerTv;

    TextView deveop_percent_Tv;
    ProgressBar develop_percent_Progress;

    TextView iaPerTv;
    ProgressBar iaProgressIng;
    ProgressBar iaProgressEnd;

    TextView testPerTv;
    ProgressBar testProgressIng;
    ProgressBar testProgressEnd;

    TextView circleTodayTv;
    TextView finishDayTv;
    TextView leftDayTv;

    TextView todayCircleTv;
    TextView busday_bottomTv;

    TextView filenameTv;
    TextView filecommentTv;
    TextView ianameTv;
    TextView iacommentTv;
    TextView iatestTv;
    TextView qaTv;

    //d3의 색을 저장하고 있는 리스트
    ArrayList<Integer> d3Color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);


        d3Color = new ArrayList<>();

        //메인의 객체들
        //===========================================================
        calendarTv = (TextView) findViewById(R.id.calendarTv);
        hiIamTrams = (TextView) findViewById(R.id.HiIamTramsTv);
        busDayTv = (TextView) findViewById(R.id.busDayTv);
        busProgress = (ProgressBar) findViewById(R.id.busDayProgress);
        busPerTv = (TextView) findViewById(R.id.busPercentTv);

        deveop_percent_Tv = (TextView)findViewById(R.id.develop_percent_Tv);
        develop_percent_Progress = (ProgressBar) findViewById(R.id.develop_percent_Progress);

        iaPerTv = (TextView) findViewById(R.id.iaPerTv);
        iaProgressIng = (ProgressBar) findViewById(R.id.iaProgress_Ing);
        iaProgressEnd = (ProgressBar) findViewById(R.id.iaProgress_Finish);

        testPerTv = (TextView) findViewById(R.id.testPerTv);
        testProgressIng = (ProgressBar) findViewById(R.id.testProgressIng);
        testProgressEnd = (ProgressBar) findViewById(R.id.testProgressEnd);

        circleTodayTv = (TextView)findViewById(R.id.TodayCircleTv);
        finishDayTv = (TextView) findViewById(R.id.ProjectFinishTv);
        leftDayTv = (TextView) findViewById(R.id.ProjectLeftdayTv);

        todayCircleTv = (TextView) findViewById(R.id.busDayCircleTv);
        busday_bottomTv = (TextView) findViewById(R.id.busDayTv_bottom);

        filenameTv = (TextView) findViewById(R.id.today_filenameTv);
        filecommentTv = (TextView) findViewById(R.id.today_filename_commentTv);
        ianameTv = (TextView) findViewById(R.id.today_iaTv);
        iacommentTv = (TextView) findViewById(R.id.today_ia_commentTv);
        iatestTv = (TextView) findViewById(R.id.today_iatestTv);
        qaTv = (TextView) findViewById(R.id.today_qaTv);
        //===========================================================

        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todayclick);
        wbsBtn.setBackgroundResource(R.drawable.wbsbutton);
        iaBtn.setBackgroundResource(R.drawable.iabutton);
        testBtn.setBackgroundResource(R.drawable.testbutton);
        moreBtn.setBackgroundResource(R.drawable.morebutton);
        //===========================================================


        //오늘
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
        java.util.Date currentTime = new java.util.Date ( );
        todayDate = Integer.parseInt( mSimpleDateFormat.format ( currentTime ) );



        //서버에서 데이터를 받아옴
        connectServer(CustomApplication.userID, "main");
        connectServer(CustomApplication.userID, "wbs");
        connectServer(CustomApplication.userID, "ia");
        connectServer(CustomApplication.userID, "test");
    }




    //흰색 메뉴
    public void onClick_whiteMenu(View v){

        Intent iT;

        switch(v.getId()) {
            case R.id.projectFinishLinear:
                iT = new Intent(this, WBS.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.businessDayLinear:
                iT = new Intent(this, WBS.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.filenameLinear:

                //업로드된 파일이 없으면 이동하지 않음
                if(latest_filetype.equals("no")){
                    Toast.makeText(getApplicationContext(), "업로드 된 파일이 없습니다", Toast.LENGTH_LONG).show();
                }
                else {
                    CustomApplication.setWbsDetail(latest_filetype, latest_filedate);
                    iT = new Intent(getApplicationContext(), WBS_detail.class);
                    startActivity(iT);
                }
                break;

            case R.id.iadevelopLinear:
                iT = new Intent(this, IA.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.iaunittestLinear:
                iT = new Intent(this, IA.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.iacombinetestLinear:
                iT = new Intent(this, Test.class);
                iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(iT);
                break;

            case R.id.callLinear:
                iT = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01086018905") );
                startActivity(iT);
                break;
        }

    }




    //ConnectServer에서 받은은값들 정리
    public void dataSetting(String JsonStr, String type) {

        StringBuffer sb = new StringBuffer();
        String str = JsonStr;

        //========================================
        //main의 자료들 정리
        //========================================
        if(type.equals("main")){
            Log.d("FFFF", "[main]");

            try {
                JSONArray jarray = new JSONArray(str);   // JSONArray 생성
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    latest_ia = jObject.getString("latest_ia");
                    latest_test = jObject.getString("latest_test");
                    latest_filename = jObject.getString("latest_filename");
                    latest_comment = jObject.getString("latest_comment");
                    latest_filedate = jObject.getInt("latest_filedate");
                    latest_filetype = jObject.getString("latest_filetype");

                    //os목록들의 이름을 저장
                    CustomApplication.osname[0] = jObject.getString("osname1");
                    CustomApplication.osname[1] = jObject.getString("osname2");
                    CustomApplication.osname[2] = jObject.getString("osname3");
                    CustomApplication.osname[3] = jObject.getString("osname4");
                    CustomApplication.osname[4] = jObject.getString("osname5");


                    //main에서 받은 데이터는 가공없이 그대로 쓰이기때문에 받을때부터 전역변수로 선언해놓고 printdata()에서 처리
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//main(if)


        //========================================
        //wbs의 자료들 정리
        //========================================
        else if(type.equals("wbs")){
            Log.d("FFFF", "[wbs]");

            //받은String형태의 데이터를 커스텀어플리케이션의 변수에 저장(앱 전체의 전역변수역할)
            CustomApplication.wbsJson = JsonStr;

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

                    //프로젝트 시작일
                    if(i==0){startday = date;}

                    //프로젝트 종료일
                    if(i==jarray.length()-1){ endday = date; }

                    //wbs안에 오늘이 있으면 우선순위(테스트>개발>디자인>기획)에 맞게 배경을 설정
                    if(todayDate == date){
                        Log.i("FFFF", "wbs상에 오늘이 존재");
                        if(test!=-1){ CustomApplication.setcheckTodayWork("test"); }
                        else if(develop!=-1){ CustomApplication.setcheckTodayWork("develop"); }
                        else if(design!=-1){ CustomApplication.setcheckTodayWork("design"); }
                        else if(plan!=-1){ CustomApplication.setcheckTodayWork("plan"); }
                    }

                    //영업일차에서 휴일은 빼야되기 때문에 휴일의 개수를 센다.
                    if( eventday.equals("휴일") ){
                        test_all_holiday++;

                        if( diffOfDate(date ,todayDate) > 0 ){
                            //프로젝트생성일~오늘이전까지의 휴일
                            test_today_holiday++;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//wbs(else if)



        //========================================
        //ia의 자료들 정리
        //========================================
        else if(type.equals("ia")){
            Log.d("FFFF", "[ia]");

            //받은String형태의 데이터를 커스텀어플리케이션의 변수에 저장(앱 전체의 전역변수역할)
            CustomApplication.iaJson = JsonStr;

            try {
                JSONArray jarray = new JSONArray(str);   // JSONArray 생성
                Log.d("AAAA", "여기2");

                boolean selected = false;

                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    int whichRow = jObject.getInt("whichRow");
                    int color = jObject.getInt("color");
                    String strData = jObject.getString("strData");
                    String develop_endDay = jObject.getString("endDay");

                    //3depth인 것의 개수센다
                    if(whichRow==3){ ia_all++; }

                    //3depth중 완료된것 개수센다
                    if(whichRow==3 && color == 3){ ia_testFinish++; }

                    //3depth중 오류수정중인것 개수센다
                    if(whichRow==3 && color == 2){ ia_testIng++; }

                    //3depth중 완료예정일이 지난 것의 개수센다 (diffOfDate(a,b)는 a와 b가 같은날이면 1을 반환함. b가 a보다 하루전이면 0반환)
                    if(whichRow==3 && diffOfDate(todayDate ,Integer.parseInt(develop_endDay))<1 ){
                        depth3_endDayPass++;
                    }

                    //오늘 완료예정일인것중 가장 위쪽것을 찝음(하나만 고르면 됨)
                    if(selected==false && Integer.parseInt(develop_endDay) == todayDate){
                        //원래 setText는 printData()매소드에서 처리하는데 이 두개는 그냥 여기서..

                        ianameTv.setText(strData);
                        iacommentTv.setText("개발중...");
                        selected = true;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//ia(else if)



        //========================================
        //test의 자료들 정리
        //========================================
        else if(type.equals("test")){
            Log.d("FFFF", "[test]");

            //받은String형태의 데이터를 커스텀어플리케이션의 변수에 저장(앱 전체의 전역변수역할)
            CustomApplication.testJson = JsonStr;




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

            //데이터 처리
            try {
                JSONArray jarray = new JSONArray(str);   // JSONArray 생성
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    int whichRow = jObject.getInt("whichRow");
                    int height = jObject.getInt("height");
                    int color = jObject.getInt("color");
                    String strData = jObject.getString("strData");
                    String develop_endDay = jObject.getString("endDay");

                    //depth1,2는 depth3에 따라 색이 바뀐다.
                    if(whichRow == 1){
                        color = settingColor(height, d1Index);
                        d1Index += height;
                    }
                    else if(whichRow == 2){
                        color = settingColor(height, d2Index);
                        d2Index += height;
                    }

                    //test 전체 칸의 수
                    test_all++;

                    //완료된것 개수
                    if(color == 3){ test_testFinish++; }

                    //오류수정중인것 개수
                    if(color == 2){ test_testIng++; }



                    //데이터들을 출력
                    //test가 제일 마지막으로 연동되니까 여기서 출력하는 매소드를 호출하는 것임
                    Log.d("ASDF", test_testFinish + ", " +test_testIng);
                    printData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//test(else if)

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


    //서버에서 받은 데이터들을 텍스트뷰와 프로그래스바에 표시해줌
    public void printData(){

        //배경화면 설정
        backgroundFrameLayout = (FrameLayout)findViewById(R.id.backgroundFrameLayout);
        backgroundFrameLayout.setBackgroundResource( CustomApplication.checkTodayWork() );

        //main===================================================================================
        Log.e("ASDF", latest_ia +" "+ latest_test+" "+latest_filename+" "+latest_comment+
                " "+latest_filedate+" "+latest_filetype);

        if(latest_filetype.equals("no")){
            filenameTv.setText("Empty");
            filecommentTv.setText("업로드된 파일이 없습니다");
        }else {
            filenameTv.setText(latest_filename);
            filecommentTv.setText(latest_comment);
        }

        iatestTv.setText(latest_ia);
        qaTv.setText(latest_test);
        //======================================================================================

        //wbs===================================================================================
        Log.d("FFFF", "[wbs]================================================");
        int workDay = diffOfDate(startday, todayDate) - test_today_holiday; //프로젝트시작일~오늘 까지의 휴일을 뺀다
        int all_workDay = diffOfDate(startday,endday) - test_all_holiday; //전체 영업일에서 휴일을 뺀다
        int per_workDay = (int)( ((double)workDay /(double)all_workDay )*100);
        int leftDay = diffOfDate(todayDate, endday)-1;

        Log.d("FFFF", "프로젝트 시작일:" + startday + " 프로젝트 종료일:" + endday);
        Log.d("FFFF", "오늘: "+ todayDate);
        Log.d("FFFF", "영업일차: "+ workDay  );
        Log.d("FFFF", "전체영업일: "+all_workDay );
        Log.d("FFFF", "영업일차퍼센트: " + per_workDay );
        Log.d("FFFF", "완료: " + leftDay + "일전");

        //프로젝트 시작 전이면 퍼센트는 0으로 표시
        if(workDay<0){ per_workDay = 0; }

        calendarTv.setText(""+leftDay);
        hiIamTrams.setText("안녕하세요. (주)트램스입니다. \n프로젝트 완료 "+leftDay+"일 전입니다.");

        //프로젝트 완료일이거나 지났을 경우
        if(leftDay == 0){
            hiIamTrams.setText("안녕하세요. (주)트램스입니다. \n오늘은 프로젝트 완료일입니다");
        }else if(leftDay < 0){
            hiIamTrams.setText("안녕하세요. (주)트램스입니다. \n프로젝트 완료일로부터 "+ Math.abs(leftDay) + "일이 지났습니다.");
        }

        busDayTv.setText(workDay+" ");
        busProgress.setProgress(per_workDay);
        busPerTv.setText(per_workDay+"%");

        String month;
        if(Integer.toString(todayDate).substring(4,5).equals("0")){ //04일같이 앞자리가 0이면 제거
            month = Integer.toString(todayDate).substring(5,6);
        }else{
            month = Integer.toString(todayDate).substring(4,6);
        }


        circleTodayTv.setText(month+"월\n"+Integer.toString(todayDate).substring(6,8)+"일");
        finishDayTv.setText("프로젝트 완료일: "+
                Integer.toString(endday).substring(0,4) +"년 "+
                Integer.toString(endday).substring(4,6)+"월 " +Integer.toString(endday).substring(6,8)+ "일");
        leftDayTv.setText("프로젝트완료 "+leftDay+"일 전");

        todayCircleTv.setText("Day\n  "+workDay);
        busday_bottomTv.setText(workDay + " 영업일차 작업 중");

        //==========================================================================================




        //ia========================================================================================
        ia_endPercent = (int)((double)ia_testFinish/(double)ia_all*100);
        ia_IngPercent = (int)((double)ia_testIng/(double)ia_all*100);

        Log.d("FFFF", "IA depth3개수: "+ia_all);
        Log.d("FFFF", "IA 완료: " + ia_testFinish);
        Log.d("FFFF", "IA 테스트중: " + ia_testIng );
        Log.d("FFFF", "완료율: "+ ia_endPercent );
        Log.d("FFFF", "수정중인거 퍼센트: " + ia_IngPercent );
        Log.e("FFFF", "차이: "+diffOfDate(20160731,20160731) );


        iaPerTv.setText((int)((double)ia_testFinish/(double)ia_all*100) + "%");
        iaProgressIng.setProgress(ia_endPercent+ia_IngPercent);
        iaProgressEnd.setProgress(ia_endPercent);

        //커스텀애플리케이션의 변수값 넣음
        CustomApplication.iaIngPercent = ia_IngPercent;
        CustomApplication.iaEndPercent = ia_endPercent;
        //==========================================================================================



        //test==================================================================================
        test_endPercent = (int)((double)test_testFinish/(double)test_all*100);
        test_IngPercent = (int)((double)test_testIng/(double)test_all*100);

        Log.d("FFFF", "Test 전체칸수: "+test_all);
        Log.d("FFFF", "Test 완료: " + test_testFinish);
        Log.d("FFFF", "Test 테스트중: " + test_testIng );
        Log.d("FFFF", "완료율: "+ test_endPercent );
        Log.d("FFFF", "수정중인거 퍼센트: " + test_IngPercent );

        testPerTv.setText((int)((double)test_testFinish/(double)test_all*100) + "%");
        testProgressIng.setProgress(test_endPercent+test_IngPercent);
        testProgressEnd.setProgress(test_endPercent);

        //커스텀애플리케이션의 변수값 넣음
        CustomApplication.testIngPercent = test_IngPercent;
        CustomApplication.testEndPercent = test_endPercent;
        //======================================================================================

        //개발진행률==================================================================================
        int develop_percent = (int)(((double)depth3_endDayPass / (double)(ia_all)*100));

        Log.d("FFFF", "[개발진행률]================================================");
        Log.d("FFFF", "지난거: " + depth3_endDayPass + " 합: "+ (ia_all) );
        Log.d("FFFF", "퍼센트: "+ develop_percent );

        deveop_percent_Tv.setText(develop_percent + "%");
        develop_percent_Progress.setProgress(develop_percent);

        //커스텀애플리케이션의 변수값 넣음(아래쪽 부분 값은 대부분 wbs부분에서 가져온것이니 참고)
        CustomApplication.developPercent = develop_percent;

        CustomApplication.wbs_workDay = Integer.toString(workDay);
        CustomApplication.wbs_workDayPercent = per_workDay;
        CustomApplication.wbs_Today = (month+"월\n"+Integer.toString(todayDate).substring(6,8)+"일");
        CustomApplication.wbs_comment1 =("프로젝트 완료일: "+
                Integer.toString(endday).substring(0,4) +"년 "+
                Integer.toString(endday).substring(4,6)+"월 " +Integer.toString(endday).substring(6,8)+ "일");
        CustomApplication.wbs_comment2 =("프로젝트완료 "+leftDay+"일 전");

        //==========================================================================================
    }



    //두 날짜 사이의 간격을 계산해주는 매소드
    public int diffOfDate(int firstday, int lastday){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String begin = Integer.toString(firstday);
        String end = Integer.toString(lastday);

        try{

            java.util.Date beginDate = formatter.parse(begin);
            java.util.Date endDate = formatter.parse(end);

            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return ( (int)(diffDays) +1);

        }catch(Exception e){e.printStackTrace(); return 0;}
    }




    /**=============================================================================================
     *
     * AyncTask를 이용하여 웹에 접속
     *
     * =============================================================================================
     */
    private void connectServer(String id, String type){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            String dataType;

            @Override //시작
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Today.this, "Loading..", null, true, true);
            }

            @Override //끝
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //받은 json데이터를 처리(dataSetting매소드에서는 타입에 따라 알아서 필요한 자료들을 정리하고 get한다)
                dataSetting(s, dataType);

                loading.dismiss();

            }

            @Override //메인루프
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String type = (String)params[1];

                    //타입을 onPostExecute에서도 쓰려고 이렇게 함
                    dataType = type;

                    //접속할 주소
                    String link = CustomApplication.serverPath;
                    link = link + "app/getdata.php";


                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");

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
        task.execute(id,type);
    }//connecttoserver클래스







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

    //하단 메뉴
    public void onClick_menu(View v){

        //CustomApplication에서 통제
        CustomApplication CusApp = (CustomApplication) getApplication();
        CusApp.controlBottomMenu(v.getId());
        overridePendingTransition(R.anim.noting, R.anim.noting);

    }

}
