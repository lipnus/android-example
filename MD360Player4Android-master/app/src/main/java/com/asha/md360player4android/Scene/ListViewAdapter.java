package com.asha.md360player4android.Scene;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.asha.md360player4android.R;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
            convertView = inflater.inflate(R.layout.list_menu, parent, false);
        }



        ImageView movieImage = (ImageView) convertView.findViewById(R.id.movielist_iv);
        ListViewItem listViewItem = listViewItemList.get(position);

        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iT = new Intent(context, MovieInfoActivity.class);
                context.startActivity(iT);

            }
        });

        //------------------------------------------------------------------------------------------
        // 데이터 반영
        //------------------------------------------------------------------------------------------
        Glide.with(context)
                .load( listViewItem.imgPath )
                .into(movieImage);
        movieImage.setScaleType(ImageView.ScaleType.FIT_XY);

        YoYo.with(Techniques.SlideInLeft)
                .duration(600)
                .playOn( movieImage );


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }



    // 아이템 데이터 추가(이미지 하나가 끝)
    public void addItem(int imgPath) {

        ListViewItem item = new ListViewItem();
        item.imgPath = imgPath;
        listViewItemList.add(item);

        Log.d("SSSS", "addItem");
    }



    // 아이템 데이터 삭제
    public void removeItem(int position){
        listViewItemList.remove(position);
    }

    //아이템 전체 삭제
    public void removeAllItem(){
        listViewItemList.clear();
    }


}