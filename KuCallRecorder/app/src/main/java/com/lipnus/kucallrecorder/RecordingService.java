package com.lipnus.kucallrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class RecordingService extends Service {

    private static final String TAG = "RecordingService";

    MediaRecorder recorder; //녹음
    File sdcard, recordedFile;
    String filepath;

    public RecordingService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onStart 호출");

        sdcard = Environment.getExternalStorageDirectory(); //SD카드
        recordedFile = new File(sdcard, "recorded.mp4");
        filepath = recordedFile.getAbsolutePath();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(intent !=  null){

            String command = intent.getStringExtra("command");

            if(command != null){
                if(command.equals("startRecording")) {
                    startRecording();
                }
                else if(command.equals("stopRecording")){
                    stopRecording();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
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
        Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_LONG).show();

        Intent iT = new Intent(getApplicationContext(), MainActivity.class);
        iT.putExtra("command", "recordingState");
        iT.putExtra("state", "start");

        //서비스는 화면이 없으므로 테스크도 만들어줘야함
        iT.addFlags(iT.FLAG_ACTIVITY_NEW_TASK|iT.FLAG_ACTIVITY_SINGLE_TOP|iT.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iT);


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

            Toast.makeText(getApplicationContext(), "녹음 끝", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy 호출");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
