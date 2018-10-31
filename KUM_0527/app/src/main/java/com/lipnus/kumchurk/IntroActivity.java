package com.lipnus.kumchurk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.lipnus.kumchurk.data.Weather;
import com.lipnus.kumchurk.join.JoinLastActivity;
import com.lipnus.kumchurk.join.JoinUploadUserInfoActivity;
import com.lipnus.kumchurk.kum_class.GetLocation;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.lipnus.kumchurk.R.id.intro_solo_Lr;
import static com.lipnus.kumchurk.R.id.intro_with_Lr;

public class IntroActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //동영상 재생에 관련된 것들
    //정보 : http://whiteduck.tistory.com/24
    //샘플 : KUM_movie
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;

    //메인의 이미지 설정
    ImageView blackCoverIv;
    ImageView logoIv;

    //카카오버튼
    com.kakao.usermgmt.LoginButton kakaoBtn;
    ImageView kakaoBtn2;

    //질문
    LinearLayout questionLr;
    LinearLayout soloLr;
    LinearLayout withLr;
    TextView questionTv;
    ImageView questionIv;

    TextView yesTv;
    TextView noTv;
    TextView skipTv;

    //같밥인지 혼밥인지
    boolean isSolo = false;

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //카카오톡 세션 콜백함수
    SessionCallback callback;

    //volley, 리스너, 날씨정보를 담을 DTO
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;
    Weather weatherData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //초기설정
        initSetting();
        initQuestion();

        //비디오뷰 초기화
        videoSetting();

        //날짜정보를 받아온다(http://openweathermap.org)
        initVolleyCallback();
        connect();

        //사용자의 위치정보가 켜져있는지 확인
        checkGPS();

        //위치정보 업데이트(독립적인 클래스 형태로 구현해놓음)
        GetLocation gl = new GetLocation( getApplicationContext() );

        //카카오톡 세션 콜백
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Log.d("KAKAKA", "세션상태: "+ Session.getCurrentSession().isClosed() );

        //가입되었는지 체크
        joinCheck();



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //재시작했을때 동영상 시작(이거 없으면 검은화면만 나온다)

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }







    //초기설정
    public void initSetting(){

        //카카오버튼
        kakaoBtn = (com.kakao.usermgmt.LoginButton)findViewById(R.id.com_kakao_login);
        kakaoBtn2 = (ImageView)findViewById(R.id.intro_kakaobtn_Iv);

        blackCoverIv = (ImageView) findViewById(R.id.intro_coverIv);
        logoIv = (ImageView) findViewById(R.id.intro_logoIv);

        Glide.with(this)
                .load( R.drawable.intro )
                .into(blackCoverIv);
        blackCoverIv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this)
                .load( R.drawable.logo_noodle )
                .into(logoIv);
        logoIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    //onCreate할때 질문준비
    public void initQuestion(){

        //질문
        questionLr = (LinearLayout) findViewById(R.id.intro_questionLr);

        //혼밥, 같밥
        soloLr = (LinearLayout) findViewById(intro_solo_Lr);
        withLr = (LinearLayout) findViewById(intro_with_Lr);

        //질문
        questionTv = (TextView)findViewById(R.id.intro_question_tv);
        questionIv = (ImageView)findViewById(R.id.intro_question_iv);

        //yes, no, skip
        yesTv = (TextView) findViewById(R.id.intro_yes_tv);
        noTv = (TextView) findViewById(R.id.intro_no_tv);
        skipTv = (TextView) findViewById(R.id.intro_skip_tv);

        skipTv.setText("START");

        yesTv.setVisibility(View.INVISIBLE);
        noTv.setVisibility(View.INVISIBLE);

        //기다릴동안 뜨는 시계아이콘
        Glide.with(this)
                .load( R.drawable.intro_wait )
                .into( questionIv );
        questionIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    //질문시작
    public void startQuestion(){

//        soloLr.setVisibility(View.VISIBLE);
//        withLr.setVisibility(View.VISIBLE);
//        yesTv.setVisibility(View.VISIBLE);
//        noTv.setVisibility(View.VISIBLE);
        yesTv.setVisibility(View.GONE);
        noTv.setVisibility(View.GONE);

        soloLr.setAlpha(0.6f);

        questionTv.setText( makeQuestion(weatherData.weather.get(0).description, weatherData.main.temp-273) );

    }

    //비디오뷰 초기화
    public void videoSetting(){

        // surfaceView 등록
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Activity로 Video Stream 전송 등록
        surfaceHolder.addCallback(this);

    }

    //gps가 켜져있는지 확인
    public boolean checkGPS() {

        //현재위치를 구하는데 필요한 지오코더
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);;

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPS) {
            Log.d("GPGP", "gps켜짐");
            return true;
        } else {
            Toast.makeText(getApplication(), "GPS를 켜주세요!", Toast.LENGTH_LONG).show();
            Log.d("GPGP", "gps꺼짐");

            //위치정보 설정창 띄우기
//            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        return false;
    }





    //가입했는지 확인
    public void joinCheck(){

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();


        //가입을 확인(두번째 파라미터는 값이 없을경우 나오는 값) - 0:안가입 1:가입된상태 2:가입했는데 로그아웃
        int joined = setting.getInt("JOIN", 0);


        if(joined == 1){
            //가입된 상태(프레퍼런스에 정보가 있음)

            Log.d("RESS", "뽑아라");
            //프레퍼런스에서 정보를 읽어와서 Application에 저장
            GlobalApplication.setUser_id(setting.getString("user_id", "2013210059"));
            GlobalApplication.setUser_nickname(setting.getString("user_nickname", "유령"));
            GlobalApplication.setUser_image(setting.getString("user_image", "image_error"));
            GlobalApplication.setUser_thumbnail(setting.getString("user_thumbnail", "thumbnail_error"));

            //카톡버튼을 지우고 질문창을 보이게 한다
            kakaoBtn.setVisibility(View.GONE);
            kakaoBtn2.setVisibility(View.GONE);
            questionLr.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(getApplicationContext(), "미가입 상태", Toast.LENGTH_LONG).show();
        }

    }






    //혼밥을 할 것인가 같밥을 할 것인가
    public void onClick_howmanyPeople(View v){

        switch (v.getId()){
            case intro_solo_Lr:
                soloLr.setAlpha(1);
                withLr.setAlpha(0.6f);
                isSolo=true;
                break;

            case intro_with_Lr:
                soloLr.setAlpha(0.6f);
                withLr.setAlpha(1);
                isSolo=false;
                break;
        }
    }

    //답을 선택한 경우
    public void onClick_question_answer(View v){

        Intent iT = new Intent(getApplicationContext(), JoinLastActivity.class);
        startActivity(iT);
        finish(); //액티비티 종료
    }



















    //volley 접속 - (1)
    public void connect(){
        Log.d("VOVO", "접속...");
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=37.56826&lon=126.977829&APPID=2aafb65d33ba4042d1b90190605c6829";

        //날씨를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("", "");




        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    //volley 콜백 - (2)
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                jsonToJava(response);

                //질문부분 시작
                startQuestion();
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                reConnectDialog();

            }
        };
    }

    //다운받은 Json데이터들을 객체화 - (3)
    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        Gson gson = new Gson();

        weatherData = gson.fromJson(jsonStr, Weather.class);

        //어플리케이션 객체의 변수에 날씨정보를 업데이트(온도는 소수점까지는 필요없으니 정수값만)
        GlobalApplication.setTemp( (int)weatherData.main.temp - 273 );
        GlobalApplication.setPresure( weatherData.main.presure );
        GlobalApplication.setHumidity( weatherData.main.humidity );
        GlobalApplication.setTemp_max( (int)weatherData.main.temp_max - 273 );
        GlobalApplication.setTemp_min( (int)weatherData.main.temp_min - 273 );
        GlobalApplication.setMain( weatherData.weather.get(0).main );
        GlobalApplication.setDescription( weatherData.weather.get(0).description );


        Log.d( "VOVO", "온도: " + GlobalApplication.getTemp() + " 날씨: "+ GlobalApplication.getMain() );
        startQuestion();
    }


    //접속실패 다이얼로그
    void reConnectDialog(){

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("sorry..")
                .setContentText("네트워크 접속 장애\n다시 시도할께요!")
                .setCustomImage(R.drawable.sorry2)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        connect();

                    }
                })
                .show();

    }


    //==============================================================================================
    // 동영상재생에 필요한 서퍼스뷰 Interface때문에 꼭 Override해줘야 하는 것들
    //==============================================================================================
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /**
         * surface 생성
         */
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            // local resource : raw에 포함시켜 놓은 리소스 호출
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cham);
            mediaPlayer.setDataSource(this, uri);

            mediaPlayer.setDisplay(holder);                                    // 화면 호출
            mediaPlayer.prepare();                                             // 비디오 load 준비
            mediaPlayer.setOnCompletionListener(completionListener);        // 비디오 재생 완료 리스너
            mediaPlayer.start(); //준비가 완료되면 시작한다

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

        //비디오재생이 끝났을때
        @Override
        public void onCompletion(MediaPlayer mp) {
            //다시시작!
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }









    //==============================================================================================
    //카카오톡으로 로그인 리스너와 콜백(2개)
    //==============================================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {

                    //[Application]에 할당(휘발성)
                    GlobalApplication.setUser_id( Long.toString(userProfile.getId()) );
                    GlobalApplication.setUser_nickname( userProfile.getNickname() );
                    GlobalApplication.setUser_image( userProfile.getProfileImagePath() );
                    GlobalApplication.setUser_thumbnail( userProfile.getThumbnailImagePath() );

                    //[Preference]JOIN에 1을 할당(1은 가입을 의미)
                    //JOIN은 카카오톡 가입을 의미
                    editor.putInt("JOIN", 1);
                    editor.commit();

                    //[Preference]에 할당(최초로 입력)
                    editor.putString("user_id", GlobalApplication.getUser_id());
                    editor.putString("user_nickname", GlobalApplication.getUser_nickname() );
                    editor.putString("user_image", GlobalApplication.getUser_image() );
                    editor.putString("user_thumbnail", GlobalApplication.getUser_thumbnail() );
                    editor.commit();

                    Log.d("URUR", "IntroActivity]"
                            + GlobalApplication.getUser_id() + ", "
                            + GlobalApplication.getUser_nickname() + ", "
                            + GlobalApplication.getUser_image() + ", "
                            + GlobalApplication.getUser_thumbnail() + ", " );

                    //회원정보 업로드 페이지로 이동
                    Intent iT = new Intent(getApplicationContext(), JoinUploadUserInfoActivity.class);
                    startActivity(iT);
                    finish(); //액티비티 종료

                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때

        }

    }









    /**
     * 클래스로 만드려고도 해봤지만 반환값이 여러가지라서 그냥 여기있는게 편리할 것 같다
     */

    //날씨에 대한 언급을 해줌
    public String makeQuestion( String weather, double temp ){

        String wC=""; //weatherComment
        String fC =""; //foodComment
        int iconPath = R.drawable.sun;


        //특이한 날씨면 거기에 대한 언급을 해준다
        if(weather.equals("rain")){
            wC = "오늘은 비가 추적추적 내리네요\n";
            iconPath = R.drawable.weather_rain;

        }
        else if(weather.equals("shower rain")){
            wC = "부슬비가 내리네요\n";
            iconPath = R.drawable.weather_rain;

        }
        else if(weather.equals("snow")){
            wC = "눈이 내려요!\n";
            iconPath = R.drawable.snow;

        }
        else if(weather.equals("thunderstorm")){
            wC = "천둥번개가 치는 무서운 날..\n";
            iconPath = R.drawable.storm;
        }
        else if(temp > 30){
            wC = "오늘은 정말 덥네요\n";
        }
        else if(temp < 0){
            wC = "너무 추워요 ~*.*~\n";
        }


        //일반적인 날씨
        if(wC.equals("") ){
            if(whatTime()==1){ fC = "아침은 드셨나요?"; }
            else if(whatTime()==2){ fC = "이른 점심.. 아점.. 브런치?!"; }
            else if(whatTime()==3){ fC = "점심메뉴 선정을 도와드릴께요"; }
            else if(whatTime()==4){ fC = "살짝 출출한데요?"; }
            else if(whatTime()==5){ fC = "저녁메뉴 선정을 도와드리겠습니다!"; }
            else if(whatTime()==6){
                fC = "아름다운 밤이예요 *^^*";
                iconPath = R.drawable.moon;}
        }

        //특이한 날씨
        else{
            if(whatTime()==1){ fC = "아침은 드셨나요?"; }
            else if(whatTime()==2){ fC = "이른 점심.. 아점.. 브런치?!"; }
            else if(whatTime()==3){ fC = "점심메뉴 선정을 도와드릴께요"; }
            else if(whatTime()==4){ fC = "살짝 출출한데요?"; }
            else if(whatTime()==5){ fC = "저녁메뉴 선정을 도와드리겠습니다!"; }
            else if(whatTime()==6){ fC = "아름다운 밤이예요 *^^*"; }
        }

        Log.d("TTMM", " " + wC);

        Glide.with(this)
                .load( iconPath )
                .into( questionIv );
        questionIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //특별한 날씨이벤트가 없으면 null값
        return wC + fC;
    }

    //현재날씨에 맞는 아이콘을 반환
    public int weatherIcon(String weather){
        return 0;
    }

    //현재시간의 속성을 반환
    public int whatTime(){

        //해당 시간의 속성
        int hS=1; //hourState

        //현재의 시간 설정
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("HH");
        String timeStr = dayTime.format(new Date(time));

        int nowHour = Integer.parseInt(timeStr);

        if(6<= nowHour && nowHour <10){
            hS = 1;
        }
        else if(10<= nowHour && nowHour <12){
            hS = 2;
        }
        else if(12<= nowHour && nowHour <14){
            hS = 3;
        }
        else if(14<= nowHour && nowHour <17){
            hS = 4;
        }
        else if(17<= nowHour && nowHour <20){
            hS = 5;
        }
        else if((20<= nowHour && nowHour <24) || 0 <= nowHour && nowHour <6 ){
            hS = 6;
        }

        return hS;

    }

}
