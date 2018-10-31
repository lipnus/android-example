package com.lipnus.webconnecttest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uriData = getIntent().getData();
        String Data = uriData.getQueryParameter("data");
        Toast.makeText(getApplication(), "*"+Data, Toast.LENGTH_LONG).show();

    }


 }