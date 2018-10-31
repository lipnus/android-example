package com.lipnus.test_intent_flag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    String textData;
    int number;

    TextView fuckingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //지정해주고
        fuckingTv = (TextView) findViewById(R.id.fucking);

        //인텐트로 값을 받고
        Intent intent = getIntent();
        textData = intent.getExtras().getString("nickname");
        number = intent.getExtras().getInt("number");

        //그 값을 표시
        fuckingTv.setText(textData);
    }
}
