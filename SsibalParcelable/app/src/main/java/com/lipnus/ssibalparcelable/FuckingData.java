package com.lipnus.ssibalparcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sunpil oni 2016-02-25.
 */

//Parcelable을 상속받아 직렬화 클래스를 구현한다.
public class FuckingData implements Parcelable {
    private int intData = 0;
    private String stringData = "씨발좆같은Parcelable";

    public int getInt(){
        return intData;
    }

    public String getString(){
        return stringData;
    }

    public void setData(int intD, String stringD){
        intData = intD;
        stringData = stringD;
    }

    //이새끼는 뭐하는 함수지? 묘사?
    @Override
    public int describeContents(){
        return 0;
    }

    //★보내는쪽이 쓰는 함수. 데이터를 Parcel에 넣는다.
    //객체를 다른 프로세스로 전달하는 프로세스에서 송신 직전에 호출된다.
    //==============================================================================================
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(intData);
        dest.writeString(stringData);
    }
    //==============================================================================================

    //CREATOR상수는 Parcel객체로부터 데이터를 읽어들여 객체를 생성하는 역할을 한다.
    public static final Parcelable.Creator<FuckingData> CREATOR = new Creator<FuckingData>() {

        //★받는쪽이 쓰는 함수
        //==========================================================================================

        //생성자를 호출해 Parcel객체에서 읽는다.
        @Override
        public FuckingData createFromParcel(Parcel in) {
            FuckingData damn = new FuckingData();
            damn.setData(in.readInt(), in.readString());
            return damn;
        }
        //==========================================================================================

        @Override
        public FuckingData[] newArray(int size) {
            return null;
        }
    };



}
