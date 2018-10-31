package com.lipnus.kumchurk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lipnus.kumchurk.data.Classified_Menu_List;
import com.lipnus.kumchurk.data.MenuInfo_JSON;
import com.lipnus.kumchurk.data.MenuRes_Info;
import com.lipnus.kumchurk.data.Menu_List;
import com.lipnus.kumchurk.data.Menu_Recommend;
import com.lipnus.kumchurk.data.Menu_Review;
import com.lipnus.kumchurk.data.Menu_Review_Vote;
import com.lipnus.kumchurk.data.Res_Info;
import com.lipnus.kumchurk.data.Res_Vote;
import com.lipnus.kumchurk.detailpage.CommentActivity;
import com.lipnus.kumchurk.detailpage.ProfileImgActivity;
import com.lipnus.kumchurk.detailpage.ReviewWrtieActivity;
import com.lipnus.kumchurk.kum_class.SimpleFunction;
import com.lipnus.kumchurk.kum_class.Temp_star;
import com.lipnus.kumchurk.kum_class.Temp_vote;
import com.lipnus.kumchurk.map.MapActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lipnus.kumchurk.R.id.pager;
import static com.lipnus.kumchurk.R.id.scrollview;
import static com.lipnus.kumchurk.kum_class.SimpleFunction.displayPrice;


/**
 * Created by Sunpil on 2017-02-01.
 * 음식메뉴 넘길 때 쓰이는 뷰페이저의 어탭터는 따로 클래스를 만들지 않고 내부클래스로 구현하였다(2017-02-01)
 */

public class ScrollActivity extends AppCompatActivity {

    //지금 화면에서 표시할 메뉴에름과 식당이름(인텐트로 받아옴)
    private String nowResName;
    private String nowMenuName;

    //지금 떠있는 리뷰에 대한 하트빠큐 투표여부
    private boolean heartOn = false; //ture면 하트에 색이 들어온것
    private boolean fuckOn = false; //ture면 빠큐에 색이 들어온것

    ScrollView scrollView;
    TextView overTv;
    TextView viewpagerCount;
    TextView reviewTv;

    ImageView heart_iv;
    ImageView fuck_iv;
    ImageView comment_iv;
    ImageView upload_iv;

    TextView heart_tv;
    TextView fuck_tv;
    TextView comment_tv;

    ImageView faceIv;
    TextView idTv;

    ImageView resPicIv;
    ImageView resPicIv2;
    LinearLayout resPicFilter;

    LinearLayout foodCommentLayout;
    LinearLayout restrantInfoLinear;
    LinearLayout scoreLinear;

    //식당의 정보출력
    TextView resNameTv;
    TextView resInfoTv;
    TextView resThemeTv;
    TextView resLeftTimeTv;
    TextView resStarTv;
    TextView resStarNumTv;
//    TextView resSumHeartTv;
//    TextView resSumFuckTv;
//    TextView resSumCommentTv;
    TextView resDistanceTv;

    //해당 식당의 메뉴리스트 위에 텍스트뷰
    TextView menulistTitleTv;

    //해당 식당의 음식메뉴들을 출력하는 리스트뷰
    ListView listview;
    ListViewAdapter adapter;


    //추천메뉴
    ImageView[] recommendIv = new ImageView[8];
    TextView[] recommend_nameTv = new TextView[8];
    TextView[] recommend_priceTv = new TextView[8];
    TextView[] recommend_resTv = new TextView[8];

    //별
    ImageView[] starIv = new ImageView[5];

    //별점 평가 위의 텍스트
    TextView starScoreTv;

    //다음으로 가는 화살표
    ImageView nextArrowIv;

    //음식메뉴를 넘기는 뷰페이저와 어댑터
    private ViewPager foodPager;
    private PagerAdapter foodPagerAdapter;
    private int PAGER_NUM = 1; //뷰페이저가 몇개인지(기본은 1개)
    private int pagerPosition; //현재 뷰페이저가 몇번째인지(onPostResume에서 0으로 초기화되고, 뷰페이서 리스너에서 업데이트됨)

    //왼쪽, 오른쪽 이동 애니매이션 객체
    Animation translateRightAnim;
    Animation fadeIn;
    Animation fadeOut;
    int resAnimCount = 0; //레스토랑 소개부분의 애니매이션 단계(0~2)
    double scroll_Location_Check = 0; //위로 스크롤 하는 것을 체크

    //댓글 유도 텍스트
    TextView comment_connect_tv;


    //좋아요와 뻐큐를 처리
    private String UPLOAD_URL2 ="http://kumchurk.ivyro.net/app/upload_menureview_vote.php";


    //volley와 리스너 (volly는 독립적인 클래스로 구현)
    IVolleyResult mResultCallback = null; //페이지 전체의 정보 받아오기
    IVolleyResult mResultCallback2 = null; //좋아요, 뻑큐 눌렀을때의 결과
    IVolleyResult mResultCallback3 = null; //별점 눌렀을때의 결과
    VolleyConnect volley;


    //volley로부터 받은 데이터들을 담을 객체들
    MenuInfo_JSON menuInfo;

    //받아온걸 여기에 다시 옮긴다(헷갈릴까봐 이렇게 해놨는데 별 도움은 안되는듯..)
    List<Menu_Review> menu_review;
    List<Res_Info> res_info;
    List<Menu_List> menu_list;
    List<Menu_Recommend> menu_recommend;
    List<Menu_Review_Vote> menu_review_vote;
    List<Res_Vote> res_vote;

    //menu_list의 리스트를 원소로 가지는 리스트(메뉴들을 소분류에 따른 집단으로 묶는다)
    List<Classified_Menu_List> menu_list_set;

    //ScrollActivity가 Task에 쌓이지 않게 하기 위해서 이런 처리를 한다
    public static Activity SCActivity;

    //투표내용을 일시적으로 저장했다가 서버에 정상적으로 올라간 것이 확인되면 APP내부에 적용
    Temp_vote temp_vote;
    Temp_star temp_star;


    //5분 이상 백그라운드에 있으면 꺼짐
//    private final long	FINSH_INTERVAL_TIME    = 300000;
//    private long startTime = 0;

    int resumeCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d("URUR", "[ScrollActivity]"
                + GlobalApplication.getUser_id() + ", "
                + GlobalApplication.getUser_nickname() + ", "
                + GlobalApplication.getUser_image() + ", "
                + GlobalApplication.getUser_thumbnail() + ", " );


        //toolbarColor(); //툴바의 색상을 흰색으로 변경

        //ScrollActivity가 Task에 쌓이지 않게 하기 위해
        SCActivity = ScrollActivity.this;

        //앞의 엑티비티로부터 식당이름과 메뉴이름을 받는다
        Intent iT = getIntent();
        nowResName = iT.getExtras().getString("res_name");
        nowMenuName = iT.getExtras().getString("menu_name");

        //여기에 다 처박아두고 onCreate에는 리스너만 놔뒀다
        initSetting();

        //뷰페이저에 변화가 있을때의 리스너
        foodPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("EEE", "position: " + position);

                //뷰페이저가 넘어갈때마다 pagerPosition에다가 뷰페이저의 현재위치를 저장

                Log.d("TWTW", "paperPosition:" + pagerPosition);
                pagerPosition = position;
                addReview();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //onScrollChangedListener은 SDK 23이상에서만 동작한다 23버전 이하에서는 꿩대신 닭으로 onTouchListener를 사용
        if(Build.VERSION.SDK_INT >=23 ){
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    scrollChanged();
                }
            });
        }else { //버전이 낮은경우
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                private ViewTreeObserver observer;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollChanged();
                    return false;
                }
            });
        }


        //Volley 콜백함수
        initVolleyCallback(); //페이지의 데이터 다운로드
        initVolleyCallback2(); //좋아요, 싫어요 투표의 결과
        initVolleyCallback3(); //별점투표 결과



        //volley를 이용하여 서버에 접속
        connect();


    }//onCreate끝

    public void initSetting(){

        //투표데이터를 임시저장하는 객체
        temp_vote = new Temp_vote();
        temp_star = new Temp_star();

        //별
        starIv[0] = (ImageView) findViewById(R.id.star1);
        starIv[1] = (ImageView) findViewById(R.id.star2);
        starIv[2] = (ImageView) findViewById(R.id.star3);
        starIv[3] = (ImageView) findViewById(R.id.star4);
        starIv[4] = (ImageView) findViewById(R.id.star5);

        //별 위의 텍스트
        starScoreTv = (TextView) findViewById(R.id.star_scoreTv);

        //추천메뉴
        recommendIv[0] = (ImageView) findViewById(R.id.recommendIv1);
        recommendIv[1] = (ImageView) findViewById(R.id.recommendIv2);
        recommendIv[2] = (ImageView) findViewById(R.id.recommendIv3);
        recommendIv[3] = (ImageView) findViewById(R.id.recommendIv4);
        recommendIv[4] = (ImageView) findViewById(R.id.recommendIv5);
        recommendIv[5] = (ImageView) findViewById(R.id.recommendIv6);
        recommendIv[6] = (ImageView) findViewById(R.id.recommendIv7);
        recommendIv[7] = (ImageView) findViewById(R.id.recommendIv8);


        recommend_nameTv[0] = (TextView) findViewById(R.id.recommend_nameTv1);
        recommend_nameTv[1] = (TextView) findViewById(R.id.recommend_nameTv2);
        recommend_nameTv[2] = (TextView) findViewById(R.id.recommend_nameTv3);
        recommend_nameTv[3] = (TextView) findViewById(R.id.recommend_nameTv4);
        recommend_nameTv[4] = (TextView) findViewById(R.id.recommend_nameTv5);
        recommend_nameTv[5] = (TextView) findViewById(R.id.recommend_nameTv6);
        recommend_nameTv[6] = (TextView) findViewById(R.id.recommend_nameTv7);
        recommend_nameTv[7] = (TextView) findViewById(R.id.recommend_nameTv8);

        recommend_priceTv[0] = (TextView) findViewById(R.id.recommend_priceTv1);
        recommend_priceTv[1] = (TextView) findViewById(R.id.recommend_priceTv2);
        recommend_priceTv[2] = (TextView) findViewById(R.id.recommend_priceTv3);
        recommend_priceTv[3] = (TextView) findViewById(R.id.recommend_priceTv4);
        recommend_priceTv[4] = (TextView) findViewById(R.id.recommend_priceTv5);
        recommend_priceTv[5] = (TextView) findViewById(R.id.recommend_priceTv6);
        recommend_priceTv[6] = (TextView) findViewById(R.id.recommend_priceTv7);
        recommend_priceTv[7] = (TextView) findViewById(R.id.recommend_priceTv8);

        recommend_resTv[0] = (TextView) findViewById(R.id.recommend_resTv1);
        recommend_resTv[1] = (TextView) findViewById(R.id.recommend_resTv2);
        recommend_resTv[2] = (TextView) findViewById(R.id.recommend_resTv3);
        recommend_resTv[3] = (TextView) findViewById(R.id.recommend_resTv4);
        recommend_resTv[4] = (TextView) findViewById(R.id.recommend_resTv5);
        recommend_resTv[5] = (TextView) findViewById(R.id.recommend_resTv6);
        recommend_resTv[6] = (TextView) findViewById(R.id.recommend_resTv7);
        recommend_resTv[7] = (TextView) findViewById(R.id.recommend_resTv8);

        //식당의 정보출력
        resNameTv = (TextView)findViewById(R.id.resNameTv);
        resInfoTv= (TextView)findViewById(R.id.resInfoTv);
        resThemeTv= (TextView)findViewById(R.id.resThemeTv);
        resLeftTimeTv = (TextView) findViewById(R.id.resLeftTimeTv);
        resStarTv= (TextView)findViewById(R.id.resStarTv);
        resStarNumTv= (TextView)findViewById(R.id.resStarNumTv);
//        resSumHeartTv= (TextView)findViewById(R.id.resSumHeartTv);
//        resSumFuckTv= (TextView)findViewById(R.id.resSumFuckTv);
//        resSumCommentTv= (TextView)findViewById(R.id.resSumCommentTv);
        resDistanceTv= (TextView)findViewById(R.id.resDistanceTv);

        //하트, 빠큐, 댓글, 업로드 아이콘과 그 밑의 텍스트
        heart_iv = (ImageView) findViewById(R.id.heart_iv);
        fuck_iv = (ImageView) findViewById(R.id.fuck_iv);
        comment_iv = (ImageView) findViewById(R.id.comment_iv);
        upload_iv = (ImageView) findViewById(R.id.upload_iv);

        heart_tv = (TextView) findViewById(R.id.heart_tv);
        fuck_tv = (TextView) findViewById(R.id.fuck_tv);
        comment_tv = (TextView) findViewById(R.id.comment_tv);

        //댓글 유도 텍스트
        comment_connect_tv = (TextView) findViewById(R.id.comment_connect_tv);

        //다음으로 가는 화살표
        nextArrowIv = (ImageView) findViewById(R.id.sc_nextarrow_iv);

        scrollView = (ScrollView) findViewById(scrollview);
        overTv = (TextView) findViewById(R.id.overTextView);
        reviewTv = (TextView) findViewById(R.id.reviewTv); //댓글(리뷰의 본문)
        viewpagerCount = (TextView) findViewById(R.id.viewPagerPageTv);

        faceIv = (ImageView) findViewById(R.id.faceIv);
        idTv = (TextView) findViewById(R.id.idTv);

        resPicIv = (ImageView) findViewById(R.id.res_pic_Iv); //식당사진
        resPicIv2 = (ImageView) findViewById(R.id.res_pic2_Iv); //식당사진2
        resPicFilter = (LinearLayout) findViewById(R.id.res_pic_filter_Linear); //식당사진 위의 검은색 필터

        foodCommentLayout = (LinearLayout) findViewById(R.id.foodCommentLinear);
        restrantInfoLinear = (LinearLayout) findViewById(R.id.resturantInfoLinear);
        scoreLinear = (LinearLayout) findViewById(R.id.scoreLinear); //별점매기는부분

        //메뉴리스트 위의 텍스트뷰
        menulistTitleTv = (TextView) findViewById(R.id.menu_list_title_tv);

        //애니매이션
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein_restaurant);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right_restaurant_pic);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        fadeIn.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);
        fadeOut.setAnimationListener(animListener);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 식당메뉴 리스트뷰와 어댑터
        listview = (ListView) findViewById(R.id.menu_listview);
        listview.setAdapter(adapter);

        //뷰페이저
        foodPager = (ViewPager) findViewById(pager);

        //뷰페이저의 전환효과(false, true에 의해서 앞뒤의 레이어 순위가 바뀜)
        foodPager.setPageTransformer(true, new DepthPageTransformer());




    } //onCreate에서 findView 이런것들 다 여기집어넣음

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //애니매이션
        overridePendingTransition(R.anim.fadein, R.anim.translate_right);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        resumeCount++;

//        long tempTime        = System.currentTimeMillis();
//        long intervalTime    = tempTime - startTime;
//
//        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
//            //백그라운드에 5분 있으면 종료
//            finish();
//        }

        Log.d("TWTW", "onCreateCount: " + resumeCount);
        //두번째 호출에서는 서버에서 다시 데이터를 받아온다
        if(resumeCount>1){

            try{
                Log.d("TWTW", "재접속");
                connect();
            }catch (Exception e){
                finish();
            }

        }

        //리뷰를 업로드 한 경우는 첫번째 페이지로 돌아옴
//        pagerPosition = 0;
        Log.d("VOVO", "onPostResume()");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    } //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )






    public void onClick_next(View v){


        //끝까지 오면 처음으로 돌아감
        if( foodPager.getCurrentItem() == foodPagerAdapter.getCount()-1 ){
            foodPager.setCurrentItem(0, false);
        }else{
            foodPager.setCurrentItem(foodPager.getCurrentItem() + 1, true);
        }


        Log.d("VPager", "뷰페이저 개수: " + foodPagerAdapter.getCount() );
        Log.d("VPager", "현재: " + foodPager.getCurrentItem() );

    } //다음메뉴로( > 버튼)
    public void onClick_heart(View v){

        //올려진 리뷰가 없을 경우 하트를 날릴 대상이 없으니 터치를 무시
        if(menu_review.isEmpty() == true){
            Log.d("CKCK", "무시무시");
            return;
        }

        //제일 최근것이 제일 앞에 있으므로
        int index = (PAGER_NUM -1) - pagerPosition ;

        Log.d("BTBT", "글번호: " + menu_review.get(index).getNo());
        Log.d("BTBT", "내용: " + menu_review.get(index).getMemo());




        //=================================================
        //선택하는 부분
        //=================================================
        if(heartOn==false && fuckOn==false){

            //하트만 +1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 1, 0); //Server에 반영
            temp_vote.setData(index, 1, 0); //여기에 값을 넣어두었다가 서버에 정상적으로 올라가면 App데이터 변경
        }else if(heartOn==true && fuckOn==false){

            //하트가 있는경우 -1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), -1, 0); //Server에 반영
            temp_vote.setData(index, -1, 0);

        }else if(heartOn==false && fuckOn==true){

            //빠큐 없에고 하트+1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 1, -1); //Server에 반영
            temp_vote.setData(index, 1, -1);
        }


//        if(heartOn==false){ //하트+1
//            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 1, 0); //Server에 반영
//            temp_vote.setData(index, 1, 0); //여기에 값을 넣어두었다가 서버에 정상적으로 올라가면 App데이터 변경
//         }else{ //이미 눌러진 거 취소(-1)
//            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), -1, 0); //Server에 반영
//            temp_vote.setData(index, -1, 0);
//        }



    } //하트
    public void onClick_fuck(View v){

        //올려진 리뷰가 없을 경우 하트를 날릴 대상이 없으니 터치를 무시
        if(menu_review.isEmpty() == true){
            Log.d("CKCK", "무시무시");
            return;
        }

        int index = PAGER_NUM - pagerPosition -1;



        //=================================================
        //선택하는 부분
        //=================================================
        if(heartOn==false && fuckOn==false){

            //빠큐만 +1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 0, 1); //Server에 반영
            temp_vote.setData(index, 0, 1); //여기에 값을 넣어두었다가 서버에 정상적으로 올라가면 App데이터 변경
        }else if(heartOn==false && fuckOn==true){

            //빠규가 있는경우 -1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 0, -1); //Server에 반영
            temp_vote.setData(index, 0, -1);

        }else if(heartOn==true && fuckOn==false){

            //하트 없에고 빠큐+1
            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), -1, 1); //Server에 반영
            temp_vote.setData(index, -1, 1);
        }


//        if(fuckOn==false){ //뻐큐 +1
//            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 0, 1);
//            temp_vote.setData(index, 0, 1); //여기에 값을 넣어두었다가 서버에 정상적으로 올라가면 App데이터 변경
//
//        }else{ //이미 눌러진 거 취소(-1)
//            connect_vote(menu_review.get(index).getUser_id(), menu_review.get(index).getNo(), 0, -1);
//            temp_vote.setData(index, 0, -1); //여기에 값을 넣어두었다가 서버에 정상적으로 올라가면 App데이터 변경
//        }

    } //뻐큐
    public void onClick_comment(View v){



        //리뷰가 있어야 댓글도 쓴다
        if(menu_review.size() > 0){
            int index = PAGER_NUM - pagerPosition -1;
            int review_num = menu_review.get(index).getNo();

            Log.d("DGDG", "인덱스: " + index + "리뷰넘버: "+ review_num);

            Intent iT = new Intent(this, CommentActivity.class);
            iT.putExtra("review_num", Integer.toString(review_num));
            iT.putExtra("list_position", 0); //이 페이지는 리스트가 아닌 단일페이지이므로 리스트position으로 0을 보내준다
            startActivity(iT);
        }else{
            Toast.makeText(getApplicationContext(), "리뷰가 있어야 댓글을 달 수 있어요.", Toast.LENGTH_LONG).show();
        }

    }//댓글로 연결
    public void onClick_reviewUpload(View v){

        Intent iT = new Intent(this, ReviewWrtieActivity.class);
        iT.putExtra("res_name", nowResName);
        iT.putExtra("menu_name", nowMenuName);
        iT.putExtra("callFrom", "ScrollActivity");
        startActivity(iT);

    } //업로드(+버튼)
    public void onClick_star(View v){

        int starScore = 0;

        //별점 몇점?
        switch (v.getId()){
            case R.id.star1:
                starScore = 1;
                break;
            case R.id.star2:
                starScore = 2;
                break;
            case R.id.star3:
                starScore = 3;
                break;
            case R.id.star4:
                starScore = 4;
                break;
            case R.id.star5:
                starScore = 5;
                break;
        }

        //서버에 해당 사용자의 해당 식당에 대한 별점을 반영
        //서버로 아이디, 식당, 별점을 전송, 서버에서 해당 아이디가 처음인지, 아닌지를 판단하여 DB에 넣음
        connect_star(starScore);
        temp_star.setData(starScore); //임시로 데이터를 여기 저장해놨다가 서버에 정상적으로 업로드 되면 콜백에서 인식하고 앱에 적용
        Log.d("badday", "찍은 별수:" + starScore);

    } //별점 매기기
    public void onClick_recommend(View v){

        //추천메뉴 정보
        final List<MenuRes_Info> mrI =  GlobalApplication.getMainData2().get(0).getMenuresInfo();

        Intent iT = new Intent(getApplicationContext(), ScrollActivity.class);

        //현재 액티비티는 끈다
        finish();

        switch (v.getId()){
            case R.id.recommendLinear1:
                iT.putExtra("res_name", mrI.get(0).getRes_name());
                iT.putExtra("menu_name", mrI.get(0).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear2:
                iT.putExtra("res_name", mrI.get(1).getRes_name());
                iT.putExtra("menu_name", mrI.get(1).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear3:
                iT.putExtra("res_name", mrI.get(2).getRes_name());
                iT.putExtra("menu_name", mrI.get(2).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear4:
                iT.putExtra("res_name", mrI.get(3).getRes_name());
                iT.putExtra("menu_name", mrI.get(3).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear5:
                iT.putExtra("res_name", mrI.get(4).getRes_name());
                iT.putExtra("menu_name", mrI.get(4).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear6:
                iT.putExtra("res_name", mrI.get(5).getRes_name());
                iT.putExtra("menu_name", mrI.get(5).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear7:
                iT.putExtra("res_name", mrI.get(6).getRes_name());
                iT.putExtra("menu_name", mrI.get(6).getMenu_name());
                startActivity(iT);
                break;
            case R.id.recommendLinear8:
                iT.putExtra("res_name", mrI.get(7).getRes_name());
                iT.putExtra("menu_name", mrI.get(7).getMenu_name());
                startActivity(iT);
                break;

        }
    }//제일 하단의 추천메뉴
    public void onClick_resMoreInfo(View v){
        Intent iT = new Intent(getApplicationContext(), MapActivity.class);
        iT.putExtra("res_latitude", res_info.get(0).getLatitude() );
        iT.putExtra("res_longitude", res_info.get(0).getLongitude() );
        iT.putExtra("res_location", res_info.get(0).getLocation() );
        iT.putExtra("res_name", res_info.get(0).getName() );
        iT.putExtra("res_phone", res_info.get(0).getPhone() );
        startActivity(iT);
    }//식당사진을 눌러서 상세정보보기(지도)
    public void onClick_scroll_faceIv(View v){

        if(menu_review.size() == 0){

            //작성된 리뷰가 하나도 없을 때:
        }
        else if(menu_review.size() > 0) { //1개이상의 리뷰가 있는 경우
            int index = ( menu_review.size() - 1 )- pagerPosition ;


            Log.d("PPPD", menu_review.get(index).getUser_nickname());
            Log.d("PPPD", menu_review.get(index).getUser_profile());
            Log.d("PPPD", "인덱스: " + index);

            Intent iT = new Intent(this, ProfileImgActivity.class);
            iT.putExtra("nickname", menu_review.get(index).getUser_nickname() );
            iT.putExtra("image_path", menu_review.get(index).getUser_profile() );
            startActivity(iT);
        }
    }//프로필 얼굴 클릭






    public void connect(){

        String url = "http://kumchurk.ivyro.net/app/download_menupage.php";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("menu_name", nowMenuName);
        params.put("res_name", nowResName);
        params.put("user_id", GlobalApplication.getUser_id() );


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //데이터 다운로드 volley
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("JSJS", response);
                jsonToJava(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "(기본)에러: "+ error);
            }
        };
    } //데이터 다운로드 callback
    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        Gson gson = new Gson();

        menuInfo = gson.fromJson(jsonStr, MenuInfo_JSON.class);
        menu_review = menuInfo.menuReview;
        res_info = menuInfo.resInfo;
        menu_list = menuInfo.menuList;
        menu_recommend = menuInfo.menuRecommend;
        menu_review_vote = menuInfo.menuReviewVote;
        res_vote = menuInfo.resVote;

        Log.d("DWDW", "" + jsonStr);




        //menu_review_vote객체의 기본값들을 설정해준다(서버에서는 본인이 투표한 값들에 대한 객체만 있는데, 투표한 적이 없는 객체도 0,0으로 다 생성해준다)
        voteSetting();

        //menulist를 category2(소뷴류)에 따라 각각의 집단으로 나눈다.
        menuCategorize();

        //객체화된 데이터을 정리하여 화면에 표시
        showingData_fromVolly();

        Log.d("DWDW", "데이터 화면에 표시");
    } //다운받은 Json데이터들을 객체화
    public void showingData_fromVolly(){

        //뷰페이저 설정
        for(int i=0; i<menu_list.size(); i++){
            if(nowMenuName.equals( menu_list.get(i).getName() )){
                double price = menu_list.get(i).getPrice();
                double price2 = menu_list.get(i).getPrice2();
                double price3 = menu_list.get(i).getPrice3();
                String strPrice = displayPrice(price, price2, price3);
                overTv.setText(nowMenuName + " "+ strPrice);
            }

        }

        Log.d("VOVO", "showingData_fromVolly()");

        //뷰페이저에 표시해야 되는 개수

        int pagerSize;
        if(menu_review.size()>0){
            pagerSize = menu_review.size();
        }else{
            pagerSize = 0;
            Log.d("MEME", "하나도 없는거");
        }

        if(pagerSize==0){//리뷰가 하나도 없으면 기본사진 1개를 업로드할 자리 1개가 필요
            Log.d( "MEME", "리뷰가 하나도 없음" );

            heart_tv.setText("");
            fuck_tv.setText("");
            reviewTv.setText("아무도 리뷰를 올리지 않은 미지의 메뉴입니다. \n처음으로 리뷰를 올려보세요!");
            PAGER_NUM = 1;
        }
        else{
            Log.d( "MEME", "가장 최근 올린글: " + menu_review.get(pagerSize-1).getMemo() );
            PAGER_NUM = pagerSize;
        }


        //최상단에서 메뉴사진을 보여주는 뷰페이저에 메뉴리뷰(menu_review) 객체를 전달한다
        //리뷰가 하나도 없는 것의 사진경로를 지정하기 위해 메뉴이름과 식당이름도 같이 보내준다.
        FoodPagerFragment.PlaceholderFragment.addMenu(menu_review, nowMenuName, nowResName);

        //뷰페이저의 어댑터를 생성한다(volley로부터 데이터를 받은 이후에 그것을 토대로 Pager의 페이지수를 정하고 어댑터를 생성한다)
        foodPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        foodPager.setAdapter(foodPagerAdapter);



        //처음 호출하는 경우에만 호출
        if(resumeCount==1){
            addResInfo(); //식당정보 출력
            addMenuList(); //메뉴리스트 출력
            addRecommendMenu(); //추천메뉴리스트 출력
        }else{
            foodPager.setCurrentItem(pagerPosition, true);
        }

        addReview(); //메뉴리뷰 출력



    } //volley에서 받고 객체화된 데이터를 이용하여 화면에 표시


    public void connect_vote(String voted_id, int review_num, int heart, int fuck){

        String url = UPLOAD_URL2;

        Log.d("ECEC", "내아이디: " + GlobalApplication.getUser_id() );

        //아이디, 리뷰쓴사람 id, 리뷰글번호, 메뉴이름, 식당이름, 하트, 뻐큐(시간은 웹에서 처리)
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("voted_id", voted_id );
        params.put("review_num", Integer.toString(review_num) );
        params.put("menu_name", nowMenuName );
        params.put("res_name", nowResName );
        params.put("heart", Integer.toString(heart) );
        params.put("fuck", Integer.toString(fuck) );


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback2, this, url, params);
    } //좋아요 싫어요 투표 volley
    void initVolleyCallback2(){
        mResultCallback2 = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분.
                Log.d("VOTE", ""+response);

                //서버에 정상적으로 올라갔으니 tempVote에서 임시로 저장해둔 업로드 데이터를 앱에 적용
                inputTempVote();
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "(vote)에러: "+ error);
            }
        };
    }//좋아요 빠큐 투표 callback
    public void inputTempVote(){

        int index = temp_vote.getIndex();
        int heart = temp_vote.getHeart();
        int fuck = temp_vote.getFuck();

        //하트조작
        if(heart == 1 || heart == -1){
            menu_review.get(index).setHeart( menu_review.get(index).getHeart()+ heart ); //menu_review 객체 수정

            //menu_review_vote 객체수정
            for(int i=0; i<menu_review_vote.size(); i++){
                if(menu_review.get(index).getNo() == menu_review_vote.get(i).getReview_num()){
                    if(heart == 1){
                        menu_review_vote.get(i).setHeart(1);
                    }else if(heart == -1) {
                        menu_review_vote.get(i).setHeart(0);
                    }
                }
            }//for 끝
        }//하트조작 if끝

        if(fuck == 1 || fuck == -1){
            menu_review.get(index).setFuck( menu_review.get(index).getFuck()+ fuck ); //menu_review 객체 수정

            //menu_review_vote 객체수정
            for(int i=0; i<menu_review_vote.size(); i++){
                if(menu_review.get(index).getNo() == menu_review_vote.get(i).getReview_num()){
                    if(fuck == 1){
                        menu_review_vote.get(i).setFuck(1);
                    }else if(fuck == -1) {
                        menu_review_vote.get(i).setFuck(0);
                    }
                }
            }//for 끝
        }//fuck조작 if끝

        addReview();



    } //임시로 저장해두었던 하트, 빠큐 데이터를 앱에 반영


    public void connect_star(int score){

        String url = "http://kumchurk.ivyro.net/app/upload_res_vote.php";

        //서버로 아이디, 식당이름, 별점을 전송한다
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("res_name", nowResName );
        params.put("score", Integer.toString(score) );

        Log.d("badday", "올린별수: " + Integer.toString(score));

        //값을 받아올 리스너(사용은 안함), Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback3, this, url, params);
    } //별점투표 volley
    void initVolleyCallback3(){
        mResultCallback3 = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분.
                Log.d("VOTE", ""+response);

                //서버에 정상적으로 올라갔으니 tempVote에서 임시로 저장해둔 업로드 데이터를 앱에 적용
                inputTempStar();
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "(vote)에러: "+ error);
            }
        };
    }//별점투표 callback
    public void inputTempStar(){

        int starScore = temp_star.getStar();

        //이전에 투표한 적이 없는경우(투표정보에 대한 객체가 아예없음) -> 앱의 투표정보 객체를 하나 만들고 투표정보를 추가 & 웹에 업로드(결과는 안받음, 걍 보내고 끝)
        if( res_vote == null ){
            Log.d("STST", "이 식당에 별점투표 첫경험");
            Res_Vote rv = new Res_Vote(starScore);

            res_vote = new ArrayList<>();
            res_vote.add(rv);

            res_info.get(0).setStar( res_info.get(0).getStar() + starScore ); //별점총합에 자신의 별점 더함
            res_info.get(0).setStar_num( res_info.get(0).getStar_num()+1 ); //별텀투표자수 +1

        }else{ //이전에 투표한 적이 있는경우(객체에 투표정보가 있는경우) -> 앱의 투표정보 객체를 업데이트 & 웹에 업로드(결과는 안받음, 걍 보내고 끝)

            int beforeStarScore = res_vote.get(0).getScore(); //전에 자신이 투표했던 점수
            res_info.get(0).setStar( res_info.get(0).getStar() - beforeStarScore + starScore ); //별점총합 업데이트
            res_vote.get(0).setScore(starScore); //자신의 투표점수도 업데이트
        }

        //식당정보 표시 업로드
        addResInfo();
    } //임시로 저장해두었던 별점 데이터를 앱에 반영


    //해당 식당의 메뉴들을 표시
    public void addMenuList(){

        //메뉴리스트 제목설정
        menulistTitleTv.setText("" + res_info.get(0).getName() + "의 메뉴");

        //===============================
        //리스트에 소분류 별로 넣는다
        //===============================

        for(int i=0; i<menu_list_set.size(); i++){ //소분류의 제목을 리스트에 삽입

            Log.d("LSLS", "리스트뷰입력: "+ menu_list_set.get(i).category_name);

            String category2 = menu_list_set.get(i).category_name;
            adapter.addItem(false, "", "", "",
                    false, "", "", "",
                    true, category2,
                    nowResName);

            for(int j=0; j<menu_list_set.get(i).c_menu_list.size(); j=j+2){ //각 소분류에 속하는 메뉴들을 리스트에 삽입

                //왼쪽꺼
                String left_name = menu_list_set.get(i).c_menu_list.get(j).getName();
                double left_price = menu_list_set.get(i).c_menu_list.get(j).getPrice();
                double left_price2 = menu_list_set.get(i).c_menu_list.get(j).getPrice2();
                double left_price3 = menu_list_set.get(i).c_menu_list.get(j).getPrice3();
                String l_price = displayPrice(left_price, left_price2, left_price3);
                String left_img = menu_list_set.get(i).c_menu_list.get(j).getImage();
//
//
//                //리스트가 두개씩 표시하도록 만들었기 때문에 왼쪽것도 넣어준다(마지막꺼가 1개만 남을경우 가로로 길게 표시)
                if(j+1 < menu_list_set.get(i).c_menu_list.size()) {

                    //오른쪽꺼
                    String right_name = menu_list_set.get(i).c_menu_list.get(j + 1).getName();
                    double right_price = menu_list_set.get(i).c_menu_list.get(j + 1).getPrice();
                    double right_price2 = menu_list_set.get(i).c_menu_list.get(j + 1).getPrice2();
                    double right_price3 = menu_list_set.get(i).c_menu_list.get(j + 1).getPrice3();
                    String r_price = SimpleFunction.displayPrice(right_price, right_price2, right_price3);
                    String right_img = menu_list_set.get(i).c_menu_list.get(j + 1).getImage();

                    //리스트에 두개 집어넣음
                    adapter.addItem(true, left_name, l_price, left_img,
                            true, right_name, r_price, right_img,
                            false, "",
                            nowResName);
                }
                else{

                    //리스에 하나(길쭉하게)집어넣음
                    adapter.addItem(true, left_name, l_price, left_img,
                            false, "", "", "",
                            false, "",
                            nowResName);
                }

            }//for문(j) 끝
        }//for문(i) 끝



        SimpleFunction.setListViewHeightBasedOnChildren(listview);
    }

    //식당정보표시
    public void addResInfo(){

        //거리
        String distance="위치정보없음";
        int dis = SimpleFunction.distance(res_info.get(0).getLatitude(), res_info.get(0).getLongitude(), "meter");

        if(dis!=-1){//gps가 제대로 구해지지 않으면 -1을 리턴한다(SimpleFunction클래스 참조)
            distance = dis + "m";
        }


        //별점
        String star = "";
        double starNum = ( res_info.get(0).getStar() / (double)res_info.get(0).getStar_num() );
        starNum = Double.parseDouble(String.format("%.1f", starNum)); //소수 첫째자리까지 표시

        //아무도 이 식당에 투표한 적이 없음
        if(res_info.get(0).getStar_num() == 0){
            starNum = 0;
        }


        if(starNum >4.5) {
            star = "★★★★★";
        }else if(starNum >= 4){
            star = "★★★★☆";
        }else if(starNum >= 3){
            star = "★★★☆☆";
        }else if(starNum >= 2){
            star = "★★☆☆☆";
        }else if(0 < starNum && starNum < 2){
            star = "凸凸凸凸凸";
        }else{
            star = "☆☆☆☆☆";
        }




        //내가 이 식당에 투표를 했던 적이 있으면 그 정보를 표시
        if( res_vote != null ){
            Log.d("STST", "이전투표 : " + res_vote.get(0).getScore() );
            fillStar( res_vote.get(0).getScore() );

        }else{
            Log.d("STST", "투표한 적이 없다");
        }



        //식당 영업시간
        String displayTime=""; //남은 시간을 표시해줌
        String time = res_info.get(0).getTime(); //서버에서 받은 HHmmHHmm으로 된 시간데이터
        String resOpenTime = time.substring(0,2) + ":" + time.substring(2,4) + "-" + time.substring(4,6) + ":" + time.substring(6,8);


        SimpleDateFormat sdf = new SimpleDateFormat ( "HHmm", Locale.KOREA ); //시간형식
        Time nowT  = new Time(System.currentTimeMillis());
        sdf.format(nowT);

        try{
            //날짜 때문에 시간차이가 이상하게 벌어질 수 있음. 이런식으로 시간만 입력하면, 1970년 1월 1일로 세팅되서 동일날짜에서 시간만 비교가능
            java.util.Date nowTime = sdf.parse( sdf.format(nowT) ); //현재시간
            java.util.Date openTime = sdf.parse(time.substring(0,4)); //식당 오픈시간
            java.util.Date closeTime = sdf.parse(time.substring(4,8)); //식당 마감시간

            Log.d("TITI", nowTime + " " + openTime  + " " + closeTime + " ");

            long diff, diffMin, diffHour; //시간차(밀리초, 분, 시간)


            if(nowTime.getTime() < openTime.getTime()){//오픈 전
                diff = openTime.getTime() - nowTime.getTime();
                diffMin = diff/(60*1000)%60;
                diffHour = diff/(60*60*1000);
                displayTime = "(오픈까지 "+diffHour+"시간 " + diffMin +"분 남았습니다)";

            }else if(openTime.getTime() <= nowTime.getTime() && nowTime.getTime() <= closeTime.getTime()){ //영업중

                diff = closeTime.getTime() - nowTime.getTime();
                diffMin = (diff/(60*1000))%60;
                diffHour = diff/(60*60*1000);

                if(diffHour > 3){ //마감까지 3시간 이상 남았을때
                    displayTime = "";
                }else{ //마감까지 3시간 이하로 남았을때
                        if(diffHour==0){
                            displayTime = "(마감까지 "+ diffMin +"분 남았습니다)";
                        }else{
                            displayTime = "(마감까지 "+diffHour+"시간 " + diffMin +"분 남았습니다)";
                        }
                    }

            }else if(closeTime.getTime() < nowTime.getTime()){//마감
                displayTime = "(영업시간 종료되었습니다)";
            }

        }catch (Exception e){}




        //메뉴리뷰들의 하트와 뻐큐, 댓글수(미구현)를 모두 더한 값을 구한다
        int sumHeart = 0;
        int sumFuck = 0;
        int sumComment = 0;
        for(int i=0; i<menu_review.size(); i++){
            sumHeart = sumHeart + menu_review.get(i).getHeart();
            sumFuck = sumFuck + menu_review.get(i).getFuck();
            sumComment = sumComment + menu_review.get(i).getComment();
        }

        //식당의 정보출력
        resNameTv.setText( res_info.get(0).getName() );
        resInfoTv.setText( res_info.get(0).getCategory() + " / " + resOpenTime );
        resThemeTv.setText( res_info.get(0).getTheme() );
        resLeftTimeTv.setText( displayTime );
        resStarTv.setText( star );
        resStarNumTv.setText( "(" + starNum +")" );
//        resSumHeartTv.setText( ""+sumHeart );
//        resSumFuckTv.setText( ""+sumFuck );
//        resSumCommentTv.setText( ""+sumComment );
        resDistanceTv.setText( distance );

        Log.d("PICPIC", "식당이미지:"+res_info.get(0).getRes_image());
        Log.d("PICPIC", "식당이미지:"+res_info.get(0).getRes_image2());

//        식당 이미지 출력
        Glide.with(this)
                .load( "http://" + res_info.get(0).getRes_image() )
                .placeholder(R.drawable.res_loading)
                .centerCrop()
                .thumbnail(0.3f)
                .into(resPicIv);
        resPicIv.setScaleType(ImageView.ScaleType.FIT_XY);

//        식당 이미지 출력2
        Glide.with(this)
                .load( "http://" + res_info.get(0).getRes_image2() )
                .placeholder(R.drawable.res_loading)
                .centerCrop()
                .thumbnail(0.3f)
                .into(resPicIv2);
        resPicIv2.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    //추천메뉴 표시
    public void addRecommendMenu(){

        //번호는 페이지대로 나눈 것이고, 0페이지는 추천메뉴 8개
        final List<MenuRes_Info> sMenu =  GlobalApplication.getMainData2().get(0).getMenuresInfo();


        for(int i=0; i<8; i++){

            //추천메뉴이미지
            Glide.with(this)
                    .load( sMenu.get(i).getMenu_image() )
                    .placeholder(R.drawable.loading)
                    .centerCrop()
                    .thumbnail(0.3f)
                    .into(recommendIv[i]);
            recommendIv[i].setScaleType(ImageView.ScaleType.FIT_XY);

            recommend_nameTv[i].setText(sMenu.get(i).getMenu_name());

            double price1 = sMenu.get(i).getMenu_price();
            double price2 = sMenu.get(i).getMenu_price2();
            double price3 = sMenu.get(i).getMenu_price3();
            String price = SimpleFunction.displayPrice(price1, price2, price3);
            recommend_priceTv[i].setText( price );
            recommend_resTv[i].setText("'" + sMenu.get(i).getRes_name() +"'");
        }



        //클릭이벤트는 onClick이벤트로 따로 모아놓았음



    }

    //리뷰부분 표시
    public void addReview(){

        //현재의 viewPager가 몇번째인지 사진 오른쪽 하단에 표시하는 TextView
        viewpagerCount.setText(pagerPosition+1 + "/" + foodPagerAdapter.getCount() );

        //메뉴가 1개이하면 넘기는 화살표 버튼 안보임
        if(menu_review.size() < 2){
            nextArrowIv.setVisibility(View.GONE);
        }



        if(menu_review.size() == 0){

            //작성된 리뷰가 하나도 없을 때
            comment_connect_tv.setVisibility(View.GONE); //리뷰글 밑에 댓글정보 안보임

        }
        else if(menu_review.size() > 0) { //1개이상의 리뷰가 있는 경우 (하트빠큐 버튼 밑의 숫자도 여기서 표시)

            //제일 마지막으로 올린 리뷰가 제일 앞에 와야하므로 뒤집어준다
            int index = ( menu_review.size() - 1 )- pagerPosition ;

            //해당 리뷰의 하트랑 빠큐개수
            int heart = menu_review.get(index).getHeart();
            int fuck = menu_review.get(index).getFuck();
            int comment = menu_review.get(index).getComment();

            //버튼 밑의 숫자
            heart_tv.setText("" + heart);
            fuck_tv.setText("" + fuck);
            comment_tv.setText("" + comment);

            //닉네임
            idTv.setText( menu_review.get(index).getUser_nickname() );

            //프사
            Glide.with(this)
                    .load( menu_review.get(index).getUser_thumbnail() )
                    .placeholder(R.drawable.face_basic)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .thumbnail(0.3f)
                    .into(faceIv);
            faceIv.setScaleType(ImageView.ScaleType.FIT_XY);



            //리뷰내용 표시
            reviewTv.setText(menu_review.get(index).getMemo());

            //댓글유도 텍스트
            if(comment == 0){
                comment_connect_tv.setText("댓글 달기");
            }else{
                comment_connect_tv.setText(comment + "개의 댓글 모두보기");
            }


            //예전에 투표한 흔적을 보여줌
            myHeartFuckVote(index);
        }



    }

    //내가 투표했던 하트,빠큐 내역을 각 리뷰에 적용 [refresh에서 호출됨]
    public void myHeartFuckVote(int index){

        //menu_review와 menu_review_vote의 정보들을 대조하여 특정 리뷰에 내가 투표를 했었는지를 판별하고 처리한다

        //menu_review_vote 객체의 크기를 구함
        int mrvSize=0;
        if(menu_review_vote != null){
            mrvSize = menu_review_vote.size();
        }

        //일반적인 경우 초기화
        heart_iv.setImageResource(R.drawable.small_menu_heart);
        fuck_iv.setImageResource(R.drawable.small_menu_fuck);
        heartOn = false;
        fuckOn = false;

        //투표를 한적이 있으면 아래의 과정을 통해 투표를 했음을 표시
        for(int i=0; i<mrvSize; i++){

            if(menu_review_vote.get(i).getReview_num() == menu_review.get(index).getNo()){

                if(menu_review_vote.get(i).getHeart() > 0 ){
                    Log.d("RVRV", "하트: " + menu_review_vote.get(i).getHeart() );
                    heart_iv.setImageResource(R.drawable.small_menu_heart2);
                    heartOn = true;
                }

                if(menu_review_vote.get(i).getFuck() > 0 ){
                    Log.d("RVRV", "빠큐: " + menu_review_vote.get(i).getFuck() );
                    fuck_iv.setImageResource(R.drawable.small_menu_fuck2);
                    fuckOn = true;
                }
            }
        }//for end
    }








    //스크롤이 변화하였을때의 처리 - onCreate안의 스크롤리스너에서 여기를 호출
    public void scrollChanged(){

        try{
            float scrollY = scrollView.getScrollY();
            Log.d("SCSC", "scroll: " + scrollY);


            //최상단부 메뉴 그림의 스크롤에 따른 변환효과
            if(scrollY < 500){
                foodPager.setScaleX( 1 + (scrollY/2500) );
                foodPager.setScaleY( 1 + (scrollY/2500) );
                overTv.setAlpha(1 - (scrollY/500));
                foodPager.setTranslationY(scrollY*1.1f);
                foodCommentLayout.setAlpha(1-scrollY/1600);
            }
            else if(scrollY < 1600){
                foodPager.setScaleX( 1.1f + (scrollY/5000) );
                foodPager.setScaleY( 1.1f + (scrollY/5000) );
                foodPager.setTranslationY(scrollY*1.1f);
                foodCommentLayout.setAlpha(1-scrollY/1600);
            }


            //식당소개부분의 작동범위
            if(900 < scrollY && scrollY <1500) {

                if (resAnimCount == 0){ //등장하지 않음
                    if(scrollY > scroll_Location_Check){//아래로 스크롤 했을때
                        resAnimCount = 1;
                        resPicFilter.setVisibility(View.VISIBLE);
                        resPicFilter.startAnimation(translateRightAnim);
                        restrantInfoLinear.setVisibility(View.VISIBLE);
                        restrantInfoLinear.startAnimation(fadeIn);
                    }
                }

                else if(resAnimCount==2){//등장이 완료되었을때

//                    if(scrollY < scroll_Location_Check){//위로 스크롤 했을때
//                        Log.d("SCSC", "위로스크롤");
//                        resAnimCount=3;
//                        resPicFilter.startAnimation(fadeOut);
//                        resPicIv.startAnimation(fadeOut);
//                        resPicIv2.startAnimation(fadeOut);
//                        restrantInfoLinear.startAnimation(fadeOut);
//                    }
                }

                scroll_Location_Check = scrollY; //스크롤의 위치를 체크한 뒤 다음 루프에서 검사
            }


            //식당사진 두개가 스크롤에 따라 바뀜
            if(1900 < scrollY && scrollY <2200){
                resPicIv.setAlpha( (2200-scrollY)/300 );
            }
            if(1900 < scrollY && scrollY <2200){
                resPicIv2.setAlpha( (scrollY-1900)/300);
            }

        }catch(Exception e){Log.d("DDD", "오류: " + e);}

    }

    //뷰페이저의 어댑터
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FoodPagerFragment.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            //뷰페이저의 총 페이지수를 리턴한다
            return PAGER_NUM;
        }
    }

    //애니매이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d("SCSCL", "애니매이션End 리스너: " + resAnimCount);

            if(resAnimCount==1){
                resPicIv.setVisibility(View.VISIBLE);
                resPicIv2.setVisibility(View.VISIBLE);

                resPicIv.setAnimation(fadeIn);
                resAnimCount=2;
                Log.d("SCSCL", "이거 왜 안돼 시발");

            }else if(resAnimCount==3){
                //위로 스크롤한 경우
                resPicFilter.setVisibility(View.INVISIBLE);

                resPicIv.setVisibility(View.INVISIBLE);
                resPicIv2.setVisibility(View.INVISIBLE);

                restrantInfoLinear.setVisibility(View.INVISIBLE);
                resAnimCount=0;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    //dp를 px로 변환
    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    //별 색칠, 별 위의 멘트
    public void fillStar(int starScore){
        //리셋후에
        for(int i=0; i<5; i++){
            starIv[i].setImageResource(R.drawable.star);
        }

        //누른것만큼 별을 색칠
        for(int i=0; i<starScore; i++){
            starIv[i].setImageResource(R.drawable.star2);
        }

        switch (starScore){
            case 1:
                starScoreTv.setText("- 최악 -");
                break;
            case 2:
                starScoreTv.setText("- 다소 실망스러움 -");
                break;
            case 3:
                starScoreTv.setText("- 쏘쏘한 곳 -");
                break;
            case 4:
                starScoreTv.setText("- 괜찮은 곳 -");
                break;
            case 5:
                starScoreTv.setText("- 전설 -");
                break;
        }
    }

    //하트, 빠큐 투표 기록 객체 세팅
    public void voteSetting(){
        //menu_review_vote에는 투표 하지 않은 건 아예 값이 없음(DB에는 투표 한 것만 저장)

        //투표를 하지 않았더라도 menu_review 개수에 맞춰서 heart:0, fuck:0으로 객체를 만들어준다.
        if(menu_review.size() > 0) {

            Log.d("SSIBAL", "시발: " + menu_review.size() );
            Log.d("SSIBAL", "시발: " + menu_review.get(0).getMemo() );

            //[첫번째것만 따로]
            //menu_review_vote가 아예없으면 size를 구할 수 없으므로, 첫번째걸로 하나 만들어주도록 한다
            if (menu_review_vote == null) {
                String user_id = menu_review.get(0).getUser_id(); //내가 투표한 글을 쓴 사람의 아이디
                int review_num = menu_review.get(0).getNo(); //투표한 글 번호

                menu_review_vote = new ArrayList<>();
                menu_review_vote.add(new Menu_Review_Vote(user_id, review_num, 0, 0));
            }

            //menu_review와 대조하여 값이 벖으면 menu_review_vote에 객체 추가(0번째는 위에서 했으니 1번부터 함)
            for (int i = 1; i < menu_review.size(); i++) {

                //1. 투표객체가 있는지 확인하고
                boolean exist_voteinfo = false;
                for (int j = 0; j < menu_review_vote.size(); j++) {
                    if (menu_review.get(i).getNo() == menu_review_vote.get(j).getReview_num()) {
                        exist_voteinfo = true;
                    }
                }

                //2. 없으면 하나 만들어준다
                if (exist_voteinfo == false) {
                    String user_id = menu_review.get(i).getUser_id();
                    int review_num = menu_review.get(i).getNo();
                    menu_review_vote.add(new Menu_Review_Vote(user_id, review_num, 0, 0));
                }
            }

            //객체들 제대로 생성되었는지구경좀 해보자
            for (int i = 0; i < menu_review_vote.size(); i++) {
                Log.d("VOVO", "투표대상의 글번호: " + menu_review_vote.get(i).getReview_num());
            }
        }


    }

    //식당메뉴리스트(menu_list)를 category2에 따라 각각의 집단으로 분류한다
    public void menuCategorize(){

        //분류를 담을 잡단(리스트 하나당 하나의 집단)
        menu_list_set = new ArrayList<>();

        Log.d("DWDW", "메뉴 카테고리별 분류완료1");

        //첫빠따의 것을 하나의 집단으로 등록
        menu_list_set.add( new Classified_Menu_List() );
        menu_list_set.get(0).category_name = menu_list.get(0).getCategory2();
        menu_list_set.get(0).c_menu_list.add( menu_list.get(0) );

        Log.d("DWDW", "메뉴 카테고리별 분류완료2");

        //기존의 것과 비교하면서 분류한다
        for(int i=1; i<menu_list.size(); i++){

            boolean setExist = false;
            for(int j=0; j<menu_list_set.size(); j++){

                //menu_list_set에 이미 존재하는 카테고리일때
                if(menu_list_set.get(j).category_name.equals( menu_list.get(i).getCategory2() )){
                    menu_list_set.get(j).c_menu_list.add( menu_list.get(i) );
                    setExist = true;
                }
            }//for문 끝

            //menu_list_set에 존재하지 않는 카테고리인 경우(새로운 집합을 하나 더 추가)
            if(setExist == false){
                menu_list_set.add( new Classified_Menu_List() );
                menu_list_set.get(menu_list_set.size()-1).category_name = menu_list.get(i).getCategory2();
                menu_list_set.get(menu_list_set.size()-1).c_menu_list.add( menu_list.get(i) );
            }

        }//for문 끝


        //잘 드갔는지 한번 보까?
        for(int i=0; i<menu_list_set.size(); i++){
            Log.d( "CLCL", "분류: " + menu_list_set.get(i).category_name );
            for(int j=0; j<menu_list_set.get(i).c_menu_list.size(); j++){
                Log.d("CLCL", "     "+menu_list_set.get(i).c_menu_list.get(j).getName() );
            }
        }
    }




}//소스끝



