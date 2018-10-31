package com.lipnus.windowmovetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Sunpil on 2016-05-07.
 */
public class HowtoActicity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);
    }

    public void onClick_backToMain2(View v){
        finish();
    }

    public void onClick_setting(View v){
        getWindowPermission2();

    }

    // 윈도우 띄우는거 권한받기
    public void getWindowPermission2(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 5152);
    }


    // 윈도우 띄우는거 권한 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 5152:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        //권한 승인이 이루어 지지 않음
                    }
                }
                break;
        }
    }




}
