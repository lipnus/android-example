package com.lipnus.kum_viewpager_basic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScreenSlidePageFragment {


    public static class PlaceholderFragment extends Fragment {

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
                    rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
                    ((ImageView)rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food);

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
                    ((ImageView)rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food2);
                    break;

                default:
                    rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
                    ((ImageView)rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food3);
                    break;
            }

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;
        }


    }
}