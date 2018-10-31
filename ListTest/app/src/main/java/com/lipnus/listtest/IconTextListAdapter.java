package com.lipnus.listtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunpil on 2016-03-14.
 */

//BaseAdapter를 상속하여 새로운 어댑터 클래스 정의
public class IconTextListAdapter extends BaseAdapter {
    private Context mContext;

    //각 아이템의 데이터를 담고있는 IconTextItem객체를 저장할 ArrayList객체 생성
    //한칸을 IconTextItem하나씩,그리고 그게 배열로 여러개 있음.
    private List<IconTextItem> mItems = new ArrayList<IconTextItem>();

    //생성자. Context값을 받아오는 역할인듯.
    public IconTextListAdapter(Context context){
        mContext = context;
    }

    public void addItem(IconTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<IconTextItem> lit) {
        mItems = lit;
    }


    //전체 아이템의 개수를 리턴
    public int getCount(){
        return mItems.size();
    }

    //하나의 아이템을 리턴
    public Object getItem(int position) {
        return mItems.get(position); //get은 ArrayList에서 제공하는 매소드
    }

    public long getItemId(int position) {
        return position;
    }

    //아이템에 표시할 뷰 리턴하는 메소드 정의
    public View getView(int position, View convertView, ViewGroup parent){

        IconTextView itemView;


        if(convertView == null){//뷰가 없으면 만듦.(뭘 만드는건지는 모르겠노..)
            itemView = new IconTextView(mContext, mItems.get(position));
        }else{
            itemView = (IconTextView) convertView;
        }

        //IconTextItem으로부터 데이터를 가져와서 매치시킴
        itemView.setIcon(mItems.get(position).getIcon());
        itemView.setText(0, mItems.get(position).getData(0));
        itemView.setText(1, mItems.get(position).getData(1));
        itemView.setText(2, mItems.get(position).getData(2));
        return itemView;
    }
}
