package com.lipnus.windowmovetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class MainActivity extends FragmentActivity {

    //윈도우
    //=========================================================================================================
    WindowManager   mWindowManager = null;
    View            mWindowLayout = null;
    WindowManager.LayoutParams windowParams = null;

    //윈도우 터치 시 시작지점을 저장
    float startX, startY; //터치위치
    float prevX, prevY; //뷰위치

    // 핀치시 두좌표간의 거리 저장
    float oldDist = 1f;
    float newDist = 1f;

    // 드래그 모드인지 핀치줌 모드인지 구분
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;

    //윈도우의 시작크기
    static final int START_WIDTH = 600;
    static final int START_HEIGHT = 750;

    int mode = NONE;

    //거울의 테두리부분
    FrameLayout edgeFrameLayout;


    //윈도우 내부의 버튼들
    Button changeBtn;
    Button closeWindwBtn;
    Button callMenuBtn;
    Button camBtn;
    Button powerBtn;
    Button zoomBtn;

    //윈도우에 있는 텍스트뷰
    TextView tV;

    boolean windowOn = false; //윈도우가 중복실행되지 않도록 함
    boolean callWindow = false; //애니매이션리스너에서 다른 애니매이션의 종료때 같이 실행되지 않도록, 버튼을 눌렀을때만 실행되도록 할때필요
    //=========================================================================================================




    //카메라
    //=========================================================================================================
    String TAG = "CAMERA";
    private Context mContext = this; //이 방법 신박하네...

    private Camera mCamera;
    private CameraPreview mPreview;

    FrameLayout camFrameLayout; //윈도우 안에 있는 카메라 프리뷰 띄워지는 레이아웃

    int whichCam = 1; //1이면 셀카, 0이면 외부카메라

    String savedImagePath; //가장 최근에 찍은 사진이 저장된 경로

    Stack<String> picturePathStack; //촬영된 사진들의 경로를 스텍이 저장해놨다가 돌려놓음. (위에처럼 하나씩으로 하니까 시간차에 의해 누락되는게 생겨서)

    boolean needDirectionEdit = false; //돌아간 셀카사진의 방향을 전환하는 작업 신호를 스레드에 보냄

    boolean isPreviewOn = true; //프리뷰 창이 켜졌는지 꺼졌는지

    boolean zoomInOK = false; //줌인이 되었을때 true


    //=========================================================================================================




    //애니매이션
    //=========================================================================================================
    Animation move_toright;
    Animation move_toleft;
    Animation move_up;
    Animation move_down;
    Animation move_rotate;

    Animation logo_centerpic;
    Animation move_miger;
    Animation move_redbox;
    Animation move_triangle;
    Animation move_centerpic;
    Animation move_grayframe;

    Animation move_text;
    boolean firstTextEnd = false; //첫번째 텍스트 종료
    boolean secondTextEnd = false; //두번째 텍스트 종료

    LinearLayout SlideLayout;

    boolean isSlideOn = false; //슬라이드가 나와있는지 아닌지
    //=========================================================================================================





    //AyncTask
    //=========================================================================================================
        int timeValue=0;
    //=========================================================================================================



    //메인
    //=========================================================================================================
    //메인의 이미지뷰
    ImageView tempiV;

    //메인의 비트맵
    Bitmap bm;

    //기본레이아웃(화면크기 구하는데 씀)
    LinearLayout mainLinear;

    ImageView logopic;
    ImageView redbox;
    ImageView grayframe;
    ImageView miger;
    ImageView blacktriangle;


    //메인에 있는 텍스트뷰
    TextView mainTv;
    //=========================================================================================================



    //Preperence(앱 최초실행시에만 권한을 띄우기 위해 사용)
    //=========================================================================================================
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    int howMany; //앱이 몇번 실행되는지 확인
    //=========================================================================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Preperence
        //--------------------------------------------------------------------------------------------------------
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        //불러오기
        howMany = setting.getInt("HowMany", 0); //없다면 기본값0반환


        //최초실행시에만 권한을 묻는다.
        if(howMany==0){
            //윈도우 그리기 권한 획득
            getWindowPermission();

            //카메라 권한획득
            checkDangerousPermissions();
        }

        Log.d("WindowTest", "실행횟수: " + howMany);

        //저장
        editor.putInt("HowMany", (howMany+1));
        editor.commit();

        //--------------------------------------------------------------------------------------------------------







        //애니매이션
        move_toright = AnimationUtils.loadAnimation(this, R.anim.move_toright);
        move_toleft = AnimationUtils.loadAnimation(this, R.anim.move_toleft);
        move_up = AnimationUtils.loadAnimation(this, R.anim.move_up);
        move_down = AnimationUtils.loadAnimation(this, R.anim.move_down);
        move_rotate =AnimationUtils.loadAnimation(this, R.anim.move_rotate);

        logo_centerpic = AnimationUtils.loadAnimation(this, R.anim.logo_centerpic);
        move_miger= AnimationUtils.loadAnimation(this, R.anim.move_miger);
        move_redbox= AnimationUtils.loadAnimation(this, R.anim.move_redbox);
        move_triangle = AnimationUtils.loadAnimation(this, R.anim.move_triangle);
        move_centerpic= AnimationUtils.loadAnimation(this, R.anim.move_centerpic);
        move_grayframe = AnimationUtils.loadAnimation(this, R.anim.move_grayframe);

        move_text = AnimationUtils.loadAnimation(this, R.anim.move_text);

        //애니매이션중에서 리스너가 필요한 애들은 연결해줌
        SlideMovingListener mainMovement = new SlideMovingListener();
        move_grayframe.setAnimationListener(mainMovement);
        move_text.setAnimationListener(mainMovement);




        //상태확인을 위한 텍스트뷰뷰
        tV = (TextView)findViewById(R.id.WindowTtextView);
        mainTv = (TextView)findViewById(R.id.MainTextView);


        //셀카로 찍은 이미지 방향ㅇ르 조정히기 위해 일시적으로 이미지를 담아두는 이미지뷰
        tempiV = (ImageView)findViewById(R.id.testImageView);


        //AyncTask의 객체를 하나 만든 뒤 실행
        BackgroundAyncTask task = new BackgroundAyncTask();
        task.execute(100);

        //촬영된 사진들의 경로를 저장할 스택생성
        picturePathStack = new Stack<String>();

        //메인의 기본레이아웃(이걸 이용해 화면의 가로세로 크기를 구함)
        mainLinear = (LinearLayout)findViewById(R.id.mainLinear);

        //메인의 로고이미지
        logopic = (ImageView)findViewById(R.id.logopic);
        redbox= (ImageView)findViewById(R.id.redbox);
        grayframe= (ImageView)findViewById(R.id.grayframe);
        miger= (ImageView)findViewById(R.id.miger);
        blacktriangle= (ImageView)findViewById(R.id.balcktriangle);

        //메인시작과 동시에 움직(if문을 쓰는 이유는 한번만)
        if(windowOn==false){
            logopic.startAnimation(logo_centerpic);
            mainTv.startAnimation(move_text);

            firstTextEnd=true; //애니매이션리스너에서 이 조건이 충족되면 애니메이션이 끝나고 두번째 멘트 송출

        }




    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("WindowTest", "액티비티에서 터치");
        return super.onTouchEvent(event);
    }

    public void onClick(View v){
        //로고이미지 애니매이션
        miger.startAnimation(move_miger);
        redbox.startAnimation(move_redbox);
        blacktriangle.startAnimation(move_triangle);
        logopic.startAnimation(move_centerpic);
        grayframe.startAnimation(move_grayframe);

        callWindow = true;
        //윈도우 띄우는건 위의 에니매이션이 끝나고 해야되니까, 애니매이션 리스너에서 띄움
    }



    public void onClick_howtouse(View v){
        Intent intentSubActivity =
                new Intent(this, HowtoActicity.class);
        startActivity(intentSubActivity);
    }


    public void onClick_information(View v){
        Intent intentSubActivity =
                new Intent(this, InfoActicity.class);
        startActivity(intentSubActivity);

    }





    //윈도우 띄우기(윈윈)
    public void makeWindow(){

        // 1. 레이아웃을 생성하기 위한 LayoutInflater 서비스 객체와
        //    윈도우를 추가하기 위한 윈도우 매니저 객체를 얻어온다.
        // ====================================================================
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // ====================================================================



        // 2. LayoutInflater 서비스를 이용하여 레이아웃을 생성한다.
        // ====================================================================
        mWindowLayout = inflater.inflate( R.layout.window_layout, null);


       //윈도우 내부의 객체들
        //-------------------------------------------------------------------------------------------------------------

        // 레이아웃에 있는 버튼 객체를 얻어온다.
        closeWindwBtn = (Button) mWindowLayout.findViewById(R.id.close_window_btn );
        callMenuBtn = (Button) mWindowLayout.findViewById(R.id.call_menu );
        changeBtn = (Button) mWindowLayout.findViewById(R.id.changeBtn);
        camBtn = (Button) mWindowLayout.findViewById(R.id.camBtn);
        powerBtn = (Button) mWindowLayout.findViewById(R.id.poweronoff);
        zoomBtn = (Button) mWindowLayout.findViewById(R.id.zoomin);


        //카메라 프래임레이아웃이 들어갈 액자틀
        edgeFrameLayout = (FrameLayout) mWindowLayout.findViewById(R.id.EdgeFrameLayout);

        //카메라프리뷰가 들어가는곳
        camFrameLayout = (FrameLayout) mWindowLayout.findViewById(R.id.cameraFrameLayout);

        //슬라이드 메뉴
        SlideLayout = (LinearLayout) mWindowLayout.findViewById(R.id.SlideLayout);

        //-------------------------------------------------------------------------------------------------------------





        // 메뉴버튼
        callMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSlideOn==false){
                    //열기의 시작
                    SlideLayout.setVisibility(View.VISIBLE);
                    SlideLayout.startAnimation(move_up);


                    camBtn.startAnimation(move_toright);
                    changeBtn.startAnimation(move_toright);
                    powerBtn.startAnimation(move_toright);

                    isSlideOn = true;

                }else{
                    //닫기의 시작
                    SlideLayout.startAnimation(move_down);
                    SlideLayout.setVisibility(View.INVISIBLE);

                    camBtn.startAnimation(move_toleft);
                    changeBtn.startAnimation(move_toleft);
                    powerBtn.startAnimation(move_toleft);

                    isSlideOn = false;
                }
            }
        });

        //카메라정지
        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPreviewOn == true){
                    camFrameLayout.removeView(mPreview);
                    zoomBtn.setBackgroundResource(R.drawable.zoomin); //단추모양 +로 바꿔줌
                    isPreviewOn = false;
                }
                else{
                    if(whichCam==0){ mCamera = getCameraInstance(0); }
                    else if(whichCam==1){ mCamera = getCameraInstance(1); }

                    mPreview = new CameraPreview(mContext, mCamera);
                    camFrameLayout.addView(mPreview);
                    isPreviewOn = true;
                }
            }

        });

        // 전후방카메라 교체버튼
        changeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               zoomBtn.setBackgroundResource(R.drawable.zoomin);

               Log.d("WindowTest", "전후방 교체");
               isPreviewOn = true; //전원버튼이 아니라 이걸 눌러도 켜진걸로 침

               if(whichCam==1){
                   camFrameLayout.removeView(mPreview);
                   mCamera = getCameraInstance(0); whichCam=0;
                   mPreview = new CameraPreview(mContext, mCamera);
                   camFrameLayout.addView(mPreview);
               }
               else{
                   camFrameLayout.removeView(mPreview);
                   mCamera = getCameraInstance(1); whichCam=1;
                   mPreview = new CameraPreview(mContext, mCamera);
                   camFrameLayout.addView(mPreview);
               }
           }
        });


        //촬영버튼
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPreviewOn==true){
                    mCamera.takePicture(null, null, mPicture);

                    //촬영시 찰칵 문구가 상단에 나오도록 한다(엄지때문에 잘 안보이니까)
                    Toast toast = Toast.makeText(mContext, "찰칵!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0,400);
                    toast.show();

                    if(whichCam==1){needDirectionEdit = true;}
                    //원래 여기서 바로 setPictureDirection(savedImagePath); 호출하면 딱인데 시발 안됨. 한템포 쉬고 다른곳에서 눌러야 됨
                    //그래서 needDirectionEdit걸어놓고, 스레드에서 호출하는걸로 함

                }else{
                    Toast.makeText(mContext, "촬영하시려면 거울을 켜주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 윈도우 종료버튼
        closeWindwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSlideOn=false;
                windowOn=false;
                mWindowManager.removeView(mWindowLayout);
            }
        });


        // 줌인, 줌아웃
        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camZoomInOut();
            }
        });

        // ====================================================================


        //★윈도우의 카메라 프리뷰
        // =========================================================================================
        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance(1); //셀카1,전방0

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다
            mPreview = new CameraPreview(this, mCamera);
            camFrameLayout = (FrameLayout) mWindowLayout.findViewById(R.id.cameraFrameLayout);
            camFrameLayout.addView(mPreview);

        }
        else{
            Toast.makeText(mContext, "no camera on this device!", Toast.LENGTH_SHORT).show();
        }


        // =========================================================================================



        // 3. LayoutParams 객체를 생성하여 윈도우 정보를 설정한다.
        // ====================================================================
        windowParams = new WindowManager.LayoutParams();

        // 1) 윈도우 명을 설정한다.
        windowParams.setTitle("테스트윈도우");

        // 2) 윈도우 타입을 설정한다.
        windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        // 3) 화면에 배치될 윈도우의 위치와 크기를 설정한다.
        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowParams.x = 100;
        windowParams.y = 0;
        windowParams.width = START_WIDTH;
        windowParams.height = START_HEIGHT;

        //새로 뜬 창을 제외한 창들도, 터치 이벤트를 받을 수 있도록 한다.
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        //포커스를 받지않음. 그래야 다른 곳에서 글자입력가능
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        // ====================================================================


        // 4. 윈도우매니저를 통해 윈도우를 추가한다.
        // ====================================================================
        mWindowManager.addView(mWindowLayout, windowParams);
        // ====================================================================

        //터치 리스너 등록
        mWindowLayout.setOnTouchListener(mViewTouchListener);
    }





    //윈도우 터치 리스너 (터터치치)
    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {

        @Override public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    Log.d("WindowTest", "터치다운");

                    startX = event.getRawX();
                    startY = event.getRawY();

                    //뷰의 시작 점
                    prevX = windowParams.x;
                    prevY = windowParams.y;

                    //기본 시작은 DRAG모드
                    mode = DRAG;
                    break;


                case MotionEvent.ACTION_POINTER_DOWN:
                    //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치 줌으로 판별)
                    mode = ZOOM;
                    newDist = spacing(event);
                    oldDist = spacing(event);
                    Log.d("WindowTest", "newDist=" + newDist);
                    Log.d("WindowTest", "oldDist=" + oldDist);
                    Log.d("WindowTest", "mode=ZOOM");
                    break;


                case MotionEvent.ACTION_MOVE:{

                    if(mode == DRAG) {   // 드래그 중이면, 이미지의 X,Y값을 변환시키면서 위치 이동.
                        //Log.d("WindowTest", "터치무브");
                        int x = (int)(event.getRawX()-startX);    //이동한 거리
                        int y = (int)(event.getRawY()-startY);    //이동한 거리


                        //터치해서 이동한 만큼 이동 시킨다
                        windowParams.x = (int)prevX + x;
                        windowParams.y = (int)prevY + y;

                        mWindowManager.updateViewLayout(mWindowLayout, windowParams);
                        }


                    else if (mode == ZOOM) {    // 핀치줌 중이면, 이미지의 거리를 계산해서 확대를 한다.
                        newDist = spacing(event);

                        if (newDist - oldDist > 20) {  // zoom in
                            Log.d("WindowTest", "확대!");

                            //화면크기가 일정이상이면 메뉴4개가 다 보이도록 함
                            if(windowParams.width > START_WIDTH){
                                powerBtn.setVisibility(View.VISIBLE);
                                zoomBtn.setVisibility(View.VISIBLE);
                            }

                            //화면크기보다 창이 더 커지지 않도록 함
                            if(windowParams.width < mainLinear.getWidth()){
                                //기존 크기와 새로 된 크기의 비율을 구한 뒤 곱하는 방식
                                float scale= (float)Math.sqrt(((newDist - oldDist) * (newDist - oldDist))
                                        / (windowParams.height * windowParams.height + windowParams.width * windowParams.width));

                                //이게 있어야 확대가 중심부분에서 이루어진다
                                windowParams.x =windowParams.x-(int)((float)windowParams.width*scale/2);
                                windowParams.y =windowParams.y-(int)((float)windowParams.height*scale/2);

                                //크기를 키운다
                                windowParams.height= (int)( (float)windowParams.height*(1+scale) );
                                windowParams.width=  (int)( (float)windowParams.width*(1+scale) );
                                mWindowManager.updateViewLayout(mWindowLayout, windowParams);
                            }

                            oldDist = newDist;
                        }

                        else if(oldDist - newDist > 20) {  // zoom out
                            Log.d("WindowTest", "축소!");

                            //화면크기가 메뉴4개들어갈 크기보다 작아지면 메뉴를 2개로 줄임
                            if(windowParams.width < START_WIDTH){
                                powerBtn.setVisibility(View.GONE);
                                zoomBtn.setVisibility(View.GONE);
                            }

                            //기존 크기와 새로 된 크기의 비율을 구한 뒤 곱하는 방식
                            float scale= (float)Math.sqrt(((newDist - oldDist) * (newDist - oldDist))
                                    / (windowParams.height * windowParams.height + windowParams.width * windowParams.width));

                            //음수를 취한다
                            scale=0-scale;

                            //이게 있어야 확대가 중심부분에서 이루어진다
                            windowParams.x =windowParams.x-(int)((float)windowParams.width*scale/2);
                            windowParams.y =windowParams.y-(int)((float)windowParams.height*scale/2);

                            //크기를 키운다
                            windowParams.height= (int)( (float)windowParams.height*(1+scale) );
                            windowParams.width=  (int)( (float)windowParams.width*(1+scale) );
                            mWindowManager.updateViewLayout(mWindowLayout, windowParams);

                            oldDist = newDist;
                        }

                    }

                    break;
                }


                case MotionEvent.ACTION_UP:
                    Log.d("WindowTest", "손가락 다땠다");

                    //액자를 윈도우의 크기에 맞춘다.
                    edgeFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(windowParams.width,
                            (int)(windowParams.height)));

                    //카메라프리뷰 창의 크기를 윈도우의 크기에 맞춘다.
                    camFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(windowParams.width - 20,
                            (int)(windowParams.height -20)));


                    mode = NONE;

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("WindowTest", "손가락 하나 땠다");
                    mode = NONE;
                    break;
            }




            return true;
        }
    };


    //더블터치 이벤트에 필요한 함수(두 포인트 사이의 거리를 구함)
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float)Math.sqrt(x * x + y * y);
    }




    //카메라 줌인 줌아웃
    public void camZoomInOut(){

        //화면이 꺼져있으면 실행하지 않고 종료
        if(isPreviewOn==false){ return; }

        int p;
        Camera.Parameters parameters = mCamera.getParameters();

        if(zoomInOK==false){
            p = parameters.getMaxZoom() - 10;
            zoomInOK=true;
            zoomBtn.setBackgroundResource(R.drawable.zoomout); //단추모양 -로 바꿔줌
        }else{
            p=0;
            zoomInOK=false;
            zoomBtn.setBackgroundResource(R.drawable.zoomin); //단추모양 +로 바꿔줌
        }
        parameters.set("zoom", p);
        mCamera.setParameters(parameters);
    }




    //카메라 몇대있는지 확인
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // 카메라가 최소한 한개 있는 경우 처리
            Log.i("SSS", "Number of available camera : " + Camera.getNumberOfCameras());
            return true;
        } else {
            // 카메라가 전혀 없는 경우
            return false;
        }
    }


    //카메라 인스턴스 호출
    public static Camera getCameraInstance(int whichCam){
        Camera c = null;
        try {
            c = Camera.open(whichCam); //0은 전면, 1은 셀카
        }
        catch (Exception e){
            // 사용중이거나 사용 불가능 한 경우
        }
        return c;
    }


    //사진저장
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // JPEG 이미지가 byte[] 형태로 들어옵니다

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Toast.makeText(mContext, "Error saving!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                //Thread.sleep(500);
                mCamera.startPreview();

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            } /*catch (InterruptedException e) {
    e.printStackTrace();
   }*/
        }
    };


    /** 이미지를 저장할 파일 객체를 생성합니다 */ //원래 priate Static File 이었는데 Static 지웠음.
    private File getOutputMediaFile(){

        // SD카드가 마운트 되어있는지 먼저 확인해야합니다
        // Environment.getExternalStorageState() 로 마운트 상태 확인 가능합니다

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MigerApp");
        // 굳이 이 경로로 하지 않아도 되지만 가장 안전한 경로이므로 추천함.

        // 없는 경로라면 따로 생성한다.
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }

        // 파일명을 적당히 생성. 여기선 시간으로 파일명 중복을 피한다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        Log.i("MyCamera", "Saved at"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));


        //저장경로
        if(whichCam==1){//if문 이렇게 안해놓으면 경우에 따라 최근에 찍은 전면카메라의 사진을 수정하는 불상사 발생
            //savedImagePath = mediaFile.getPath();

            //저장된 파일의 경로를 스텍에 저장(하나씩 불러와서 방향 돌려놓는다)
            picturePathStack.push(mediaFile.getPath());

            //setPictureDirection호출은 단추 누르는 곳에서 함
        }

        //후방카메라일때는 수정없이 바로 갱신
        if(whichCam==0){
            MediaScanner scanner = new MediaScanner(this);
            scanner.startScan(mediaFile.getPath());
        }

        return mediaFile;
    }



    public void setPictureDirection(String imagePath){
        //지금 이 소스는 각 단계마다 병신같이 이미지뷰를 거쳐간다. 근데 이렇게 안하면 안됨
        //딜레이 때문에 그런가.. 사이사이에 쓰레드를 이용해 텀을 주면 될 것 같기도 한데 일단 이렇게 해놓음.

        Log.d("WindowTest", "setPictureDirection() - 사진불러오기, 방향수정, 다시저장");

        try{

        //불러오기
        bm = BitmapFactory.decodeFile(imagePath);
        tempiV.setImageBitmap(bm);

        //사진방향
        BitmapDrawable a = (BitmapDrawable)((ImageView) findViewById(R.id.testImageView)).getDrawable();
        Bitmap b = a.getBitmap();
        tempiV.setImageBitmap(rotate(b, -90));

        //사진저장
        BitmapDrawable c = (BitmapDrawable)((ImageView) findViewById(R.id.testImageView)).getDrawable();
        Bitmap d = c.getBitmap();
        SaveBitmapToFileCache(d, imagePath); //바로하면 여기가 안됨


        //갤러리 갱신을 위한 부분(미디어 스캐너)
        //=======================================================
        MediaScanner scanner = new MediaScanner(this);
        scanner.startScan(imagePath);
        //=======================================================

        }catch(Exception e){Toast.makeText(getApplicationContext(), "사진 수정하는부분에서 에러", Toast.LENGTH_SHORT).show();}
    }


    //비트맵 이미지 저장
    private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        Log.d("WindowTest", "SaveBitmapToFileCache()");

        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }

        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // 비트맵 이미지 돌리기
    //bm = BitmapFactory.decodeFile(파일경로);
    //ImageView.setImageBitmap(rotate(bm, 90));
    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap = null;
                    bitmap = converted;
                    converted = null;
                }
            } catch (OutOfMemoryError ex) {
                Toast.makeText(getApplicationContext(), "메모리부족", Toast.LENGTH_LONG).show();
            }
        }
        return bitmap;
    }












    public class BackgroundAyncTask extends AsyncTask<Integer , Integer , Integer> {

        protected void onPreExecute() {
            timeValue = 0;
        }


        //계속해서 실행되는 부분
        protected Integer doInBackground(Integer ... values) {

            //캔슬되지 않는동안은 계속실행
            while (isCancelled() == false) {
                timeValue++;
                if (timeValue >= 1) {


                    publishProgress(timeValue);
                    timeValue = 0;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {}
            }
            return timeValue;
        }

        //실행 중간중간 업데이트하는부분(publicProgress에 의해 호출)
        protected void onProgressUpdate(Integer ... values) {
            //sleep한 초만큼의 딜레이로 호출됨

            //요청이 있을 시 셀카방향회전 메소드 실행
            if(whichCam==1 && needDirectionEdit==true){



                //셀카일때 그냥 저장하면 거꾸로 저장됨. 그래서 돌려준다.
                Log.d("WindowTest", "스레드에서 setPictureDirection호출");

                //스텍에 저장되어 있는 파일들을 불러와서 방향을 하나하나씩 돌려준다
                while (!picturePathStack.isEmpty()){
                    setPictureDirection(picturePathStack.pop());
                }

                needDirectionEdit = false;
            }


        }

        //aynctast가 종료되었을때
        protected void onPostExecute(Integer result) {

        }

        //aynctast가 정지되었을때
        protected void onCancelled() {

        }
    }




















    /**
     애니매이션의 움직임을 감지하는 리스너이다.
     */
    private class SlideMovingListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //두번째 멘트 송출
            if(firstTextEnd==true ){
                mainTv.setText("세상에서 누가 제일 예쁘니?");
                mainTv.startAnimation(move_text);
                firstTextEnd = false; //두번 실행되지 않도록
                secondTextEnd = true; //이걸 끔으로써 멘트는 한번만 반복됨
            }
            else if(secondTextEnd==true){
                mainTv.setVisibility(View.INVISIBLE);
            }

            //윈도우를 띄운다
            if(windowOn==false && callWindow ==true) {
                windowOn=true;
                callWindow = false; //다른 애니매이션의 End때 같이 실행되지 않도록 해줌
                makeWindow();

                //메인어플은 백그라운드로 넘김
                moveTaskToBack(true);
            }else if(callWindow ==true){
                Toast.makeText(mContext, "이미 거울이 실행중입니다.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }




























    // 윈도우 띄우는거 권한받기
    public void getWindowPermission(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 5151);
    }


    // 윈도우 띄우는거 권한 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 5151:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        //권한 승인이 이루어 지지 않음
                    }
                }
                break;
        }
    }



    //앱의 권한받기
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }


    //권한이 잘 되었는지 결과를 확인받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
