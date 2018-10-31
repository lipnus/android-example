package com.lipnus.keyeventtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn = null;

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "이렇게 하는건가?", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode==KeyEvent.KEYCODE_BACK){
            Toast.makeText(getApplicationContext(), "취소버튼", Toast.LENGTH_LONG).show();
            return super.onKeyDown(keyCode, event);
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.startBtn);
        btn.setOnClickListener(this);
    }
}
