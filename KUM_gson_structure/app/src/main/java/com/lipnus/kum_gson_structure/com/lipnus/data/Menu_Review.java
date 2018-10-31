package com.lipnus.kum_gson_structure.com.lipnus.data;

/**
 * Created by Sunpil on 2017-02-26.
 */

public class Menu_Review {
    public int user_id;
    String user_nickname;
    String user_icon;

    int heart;
    int fuck;
    String memo;
    int comment;
    String res_name;
    String menu_name;
    String menu_pic;

    String created_at;
    String updated_at;

    public Menu_Review(int user_id, String user_nickname, String user_icon, int heart, int fuck, String memo, int comment, String res_name, String menu_name, String menu_pic, String created_at, String updated_at) {
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.user_icon = user_icon;
        this.heart = heart;
        this.fuck = fuck;
        this.memo = memo;
        this.comment = comment;
        this.res_name = res_name;
        this.menu_name = menu_name;
        this.menu_pic = menu_pic;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
