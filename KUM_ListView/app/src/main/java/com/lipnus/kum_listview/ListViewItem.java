package com.lipnus.kum_listview;

import android.graphics.Bitmap;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ListViewItem {

    private boolean left_menu_visible;
    private String left_menu_name;
    private String left_menu_cost;
    private Bitmap left_menu_image = null;

    private boolean right_menu_visible;
    private String right_menu_name;
    private String right_menu_cost;
    private Bitmap right_menu_image = null;

    private boolean middle_menu_visible;
    private String middle_menu_name;
    private String middle_menu_cost;
    private Bitmap middle_menu_image = null;




    //get,set 매소드

    public boolean isLeft_menu_visible() {
        return left_menu_visible;
    }

    public void setLeft_menu_visible(boolean left_menu_visible) {
        this.left_menu_visible = left_menu_visible;
    }

    public String getLeft_menu_name() {
        return left_menu_name;
    }

    public void setLeft_menu_name(String left_menu_name) {
        this.left_menu_name = left_menu_name;
    }

    public String getLeft_menu_cost() {
        return left_menu_cost;
    }

    public void setLeft_menu_cost(String left_menu_cost) {
        this.left_menu_cost = left_menu_cost;
    }

    public Bitmap getLeft_menu_image() {
        return left_menu_image;
    }

    public void setLeft_menu_image(Bitmap left_menu_image) {
        this.left_menu_image = left_menu_image;
    }

    public boolean isRight_menu_visible() {
        return right_menu_visible;
    }

    public void setRight_menu_visible(boolean right_menu_visible) {
        this.right_menu_visible = right_menu_visible;
    }

    public String getRight_menu_name() {
        return right_menu_name;
    }

    public void setRight_menu_name(String right_menu_name) {
        this.right_menu_name = right_menu_name;
    }

    public String getRight_menu_cost() {
        return right_menu_cost;
    }

    public void setRight_menu_cost(String right_menu_cost) {
        this.right_menu_cost = right_menu_cost;
    }

    public Bitmap getRight_menu_image() {
        return right_menu_image;
    }

    public void setRight_menu_image(Bitmap right_menu_image) {
        this.right_menu_image = right_menu_image;
    }

    public boolean isMiddle_menu_visible() {
        return middle_menu_visible;
    }

    public void setMiddle_menu_visible(boolean middle_menu_visible) {
        this.middle_menu_visible = middle_menu_visible;
    }

    public String getMiddle_menu_name() {
        return middle_menu_name;
    }

    public void setMiddle_menu_name(String middle_menu_name) {
        this.middle_menu_name = middle_menu_name;
    }

    public String getMiddle_menu_cost() {
        return middle_menu_cost;
    }

    public void setMiddle_menu_cost(String middle_menu_cost) {
        this.middle_menu_cost = middle_menu_cost;
    }

    public Bitmap getMiddle_menu_image() {
        return middle_menu_image;
    }

    public void setMiddle_menu_image(Bitmap middle_menu_image) {
        this.middle_menu_image = middle_menu_image;
    }
}