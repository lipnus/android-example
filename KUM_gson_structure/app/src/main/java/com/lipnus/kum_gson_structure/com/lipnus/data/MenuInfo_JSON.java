package com.lipnus.kum_gson_structure.com.lipnus.data;

import java.util.List;

/**
 * Created by Sunpil on 2017-02-26.
 */

public class MenuInfo_JSON {
    public List<Menu_Review> menuReview;
    public List<Res_Info> resInfo;
    public List<Menu_List> menuList;
    public List<Menu_Recommend> menuRecommend;

    //생성자
    public MenuInfo_JSON(List<Menu_Review> menuReview, List<Res_Info> resInfo, List<Menu_List> menuList, List<Menu_Recommend> menuRecommend) {
        this.menuReview = menuReview;
        this.resInfo = resInfo;
        this.menuList = menuList;
        this.menuRecommend = menuRecommend;
    }


}
