package com.lipnus.kum_listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

        FrameLayout middle_menu_fL = (FrameLayout) convertView.findViewById(R.id.middle_menu_FrameLayout);
        ImageView middle_menu_Iv = (ImageView) convertView.findViewById(R.id.middle_menu_Iv);
        TextView middle_menu_name_Tv = (TextView) convertView.findViewById(R.id.middle_menu_nameTv);
        TextView middle_menu_cost_Tv = (TextView) convertView.findViewById(R.id.middle_menu_costTv);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        //------------------------------------------------------------------------------------------
        // 터치 이벤트(뭐 이런식으로 하면 될듯)
        //------------------------------------------------------------------------------------------
        left_menu_fL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewItem listViewItem = listViewItemList.get(pos);
                Log.d("SBSB", "내위치: " + listViewItem.getLeft_menu_name() );
            }
        });

        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        // (이미지 경로는 반영되어 있지 않고 일단 Drawable의 것을 가져다 쓴다)
        //------------------------------------------------------------------------------------------
        if( listViewItem.isLeft_menu_visible() ){ left_menu_fL.setVisibility(View.VISIBLE); }
        else{ left_menu_fL.setVisibility(View.GONE); }
        left_menu_name_Tv.setText( listViewItem.getLeft_menu_name() );
        left_menu_cost_Tv.setText( listViewItem.getLeft_menu_cost() );
        left_menu_Iv.setBackgroundResource(R.drawable.foodmenu1);

        if( listViewItem.isRight_menu_visible() ){ right_menu_fL.setVisibility(View.VISIBLE); }
        else{ right_menu_fL.setVisibility(View.GONE); }
        right_menu_name_Tv.setText( listViewItem.getRight_menu_name() );
        right_menu_cost_Tv.setText( listViewItem.getRight_menu_cost() );
        right_menu_Iv.setBackgroundResource(R.drawable.foodmenu2);

        if( listViewItem.isMiddle_menu_visible() ){ middle_menu_fL.setVisibility(View.VISIBLE);}
        else{ middle_menu_fL.setVisibility(View.GONE); }
        middle_menu_name_Tv.setText( listViewItem.getMiddle_menu_name() );
        middle_menu_cost_Tv.setText( listViewItem.getMiddle_menu_cost() );
        middle_menu_Iv.setBackgroundResource(R.drawable.foodmenu5);

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
    public void addItem(boolean lvisible, String lname, String lcost, Bitmap lPic,
                        boolean rvisible, String rname, String rcost, Bitmap rPic,
                        boolean mvisible, String mname, String mcost, Bitmap mPic) {

        ListViewItem item = new ListViewItem();

        item.setLeft_menu_visible(lvisible);
        item.setLeft_menu_name(lname);
        item.setLeft_menu_cost(lcost);
//      item.setLeft_menu_image(lPic);

        item.setRight_menu_visible(rvisible);
        item.setRight_menu_name(rname);
        item.setRight_menu_cost(rcost);
//      item.setLeft_menu_image(rPic);

        item.setMiddle_menu_visible(mvisible);
        item.setMiddle_menu_name(mname);
        item.setMiddle_menu_cost(mcost);
//      item.setLeft_menu_image(mPic);

        listViewItemList.add(item);
    }
}