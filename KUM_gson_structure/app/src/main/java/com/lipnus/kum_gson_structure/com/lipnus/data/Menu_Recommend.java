package com.lipnus.kum_gson_structure.com.lipnus.data;

/**
 * Created by Sunpil on 2017-02-26.
 */

//no	name	res_name	price	categoty1	category2	heart	fuck	comment


public class Menu_Recommend {
    String name;
    String res_name;

    int price;
    int price2;
    int price3;

    String category1;
    String category2;

    int heart;
    int fuck;
    int comment;

    String image;

    public Menu_Recommend(String name, String res_name, int price, int price2, int price3, String category1, String category2, int heart, int fuck, int comment, String image) {
        this.name = name;
        this.res_name = res_name;
        this.price = price;
        this.price2 = price2;
        this.price3 = price3;
        this.category1 = category1;
        this.category2 = category2;
        this.heart = heart;
        this.fuck = fuck;
        this.comment = comment;
        this.image = image;
    }
}
