package com.lipnus.facedetect;

import android.app.Application;

/**
 * Created by Sunpil on 2016-11-19.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    //MyView에서 값을 여기에 보내고, MainActivity에서 이 값을 받아서 블루투스로 전송한다

    //==================================================================================
    // 0:대기모드(정지)  1:얼굴최초발견(인사?하기)  2:얼굴재발견(일시정지)  3:움직
    //==================================================================================
    static int faceState;
}
