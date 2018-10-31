package com.lipnus.kumchurk.submenu;

import com.lipnus.kumchurk.data.MenuRes_Info;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class SearchListViewItem {

    MenuRes_Info mrI;
    String callFrom; //어디서 호출했는지

    public SearchListViewItem(MenuRes_Info mrI, String callFrom) {
        this.mrI = mrI;
        this.callFrom = callFrom;
    }

    public MenuRes_Info getMrI() {
        return mrI;
    }

    public String getCallFrom() {
        return callFrom;
    }
}