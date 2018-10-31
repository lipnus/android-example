package com.lipnus.fragmentsample;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.mainFragment);
        menuFragment = new MenuFragment();
    }

    public void onFragmentChanged(int index){
        Log.d("LIPNUS", "onFragmentChanged");
        if(index==0){
            Log.d("LIPNUS", "onFragmentChanged IF문 안쪽");
            getFragmentManager().beginTransaction().replace(R.id.container, menuFragment);
        }
        else if(index ==1){
            getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        }
    }


}
