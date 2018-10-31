package com.lipnus.kucallrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent iT = getIntent();
        processIntent(iT);
    }


    //MainActivity가 이미 떠있을 때
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent){

        if(intent != null){
            String command = intent.getStringExtra("command");

            if(command != null){
                if(command.equals("recordingState")){
                    String state = intent.getStringExtra("state");
                    Toast.makeText(this, "녹음상태: "+ state, Toast.LENGTH_LONG).show();
                }
            }


        }


    }
}
