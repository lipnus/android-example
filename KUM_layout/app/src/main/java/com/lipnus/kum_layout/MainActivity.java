package com.lipnus.kum_layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    //왼쪽, 오른쪽 이동 애니매이션 객체
    Animation translateLeftAnim;
    Animation translateRightAnim;

    LinearLayout SlidePage; //슬라이딩 페이지
    LinearLayout SlideShadow; //슬라이딩 페이지 우측의 검은 그림자부분
    boolean isPageOpen = false; //슬라이딩 페이지가 열려있는지 닫혀있는지



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //타이틀바는 res/values 폴더의 styles.xml에서 수정했음

        //액티비티 전환 시 애니매이션 삭제
        getWindow().setWindowAnimations(0);

        //애니매이션 부분
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

        //왼쪽에서 나오는 레이아웃
        SlidePage = (LinearLayout) findViewById(R.id.slidingMenu);
        SlideShadow = (LinearLayout) findViewById(R.id.slidingShadow);

    }

    //왼쪿 상단 메뉴 단추 클릭
    public void onClick_LeftMenu(View v){
        SlidePage.setVisibility(View.VISIBLE);
        SlidePage.startAnimation(translateRightAnim);
    }

    //오른쪽 상단 돋보기 단추 클릭
    public void onClick_Search(View v){
        Intent iT = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(iT);
    }

    //슬라이드의 검은영역 클릭
    public void onClick_SlideShadow(View v){
        SlideShadow.setVisibility(View.GONE);
        SlidePage.startAnimation(translateLeftAnim);

    }


    //애니매이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen==true){
                SlidePage.setVisibility(View.GONE);
                isPageOpen=false;
            }
            else{
                SlideShadow.setVisibility(View.VISIBLE);
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
