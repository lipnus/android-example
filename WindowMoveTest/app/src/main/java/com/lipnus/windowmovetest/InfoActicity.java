package com.lipnus.windowmovetest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Sunpil on 2016-05-07.
 */
public class InfoActicity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
    }

    public void onClick_backToMain(View v){
        finish();
    }
}
