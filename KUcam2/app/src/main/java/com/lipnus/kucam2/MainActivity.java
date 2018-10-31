package com.lipnus.kucam2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView iV1;
    ImageView iV2;
    ImageView iV3;

    Button btn;

    int takeNumber = 0;

    CameraSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iV1 = (ImageView) findViewById(R.id.imageView1);
        iV2 = (ImageView) findViewById(R.id.imageView2);
        iV3 = (ImageView) findViewById(R.id.imageView3);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        view = new CameraSurfaceView(this);
        container.addView(view);


        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
