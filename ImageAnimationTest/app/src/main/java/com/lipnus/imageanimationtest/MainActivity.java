package com.lipnus.imageanimationtest;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    ImageSwitcher switcher;
    Handler mHandler = new Handler();
    ImageThread thread;
    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 시작 버튼 이벤트 처리
        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAnimation();
            }
        });

        // 중지 버튼 이벤트 처리
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAnimation();
            }
        });


        //이미지스위처 등록하고, 안보이게 해놓음
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        switcher.setVisibility(View.INVISIBLE);

        //팩토리?? 시발 공장이야 뭐야
        switcher.setFactory(new ViewSwitcher.ViewFactory(){
            public View makeView(){

                //xml이 아닌 자비소스에서 ImageView를 생성하네..
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0xFF000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

                return imageView;
            }
        });
    }

    private void startAnimation(){
        switcher.setVisibility(View.VISIBLE);

        thread = new ImageThread();
        thread.start();
    }

    private void stopAnimation(){
        Toast.makeText(getApplicationContext(), "그만", Toast.LENGTH_LONG).show();
        running = false;
        try{
            thread.join();
        } catch(InterruptedException e){}

        switcher.setVisibility(View.INVISIBLE);
    }


    class ImageThread extends Thread{
        int duration = 250;
        final int imageId[] = { R.drawable.emo_im_crying,
                R.drawable.emo_im_happy,
                R.drawable.emo_im_laughing,
                R.drawable.emo_im_surprised };
        int currentIndex = 0;

        public void run(){
            running = true;

            while(running){
                synchronized (this){
                    mHandler.post(new Runnable(){
                        public void run(){
                            switcher.setImageResource(imageId[currentIndex]);
                        }
                    });

                    currentIndex++;
                    if(currentIndex == imageId.length){
                        currentIndex = 0;
                    }

                    try{
                        Thread.sleep(duration);
                    }catch(InterruptedException e){}
                }
            }
        }
    }
}
