package com.lipnus.fucking;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sunpil on 2017-01-24.
 */
//프래그먼트
public class ViewPager_Fragment {

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
                    rootView = inflater.inflate(R.layout.fragment_layout, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_layout2, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_layout3, container, false);
                    break;

                default:
                    rootView = inflater.inflate(R.layout.fragment_layout, container, false);
                    break;
            }

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;
        }


    }
}
