package com.lipnus.google_gongdri;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Sunpil on 2017-06-04.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //폰트
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumGothic.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumGothicBold.otf"));
    }
}
