package com.lipnus.brochure;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Page3Activity extends FragmentActivity {

    private VoiceRecognition voiceRecognition;

    //레벨
    int level = 1;

    //음악플레이어
    MediaPlayer mp = new MediaPlayer();

    //객체들
    ImageView sayiV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            //이전액티비티 종료
            //==========================================================================================
            //1
            Page2Activity p2a1 = (Page2Activity)Page2Activity.page2Activity;
            p2a1.finish();

            //1
            Page2Activity_1Music p2a11 = (Page2Activity_1Music)Page2Activity_1Music.page2Activity_1Music;
            p2a11.finish();

            //==========================================================================================
        } catch (Exception e) {
            e.printStackTrace();
        }

        //객체
        sayiV = (ImageView) findViewById(R.id.sayho);
    }
    public void onClick(View v){

        startVoiceRecognition();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    public void judgeInput(String answer){
        switch (level){
            case 1:
                if(answer.equals("샤샤샤")){
                    Toast.makeText(this, "샤!샤!샤!", Toast.LENGTH_LONG).show();
                    mp.release();
                    mp = MediaPlayer.create(this, R.raw.uwa);
                    mp.start();

                    sayiV.setImageResource(R.drawable.saycg);

                    //다음단계는 참깨!
                    level = 2;
                }
                else{
                    Toast.makeText(this, answer, Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                if(answer.equals("참깨")){
                    Toast.makeText(this, "참!깨!라!면!", Toast.LENGTH_LONG).show();


                    sayiV.setImageResource(0);
                    Toast.makeText(this, "♬ 알록달록(Mad Clown feat. 주헌) ", Toast.LENGTH_LONG).show();
                    Intent iT = new Intent(this, Page3Activity_2.class);
                    startActivity(iT);


                    finish();
                    return;
                }
                else{
                    Toast.makeText(this, answer, Toast.LENGTH_LONG).show();
                }
                break;
        }


    }

    // 음성 인식 시작
    private void startVoiceRecognition() {

        voiceRecognition = new VoiceRecognition(this);

        if (voiceRecognition.recognitionAvailable()) {

            Intent intent = voiceRecognition
                    .getVoiceRecognitionIntent("Speak now");

            startActivityForResult(intent,
                    voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE);

        } else {
            Toast toast = Toast.makeText(this,
                    "Voice recognition is not available.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // 음성 인식 결과
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==
                voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE
                && resultCode == -1) {

            String result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS).get(0);
            // 이 부분에서 result 를 가지고 검색을 하거나, 명령을 실행 하면 됨


            judgeInput(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
