package com.lipnus.listviewtest;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */
public class ListViewItem {
    private String filenameStr ;
    private String commentStr ;


    public void setTitle(String filename) {
        filenameStr = filename ;
    }
    public void setDesc(String comment) {
        commentStr = comment ;
    }


    public String getTitle() {
        return this.filenameStr ;
    }
    public String getDesc() {
        return this.commentStr ;
    }
}