package com.lipnus.kum_layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    ImageView iV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        //액티비티 화면 전환효과 삭제
        getWindow().setWindowAnimations(0);
        iV = (ImageView) findViewById(R.id.aaa);
    }

    public void onClick_BacktoMain(View v){
        Intent iT = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(iT);
    }

    public void onClick_Find(View v){
        iV.setVisibility(View.VISIBLE);
    }
}
