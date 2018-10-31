package com.lipnus.megabox;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //경로
    public static final String sPath = "file:///mnt/sdcard/vr/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }




    public void onClick_start(View v){

        String url = sPath + "megabox.jpg";
        MD360PlayerActivity.startBitmap(MainActivity.this, Uri.parse(url));

    }


}
