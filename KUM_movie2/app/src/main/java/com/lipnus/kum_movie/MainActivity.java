package com.lipnus.kum_movie;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // surfaceView 등록
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Activity로 Video Stream 전송 등록
        surfaceHolder.addCallback(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

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
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        /**
         * 비디오가 전부 재생된 상태의 리스너
         * @param mp : 현재 제어하고 있는 MediaPlayer
         */
        @Override
        public void onCompletion(MediaPlayer mp) {
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
}
