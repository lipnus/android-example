package com.lipnus.kum_connect_image_db;

/**
 * Created by Sunpil on 2017-02-24.
 */

//귀찮으니까 get, set안함 아몰랑...
public class MenuComment {

    //DB의 menu_comment 테이블을 담당

//    int no;
    int user_id;
    int heart;
    int fuck;
    String memo;
    int comment_num;
    String res_name;
    String menu_name;
    String menu_pic;

    String created_at;
    String updated_at;

    //생성자
    public MenuComment(int user_id, int heart, int fuck, String memo, int comment_num, String res_name,
                       String menu_name, String menu_pic, String created_at, String updated_at) {
        this.user_id = user_id;
        this.heart = heart;
        this.fuck = fuck;
        this.memo = memo;
        this.comment_num = comment_num;
        this.res_name = res_name;
        this.menu_name = menu_name;
        this.menu_pic = menu_pic;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
