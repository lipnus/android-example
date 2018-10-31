package com.lipnus.pageslidingtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    boolean isPageOpen = false; //슬라이딩 페이지가 보이는지 여부

    //왼쪽, 오른쪽 이동 애니매이션 객체
    Animation translateLeftAnim;
    Animation translateRightAnim;

    LinearLayout SlidePage;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.button1);

        SlidePage = (LinearLayout) findViewById(R.id.slidingPage01);

        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);
    }

    public void onButton1Clicked(View v){
        if(isPageOpen==true){
            SlidePage.startAnimation(translateRightAnim);
        }
        else{
            SlidePage.setVisibility(View.VISIBLE);
            SlidePage.startAnimation(translateLeftAnim);
        }
    }


    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen==true){
                SlidePage.setVisibility(View.INVISIBLE);
                btn.setText("열기");
                isPageOpen=false;
            }
            else{
                btn.setText("닫기");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
