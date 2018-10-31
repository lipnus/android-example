package com.lipnus.kudataandcam;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ImageView iV1;
    ImageView iV2;
    ImageView iV3;

    Button btn, btn2;

    int takeNumber = 0;

    //파일로저장
    File sdcard, file;
    CameraSurfaceView view;

    //데이터베이스
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iV1 = (ImageView) findViewById(R.id.imageView1);
        iV2 = (ImageView) findViewById(R.id.imageView2);
        iV3 = (ImageView) findViewById(R.id.imageView3);

        //SD카드
        sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg"); //sd카드 폴더 안에 파일객체를 만듦


        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        view = new CameraSurfaceView(this);
        container.addView(view);

        //데이터베이스를 초기화한다
        databaseSetting();


        //사진찍기
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //사진확인(데이터베이스창 띄우기)
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iT = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(iT);
            }
        });

    }




    //데이터베이스 초기세팅
    public void databaseSetting(){

        //데이터베이스 열기
        String databaseName = "picture";
        try {
            database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            Log.d("SSSS", "데이터베이스 오픈");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("SSSS", "데이터베이스 오픈 시 에러");
        }

        //테이블생성
        String sql = "CREATE TABLE customer(name text, path text)";
        try {
            database.execSQL(sql);
            Log.d("SSSS", "테이블생성");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("SSSS", "테이블생성 시 에러");
        }


    }

    //사진을 위의 3개의 이미지박스에 넣어주는 매소드
    public void takePicture(){
        view.capture(new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 12;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);


                //사진찍으면 이미지뷰3개에 순서대로 표시
                showPreviewImage(bitmap);

                //사진찍고 프리뷰가 멈추는 경우 다시 실행시키게 함
                camera.startPreview();
            }
        });
    }

    public void showPreviewImage(Bitmap bitmap){
        switch (takeNumber) {
            case 0:
                iV1.setImageBitmap(bitmap);
                takeNumber++;
                break;
            case 1:
                iV2.setImageBitmap(bitmap);
                takeNumber++;
                break;
            case 2:
                iV3.setImageBitmap(bitmap);
                takeNumber=0;
                break;
            default:
                break;
        }
    }

    class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        SurfaceHolder holder;
        Camera camera = null;



        public CameraSurfaceView(Context context) {
            super(context);

            init(context);
        }

        public CameraSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        //터치
        public boolean onTouchEvent(MotionEvent event){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){

            }else if(action == MotionEvent.ACTION_MOVE){

            }else if(action == MotionEvent.ACTION_UP){
                takePicture();
            }
            return true;
        }

        private void init(Context context){
            holder = getHolder();
            holder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = camera.open();

            try{
                camera.setPreviewDisplay(holder);
            }catch(Exception e){}

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            //카메라컨트롤을 놓아준다
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        public boolean capture(Camera.PictureCallback callback){
            if(camera != null){
                camera.takePicture(null, null, callback);
                return true;
            }else{
                return false;
            }
        }
    }
}
