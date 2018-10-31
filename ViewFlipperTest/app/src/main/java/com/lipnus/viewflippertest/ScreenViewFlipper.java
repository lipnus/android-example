package com.lipnus.viewflippertest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 * Created by Sunpil on 2016-03-11.
 */
public class ScreenViewFlipper extends LinearLayout implements View.OnTouchListener {

    public static int countIndexes = 3;

    ImageView iV;
    View[] views;

    ViewFlipper flipper;



    float downY;
    float upY;
    int currentIndex = 0;



    public ScreenViewFlipper(Context context) {
        super(context);
        init(context);
    }

    public ScreenViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //이것은 무엇인고...??
    public void init(Context context){

        setBackgroundColor(0xffbfbfbf); //LinearLayout이라 배경색 지정이 가능한 것인가?

        //뷰플리퍼화면을 위한 레이아웃인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.screenview, this, true);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setOnTouchListener(this); //뷰플리퍼객체에 터치리스너 설정(뷰가 개인적으로 가진 리스너를 이용하는 방법인듯.)

        //레이아웃 설정하는 부분인가..?
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;

        //==============================================================================================

        iV = new ImageView(context);
        iV.setPadding(0, 0, 0, 0);
        flipper.addView(iV, params);
        //==============================================================================================
    }

    private void updateIndexes(){

        if(currentIndex == 1){
            iV.setImageResource(R.drawable.main1);
        }else if(currentIndex == 2){
            iV.setImageResource(R.drawable.main2);
        }else if(currentIndex == 3){
            iV.setImageResource(R.drawable.main3);
        }

    }

    public boolean onTouch(View v, MotionEvent event){

        //뷰플리퍼에 터치한 것만 받겠다는 강력한 의지
        if(v != flipper){
            return false;
        }

        //뷰 플리퍼에 손을 댈때랑 땔때의 위치를 기억
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downY = event.getY();
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){

            upY = event.getY();

            if(upY < downY){ //오른쪽에서 왼쪽으로 문질 (오른쪽으로 이동)

                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.wallpaper_open_enter));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.wallpaper_open_exit));

                if(currentIndex < (countIndexes-1)){
                    flipper.showNext();
                    currentIndex++;
                    updateIndexes(); //초록불 위치 업데이트
                }
            }
            else if(upY > downY){ //왼쪽에서 오른쪽으로 문질 (왼쪽으로 이동)
                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));

                if(currentIndex > 0){
                    flipper.showPrevious();
                    currentIndex--;
                    updateIndexes();
                }
            }
        }

        return true; //터치이벤트를 처리한 것을 알림.
    }
}
