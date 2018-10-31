package com.lipnus.pd4;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OrderActivity extends BaseActivity {

    ImageView titleIv;
    HorizontalScrollView scroll;

    ImageView planeIv;
    ImageView up_layout_iv, mid_layout_iv;
    ImageView coffeeBackIv, coffeeIv, destCapIv;
    ImageView destIv, temperatureIv, sizeIv;

    FrameLayout ppFL, ppTemperFL;
    ImageView ppBackIv, ppNameIv, ppWorldIv;

    ImageView hotIv, coldIv;
    ImageView smallIv, regularIv, largeIv;
    ImageView ppTempBackIv;

    int user_temp = 0;
    int user_size = 0;

    String nation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


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

        navi_setting();
        plane_setting();
        init_basic();
        init_nation();


        init_type(1,1);
        select_type(user_temp, user_size);

        nation = null;

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

    public void navi_setting(){
        //네비게이션 버튼 객체들 등록
        naveMenu1 = (ImageView) findViewById(R.id.navi_menu1);
        naveMenu2 = (ImageView) findViewById(R.id.navi_menu2);
        naveMenu3 = (ImageView) findViewById(R.id.navi_menu3);
        naveMenu4 = (ImageView) findViewById(R.id.navi_menu4);
        naviGradiation = (ImageView) findViewById(R.id.navi_gardation_iv);

        Glide.with(this)
                .load(R.drawable.order)
                .into(naveMenu1);

        Glide.with(this)
                .load(R.drawable.stamp2)
                .into(naveMenu2);

        Glide.with(this)
                .load(R.drawable.translation2)
                .into(naveMenu3);

        Glide.with(this)
                .load(R.drawable.mailbox2)
                .into(naveMenu4);

        Glide.with(this)
                .load(R.drawable.gradation)
                .into(naviGradiation);
    }

    public void init_basic(){
        up_layout_iv = (ImageView) findViewById(R.id.up_layout_iv);
        mid_layout_iv = (ImageView) findViewById(R.id.mid_layout_iv);
        titleIv = (ImageView) findViewById(R.id.title_iv);
        coffeeBackIv = (ImageView) findViewById(R.id.order_coffee_background_iv);
        coffeeIv = (ImageView) findViewById(R.id.order_coffee_pic_iv);
        destCapIv = (ImageView) findViewById(R.id.order_location_iv);
        destIv = (ImageView) findViewById(R.id.order_location2_iv);
        sizeIv = (ImageView) findViewById(R.id.order_size_iv);
        temperatureIv = (ImageView) findViewById(R.id.order_temperature_iv);



        Glide.with(this)
                .load(R.drawable.boarding)
                .into(up_layout_iv);

        Glide.with(this)
                .load(R.drawable.coffeselect)
                .into(mid_layout_iv);

        Glide.with(this)
                .load(R.drawable.title_boarding)
                .into(titleIv);

        Glide.with(this)
                .load(R.drawable.coffeeselect)
                .into(coffeeBackIv);

//        //목적지 알파벳
//        Glide.with(this)
//                .load(R.drawable.mixcoffee_skorea_1)
//                .into(destCapIv);

//        //목적지
//        Glide.with(this)
//                .load(R.drawable.mix_coffee_1)
//                .into(destIv);

        //온도
        Glide.with(this)
                .load(R.drawable.bighot)
                .into(temperatureIv);

        //사이즈
        Glide.with(this)
                .load(R.drawable.big_large)
                .into(sizeIv);


    }

    public void init_nation(){

        ppFL = (FrameLayout) findViewById(R.id.popup_fL);
        ppBackIv = (ImageView) findViewById(R.id.popup_background_iv);
        ppWorldIv = (ImageView) findViewById(R.id.popup_world_iv);
        ppNameIv = (ImageView) findViewById(R.id.popup_counrry_name_iv);

        //노란뒷배경
        Glide.with(this)
                .load(R.drawable.popupdestination)
                .into(ppBackIv);

        //지도
        Glide.with(this)
                .load(R.drawable.worldmap)
                .into(ppWorldIv);

        //나라이름
//        Glide.with(this)
//                .load(R.drawable.skorea)
//                .into(ppNameIv);


        //미국
        Glide.with(this)
                .load(R.drawable.usaflagbw)
                .into( (ImageView)findViewById(R.id.flag_usa) );

        //아일랜드
        Glide.with(this)
                .load(R.drawable.irelandflagbw)
                .into( (ImageView)findViewById(R.id.flag_ireland) );

        //이탈리아
        Glide.with(this)
                .load(R.drawable.italyflagbw)
                .into( (ImageView)findViewById(R.id.flag_italy) );

        //오스트리아
        Glide.with(this)
                .load(R.drawable.austriaflagbw)
                .into( (ImageView)findViewById(R.id.flag_austria) );

        //터키
        Glide.with(this)
                .load(R.drawable.turkeyflagbw)
                .into( (ImageView)findViewById(R.id.flag_turkey) );

        //한국
        Glide.with(this)
                .load(R.drawable.skoreaflagbw)
                .into( (ImageView)findViewById(R.id.flag_korea) );

        //베트남
        Glide.with(this)
                .load(R.drawable.vietnamflagbw)
                .into( (ImageView)findViewById(R.id.flag_vietnam) );

        //오스트레일리아
        Glide.with(this)
                .load(R.drawable.australiaflagbw)
                .into( (ImageView)findViewById(R.id.flag_australia) );

    }

    public void init_type( int temp, int size ){
        ppTemperFL = (FrameLayout) findViewById(R.id.popup_temper_fL);
        ppTempBackIv = (ImageView) findViewById(R.id.popup_temper_background_iv);

        //노란뒷배경
        Glide.with(this)
                .load(R.drawable.popuptype)
                .into(ppTempBackIv);

        if(temp==1){
            //hot
            Glide.with(this)
                    .load(R.drawable.hot_)
                    .into( (ImageView)findViewById(R.id.popup_type_hot_iv) );

            //cold
            Glide.with(this)
                    .load(R.drawable.cold_)
                    .into( (ImageView)findViewById(R.id.popup_type_ice_iv) );
        }


        if(size == 1){
            //small
            Glide.with(this)
                    .load(R.drawable.small_)
                    .into( (ImageView)findViewById(R.id.popup_type_small_iv) );

            //regular
            Glide.with(this)
                    .load(R.drawable.regular_)
                    .into( (ImageView)findViewById(R.id.popup_type_regular_iv) );

            //large
            Glide.with(this)
                    .load(R.drawable.large_)
                    .into( (ImageView)findViewById(R.id.popup_type_large_iv) );

        }






    }

    //특정 버튼에 색넣기
    public void select_type( int temp, int size ){

        if(temp==0){
            //hot
            Glide.with(this)
                    .load(R.drawable.hot)
                    .into( (ImageView)findViewById(R.id.popup_type_hot_iv) );

            Glide.with(this)
                    .load(R.drawable.bighot)
                    .into(temperatureIv);


        } else if(temp==1){
            //cold
            Glide.with(this)
                    .load(R.drawable.cold)
                    .into( (ImageView)findViewById(R.id.popup_type_ice_iv) );

            Glide.with(this)
                    .load(R.drawable.ice)
                    .into(temperatureIv);
        }


        if(size == 0) {
            //small
            Glide.with(this)
                    .load(R.drawable.small)
                    .into((ImageView) findViewById(R.id.popup_type_small_iv));

            Glide.with(this)
                    .load(R.drawable.big_small)
                    .into(sizeIv);

        }else if(size == 1){
            //regular
            Glide.with(this)
                    .load(R.drawable.regular)
                    .into( (ImageView)findViewById(R.id.popup_type_regular_iv) );

            Glide.with(this)
                    .load(R.drawable.big_regular)
                    .into(sizeIv);

        }else if(size == 2) {
            //large
            Glide.with(this)
                    .load(R.drawable.large)
                    .into((ImageView) findViewById(R.id.popup_type_large_iv));

            Glide.with(this)
                    .load(R.drawable.big_large)
                    .into(sizeIv);
        }
    }



    //각 나라의 국기를 터치했을 때
    public void onClick_flag(View v){

        //전부 다 회색으로 초기화 시킴
        init_nation();

        int coffeePic = 0;
        int destCapPic = 0;
        int destPic = 0;

        switch( v.getId() ){

            case R.id.flag_usa:
                Glide.with(this)
                        .load(R.drawable.usaflag)
                        .into( (ImageView)findViewById(R.id.flag_usa));

                Glide.with(this)
                        .load(R.drawable.usa)
                        .into( ppNameIv);

                coffeePic = R.drawable.americano;
                destCapPic = R.drawable.americano_usa_1;
                destPic = R.drawable.americano_1;
                nation = "usa";
                break;

            case R.id.flag_ireland:
                Glide.with(this)
                        .load(R.drawable.irelandflag)
                        .into( (ImageView)findViewById(R.id.flag_ireland));

                Glide.with(this)
                        .load(R.drawable.ireland)
                        .into( ppNameIv);

                coffeePic = R.drawable.caifegaelach;
                destCapPic = R.drawable.caifegaelach_ireland_1;
                destPic = R.drawable.caifegaelach_1;
                nation = "ireland";
                break;

            case R.id.flag_italy:
                Glide.with(this)
                        .load(R.drawable.italyflag)
                        .into( (ImageView)findViewById(R.id.flag_italy));

                Glide.with(this)
                        .load(R.drawable.italy)
                        .into( ppNameIv);

                coffeePic = R.drawable.espresso;
                destCapPic = R.drawable.espresso_italy_1;
                destPic = R.drawable.espresso_1;
                nation = "italy";
                break;

            case R.id.flag_austria:
                Glide.with(this)
                        .load(R.drawable.austriaflag)
                        .into( (ImageView)findViewById(R.id.flag_austria));

                Glide.with(this)
                        .load(R.drawable.austria)
                        .into( ppNameIv);

                coffeePic = R.drawable.einspanner;
                destCapPic = R.drawable.einspanner_austria_1;
                destPic = R.drawable.einspanner_1;
                nation = "austria";
                break;

            case R.id.flag_turkey:
                Glide.with(this)
                        .load(R.drawable.turkeyflag)
                        .into( (ImageView)findViewById(R.id.flag_turkey));

                Glide.with(this)
                        .load(R.drawable.turkey)
                        .into( ppNameIv);

                coffeePic = R.drawable.turkkahvesi;
                destCapPic = R.drawable.turkkahvesi_turkey_1;
                destPic = R.drawable.turk_kahvesi_1;
                nation = "turkey";
                break;

            case R.id.flag_korea:
                Glide.with(this)
                        .load(R.drawable.skoreaflag)
                        .into( (ImageView)findViewById(R.id.flag_korea));

                Glide.with(this)
                        .load(R.drawable.skorea)
                        .into( ppNameIv);

                coffeePic = R.drawable.mixcoffee;
                destCapPic = R.drawable.mixcoffee_skorea_1;
                destPic = R.drawable.mix_coffee_1;
                nation = "korea";
                break;

            case R.id.flag_vietnam:
                Glide.with(this)
                        .load(R.drawable.vietnamflag)
                        .into( (ImageView)findViewById(R.id.flag_vietnam));

                Glide.with(this)
                        .load(R.drawable.vietnam)
                        .into( ppNameIv);

                coffeePic = R.drawable.caphesuada;
                destCapPic = R.drawable.caphesuada_vietnam_1;
                destPic = R.drawable.caphesuada_1;
                nation = "vietnam";
                break;

            case R.id.flag_australia:
                Glide.with(this)
                        .load(R.drawable.australiaflag)
                        .into( (ImageView)findViewById(R.id.flag_australia));

                Glide.with(this)
                        .load(R.drawable.australia)
                        .into( ppNameIv);

                coffeePic = R.drawable.flatwhite;
                destCapPic = R.drawable.flatwhite_australia_1;
                destPic = R.drawable.flat_white_1;
                nation = "australia";
                break;
        }

        Glide.with(this)
                .load( coffeePic )
                .into( coffeeIv );

        Glide.with(this)
                .load( destCapPic )
                .into( destCapIv );

        Glide.with(this)
                .load( destPic )
                .into( destIv );
    }

    //팝업 닫기
    public void onClick_pop_cancel(View v){
        Log.d("SSSS", "종료");
        ppFL.setVisibility(View.INVISIBLE);
        ppTemperFL.setVisibility(View.INVISIBLE);

    }

    //나라 선택(팝업창 뜸)
    public void onClick_destination(View v){

        ppFL.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeInUp)
                .duration(500)
                .playOn(ppFL);

    }

    //타입 선택(팝업창 뜸)
    public void onClick_type(View v){
        ppTemperFL.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeInUp)
                .duration(500)
                .playOn(ppTemperFL);
    }

    //온도, 크기 고르기
    public void onClick_type_select(View v){

        switch( v.getId() ){
            case R.id.popup_type_hot_iv:
                init_type(1,0);
                user_temp = 0;
                break;

            case R.id.popup_type_ice_iv:
                init_type(1,0);
                user_temp = 1;
                break;

            case R.id.popup_type_small_iv:
                init_type(0,1);
                user_size = 0;
                break;

            case R.id.popup_type_regular_iv:
                init_type(0,1);
                user_size = 1;
                break;

            case R.id.popup_type_large_iv:
                init_type(0,1);
                user_size = 2;
                break;
        }

        select_type(user_temp, user_size);
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

                        if(nation == null){
                            Toast.makeText(getApplicationContext(), "목적지를 선택해주세요", Toast.LENGTH_LONG).show();
                        }else{
                            Intent iT = new Intent(getApplicationContext(), OrderActivity2.class);
                            startActivity(iT);
                            finish(); //액티비티 종료
                        }



                    }
                });
            }

        }catch(Exception e){
            Log.d("DDD", "오류: " + e);
        }

    }
}