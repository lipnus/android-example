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
import com.lipnus.kumchurk.ScrollActivity;
import com.lipnus.kumchurk.data.NewsFeed;
import com.lipnus.kumchurk.detailpage.CommentActivity;
import com.lipnus.kumchurk.detailpage.ProfileImgActivity;
import com.lipnus.kumchurk.kum_class.SimpleFunction;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class NewsFeedListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<NewsFeedListViewItem> listViewItemList = new ArrayList<NewsFeedListViewItem>() ;


    // ListViewAdapter의 생성자
    public NewsFeedListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_newsfeed, parent, false);
        }


        //------------------------------------------------------------------------------------------
        // 화면에 표시될 View의 구성요소
        //------------------------------------------------------------------------------------------
        LinearLayout topLr = (LinearLayout) convertView.findViewById(R.id.nf_topMarginLr);

        TextView menuNameTv = (TextView) convertView.findViewById(R.id.nf_menu_nameTv);
        TextView resNameTv = (TextView) convertView.findViewById(R.id.nf_res_nameTv);
        ImageView menuImgIv = (ImageView) convertView.findViewById(R.id.nf_menu_img_Iv);
        ImageView faceIv = (ImageView) convertView.findViewById(R.id.nf_faceIv);
        TextView nicknameTv = (TextView) convertView.findViewById(R.id.nf_nicknameTv);

        LinearLayout heartLr = (LinearLayout) convertView.findViewById(R.id.nf_heart_Lr);
        LinearLayout fuckLr = (LinearLayout) convertView.findViewById(R.id.nf_fuck_Lr);
        LinearLayout commentLr = (LinearLayout) convertView.findViewById(R.id.nf_comment_Lr);

        ImageView heartIv = (ImageView) convertView.findViewById(R.id.nf_heart_iv);
        TextView heartTv = (TextView) convertView.findViewById(R.id.nf_heart_tv);
        ImageView fuckIv = (ImageView) convertView.findViewById(R.id.nf_fuck_iv);
        TextView fuckTv = (TextView) convertView.findViewById(R.id.nf_fuck_tv);
        ImageView commentIv = (ImageView) convertView.findViewById(R.id.nf_comment_iv);
        TextView commentTv = (TextView) convertView.findViewById(R.id.nf_comment_tv);

        TextView dateTv = (TextView) convertView.findViewById(R.id.nf_date_Tv);
        TextView reviewTv = (TextView) convertView.findViewById(R.id.nf_review_Tv);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        NewsFeedListViewItem listViewItem = listViewItemList.get(position);



        //------------------------------------------------------------------------------------------
        // 데이터 정리
        //------------------------------------------------------------------------------------------
        NewsFeed nf = listViewItem.getNewsFeed(); //여기서 선언하면 클릭리스너 안에 안먹혀서 밖에다가 선언

        //가격
        String price = SimpleFunction.displayPrice( nf.getPrice(), 0, 0);
        if(nf.getPrice2() != 0){
            price = price + "~";
        }

        //거리
        String distance="";
        int dis = SimpleFunction.distance(nf.getLatitude(), nf.getLongitude(), "meter");

        if(dis!=-1){//gps가 제대로 구해지지 않으면 -1을 리턴한다(SimpleFunction클래스 참조)
            distance = dis + "m";
        }

        //날짜
        String date = nf.getUpdated_at();
        date = SimpleFunction.timeGap(date);

        //하트와 빠큐 상태설정
        int heartPath;
        int fuckPath;

        if( nf.getVote_heart() == 1){
            heartPath = R.drawable.small_menu_heart2;
        }else{
            heartPath = R.drawable.small_menu_heart;
        }

        if( nf.getVote_fuck() == 1){
            fuckPath = R.drawable.small_menu_fuck2;
        }else{
            fuckPath = R.drawable.small_menu_fuck;
        }


        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------

        //하트터치
        heartLr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //클릭리스너 안에는 범위가 안먹혀서 다시 선언
                NewsFeedListViewItem listViewItem = listViewItemList.get(position);
                NewsFeed nf = listViewItem.getNewsFeed();

                NewsFeedActivity nfActivity = (NewsFeedActivity) NewsFeedActivity.NFActiviry;

                //하트 있을경우 -1
                if(nf.getVote_heart() == 1){
                    nfActivity.connect_vote(position, nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), -1, 0);
                }

                //하트만 +1
                else if(nf.getVote_heart() == 0 && nf.getVote_fuck() == 0){
                    nfActivity.connect_vote(position,nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 1, 0);
                }

                //빠큐 했던건 없에고, 하트 추가
                else if(nf.getVote_heart() == 0 && nf.getVote_fuck() == 1){
                    nfActivity.connect_vote(position,nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 1, -1);
                }

                //오류가 나서 이상한 값이 들어있는 경우
                else{
//                    nfActivity.connect_vote(position,nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 0, 0);
                }
            }
        });


        //빠큐터치
        fuckLr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //클릭리스너 안에는 범위가 안먹혀서 다시 선언
                NewsFeedListViewItem listViewItem = listViewItemList.get(position);
                NewsFeed nf = listViewItem.getNewsFeed();

                NewsFeedActivity nfActivity = (NewsFeedActivity) NewsFeedActivity.NFActiviry;

                //빠큐만 -1
                if(nf.getVote_fuck() == 1){
                    nfActivity.connect_vote(position, nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 0, -1);
                }

                //빠큐만 +1
                else if(nf.getVote_heart() == 0 && nf.getVote_fuck() == 0 ){
                    nfActivity.connect_vote(position, nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 0, 1);
                }

                //하트 없에고 빠큐만 +1
                else if(nf.getVote_heart() == 1 && nf.getVote_fuck() == 0){
                    nfActivity.connect_vote(position, nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), -1, 1);
                }

                //오류가 나서 이상한 값이 들어있는 경우
                else{
//                    nfActivity.connect_vote(position, nf.getUser_id(), nf.getMenu_name(), nf.getRes_name(), nf.getNo(), 0, 0);
                }

            }
        });

        //댓글터치
        commentLr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //클릭리스너 안에는 범위가 안먹혀서 다시 선언
                NewsFeedListViewItem listViewItem = listViewItemList.get(position);
                NewsFeed nf = listViewItem.getNewsFeed();

                String review_num = Integer.toString(nf.getNo());

                Intent iT = new Intent(context, CommentActivity.class);
                iT.putExtra("review_num", review_num);
                iT.putExtra("list_position", position);
                context.startActivity(iT);
            }
        });

        //메뉴사진클릭
        menuImgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //클릭리스너 안에는 범위가 안먹혀서 다시 선언
                NewsFeedListViewItem listViewItem = listViewItemList.get(position);
                NewsFeed nf = listViewItem.getNewsFeed();

                Intent iT = new Intent(context, ScrollActivity.class);
                iT.putExtra("res_name", nf.getRes_name() );
                iT.putExtra("menu_name", nf.getMenu_name() );
                context.startActivity(iT);


            }
        });


        //프사클릭
        faceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //클릭리스너 안에는 범위가 안먹혀서 다시 선언
            NewsFeedListViewItem listViewItem = listViewItemList.get(position);
            NewsFeed nf = listViewItem.getNewsFeed();

            Intent iT = new Intent(context, ProfileImgActivity.class);
            iT.putExtra("nickname", nf.getUser_nickname() );
            iT.putExtra("image_path", nf.getUser_profile() );
            context.startActivity(iT);

            }
        });


        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        //------------------------------------------------------------------------------------------
        if(position==0){
            //가장 첫페이지는 위쪽에 여백을 띄워줌
            topLr.setVisibility(View.VISIBLE);
        }

        menuNameTv.setText( nf.getMenu_name()+ " " + price);
        resNameTv.setText( nf.getRes_name() + ", " +distance );
        nicknameTv.setText( nf.getUser_nickname() );
        heartTv.setText( Integer.toString( nf.getHeart() ) );
        fuckTv.setText( Integer.toString( nf.getFuck() ) );
        commentTv.setText( Integer.toString( nf.getComment() ) );
        dateTv.setText( date );
        reviewTv.setText( nf.getMemo() );

        //메뉴이미지
        Glide.with(context)
                .load( nf.getMenu_image() )
                .centerCrop()
                .thumbnail(0.3f)
                .into(menuImgIv);
        menuImgIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //프사
        Glide.with(context)
                .load( nf.getUser_thumbnail() )
                .placeholder(R.drawable.face_basic)
                .bitmapTransform(new CropCircleTransformation(context))
                .thumbnail(0.3f)
                .into(faceIv);
        faceIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //하트
        Glide.with(context)
                .load( heartPath )
                .centerCrop()
                .into(heartIv);

        //빠큐
        Glide.with(context)
                .load( fuckPath )
                .centerCrop()
                .into(fuckIv);

        //댓글
        Glide.with(context)
                .load( R.drawable.small_menu_comment )
                .centerCrop()
                .into(commentIv);

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
    public void addItem(NewsFeed nf) {

        NewsFeedListViewItem item = new NewsFeedListViewItem( nf );
        listViewItemList.add(item);
    }
}