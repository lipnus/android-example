package com.asha.md360player4android.Scene;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.asha.md360player4android.R;
import com.bumptech.glide.Glide;

public class MainList extends AppCompatActivity {


    //리스트뷰와 어댑터
    ListView listview;
    ListViewAdapter adapter;

    ImageView topmenuIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topmenuIv = (ImageView) findViewById(R.id.top_menu_iv);

        //상단부분 메뉴 설정
        Glide.with(this)
                .load( R.drawable.topmenu )
                .into(topmenuIv);
        topmenuIv.setScaleType(ImageView.ScaleType.FIT_XY);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        //리스트뷰와 어댑터
        listview = (ListView) findViewById(R.id.movie_list);
        listview.setAdapter(adapter);

        adapter.addItem(R.drawable.movie1);
        adapter.addItem(R.drawable.movie2);
        adapter.addItem(R.drawable.movie3);
        adapter.addItem(R.drawable.movie4);
        adapter.addItem(R.drawable.movie5);
        adapter.addItem(R.drawable.movie6);
        adapter.addItem(R.drawable.movie7);
        adapter.addItem(R.drawable.movie8);
        adapter.addItem(R.drawable.movie9);
        adapter.addItem(R.drawable.movie10);
        adapter.notifyDataSetChanged();
    }
}
