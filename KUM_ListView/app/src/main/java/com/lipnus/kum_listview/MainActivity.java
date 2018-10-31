package com.lipnus.kum_listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.menu_listview);
        listview.setAdapter(adapter);

        //리스트뷰에 추가
        adapter.addItem(true, "로제 스파게티", "S:9.9  L:18.0", null,
                true, "해산물 토마토 스파게티", "S:9.9  L:18.0", null,
                false, "", "", null);

        adapter.addItem(true, "롸졔 스파게티", "S:9.9  L:18.0", null,
                true, "해산물 토마토 스파게티", "S:9.9  L:18.0", null,
                false, "", "", null);

        adapter.addItem(false, "", "", null,
                false, "", "", null,
                true, "까르보나라 해산물 스파게티", "S:9.9  L:18.0", null);

        adapter.addItem(true, "료줴 스파게티", "S:9.9  L:18.0", null,
                true, "해산물 토마토 스파게티", "S:9.9  L:18.0", null,
                false, "", "", null);

        adapter.notifyDataSetChanged();

    }
}
