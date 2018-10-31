package com.lipnus.fragmenttest2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Sunpil on 2016-03-03.
 */
public class ViewerFragment extends Fragment {
    ImageView iV = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewer, container, false);
        iV = (ImageView) rootView.findViewById(R.id.imageView);

        return rootView;
    }

    public void setImage(int resId){
        iV.setImageResource(resId);
    }
}
