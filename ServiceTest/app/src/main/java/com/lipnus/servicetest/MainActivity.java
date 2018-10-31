package com.lipnus.servicetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //윈도우에 관한 변수
    WindowManager   mWindowManager = null;
    View            mWindowLayout = null;
    WindowManager.LayoutParams windowParams = null;


    //권한
    final int PERMISSIONS_REQ_NUM = 5151;


    //카메라
    String TAG = "CAMERA";
    private Context mContext = this; //이 방법 신박하네...
    private Camera mCamera;
    private CameraPreview mPreview;



    TextView tV;
    TextView windowtV;


    //터치이벤트
    private GestureDetector mGestures = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.tV);
        windowtV = (TextView) findViewById(R.id.windowTv);

        mContext = this;

        //메인엑티비티에 있는 카메라프리뷰

        mContext = this;

        // 카메라 인스턴스 생성
        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance();

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            Button captureButton = (Button) findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // JPEG 콜백 메소드로 이미지를 가져옵니다
                    mCamera.takePicture(null, null, mPicture);
                }
            });
        }
        else{
            Toast.makeText(mContext, "no camera on this device!", Toast.LENGTH_SHORT).show();
        }



    }

    public void touchMoveEvent(){
        // 텍스트뷰를 가지고 있는 레이아웃
        View rootLayout = findViewById(R.id.windowLinearLayout);



        // 제스처로 인식하면 복잡한 이벤트 처리를 좀 더 간단하게 할 수 있습니다.
        mGestures = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    // fling 이벤트가 발생할 때 처리합니다.
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        tV.append("\nonFling \n\tvelocityX = " + velocityX + "\n\tvelocityY=" + velocityY);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                    // scroll 이벤트가 발생할 때 처리합니다.
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        tV.append("\nonScroll \n\tdistanceX = " + distanceX + "\n\tdistanceY = " + distanceY);

                        return super.onScroll(e1, e2, distanceX, distanceY);
                    }
                });


    }


    /**
     * 터치 이벤트를 제스처로 인식할 수 있도록 합니다.
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestures != null) {
            return mGestures.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
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



    //서비스 샐행
    public void onClick(View v){
        Intent iT = new Intent(this, TestService.class);
        startService(iT);
    }


    //윈도우 띄우기
    public void onClick2(View v){

        // 1. 레이아웃을 생성하기 위한 LayoutInflater 서비스 객체와
        //    윈도우를 추가하기 위한 윈도우 매니저 객체를 얻어온다.
        // ====================================================================
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // ====================================================================



        // 2. LayoutInflater 서비스를 이용하여 레이아웃을 생성한다.
        // ====================================================================
        mWindowLayout = inflate.inflate( R.layout.window_layout, null);

        // 레이아웃에 있는 버튼 객체를 얻어온다.
        Button closeWindwBtn = (Button) mWindowLayout.findViewById(
                R.id.close_window_btn );

        // 버튼을 클릭했을 때 윈도우를 종료하도록 한다.
        closeWindwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(mWindowLayout);
            }
        });
        // ====================================================================





        //윈도우의 카메라 프리뷰
        // ====================================================================

        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance();

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout camFrameLayout = (FrameLayout) mWindowLayout.findViewById(R.id.cameraFrameLayout);
            camFrameLayout.addView(mPreview);

        }
        else{
            Toast.makeText(mContext, "no camera on this device!", Toast.LENGTH_SHORT).show();
        }

        // ====================================================================



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
        windowParams.width = 600;
        windowParams.height = 800;

        //새로 뜬 창을 제외한 창들도, 터치 이벤트를 받을 수 있도록 한다.
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        //포커스를 받지않음. 그래야 다른 곳에서 글자입력가능
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // ====================================================================


        //윈도우의 위치를 이동시키는 부분
        // ====================================================================
        Button moveWindowBtn = (Button) mWindowLayout.findViewById(R.id.move_btn);

        moveWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tV.append("이동");
                windowParams.x += 10;
                windowParams.y += 10;

                //mWindowLayout.setX(70); 뷰 내에서 창의 위치 이동


                mWindowManager.updateViewLayout(mWindowLayout, windowParams);

                //mWindowManager.removeView(mWindowLayout);
                //mWindowManager.addView(mWindowLayout, windowParams);

            }
        } );
        // ====================================================================


        // 4. 윈도우매니저를 통해 윈도우를 추가한다.
        // ====================================================================
        mWindowManager.addView(mWindowLayout, windowParams);
        // ====================================================================

        touchMoveEvent();
    }


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
            tV.append("사용가능한 카메라개수 : " + Camera.getNumberOfCameras());
            Log.i("SSS", "Number of available camera : " + Camera.getNumberOfCameras());
            return true;
        } else {
            // 카메라가 전혀 없는 경우
            tV.append("카메라없음");
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



    // 윈도우 띄우는거 권한받기
    public void onClick3(View v){

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSIONS_REQ_NUM);

    }

    // 윈도우 띄우는거 권한 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PERMISSIONS_REQ_NUM:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        //권한 승인이 이루어 지지 않음
                    }
                }
                break;
        }
    }


    //카메라 권한받기
    public void onClick_Cam(View v){
        checkDangerousPermissions();
    }

    //앱의 카메라 권한을 얻어오는 메소드
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

    public void onClick_CamCount(View v){
        checkCameraHardware(this);
    }
}
