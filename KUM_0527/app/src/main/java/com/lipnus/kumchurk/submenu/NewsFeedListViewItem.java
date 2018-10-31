package com.lipnus.kumchurk.submenu;

import com.lipnus.kumchurk.data.NewsFeed;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class NewsFeedListViewItem {

    private NewsFeed newsFeed;

    public NewsFeedListViewItem(NewsFeed newsFeed) {
        this.newsFeed = newsFeed;
    }

    public NewsFeed getNewsFeed() {
        return newsFeed;
    }
}