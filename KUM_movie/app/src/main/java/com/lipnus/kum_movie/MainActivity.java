package com.lipnus.kum_movie;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
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

        Uri raw_uri = Uri.parse("android.resource://com.lipnus.kum_movie/" + R.raw.cham);

        videoView.setVideoURI(raw_uri); //영상의 위치
        videoView.requestFocus();
        videoView.seekTo(0);
        videoView.start();

        //동영상이 끝났을때 호출
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
    }


}
