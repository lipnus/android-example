package com.lipnus.pd4;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OrderActivity2 extends BaseActivity {

    HorizontalScrollView scroll;
    ImageView planeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);

        scroll = (HorizontalScrollView) findViewById(R.id.scroll_plane);

        //스크롤을 추적한다
        if(Build.VERSION.SDK_INT >=23 ) {
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    scrollChanged();
                }
            });
        }

        //비행기가 중간에 오게..
        scroll.post(new Runnable() {
            public void run() {
                scroll.scrollTo(520,0);
            }
        });

        init_basic();
        setting_navi();
        plane_setting();


    }


    @Override
    public void setting_navi() {
        //네비게이션 버튼 객체들 등록
        naveMenu1 = (ImageView) findViewById(R.id.navi_menu1);
        naveMenu2 = (ImageView) findViewById(R.id.navi_menu2);
        naveMenu3 = (ImageView) findViewById(R.id.navi_menu3);
        naveMenu4 = (ImageView) findViewById(R.id.navi_menu4);
        naviGradiation = (ImageView) findViewById(R.id.navi_gardation_iv);

        super.setting_navi();
    }

    public void init_basic(){


        Glide.with(this)
                .load(R.drawable.purchase)
                .into( (ImageView) findViewById(R.id.purchase_title) );

        Glide.with(this)
                .load(R.drawable.receipt)
                .into( (ImageView) findViewById(R.id.purchase_receipt) );

        Glide.with(this)
                .load(R.drawable.card)
                .into( (ImageView) findViewById(R.id.purchase_card) );

        Glide.with(this)
                .load(R.drawable.carddot)
                .into( (ImageView) findViewById(R.id.purchase_dot) );

    }

    public void plane_setting(){
        planeIv = (ImageView) findViewById(R.id.plane_iv);

        Glide.with(this)
                .load(R.drawable.airplane)
                .into(planeIv);

        //애니매이션
        planeIv.post(new Runnable() {
            public void run() {
                YoYo.with(Techniques.SlideInLeft)
                        .duration(1000)
                        .playOn( planeIv );
            }
        });

    }

    //스크롤의 변화를 감지
    public void scrollChanged(){

        try{
            float scrollX = scroll.getScrollX();
            Log.d("SCSC", "scroll: " + scrollX);
//
            if(scrollX == 0){
//                scroll.smoothScrollTo(520, 0);
                scroll.post(new Runnable() {
                    @Override public void run() {
                        ObjectAnimator.ofInt(scroll, "scrollX", 520).setDuration(300).start();


                        Toast.makeText(getApplicationContext(), "결제완료", Toast.LENGTH_LONG).show();
//                        Intent iT = new Intent(getApplicationContext(), OrderActivity2.class);
//                        startActivity(iT);
//                        finish(); //액티비티 종료

                    }
                });
            }

        }catch(Exception e){
            Log.d("DDD", "오류: " + e);
        }

    }
}
