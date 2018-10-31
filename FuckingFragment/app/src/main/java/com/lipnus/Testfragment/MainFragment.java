package com.lipnus.Testfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Sunpil on 2016-03-03.
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        //버튼에 관한 설정
        //==========================================================================================
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });
        //==========================================================================================

        return rootView;
    }
}
