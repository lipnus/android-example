package com.lipnus.pi_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTv = (TextView) findViewById(R.id.test_tv);

        String aaa = testTv.getText().toString(); //hello world
        testTv.setText("안녕!");
        aaa = testTv.getText().toString(); //안녕

    }

    public void btn_click(View v){
        Toast.makeText(getApplication(), "안녕!", Toast.LENGTH_LONG).show();
    }
}
