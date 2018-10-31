package com.lipnus.kumchurk.submenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.data.Review;
import com.lipnus.kumchurk.detailpage.DetailReveiwActivity;

import java.util.ArrayList;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class MyReviewListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MyReviewListViewItem> listViewItemList = new ArrayList<MyReviewListViewItem>() ;


    // ListViewAdapter의 생성자
    public MyReviewListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // menulist.xml을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_myreview, parent, false);
        }


        //------------------------------------------------------------------------------------------
        // 화면에 표시될 View의 구성요소
        //------------------------------------------------------------------------------------------
        LinearLayout rvLr = (LinearLayout) convertView.findViewById(R.id.rv_lr);
        ImageView reviewIv = (ImageView) convertView.findViewById(R.id.rv_review_iv);
        TextView menuResNameTv = (TextView) convertView.findViewById(R.id.rv_menu_res_name_tv);
        TextView infoTv = (TextView) convertView.findViewById(R.id.rv_info_tv);
        ImageView moreIv = (ImageView) convertView.findViewById(R.id.rv_more_iv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MyReviewListViewItem listViewItem = listViewItemList.get(position);



        //------------------------------------------------------------------------------------------
        // 데이터 정리
        //------------------------------------------------------------------------------------------
        Review rv = listViewItem.getReview(); //여기서 선언하면 클릭리스너 안에 안먹혀서 밖에다가 선언

        //메뉴이름
        String menuName = rv.getMenu_name();
        String resName = rv.getRes_name();
        String fuck = rv.getReview_fuck();
        String heart= rv.getReview_heart();
        String comment = rv.getReview_comment();


        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------

        //내가 올린 리뷰 클릭
        rvLr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyReviewListViewItem listViewItem = listViewItemList.get(position);
                Review rv = listViewItem.getReview();

                Intent iT = new Intent(context, DetailReveiwActivity.class);
                iT.putExtra("review_num", rv.getReview_num());
                context.startActivity(iT);

            }

        });

        //삭제
        moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyReviewListViewItem listViewItem = listViewItemList.get(position);
                Review rv = listViewItem.getReview();
                MyReviewActivity rvActivity = (MyReviewActivity) MyReviewActivity.RVActiviry;

                String reviewNum = rv.getReview_num();
                String menuName = rv.getMenu_name();
                String resName = rv.getRes_name();

                //MyReviewActivity의 메소드를 호출
                rvActivity.deleteReview( reviewNum, menuName, resName );

            }

        });



        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        //------------------------------------------------------------------------------------------

        menuResNameTv.setText(resName + "의 " + menuName);
        infoTv.setText("댓글:" + rv.getReview_comment() + "  좋아요:" + rv.getReview_heart());

        //내 리뷰사진
        Glide.with(context)
                .load( rv.getReview_image() )
                .centerCrop()
                .into(reviewIv);
        reviewIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //삭제하기
        Glide.with(context)
                .load( R.drawable.more )
                .into(moreIv);
        moreIv.setScaleType(ImageView.ScaleType.FIT_XY);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }


    // 아이템 데이터 추가
    public void addItem(Review rv) {

        MyReviewListViewItem item = new MyReviewListViewItem( rv );
        listViewItemList.add(item);
    }
}