package com.asha.md360player4android.Other;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.asha.md360player4android.R;
import com.asha.md360player4android.Scene.MainList;
import com.asha.md360player4android.Scene.MovieInfoActivity;
import com.asha.md360player4android.Scene.SeatSelect;

/**
 * Created by hzqiujiadi on 16/1/26.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class DemoActivity extends AppCompatActivity {

    public static final String sPath = "file:///mnt/sdcard/vr/";

    //public static final String sPath = "file:////storage/sdcard1/vr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("SSSS", size.x + " " + size.y );



        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int pxWidth  = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        float dipWidth  = displayMetrics.widthPixels  / displayMetrics.density;
        float dipHeight = displayMetrics.heightPixels / displayMetrics.density;


        Log.d("SSSS", pxWidth + " " + pxHeight + " " + dipWidth+ " " +dipHeight );


        findViewById(R.id.bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "android.resource://com.asha.md360player4android/drawable/aaa";
                if (!TextUtils.isEmpty(url)){

                    Log.d("SSSS", "시작");
                    SeatSelect.startBitmap(DemoActivity.this, null );
                } else {
                    Toast.makeText(DemoActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //큐레이션 스크롤
    public void onClick_mainlist(View v){
        Intent intent = new Intent(getApplicationContext(),MainList.class);
        startActivity(intent);
    }


    //영화리뷰 스크롤
    public void onClick_movieInfo(View v){
        Intent intent = new Intent(getApplicationContext(),MovieInfoActivity.class);
        startActivity(intent);
    }




    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }







}
