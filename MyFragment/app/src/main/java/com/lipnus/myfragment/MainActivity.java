package com.lipnus.myfragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MainFragment fragment = new MainFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();



    }

    public void onClick_button1(View v){
        MainFragment fragment1 = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
    }

    public void onClick_button2(View v){
        MainFragment2 fragment2 = new MainFragment2();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
    }
}
