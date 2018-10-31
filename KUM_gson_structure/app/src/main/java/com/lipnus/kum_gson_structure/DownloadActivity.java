package com.lipnus.kum_gson_structure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity {

    ImageView iV;
    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        iV = (ImageView) findViewById(R.id.downIv);
        tV = (TextView) findViewById(R.id.downTv);
    }
}
