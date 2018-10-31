package com.lipnus.kum_imgpicker;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //퍼미션
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                Toast.makeText(RationaleDenyActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(RationaleDenyActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//
//        };

        //이미지피커커
        TedBotomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
//                        Toast.makeText(getApplicationContext(), "url: " + uri, Toast.LENGTH_LONG).show();
                    }
                })
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());


    }
}
