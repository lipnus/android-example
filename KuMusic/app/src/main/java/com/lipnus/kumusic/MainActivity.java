package com.lipnus.kumusic;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btn, btn2, btn3, btn4, btn5, btn6, btn7;
    MediaPlayer player;

    MediaRecorder recorder; //녹음

    File sdcard, musicfile;

    String url = "http://sites.google.com/site/ubiaccessmobile/sample_audio.amr";
    String filepath;

    //재시작할때 원래의 위치를 기억
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //SD카드
        sdcard = Environment.getExternalStorageDirectory();
        musicfile = new File(sdcard, "music.mp4"); //sd카드 폴더 안에 파일객체를 만듦

        File recordedFile = new File(sdcard, "recorded.mp4");
        filepath = recordedFile.getAbsolutePath();


        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume();
            }
        });

        btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start2();
            }
        });

        btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

    }

    public void startRecording(){

        //전에 실행되고 있던 게 있으면 제거해줌
        if(recorder != null){
            recorder.release();
            recorder = null;
        }

        recorder = new MediaRecorder();

        //녹음할때는 설정이 필요
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //어디서 들어온 사운드인지
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //포맷
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); //단말에서 기본설정된 인코더사용

        recorder.setOutputFile(filepath); //저장할 위치
        Snackbar.make(getWindow().getDecorView(), "녹음시작", Snackbar.LENGTH_LONG).show();

        try {
            recorder.prepare();
            recorder.start();
        }catch(Exception e){}
    }


    public void stopRecording(){
        if (recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;

            Snackbar.make(getWindow().getDecorView(), "녹음중지", Snackbar.LENGTH_LONG).show();
        }
    }

    public void start2(){
        player = new MediaPlayer();

        try{
            player.setDataSource(filepath);
            player.prepare();
            player.start();

            Snackbar.make(getWindow().getDecorView(), "시작됨", Snackbar.LENGTH_LONG).show();

        }catch(Exception e){}
    }

    public void stop(){
        if(player != null){
            player.stop();
            player.release();
            player = null;

            Snackbar.make(getWindow().getDecorView(), "정지됨", Snackbar.LENGTH_LONG).show();
        }
    }

    public void pause(){
        if(player != null){
            position = player.getCurrentPosition();
            player.pause();

            Snackbar.make(getWindow().getDecorView(), "일시정지됨", Snackbar.LENGTH_LONG).show();
        }
    }

    public void resume(){
        if(player != null){
            player.seekTo(position);
            player.start();

            Snackbar.make(getWindow().getDecorView(), "다시시작됨", Snackbar.LENGTH_LONG).show();
        }
    }

    public void play(){
        player = new MediaPlayer();

        try{
            player.setDataSource(url);
            player.prepare();
            player.start();

            Snackbar.make(getWindow().getDecorView(), "시작됨", Snackbar.LENGTH_LONG).show();

        }catch(Exception e){}

    }
}
