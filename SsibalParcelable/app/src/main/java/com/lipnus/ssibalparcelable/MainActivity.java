package com.lipnus.ssibalparcelable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){



        //객체 자체를 인텐드로 전달한다 이기!
        Intent iT = new Intent(getApplicationContext(), SubActivity.class);

        FuckingData fuck = new FuckingData();
        fuck.setData(18, "겁이나서그래 에~오!");

        iT.putExtra("LIP", fuck);

        startActivity(iT);

    }
}
