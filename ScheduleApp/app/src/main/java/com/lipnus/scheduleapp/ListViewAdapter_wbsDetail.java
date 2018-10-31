package com.lipnus.scheduleapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class ListViewAdapter_wbsDetail extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem_wbsDetail> listViewItemList = new ArrayList<ListViewItem_wbsDetail>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter_wbsDetail() {}

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

        // "customitem" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customitem, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView filenameTv = (TextView) convertView.findViewById(R.id.listFilenameTv) ;
        TextView commentTv = (TextView) convertView.findViewById(R.id.listCommentTv) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem_wbsDetail listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        filenameTv.setText(listViewItem.getTitle());
        commentTv.setText(listViewItem.getDesc());

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


    // 아이템 데이터 추가를 위한 함수. 원하는대로 작성 가능.
    public void addItem(String filename, String comment) {
        ListViewItem_wbsDetail item = new ListViewItem_wbsDetail();

        item.setTitle(filename);
        item.setDesc(comment);

        listViewItemList.add(item);



        Log.d("FFFF", "어댑터호출: "+filename + " " + comment);

    }
}