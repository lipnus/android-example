package com.lipnus.kumchurk.kum_class;

/**
 * Created by Sunpil on 2017-04-21.
 * 사용자가 투표하고 결과를 받을 때까지 일시적으로 여기에 값을 저장
 * 서버에 정상적으로 데이터가 전달된 것이 확인되면 App에 표시
 */


public class Temp_vote {
    private int index;
    private int heart;
    private int fuck;

    //값을 임시로 저장
    public void setData(int index, int heart, int fuck){
        this.index = index;
        this.heart = heart;
        this.fuck = fuck;
    }

    public int getIndex() {
        return index;
    }

    public int getHeart() {
        return heart;
    }

    public int getFuck() {
        return fuck;
    }
}
