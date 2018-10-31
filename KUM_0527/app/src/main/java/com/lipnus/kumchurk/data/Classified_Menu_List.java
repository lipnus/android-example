package com.lipnus.kumchurk.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunpil on 2017-03-27.
 * 소분류에 따라 몇개의 집단으로 나뉜 메뉴리스트
 */

public class Classified_Menu_List {
    public List<Menu_List> c_menu_list;
    public String category_name; //들어간 집단의 소분류(category2)


    public Classified_Menu_List(){
        c_menu_list = new ArrayList<>();
    }


}
