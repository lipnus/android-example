package com.lipnus.kumchurk.submenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.data.Alarm;
import com.lipnus.kumchurk.detailpage.DetailReveiwActivity;
import com.lipnus.kumchurk.detailpage.ProfileImgActivity;
import com.lipnus.kumchurk.kum_class.SimpleFunction;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class AlarmListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<AlarmListViewItem> listViewItemList = new ArrayList<AlarmListViewItem>() ;


    // ListViewAdapter의 생성자
    public AlarmListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_alarm, parent, false);
        }


        //------------------------------------------------------------------------------------------
        // 화면에 표시될 View의 구성요소
        //------------------------------------------------------------------------------------------
        ImageView profileIv = (ImageView) convertView.findViewById(R.id.al_profile_iv);
        TextView noticeTv = (TextView) convertView.findViewById(R.id.al_notice_tv);
        TextView timeTv = (TextView) convertView.findViewById(R.id.al_time_tv);
        ImageView myReviewIv = (ImageView) convertView.findViewById(R.id.al_myreview_iv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        AlarmListViewItem listViewItem = listViewItemList.get(position);



        //------------------------------------------------------------------------------------------
        // 데이터 정리
        //------------------------------------------------------------------------------------------
        Alarm al = listViewItem.getAlarmData(); //여기서 선언하면 클릭리스너 안에 안먹혀서 밖에다가 선언

        //좋아요 누른사람 정보
        String nickname = al.getGiver_nickname();
        String updateTime = SimpleFunction.timeGap( al.getGiver_updated_at() ); //현재시간과의 차이


        //메뉴이름
        String menuName = al.getMy_menu_name();

        String notice;

        if(al.getGiver_comment().equals("0")){
            notice = nickname + "님이 회원님의 " + menuName + " 리뷰를 좋아합니다";

        }else{
            notice = nickname + "님의 댓글: " + al.getGiver_comment().toString();
        }



        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------

        //프사클릭
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlarmListViewItem listViewItem = listViewItemList.get(position);
                Alarm al = listViewItem.getAlarmData();

                Intent iT = new Intent(context, ProfileImgActivity.class);
                iT.putExtra("nickname", al.getGiver_nickname() );
                iT.putExtra("image_path", al.getGiver_image() );
                context.startActivity(iT);

            }

        });

        //내가 올린 리뷰 클릭
        myReviewIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlarmListViewItem listViewItem = listViewItemList.get(position);
                Alarm al = listViewItem.getAlarmData();

                Intent iT = new Intent(context, DetailReveiwActivity.class);
                iT.putExtra("review_num", al.getReview_num());
                context.startActivity(iT);

            }

        });



        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        //------------------------------------------------------------------------------------------

        noticeTv.setText(notice);
        timeTv.setText(updateTime);

        //프사
        Glide.with(context)
                .load( al.getGiver_thumbnail() )
                .placeholder(R.drawable.face_basic)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(profileIv);
        profileIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //내가 올린 리뷰
        Glide.with(context)
                .load( al.getMy_review_image() )
                .thumbnail(0.3f)
                .into(myReviewIv);
        myReviewIv.setScaleType(ImageView.ScaleType.FIT_XY);



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
    public void addItem(Alarm al) {

        AlarmListViewItem item = new AlarmListViewItem( al );
        listViewItemList.add(item);
    }
}