package com.lipnus.kumchurk.submenu;

import com.lipnus.kumchurk.data.Review;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class MyReviewListViewItem {

    private Review review;

    public Review getReview() {
        return review;
    }

    public MyReviewListViewItem(Review review) {
        this.review = review;
    }
}