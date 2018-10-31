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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lipnus.kumchurk.detailpage.ReviewWrtieActivity;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.ScrollActivity;
import com.lipnus.kumchurk.data.MenuRes_Info;
import com.lipnus.kumchurk.kum_class.SimpleFunction;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class SearchListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SearchListViewItem> listViewItemList = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public SearchListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_search, parent, false);
        }


        //------------------------------------------------------------------------------------------
        // 화면에 표시될 View의 구성요소
        //------------------------------------------------------------------------------------------

        LinearLayout searchLr = (LinearLayout) convertView.findViewById(R.id.sl_lr);
        ImageView foodIv = (ImageView) convertView.findViewById(R.id.sl_foodIv);
        TextView menuTv = (TextView) convertView.findViewById(R.id.sl_menu_nameTv);
        TextView resTv = (TextView) convertView.findViewById(R.id.sl_res_nameTv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchListViewItem listViewItem = listViewItemList.get(position);


        //------------------------------------------------------------------------------------------
        // 데이터 정리
        //------------------------------------------------------------------------------------------

        MenuRes_Info mrI = listViewItem.getMrI();

        String menu_name = mrI.getMenu_name();
        String resName = mrI.getRes_name();

        //가격
        String price = SimpleFunction.displayPrice( mrI.getMenu_price(), 0, 0);
        if(mrI.getMenu_price2() != 0){
            price = price + "~";
        }




        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------
        searchLr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //이 스코프 안에는 범위가 안먹혀서 제일 위의 전역변수를 사용해서 다시 객체를 만들어줌
                SearchListViewItem listViewItem = listViewItemList.get(position);
                MenuRes_Info mrI = listViewItem.getMrI();


                String res_name = mrI.getRes_name();
                String menu_name = mrI.getMenu_name();

                //SearchActivity에서 쓰이는 경우 메뉴정보로 이동
                if(listViewItem.getCallFrom().equals("search")){
                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    context.startActivity(iT);
                }

                //ReviewAcitivity에서 쓰이는 경우 리뷰쓰기로 이동
                else if(listViewItem.getCallFrom().equals("review")){
                    Intent iT = new Intent(context, ReviewWrtieActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    iT.putExtra("callFrom", "ReviewSearchActivity");
                    context.startActivity(iT);


                    //지금 창은 끈다
                    ReviewSearchActivity rvActivity = (ReviewSearchActivity) ReviewSearchActivity.RVActivity;
                    rvActivity.finish();
                }



            }
        });



        //------------------------------------------------------------------------------------------
        // 아이템 내 각 위젯에 데이터 반영
        //------------------------------------------------------------------------------------------


        menuTv.setText(menu_name + " " + price);
        resTv.setText(resName);

        //메뉴이미지
        Glide.with(context)
                .load( mrI.getMenu_image() )
                .bitmapTransform(new CenterCrop(context) ,new CropCircleTransformation(context))
                .placeholder( R.drawable.appicon )
                .into(foodIv);
        foodIv.setScaleType(ImageView.ScaleType.FIT_XY);

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
    public void addItem(MenuRes_Info mrI, String cF) {

        SearchListViewItem item = new SearchListViewItem(mrI, cF);
        listViewItemList.add(item);
    }
}