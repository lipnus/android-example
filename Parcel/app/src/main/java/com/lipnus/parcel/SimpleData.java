package com.lipnus.parcel;

import android.os.Parcel;
import android.os.Parcelable;


public class SimpleData implements Parcelable {

    int number;
    String message;

    public SimpleData(int num, String msg){
        number = num;
        message = msg;
    }

    //Parcel객체에서 읽기
    public SimpleData(Parcel src){
        number = src.readInt();
        message = src.readString();
    }

    //CREATOR상수 정의
    //==============================================================================================
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
      public SimpleData createFromParcel(Parcel in){
          return new SimpleData(in);
      }

      public SimpleData[] newArray(int size){
        return new SimpleData[size];
      }
    };
    //==============================================================================================


    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(number);
        dest.writeString(message);
    }



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


