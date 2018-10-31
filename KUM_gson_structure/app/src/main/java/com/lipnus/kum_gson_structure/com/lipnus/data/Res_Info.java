package com.lipnus.kum_gson_structure.com.lipnus.data;

/**
 * Created by Sunpil on 2017-02-26.
 */

public class Res_Info {
    String name;
    String theme;
    String category;
    String time;
    String phone;
    int heart;
    int fuck;
    int comment;
    String location;
    String star;
    String star_num;

    public Res_Info(String name, String theme, String category, String time, String phone, int heart, int fuck, int comment, String location, String star, String star_num) {
        this.name = name;
        this.theme = theme;
        this.category = category;
        this.time = time;
        this.phone = phone;
        this.heart = heart;
        this.fuck = fuck;
        this.comment = comment;
        this.location = location;
        this.star = star;
        this.star_num = star_num;
    }
}
