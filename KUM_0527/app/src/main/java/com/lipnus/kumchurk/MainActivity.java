package com.lipnus.kumchurk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lipnus.kumchurk.detailpage.DetailReveiwActivity;
import com.lipnus.kumchurk.kum_class.GetLocation;
import com.lipnus.kumchurk.submenu.AlarmActivity;
import com.lipnus.kumchurk.submenu.MyReviewActivity;
import com.lipnus.kumchurk.submenu.NewsFeedActivity;
import com.lipnus.kumchurk.submenu.ReviewSearchActivity;
import com.lipnus.kumchurk.submenu.SearchActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lipnus.kumchurk.R.id.left_menu_iv1;
import static com.lipnus.kumchurk.R.id.left_menu_iv2;
import static com.lipnus.kumchurk.R.id.left_menu_iv3;
import static com.lipnus.kumchurk.R.id.left_menu_iv4;
import static com.lipnus.kumchurk.R.id.left_menu_iv5;
import static com.lipnus.kumchurk.R.id.left_menu_iv6;
import static com.lipnus.kumchurk.R.id.left_menu_iv7;
import static com.lipnus.kumchurk.R.id.left_menu_iv8;

public class MainActivity extends AppCompatActivity {

    //왼쪽, 오른쪽 이동 애니매이션 객체
    Animation translateLeftAnim;
    Animation translateRightAnim;

    LinearLayout SlidePage; //슬라이딩 페이지
    LinearLayout SlideShadow; //슬라이딩 페이지 우측의 검은 그림자부분
    boolean isPageOpen = false; //슬라이딩 페이지가 열려있는지 닫혀있는지

    //좌측메뉴의 객체들
    ImageView[] leftIv = new ImageView[8];
    ImageView left_profileIv;
    TextView left_profileTv;

    ImageView topLeftIv;
    ImageView topMidIv;
    ImageView topRightIv;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;


    public static Activity MNActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //타이틀바는 res/values 폴더의 styles.xml에서 없엤음
        //상태바의 색은 colors.xml 에서 수정

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d("RREE", "onCreate()");

        //초기설정
        initSetting();

        //다른곳에서 이 엑티비티를 피니쉬하기 위해 필요
        MNActivity = MainActivity.this;

        //Aplication의 위도, 경도를 업데이트
        GetLocation gl = new GetLocation( getApplicationContext() );

        Log.d("URUR", "[MainActivity]"
                + GlobalApplication.getUser_id() + ", "
                + GlobalApplication.getUser_nickname() + ", "
                + GlobalApplication.getUser_image() + ", "
                + GlobalApplication.getUser_thumbnail() + ", " );

        //에러가 나면 GlobalApplication값이 다 날아감, 만약 그런 일이 발생하면 복구해준다
        if(GlobalApplication.getUser_id() == "" || GlobalApplication.getUser_id() == null){
            Log.d("URUR", "값이 사라져서 프레퍼런스에서 읽어와서 복구");
            restoreProfile();
        }

        //좌측메뉴설정
        subMenuInit();

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }//onCreate

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.d("RREE", "onPostResume()");

        //에러가 나면 GlobalApplication값이 다 날아감, 만약 그런 일이 발생하면 복구해준다
        if(GlobalApplication.getUser_id() == ""){
            Log.d("URUR", "값이 사라져서 프레퍼런스에서 읽어와서 복구");
            restoreProfile();
        }

        //오른쪽 메뉴가 열려있으면 닫아준다
        if(isPageOpen=true){
            //열려있던 메뉴는 닫음
            isPageOpen=false;
            SlideShadow.setVisibility(View.GONE);
            SlidePage.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("RREE", "onRestart()");
    }

    //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    //초기설정
    public void initSetting(){

        topLeftIv = (ImageView) findViewById(R.id.main_leftIv);
        topMidIv = (ImageView) findViewById(R.id.main_logoIv);
        topRightIv = (ImageView) findViewById(R.id.main_rightIv);


        //상단메뉴
        Glide.with(this)
                .load( R.drawable.search )
                .into( topLeftIv );
        topLeftIv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this)
                .load( R.drawable.logo_noodle )
                .into( topMidIv );
        topMidIv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this)
                .load( R.drawable.menu )
                .into( topRightIv );
        topRightIv.setScaleType(ImageView.ScaleType.FIT_XY);



        //기존의 뷰페이저를 세로방향으로 바꾼 VerticalViewPager 라이브러리를 이용.
        VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        verticalViewPager.setAdapter(new ViewPager_Adapter(getFragmentManager()));

        //전환효과(false, true에 의해서 앞뒤의 레이어 순위가 바뀜)
        verticalViewPager.setPageTransformer(false, new ViewPager_DepthPageTransformer());

        //뷰페이저에 변화가 있을때의 리스너(쓰진 않음)
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //액티비티 전환 시 애니매이션 삭제
        getWindow().setWindowAnimations(0);

        //애니매이션 부분
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.main_translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.main_translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

        //오른쪽에서 나오는 레이아웃
        SlidePage = (LinearLayout) findViewById(R.id.slidingMenu);
        SlideShadow = (LinearLayout) findViewById(R.id.slidingShadow);
    }

    //취소버튼
    @Override
    public void onBackPressed() {
        if(isPageOpen==true){
            SlideShadow.setVisibility(View.GONE);
            SlidePage.startAnimation(translateRightAnim);
        }else{

                long tempTime        = System.currentTimeMillis();
                long intervalTime    = tempTime - backPressedTime;

                if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
                    //3초안에 한번 더 누르면 종료
                    this.overridePendingTransition(R.anim.fadein, R.anim.translate_right);
                    super.onBackPressed();
                }
                else {
                    backPressedTime = tempTime;
                    Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르시면 종료됩니다",Toast.LENGTH_SHORT).show();
                }

        }
    }

    public void onClick_LeftMenu(View v){
        Intent iT = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(iT);
        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.search_translate_right, 0);

    }//왼쪽 상단 돋보기 검색 검색버튼
    public void onClick_RightMenu(View v){
        SlidePage.setVisibility(View.VISIBLE);
        SlidePage.startAnimation(translateLeftAnim);
    }//오른쪽 상단 석삼메뉴 버튼
    public void onClick_SlideShadow(View v){
        SlideShadow.setVisibility(View.GONE);
        SlidePage.startAnimation(translateRightAnim);
    }//슬라이드의 검은영역 클릭
    public void onClick_subMenu(View v){

        Intent iT;

        switch (v.getId()){
            //메인메뉴
            case left_menu_iv1:
                iT = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(iT);
                finish();
                break;

            //뉴스피드
            case left_menu_iv2:
                iT = new Intent(getApplicationContext(), NewsFeedActivity.class);
                startActivity(iT);
                break;

            case left_menu_iv3:
                iT = new Intent(getApplicationContext(), ReviewSearchActivity.class);
                startActivity(iT);
                break;
            case left_menu_iv4:
                iT = new Intent(getApplicationContext(), MyReviewActivity.class);
                startActivity(iT);
                break;
            case left_menu_iv5:
                break;
            case left_menu_iv6:
                iT = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(iT);
                break;
            case left_menu_iv7:
                break;
            case left_menu_iv8:
                break;
        }



    } //서브메뉴의 각 버튼들 클릭
    public void onClick_logo(View v){
//        Intent iT = new Intent(this, DetailReveiwActivity.class);
//        iT.putExtra("review_num", "64");
//        startActivity(iT);
    }//프로필로 쓸거지만 테스트로 해둠


    //우측메뉴 초기화
    public void subMenuInit(){

        //프사부분
        left_profileIv = (ImageView) findViewById(R.id.left_profile_circle_iv);
        left_profileTv = (TextView) findViewById(R.id.left_profile_tv);

        //동그라미 프사
        Glide.with(this)
                .load( GlobalApplication.getUser_thumbnail() )
                .placeholder(R.drawable.loading)
                .bitmapTransform(new CropCircleTransformation(this))
                .thumbnail(0.3f)
                .into(left_profileIv);

        //아이디
        left_profileTv.setText( GlobalApplication.getUser_nickname() );

        //=========================================================
        //아래쪽 정사각형 그림으로 된 메뉴부분
        //=========================================================

        //할당
        leftIv[0] = (ImageView) findViewById(left_menu_iv1);
        leftIv[1] = (ImageView) findViewById(left_menu_iv2);
        leftIv[2] = (ImageView) findViewById(left_menu_iv3);
        leftIv[3] = (ImageView) findViewById(left_menu_iv4);
        leftIv[4] = (ImageView) findViewById(left_menu_iv5);
        leftIv[5] = (ImageView) findViewById(left_menu_iv6);
        leftIv[6] = (ImageView) findViewById(left_menu_iv7);
        leftIv[7] = (ImageView) findViewById(left_menu_iv8);

        //그림들의 경로(글라이드로 그림을 넣음)
        int[] leftPic = new int[8];
        leftPic[0] = R.drawable.left_menu1;
        leftPic[1] = R.drawable.left_menu2;
        leftPic[2] = R.drawable.left_menu3;
        leftPic[3] = R.drawable.left_menu4;
        leftPic[4] = R.drawable.left_menu5;
        leftPic[5] = R.drawable.left_menu6;
        leftPic[6] = R.drawable.left_menu7;
        leftPic[7] = R.drawable.left_menu8;

        for(int i=0; i<8; i++){
            Glide.with(this)
                    .load( leftPic[i] )
                    .centerCrop()
                    .into(leftIv[i]);
        }


    }

    //Application의 데이터가 날아갔을 때 복구
    public void restoreProfile(){

        Log.d("RESS", "복구");

        //에러가 나면 Globalapplication에 있던 변수들이 날아감. 프레퍼런스에서 값을 불러와서 다시 넣어준다

        SharedPreferences setting;
        SharedPreferences.Editor editor;

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        //Preference에서 값을 가져와서 Application에 넣어준다
        GlobalApplication.setUser_id(setting.getString("user_id", "id_error"));
        GlobalApplication.setUser_nickname(setting.getString("user_nickname", "nickname_error"));
        GlobalApplication.setUser_image(setting.getString("user_image", "image_error"));
        GlobalApplication.setUser_thumbnail(setting.getString("user_thumbnail", "thumbnail_error"));
    }



    //애니매이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen==true){
                SlidePage.setVisibility(View.GONE);
                isPageOpen=false;
            }
            else{
                SlideShadow.setVisibility(View.VISIBLE);
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
