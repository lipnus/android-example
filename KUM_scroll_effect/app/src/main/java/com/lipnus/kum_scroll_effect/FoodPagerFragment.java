package com.lipnus.kum_scroll_effect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FoodPagerFragment {


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
                    rootView = inflater.inflate(R.layout.fragment_image_inside, container, false);
                    (rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_image_inside, container, false);
                    (rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food2);
                    break;

                default:
                    rootView = inflater.inflate(R.layout.fragment_image_inside, container, false);
                    (rootView.findViewById(R.id.imageView)).setBackgroundResource(R.drawable.food3);
                    break;
            }

            //음식메뉴이미지 기본크기는 110%
//            (rootView.findViewById(R.id.imageView)).setScaleX(1.1f);
//            (rootView.findViewById(R.id.imageView)).setScaleY(1.1f);

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;
        }


    }
}