package com.lipnus.listviewtest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("Box", "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        adapter.addItem("Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem("Ind", "Assignment Ind Black 36dp") ;

        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다") ;
        adapter.addItem("파일명이 요로코롬 길다면?.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;
        adapter.addItem("파일명.pptx", "수고했다 시발놈아") ;



    }


}

