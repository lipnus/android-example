package com.lipnus.kum_imgpicker2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        Toast.makeText(getApplicationContext(), "uri: " + uri, Toast.LENGTH_LONG).show();
                    }
                })
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());




    }
}
