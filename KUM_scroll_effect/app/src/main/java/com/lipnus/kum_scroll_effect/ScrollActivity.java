package com.lipnus.kum_scroll_effect;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Created by Sunpil on 2017-02-01.
 * 음식메뉴 넘길 때 쓰이는 뷰페이저의 어탭터는 따로 클래스를 만들지 않고 내부클래스로 구현하였다(2017-02-01)
 */
public class ScrollActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView overTv;
    TextView viewpagerCount;
    TextView reviewTv;
    TextView likeitTv;

    ImageView faceIv;
    TextView idTv;

    ImageView resSmallIv;
    ImageView resPicIv;
    LinearLayout resPicFilter;


    LinearLayout foodCommentLayout;
    LinearLayout restrantInfoLinear;

    //액티비티에 있지 않고 뷰페이저 안의 프래그먼트 안에 있기 때문에 onCreate에서 선언하지 않는다
    ImageView iV;

    //음식메뉴를 넘기는 뷰페이저와 어댑터
    private ViewPager foodPager;
    private PagerAdapter foodPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        overTv = (TextView) findViewById(R.id.overTextView);
        reviewTv = (TextView) findViewById(R.id.reviewTv); //댓글
        likeitTv = (TextView) findViewById(R.id.likeitTv); //좋아요
        viewpagerCount = (TextView) findViewById(R.id.viewPagerPageTv);

        faceIv = (ImageView) findViewById(R.id.faceIv);
        idTv = (TextView) findViewById(R.id.idTv);

        resSmallIv = (ImageView) findViewById(R.id.resSmallIv); //조그마한 정사각형 식당그림
        resPicIv = (ImageView) findViewById(R.id.res_pic_Iv); //식당사진
        resPicFilter = (LinearLayout) findViewById(R.id.res_pic_filter_Linear); //식당사진 위의 검은색 필터

        foodCommentLayout = (LinearLayout) findViewById(R.id.foodCommentLinear);
        restrantInfoLinear = (LinearLayout) findViewById(R.id.resturantInfoLinear);

        //뷰페이저와 어댑터의 객체
        foodPager = (ViewPager) findViewById(R.id.pager);
        foodPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        foodPager.setAdapter(foodPagerAdapter);

        //전환효과(false, true에 의해서 앞뒤의 레이어 순위가 바뀜)
        foodPager.setPageTransformer(true, new DepthPageTransformer());

        //뷰페이저에 변화가 있을때의 리스너
        foodPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("EEE", "position: " + position);

                //현재의 viewPager가 몇번째인지 사진 오른쪽 하단에 표시하는 TextView
                viewpagerCount.setText(position+1 + "/" + foodPagerAdapter.getCount() );

                switch (position){
                    case 0:
                        idTv.setText("sh_9513");
                        faceIv.setBackgroundResource(R.drawable.face);
                        reviewTv.setText("먹기좋은 한입크기라 폭풍흡입했습니다. \n양도 많아서 가성비도 짱짱이예요");
                        likeitTv.setText("♥ 좋아요 10개");
                        break;

                    case 1:
                        idTv.setText("kti7679");
                        faceIv.setBackgroundResource(R.drawable.face2);
                        reviewTv.setText("너와 함께 한 목살 스테이크 눈부셨다\n" +
                                "날이 좋아서 날이 좋지 않아서 날이 적당해서\n" +
                                "한입 한입이 좋았다");
                        likeitTv.setText("♥ 좋아요 8 -2 = 6개");
                        break;

                    case 2:
                        idTv.setText("skawngur");
                        faceIv.setBackgroundResource(R.drawable.face3);
                        reviewTv.setText("육질이 살아있네요\n" +
                                "You're Ma heart (heart) heart (heart) heartbreaker\n" +
                                "내가 뭘 잘못 했는지\n" +
                                "You're Ma heart (heart) heart (heart) heartbreaker\n" +
                                "No way (No Way) No Way (No way)\n" +
                                "니가 날 떠나간데도 난 인정 못한다고\n" +
                                "잘 사나 보자고 (Love is hate it)\n" +
                                "지긋지긋지긋해 삐끗삐끗삐끗해");
                        likeitTv.setText("♥ 좋아요 2개");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        //onScrollChangedListener은 SDK 23이상에서만 동작한다 23버전 이하에서는 꿩대신 닭으로 onTouchListener를 사용
        if(Build.VERSION.SDK_INT >=23 ){
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    scrollChanged();
                }
            });
        }else {
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                private ViewTreeObserver observer;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollChanged();
                    return false;
                }
            });


        }
    }

    public void onClick(View v){
    }

    //스크롤이 변화하였을때의 처리
    public void scrollChanged(){

        try{

            //tV.setText( "위치: " + scrollView.getScrollY() );

            float scrollY = scrollView.getScrollY();
            Log.d("SCSC", "scroll: " + scrollY);

            //scrollView.getScrollY() 0~400일때, scale 1.5 -> 1

            if(scrollY < 500){
                //그림 축소
                foodPager.setScaleX( 1 + (scrollY/2500) );
                foodPager.setScaleY( 1 + (scrollY/2500) );
                overTv.setAlpha(1 - (scrollY/500));
                foodPager.setTranslationY(scrollY*1.1f);

                foodCommentLayout.setAlpha(1-scrollY/1600);
            }
            else if(scrollY < 1600){
                foodPager.setScaleX( 1.1f + (scrollY/5000) );
                foodPager.setScaleY( 1.1f + (scrollY/5000) );
                foodPager.setTranslationY(scrollY*1.1f);

                foodCommentLayout.setAlpha(1-scrollY/1600);
            }


            //식당메뉴부분의 애니매이션 설정
            if(1500 <= scrollY && scrollY <= 2000){

                //회색부분
                restrantInfoLinear.setAlpha((scrollY-1500)/200);

                //1500일때 45도, 2000일때 0도
                resSmallIv.setVisibility(View.VISIBLE);
                resSmallIv.setRotation( 45- (scrollY-1500)/500 * 45  ); //움직임: ~2000
                resSmallIv.setAlpha( (scrollY-1500)/400 ); //알파 : ~1900

                //식당사진필터
                resPicFilter.setVisibility(View.VISIBLE);
                resPicFilter.setAlpha( (scrollY-1700)/300 * 0.6f ); //알파: ~1700~2000
                if(scrollY <= 1900){
                    resPicFilter.setTranslationX(800 - (scrollY-1700)/200*800 ); //움직임: ~1900
                }

                //식당사진
                resPicIv.setVisibility(View.VISIBLE);
                resPicIv.setAlpha( (scrollY-1900)/100 );
            }
            else if(scrollY < 1500){
                restrantInfoLinear.setAlpha(0);
                resSmallIv.setVisibility(View.INVISIBLE);
                resPicIv.setVisibility(View.INVISIBLE);
                resPicFilter.setVisibility(View.INVISIBLE);
            }
            else if(scrollY > 2000){
                resPicFilter.setTranslationX(0);
            }

        }catch(Exception e){Log.d("DDD", "오류: " + e);}


    }

    /**
     * 뷰페이저의 어댑터
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FoodPagerFragment.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            //뷰페이저의 총 페이지수를 리턴한다
            return 3;
        }
    }
}
