package com.lipnus.fucking;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //기존의 뷰페이저를 세로방향으로 바꾼 VerticalViewPager 라이브러리를 이용.
        VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        verticalViewPager.setAdapter(new ViewPager_Adapter(getFragmentManager()));

        //전환효과(false, true에 의해서 앞뒤의 레이어 순위가 바뀜)
        verticalViewPager.setPageTransformer(false, new ViewPager_DepthPageTransformer());

        //뷰페이저에 변화가 있을때의 리스너(쓰진 않음)
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }







}