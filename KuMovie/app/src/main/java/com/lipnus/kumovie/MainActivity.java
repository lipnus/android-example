package com.lipnus.kumovie;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    private static String url = "http://sites.google.com/site/ubiaccessmobile/sample_video.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //비디오뷰
        videoView = (VideoView) findViewById(R.id.videoView);
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(url)); //영상의 위치
        videoView.requestFocus();


        //버튼
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(0);
                videoView.start();
            }
        });

        //동영상이 끝났을때 호출
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "재생종료", Toast.LENGTH_LONG).show();
            }
        });

    }
}
