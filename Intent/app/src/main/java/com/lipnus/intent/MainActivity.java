package com.lipnus.intent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ANOTHER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){

        Intent iT = new Intent(getApplicationContext(), AnotherActivity.class);
        startActivityForResult(iT, REQUEST_CODE_ANOTHER );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent iT){
        super.onActivityResult(requestCode, resultCode, iT);

        if(requestCode == REQUEST_CODE_ANOTHER){
            Toast.makeText(getBaseContext(), "onActivityResult: "+requestCode +"," + resultCode, Toast.LENGTH_LONG).show();
        }

        if(resultCode == RESULT_OK){
            String name = iT.getExtras().getString("name");
            Toast.makeText(getBaseContext(), "name: "+ name, Toast.LENGTH_LONG).show();
        }
    }
}
