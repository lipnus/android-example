package com.lipnus.kum_drawabletst;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView iV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        iV = (ImageView)findViewById(R.id.iV);
        Drawable drawable = getResources().getDrawable(R.drawable.foodmenu1);
        iV.setBackgroundDrawable( drawable );
    }
}
