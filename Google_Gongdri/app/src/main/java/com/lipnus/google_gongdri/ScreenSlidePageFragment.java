package com.lipnus.google_gongdri;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ScreenSlidePageFragment {


    public static class PlaceholderFragment extends Fragment {

        ImageView iv;
        FrameLayout fr;
        ScrollView sc;
        LinearLayout centerLr;
        TextView emotionTv;
        TextView movieTitleTv;
        ImageView cocktailIv;
        TextView cocktailTv;

        Context context;



        //현재 어느페이지인지
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int viewNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;


            switch(viewNum){
                case 1:
                    rootView = movieFragment1(container, inflater);
                    break;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
                    ((ImageView)rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.mood2);
                    emotionTv = (TextView) rootView.findViewById(R.id.emotion_tv);
                    emotionTv.setText("Gloomy");

                    break;

                default:
                    rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
                    ((ImageView)rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.mood1);
                    emotionTv = (TextView) rootView.findViewById(R.id.emotion_tv);
                    emotionTv.setText("Angry");
                    break;
            }

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;
        }


        public View movieFragment1(ViewGroup container, LayoutInflater inflater){
            View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

            context = rootView.getContext();


            iv = (ImageView)rootView.findViewById(R.id.imageView);
            sc = (ScrollView) rootView.findViewById(R.id.scroll);
            fr = (FrameLayout) rootView.findViewById(R.id.movie_img_fr);
            centerLr = (LinearLayout) rootView.findViewById(R.id.center_box_lr);
            emotionTv = (TextView) rootView.findViewById(R.id.emotion_tv);
            movieTitleTv = (TextView) rootView.findViewById(R.id.movie_name_tv);
            cocktailIv = (ImageView) rootView.findViewById(R.id.cocktail_iv);
            cocktailTv = (TextView) rootView.findViewById(R.id.cocktail_tv);

            //그림
            Glide.with(this)
                    .load( R.drawable.mood4 )
                    .into( iv );
            iv.setScaleType(ImageView.ScaleType.FIT_XY);




            //원래는 터치 이후에 나와야 할 것들
            Glide.with(this)
                    .load( R.drawable.cocktail_mood)
                    .into( cocktailIv );
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            cocktailTv.setText("Mood Indigo");


            //======================================================================================
            // 버튼터치
            //======================================================================================
            centerLr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    centerLr.setBackgroundColor(Color.rgb(255,255,255));
                    movieTitleTv.setTextColor(Color.rgb(20,20,20));
                    emotionTv.setTextColor(Color.rgb(20,20,20));

                    //그림
                    Glide.with(context)
                            .load( R.drawable.mood4 )
                            .bitmapTransform(new BlurTransformation(context, 5, 2))
                            .into( iv );
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);

                }
            });

//            ViewGroup.LayoutParams params = tv.getLayoutParams();
//            params.height = 1000;
//            tv.setLayoutParams(params);
//            tv.requestLayout();

            if(Build.VERSION.SDK_INT >=23 ){
                sc.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        scrollEvent();
                    }
                });
            }


            return rootView;
        }

        public void scrollEvent(){
            try{
                float scrollY = sc.getScrollY();
                Log.d("SCSC", "scroll: " + scrollY);


                //커짐
                if(scrollY < 700 ){
                    iv.setScaleX( 1 + (scrollY/2500) );
                    iv.setScaleY( 1 + (scrollY/2500) );
                    iv.setTranslationY(scrollY*1.1f);
                }

                //버팅김
                else if(scrollY < 2000 ){
                    iv.setTranslationY(scrollY*1.1f);
                }

            }catch(Exception e){Log.d("DDD", "오류: " + e);}
        }

    }
}