package com.lipnus.viewflippertest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Created by Sunpil on 2016-03-11.
 */
public class ScreenViewFlipper_backup extends LinearLayout implements View.OnTouchListener {



    public static int countIndexes = 3;
    LinearLayout buttonLayout;

    ImageView[] indexButtons;
    View[] views;

    ViewFlipper flipper;



    float downX;
    float upX;
    int currentIndex = 0;



    public ScreenViewFlipper_backup(Context context) {
        super(context);
        init(context);
    }

    public ScreenViewFlipper_backup(Context context, AttributeSet attrs) {
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
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;

        indexButtons = new ImageView[countIndexes]; //상단의 버튼들 객체 생성
        views = new TextView[countIndexes];

        //처음 버튼이미지를 추가하는 부분인것 같은데, 이것도 upddateIndex에 넣어버려야지
        //==============================================================================================
        TextView tV = (TextView)findViewById(R.id.showInformationTextView);
        tV.setText("currentIndex="+currentIndex);
        //==============================================================================================
        for(int i=0; i<countIndexes; i++){
            indexButtons[i] = new ImageView(context);

            if(i== currentIndex){
                indexButtons[i].setImageResource(R.drawable.green);
            }
            else{
                indexButtons[i].setImageResource(R.drawable.white);
            }

            indexButtons[i].setPadding(10, 10, 10, 10);
            buttonLayout.addView(indexButtons[i], params);

            TextView curView = new TextView(context);
            curView.setText("View #"+i);
            curView.setTextColor(Color.RED);
            curView.setTextSize(32);
            views[i] = curView; //views객체는 TextView인듯. 그럴거면 TestView로 선언하지 왜 걍 View로 선언했지?

            flipper.addView(views[i]); //플리퍼에 뷰를 추가한다. (추가되는 뷰는 for문에 의해 만드러진 3개의 TextView이다.)
        }
        //==============================================================================================
    }

    private void updateIndexes(){

        //==============================================================================================
        TextView tV = (TextView)findViewById(R.id.showInformationTextView);
        tV.setText("currentIndex="+currentIndex);
        //==============================================================================================

        for(int i=0; i<countIndexes; i++){
            if(i==currentIndex){
                Toast.makeText(getContext(), currentIndex+"번째를 초록으로!", Toast.LENGTH_LONG).show();
                indexButtons[i].setImageResource(R.drawable.green);
            }else{
                indexButtons[i].setImageResource(R.drawable.white);
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event){

        //뷰플리퍼에 터치한 것만 받겠다는 강력한 의지
        if(v != flipper){
            return false;
        }

        //뷰 플리퍼에 손을 댈때랑 땔때의 위치를 기억
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.wallpaper_open_exit));
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){

            upX = event.getX();

            if(upX < downX){ //오른쪽에서 왼쪽으로 문질 (오른쪽으로 이동)

                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.wallpaper_open_enter));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.wallpaper_open_exit));

                if(currentIndex < (countIndexes-1)){
                    flipper.showNext();
                    currentIndex++;
                    updateIndexes(); //초록불 위치 업데이트
                }
            }
            else if(upX > downX){ //왼쪽에서 오른쪽으로 문질 (왼쪽으로 이동)
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
