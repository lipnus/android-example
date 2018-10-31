package com.asha.md360player4android;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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


        findViewById(R.id.bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = sPath + "megabox";

                if (!TextUtils.isEmpty(url)){
                    MD360PlayerActivity.startBitmap(DemoActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(DemoActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }
}
