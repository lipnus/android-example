package com.lipnus.listtest;

import android.graphics.drawable.Drawable;

//이 클래스에서 데이터들을 담아두는듯

//리스트뷰의 한 아이템에 표시될 데이터를 담고 있을 클래스 정의
public class IconTextItem {

    private boolean mSelectable = true;

    //Drawable타입의 변수와 문자열 타입의 배열 변수 선언
    private Drawable mIcon;
    private String[] mData;

    //Drawable객체와 문자열 타입의 배열을 파라미터로 전달받는 생성자
    public IconTextItem(Drawable icon, String[] obj){
        mIcon = icon;
        mData = obj;
    }

    //Drawable객체와 각 문자열을 파라미터로 전달받는 생성자
    public IconTextItem(Drawable icon, String obj01, String obj02, String obj03){
        mIcon = icon;

        mData = new String[3];

        mData[0] = obj01;
        mData[1] = obj02;
        mData[2] = obj03;
    }



    //반환하는 매소드들
    //==============================================================================================
    //문자열 배열 통채로 반환
    public String[] getData(){
        return mData;
    }

    //특정 번째의 문자열 반환
    public String getData(int index){
        if(mData ==null || index >= mData.length){
            return null;
        }
        return mData[index];
    }

    public Drawable getIcon(){
        return mIcon;
    }
    //==============================================================================================

    public void setmData(String[] obj){
        mData = obj;
    }
    public void setIcon(Drawable icon){
        mIcon = icon;
    }
    public boolean isSelectable() {
        return mSelectable;
    }


}
