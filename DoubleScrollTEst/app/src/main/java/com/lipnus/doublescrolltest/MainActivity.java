package com.lipnus.doublescrolltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScrollView mScroll1, mScroll2;                        //스크롤 뷰 1, 2
    private TextView mText1, mText2;                            //텍스트 뷰 1, 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScroll1 = (ScrollView)findViewById(R.id.scroll1);        //스크롤뷰 1번
        mScroll1.setOnTouchListener(mListener);                    //터치 이벤트 리스너 등록

        mScroll2 = (ScrollView)findViewById(R.id.scroll2);        //스크롤뷰 2번
        mScroll2.setOnTouchListener(mListener);                    //터치 이벤트 리스너 등록

        mText1 = (TextView)findViewById(R.id.text1);            //텍스트 뷰 1번
        mText2 = (TextView)findViewById(R.id.text2);            //텍스트 뷰 2번

    }

    //스크롤 뷰 터치 이벤트 리스너. 여기서 동기화를 해준다.
    private View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            int id =  v.getId();                    //이벤트 들어온 뷰의 아이디값

            //1번 스크롤 뷰가 터치 되면 2번 스크롤 뷰에 이벤트 전달
            if(id == R.id.scroll1)
                mScroll2.dispatchTouchEvent(event);    //터치 이벤트 전달

            return false;
        }
    };
}
