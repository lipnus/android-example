package com.lipnus.animationtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Animation flowAnim = null; //애니매이션 객체 선언
    TextView textView1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView)findViewById(R.id.textView1);

        //애니메이션 액션정보 로딩
        flowAnim = AnimationUtils.loadAnimation(this, R.anim.flow);
    }

    //애니매이션이 뭘 하고 있는지 엳듣고 있다.
    public void onButton1Clicked(View v){
        flowAnim.setAnimationListener( new FlowAnimationListener()); //애니메이션 리스너 설정
        textView1.startAnimation(flowAnim);
    }

    public final class FlowAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationEnd(Animation animation){
            Toast.makeText(getApplicationContext(), "애니매이션 종료", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAnimationRepeat(Animation animation){

        }

        @Override
        public void onAnimationStart(Animation animation){

        }
    }




}
