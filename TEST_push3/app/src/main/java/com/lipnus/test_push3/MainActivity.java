package com.lipnus.test_push3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call this onCreate() or onResume()
        FCMManager.getInstance(this).registerListener(this);

    //call this onDestroy()
        FCMManager.getInstance(this).unRegisterListener();
    }
}
