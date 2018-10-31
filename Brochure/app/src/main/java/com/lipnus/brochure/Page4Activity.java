package com.lipnus.brochure;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Page4Activity extends FragmentActivity {

    //카메라
    //=========================================================================================================
    String TAG = "CAMERA";
    private Context mContext = this; //이 방법 신박하네...

    private Camera mCamera;
    private CameraPreview mPreview;


    FrameLayout preview;

    //음악플레이어
    MediaPlayer mp = new MediaPlayer();

    int touchCount=0;

    //=========================================================================================================

    //객체들
    Button btn;
    TextView tV;
    ImageView mf;
    LinearLayout previewL;

    LinearLayout whiteL;
    ImageView iamSunpil;
    ImageView miniProfile;
    Button endbtn;

    //애니매이션
    Animation out;
    Animation fadeout;
    Animation fadein;
    Animation comein;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //객체들
        btn = (Button) findViewById(R.id.faceCheckBtn);
        tV = (TextView) findViewById(R.id.informationTextView);
        mf = (ImageView) findViewById(R.id.myfavorite);
        previewL = (LinearLayout)findViewById(R.id.previewLinear);

        whiteL = (LinearLayout) findViewById(R.id.whiteLinear);
        iamSunpil = (ImageView) findViewById(R.id.iamsunpil);
        miniProfile = (ImageView) findViewById(R.id.miniprofile);
        endbtn = (Button) findViewById(R.id.endbtn);


        //애니매이션
        out = AnimationUtils.loadAnimation(this, R.anim.out);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        comein = AnimationUtils.loadAnimation(this, R.anim.comein);

        MovingListener mainMovement = new MovingListener();
        out.setAnimationListener(mainMovement);



        // 카메라 인스턴스 생성
        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance();

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다
            mPreview = new CameraPreview(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);



        }
        else{
            Toast.makeText(mContext, "no camera on this device!", Toast.LENGTH_SHORT).show();
        }

    }


    public void onClick(View V){

        switch(touchCount){
            case 0:
                mp = MediaPlayer.create(this, R.raw.beer);
                mp.start();

                mCamera.stopPreview();
                btn.setText("■□□");
                preview.setVisibility(View.VISIBLE);
                Toast.makeText(this, "YOU", Toast.LENGTH_LONG).show();
                tV.setText("- 저는 여러분이 좋아하는 것에 항상 관심을 기울입니다 -");

                touchCount++;
                break;
            case 1:
                mCamera.startPreview();
                btn.setText("■■□");
                Toast.makeText(this, "얼음 땡", Toast.LENGTH_LONG).show();
                tV.setText("- 마음에 다가가는 서비스를 만드는게 나의 목표! -");
                touchCount++;
                break;
            case 2:
                btn.setText("■■■");
                Toast.makeText(this, "♬ 2_BEER (KISUM)", Toast.LENGTH_LONG).show();
                newSetting();
                touchCount++;
                break;
            case 3:
                mp.release();
                Toast.makeText(this, "GOOD BYE", Toast.LENGTH_LONG).show();

                finish();
                return;
        }
    }

    public void newSetting(){
        mCamera.stopPreview();
        previewL.setVisibility(View.GONE);

        tV.startAnimation(out);
        btn.startAnimation(out);
        mf.startAnimation(out);



    }

    public void onClick_test(View v){

        //촬영
        mCamera.takePicture(null, null, mPicture);

        //정지
        mCamera.stopPreview();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void onClick_end(View v){

        mp.release();
        Toast.makeText(this, "GOOD BYE", Toast.LENGTH_LONG).show();

        finish();
        return;
    }


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



    //카메라 인스턴스 호출
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(1); //0은 전면, 1은 셀카
        }
        catch (Exception e){
            // 사용중이거나 사용 불가능 한 경우
        }
        return c;
    }


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


    /** 이미지를 저장할 파일 객체를 생성합니다 */
    private static File getOutputMediaFile(){
        // SD카드가 마운트 되어있는지 먼저 확인해야합니다
        // Environment.getExternalStorageState() 로 마운트 상태 확인 가능합니다

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
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

        return mediaFile;
    }
    @Override
    public void onPause(){
        super.onPause();
        // 보통 안쓰는 객체는 onDestroy에서 해제 되지만 카메라는 확실히 제거해주는게 안전하다.

    }


    /**
     애니매이션의 움직임을 감지하는 리스너이다.
     */
    private class MovingListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            endbtn.bringToFront();
            endbtn.invalidate();

            whiteL.setVisibility(View.VISIBLE);
            iamSunpil.setVisibility(View.VISIBLE);
            miniProfile.setVisibility(View.VISIBLE);

            whiteL.startAnimation(fadein);
            iamSunpil.startAnimation(comein);
            miniProfile.startAnimation(comein);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
