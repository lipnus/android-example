package com.lipnus.kum_viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


public class MainActivity extends FragmentActivity {


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);


        //전환효과(false, true에 의해서 앞뒤의 레이어 순위가 바뀜)
        mPager.setPageTransformer(true, new DepthPageTransformer());

        //뷰페이저에 변화가 있을때의 리스너(쓰진 않음)
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("FFF", "position: " + position);
//                ImageView iV = (ImageView)findViewById(R.id.imageView);
//                iV.setImageResource(R.drawable.food2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void onClick(View v){
        MenuData menudata = new MenuData();
        menudata.str = "이 글자를 프래그먼트에 넣자";
        ScreenSlidePageFragment.PlaceholderFragment.addData(menudata);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            //뷰페이저의 총 페이지수를 리턴한다
            return 3;
        }
    }
}