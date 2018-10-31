package com.lipnus.myfragment;

/**
 * Created by Sunpil on 2017-01-10.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sunpil on 2017-01-10.
 */

public class MainFragment2 extends Fragment {

    //연결된다
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main2, container, false);
        return rootView;
    }
}