package com.lipnus.eventtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tV = null;

    //GestureDetector객체 선언
    private GestureDetector mGestures = null;

    //이건 액티비티 영역의 onTouchEvent
    public boolean onTouchEvent(MotionEvent event){

        //터치이벤트를 제스쳐와 구분하여 처리(만약 제스처가 있다면)
        if(mGestures != null){
            return mGestures.onTouchEvent(event);
        }
        else{
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView)findViewById(R.id.textView);

        // 텍스트'뷰'를 가지고 있는 레이아웃
        View rootLayout = findViewById(R.id.rootLayout);

        //Gesture Detector객체정의
        mGestures = new GestureDetector( new GestureDetector.SimpleOnGestureListener(){

            //플링
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
                tV.append("\n플링\n\tx속도="+velocityX + "\n\ty속도="+velocityY);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            //스크롤
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
                tV.append("\n스크롤\n\tx거리="+distanceX + "\n\ty거리="+distanceX);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

        });

        //롱클릭(View.java 에 있는 OnLongClickListener인터페이스 재정의) - 500ms
        tV.setOnLongClickListener( new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                tV.append("\n롱클릭:"+v.toString());
                return true;
            }
        });


        //포커스 체인지
        tV.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                //포커스를 가졌을때
                if(hasFocus){
                    tV.append("\n포커스를 가짐");
                }
                else{
                    tV.append("\n포커스를 잃음");
                }
            }
        });

    }
}
