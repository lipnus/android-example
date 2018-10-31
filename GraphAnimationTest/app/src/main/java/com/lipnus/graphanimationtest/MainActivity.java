package com.lipnus.graphanimationtest;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    Resources res;
    Animation growAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        growAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        addItem("Apple", 80);
        addItem("Orange", 100);
        addItem("Kiwi", 40);
    }

    //JAVA를 이용해 레이아웃을 형성
    private void addItem(String name, int value){
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        //텍스트뷰
        TextView textView = new TextView(this);
        textView.setText(name);
        params1.width=180;
        params1.setMargins(0, 4, 0, 4);
        itemLayout.addView(textView, params1);

        //프로그래스바
        ProgressBar proBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        proBar.setIndeterminate(false);
        proBar.setMax(100);
        proBar.setProgress(100);
        proBar.setAnimation(growAnim);
        params2.height=80;
        params2.width = value * 3;
        params2.gravity = Gravity.LEFT;
        itemLayout.addView(proBar,params2);

        mainLayout.addView(itemLayout, params3);
    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        Toast.makeText(this, "onWindowFocusChanged: "+hasFocus, Toast.LENGTH_LONG).show();

        if(hasFocus){
            growAnim.start();
        } else{
            growAnim.reset();
        }
    }
}
