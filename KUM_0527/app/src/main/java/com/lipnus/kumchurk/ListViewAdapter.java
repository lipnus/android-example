package com.lipnus.kumchurk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // menulist.xml을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menulist, parent, false);
        }

        //------------------------------------------------------------------------------------------
        // 화면에 표시될 View의 구성요소
        //------------------------------------------------------------------------------------------
        FrameLayout left_menu_fL = (FrameLayout) convertView.findViewById(R.id.left_menu_FrameLayout);
        ImageView left_menu_Iv = (ImageView) convertView.findViewById(R.id.left_menu_Iv);
        TextView left_menu_name_Tv = (TextView) convertView.findViewById(R.id.left_menu_nameTv);
        TextView left_menu_cost_Tv = (TextView) convertView.findViewById(R.id.left_menu_costTv);

        FrameLayout right_menu_fL = (FrameLayout) convertView.findViewById(R.id.right_menu_FrameLayout);
        ImageView right_menu_Iv = (ImageView) convertView.findViewById(R.id.right_menu_Iv);
        TextView right_menu_name_Tv = (TextView) convertView.findViewById(R.id.right_menu_nameTv);
        TextView right_menu_cost_Tv = (TextView) convertView.findViewById(R.id.right_menu_costTv);

        LinearLayout category_LL = (LinearLayout) convertView.findViewById(R.id.category_menu_LinearLayout);
        TextView category_Tv = (TextView) convertView.findViewById(R.id.category_menuTv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------
        left_menu_fL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewItem listViewItem = listViewItemList.get(pos);
                Log.d("CLCL", "여기가 어디냐: " + listViewItem.getLeft_menu_name() );
                Log.d("CLCL", "식당: " +  listViewItem.getRes_name() + " 메뉴: "+ listViewItem.getLeft_menu_name());

                Intent iT = new Intent(context, ScrollActivity.class); // 다음넘어갈 화면
                iT.putExtra("res_name", listViewItem.getRes_name());
                iT.putExtra("menu_name", listViewItem.getLeft_menu_name());
                context.startActivity(iT);


                //ScrollActivity가 Task에 쌓이지 않게 하기 위해
                //이 리스트가 위치한건 ScrollActivity지만 여기는 액티비티가 아니라서 피니쉬할라면 이렇게 귀찮게 해야함
                ScrollActivity scActivity = (ScrollActivity)ScrollActivity.SCActivity;
                scActivity.finish();

            }
        });

        right_menu_fL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewItem listViewItem = listViewItemList.get(pos);
                Log.d("CLCL", "여기가 어디냐: " + listViewItem.getRight_menu_name() );

                Intent iT = new Intent(context, ScrollActivity.class); // 다음넘어갈 화면
                iT.putExtra("res_name", listViewItem.getRes_name());
                iT.putExtra("menu_name", listViewItem.getRight_menu_name());
                context.startActivity(iT);

                //자폭
                ScrollActivity scActivity = (ScrollActivity)ScrollActivity.SCActivity;
                scActivity.finish();
            }
        });

        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        //------------------------------------------------------------------------------------------

        //좌측부분
        if( listViewItem.isLeft_menu_visible() ){ left_menu_fL.setVisibility(View.VISIBLE); }
        else{ left_menu_fL.setVisibility(View.GONE); }
        left_menu_name_Tv.setText( listViewItem.getLeft_menu_name() );
        left_menu_cost_Tv.setText( listViewItem.getLeft_menu_cost() );
        Glide.with(context)
                .load( listViewItem.getLeft_menu_image() )
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(left_menu_Iv);
        left_menu_Iv.setScaleType(ImageView.ScaleType.FIT_XY);


        //우측부분
        if( listViewItem.isRight_menu_visible() ){ right_menu_fL.setVisibility(View.VISIBLE); }
        else{ right_menu_fL.setVisibility(View.GONE); }
        right_menu_name_Tv.setText( listViewItem.getRight_menu_name() );
        right_menu_cost_Tv.setText( listViewItem.getRight_menu_cost() );
        Glide.with(context)
                .load( listViewItem.getRight_menu_image() )
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(right_menu_Iv);
        right_menu_Iv.setScaleType(ImageView.ScaleType.FIT_XY);


        //카테고리

        if( listViewItem.isCategory_visible() ){
            category_LL.setVisibility(View.VISIBLE);
        }
        else{ category_LL.setVisibility(View.GONE); }
        category_Tv.setText( listViewItem.getCategory_name() );

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
    public void addItem(boolean lvisible, String lname, String lcost, String lPic,
                        boolean rvisible, String rname, String rcost, String rPic,
                        boolean category_visible, String category_name,
                        String res_name) {

        ListViewItem item = new ListViewItem();

        item.setLeft_menu_visible(lvisible);
        item.setLeft_menu_name(lname);
        item.setLeft_menu_cost(lcost);
        item.setLeft_menu_image(lPic);
        Log.d("IMAGE_CONTROL", "addItem: "+lPic);

        item.setRight_menu_visible(rvisible);
        item.setRight_menu_name(rname);
        item.setRight_menu_cost(rcost);
        item.setRight_menu_image(rPic);

        item.setCategory_visible(category_visible);
        item.setCategory_name(category_name);

        item.setRes_name(res_name);

        listViewItemList.add(item);
    }
}