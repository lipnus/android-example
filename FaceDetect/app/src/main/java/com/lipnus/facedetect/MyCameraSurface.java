package com.lipnus.facedetect;

/*
 * SurfaceView 클래스. 카메라 미리보기를 위한 클래스.
 *
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    public Camera mCamera;
    public SurfaceHolder mHolder;
    public PreviewData myData; // 이미지 저장할곳.

    public MyCameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        myData = MainActivity.myData; // 이미지 저장할곳.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback); // 여기서 setPreviewCallback 정의함.
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
            } finally {
                mCamera = null;
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,   int height) {
        Camera.Parameters params = mCamera.getParameters();
        // 여기서 setFrameRate 니.. setPictureFormat 이니 이딴거 아무 영향을 주지 않는것 같다.
        // params.getSupportedPreviewSizes(); 를 이용해서 가능한 사이즈를 계산하는 방법도 있다.
        // 여기서는 고정폭 화면을 쓰겠다.
        params.setPreviewSize(MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT);
        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    // 이것이 PreviewCallback 의 정체다. 프리뷰가 갱신될때마다 뷸려와서 data를 던저준다
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] yuvdata, Camera camera) {
            // 여기 왜 yuvdata 라고 적었냐면.. 여기 넘어오는 데이터는 YUV420 이라는 포멧의 data 이다.
            // 일반적인 JPEG나 Bitmap 형식이 아니라서 그냥 BitmapFactory 나 다른 이미지 클래스를 사용해서 처리할 수 없다.
            // 자세한 내용은 PreviewData.java 를 까보시라.
            if(yuvdata != null && myData != null){
                myData.setbitmap(yuvdata);
            }
        }
    };

}