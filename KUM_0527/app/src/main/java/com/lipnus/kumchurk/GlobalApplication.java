package com.lipnus.kumchurk;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;
import com.lipnus.kumchurk.data.main.Main_Data;
import com.lipnus.kumchurk.kakaologin.KakaoSDKAdapter;
import com.tsengvn.typekit.Typekit;

import java.util.List;

/**
 * Created by Sunpil on 2017-03-12.
 * 전체 액티비티를 통제한다.
 * 카카오톡 로그인에 필요한 부분도 있음
 */

public class GlobalApplication extends Application {

    //여기 변수를은 한번 앱이 실행될 동안만 유효(휘발성)

    //카카오톡꺼
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    //유저의 현재위치
    private static volatile double user_latitude;
    private static volatile double user_longitude;

    //접속 유저의 개인정보
    private static String user_id="";
    private static String user_nickname="";
    private static String user_sex="";
    private static String user_grade="";
    private static String user_image="";
    private static String user_thumbnail="";

    //날씨정보
    private static double temp = 10;
    private static double presure = 1000; //기압은 쓸 일이 없지만 받아둔다..
    private static int humidity = 50;
    private static double temp_min = 10;
    private static double temp_max = 10;
    private static String main = "Clear";
    private static String description = "clear sky";

    //메인페이지에 표시될 데이터(식당목록, 메뉴목록) - 서버에서 받은 전체목록
    private static Main_Data mainData;

    //메인페이지에 표시될 데이터(식당목록, 메뉴목록) - 조건에 맞게 정리된 목록(Select_menu클래스에서 분류)
    private static List<Main_Data> mainData2;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());


        // 초깃값은 0,0이다.
        // 단말기의 gps가 켜지지 않았거나, 위치정보를 받는데 실패할 경우 이 값이 그대로 유지.
        user_latitude=0;
        user_longitude=0;

        //폰트
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumGothic.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumGothicBold.otf"));

    }



    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }




    /**
     * Getter, Setter
     */
    public static List<Main_Data> getMainData2() {
        return mainData2;
    }

    public static void setMainData2(List<Main_Data> mainData2) {
        GlobalApplication.mainData2 = mainData2;
    }

    public static Main_Data getMainData() {
        return mainData;
    }

    public static void setMainData(Main_Data mainData) {
        GlobalApplication.mainData = mainData;
    }

    public static double getUser_latitude() {
        return user_latitude;
    }

    public static double getUser_longitude() {
        return user_longitude;
    }

    public static void setLocation(double user_latitude, double user_longitude) {
        GlobalApplication.user_latitude = user_latitude;
        GlobalApplication.user_longitude = user_longitude;
    }

    public static String getUser_nickname() {
        return user_nickname;
    }

    public static void setUser_nickname(String user_nickname) {
        GlobalApplication.user_nickname = user_nickname;
    }

    public static String getUser_image() {
        return user_image;
    }

    public static void setUser_image(String user_image) {
        GlobalApplication.user_image = user_image;
    }

    public static String getUser_thumbnail() {
        return user_thumbnail;
    }

    public static void setUser_thumbnail(String user_thumbnail) {
        GlobalApplication.user_thumbnail = user_thumbnail;
    }

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        GlobalApplication.user_id = user_id;
    }

    public static GlobalApplication getObj() {
        return obj;
    }

    public static void setObj(GlobalApplication obj) {
        GlobalApplication.obj = obj;
    }

    public static void setUser_latitude(double user_latitude) {
        GlobalApplication.user_latitude = user_latitude;
    }

    public static void setUser_longitude(double user_longitude) {
        GlobalApplication.user_longitude = user_longitude;
    }

    public static double getTemp() {
        return temp;
    }

    public static void setTemp(double temp) {
        GlobalApplication.temp = temp;
    }

    public static double getPresure() {
        return presure;
    }

    public static void setPresure(double presure) {
        GlobalApplication.presure = presure;
    }

    public static int getHumidity() {
        return humidity;
    }

    public static void setHumidity(int humidity) {
        GlobalApplication.humidity = humidity;
    }

    public static double getTemp_min() {
        return temp_min;
    }

    public static void setTemp_min(double temp_min) {
        GlobalApplication.temp_min = temp_min;
    }

    public static double getTemp_max() {
        return temp_max;
    }

    public static void setTemp_max(double temp_max) {
        GlobalApplication.temp_max = temp_max;
    }

    public static String getUser_sex() {
        return user_sex;
    }

    public static void setUser_sex(String user_sex) {
        GlobalApplication.user_sex = user_sex;
    }

    public static String getUser_grade() {
        return user_grade;
    }

    public static void setUser_grade(String user_grade) {
        GlobalApplication.user_grade = user_grade;
    }

    public static String getMain() {
        return main;
    }

    public static void setMain(String main) {
        GlobalApplication.main = main;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        GlobalApplication.description = description;
    }
}