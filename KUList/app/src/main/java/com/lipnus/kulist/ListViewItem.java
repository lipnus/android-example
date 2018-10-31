package com.lipnus.kulist;

import android.graphics.Bitmap;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ListViewItem {
    private String nameStr ;
    private String commentStr ;
    private Bitmap bitmap ;


    //Set 매소드들
    public void setName(String filename) {
        nameStr = filename ;
    }
    public void setComment(String comment) {
        commentStr = comment ;
    }
    public void setBitmap (Bitmap bitmapData){bitmap = bitmapData; }

    //Get 매소드들
    public String getName() {
        return this.nameStr ;
    }
    public String getComment() {
        return this.commentStr ;
    }
    public Bitmap getBitmap(){ return this.bitmap; }
}