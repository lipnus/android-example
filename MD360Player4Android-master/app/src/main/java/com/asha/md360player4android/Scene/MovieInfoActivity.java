package com.asha.md360player4android.Scene;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.asha.md360player4android.R;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MovieInfoActivity extends AppCompatActivity {

    ImageView topIv, topGradationIv;
    ImageView titleIv;
    ImageView graphIv, graphIv2;
    ImageView commentIv;

    ImageView topBarIv;

    ScrollView scroll;

    float scroll_Location_Check = 0;

    boolean scrollDown = false;
    boolean animation_graph_on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        imgSetting();

        //스크롤 변화를 캐치하는 리스너
        if(Build.VERSION.SDK_INT >=23 ){
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    scrollChanged();
                }
            });
        }



    }

    public void init(){
        topIv = (ImageView) findViewById(R.id.mi_top_iv);
        topGradationIv = (ImageView) findViewById(R.id.mi_top_gradation_iv);
        titleIv = (ImageView) findViewById(R.id.mi_title_iv);
        graphIv = (ImageView) findViewById(R.id.mi_graph_iv);
        graphIv2 = (ImageView) findViewById(R.id.mi_graph2_iv);
        commentIv = (ImageView) findViewById(R.id.mi_comment_iv);
        scroll = (ScrollView) findViewById(R.id.mi_scroll);
        topBarIv = (ImageView) findViewById(R.id.mi_topbar_iv);
    }

    public void imgSetting(){

        Glide.with(this)
                .load( R.drawable.movie_info_pic )
                .into(topIv);

        Glide.with(this)
                .load( R.drawable.moive_info_gradation )
                .into(topGradationIv);

        Glide.with(this)
                .load( R.drawable.moive_info_top )
                .into(titleIv);

        Glide.with(this)
                .load( R.drawable.movie_info_score )
                .into(graphIv);

        Glide.with(this)
                .load( R.drawable.movie_info_graph )
                .into(graphIv2);

        Glide.with(this)
                .load( R.drawable.moive_info_comment )
                .into(commentIv);

        Glide.with(this)
                .load( R.drawable.topbar )
                .into(topBarIv);
    }

    public void scrollChanged(){

        try{
            float scrollY = scroll.getScrollY();
            Log.d("SCSC", "scroll: " + scrollY);

            //최상단 이미지
            if(scrollY < 700){
                topIv.setScaleX( 1 + (scrollY/700) );
                topIv.setScaleY( 1 + (scrollY/700)  );
            }

            if(scrollY < 800) {
                animation_graph_on = false;
            }

            if(scrollY > 800){

                //하트에 애니메이션
                if(animation_graph_on == false){
                    Log.d("SCSC", "animation");

                    YoYo.with(Techniques.ZoomIn)
                            .duration(1000)
                            .playOn( graphIv2 );

                    animation_graph_on = true;
                }
            }

            //스크롤 올라가고 내려가는 변화점을 캐치

            //내려가는 중
            if(scrollY > scroll_Location_Check && scrollY > 50){
                if(scrollDown == false){ //직전까지 올라가고 있었음
                    Log.d("SSSS", "내려가기 시작");

                    YoYo.with(Techniques.FadeOutUp)
                            .duration(700)
                            .playOn( topBarIv );

//                    topBarIv.setVisibility(View.INVISIBLE);
                    scrollDown = true;
                }
            }

            //올라가는 중
            if(scrollY < scroll_Location_Check){
                if(scrollDown == true){ //직전까지 내려가고 있었음
                    Log.d("SSSS", "올라가기 시작");
                    topBarIv.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.FadeInDown)
                            .duration(700)
                            .playOn( topBarIv );

                    scrollDown = false;
                }

            }



            scroll_Location_Check = scrollY; //스크롤의 위치를 체크한 뒤 다음 루프에서 검사

        }catch(Exception e){
            Log.d("DDD", "오류: " + e);}

    }

    public void onClick_mi_reserve(View v){
        Toast.makeText(getApplicationContext(), "ㅎㅎㅎ", Toast.LENGTH_LONG).show();
    }

    public void onClick_mi_topbar(View v){
        finish();
    }
}
