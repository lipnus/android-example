package com.lipnus.fragmentsample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Sunpil on 2016-03-02.
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //==========================================================================================
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        //==========================================================================================


        //버튼을 클릭했을때 다른 프래그먼트를 띄운다.
        //==========================================================================================
        Button button = (Button) rootView.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //메인액티비티 객체설정
                MainActivity activity = (MainActivity) getActivity();

                //메인액티비티 객체에 있는 onFragmentChanged()메소드 호출
                activity.onFragmentChanged(0);
            }
        });
        //==========================================================================================

        return rootView;
 
    }
}
