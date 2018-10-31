package com.lipnus.kumchurk.detailpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;
import com.lipnus.kumchurk.data.ReviewComment_JSON;
import com.lipnus.kumchurk.kum_class.SimpleFunction;
import com.lipnus.kumchurk.submenu.NewsFeedActivity;
import com.lipnus.kumchurk.submenu.SubMenuControl;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    //댓글의 리스트뷰 어댑터
    ListView listview;
    CommentListViewAdapter adapter;


    //volley와 리스너 (volly는 독립적인 클래스로 구현)
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    //체크
    ImageView checkIv;

    //현재 표시된 앳글의 개수
    int cmCount = 0;

    //리뷰번호
    String reviewNum;

    //댓글적는곳
    EditText commentEt;

    //이 리뷰에 해당하는 댓글정보
    ReviewComment_JSON rCJ;

    //다른 곳에서 여기를 finish()할 수 있도록 함.
    public static Activity CMActiviry;

    //상단, 서브메뉴 컨트롤
    SubMenuControl smc;

    //이 댓글 페이지가 아이템인지 정보를 받음(단일 페이지인 경우 0)
    int listPosition;

    boolean lastItemVisibleFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //현재의 뷰(서브메뉴에 넘김)
        View thisView = this.getWindow().getDecorView();

        //서브메뉴 생성
        smc = new SubMenuControl(this, thisView, "COMMENT", false);

        //앞의 엑티비티로부터 리뷰번호를 받는다
        Intent iT = getIntent();
        reviewNum = iT.getExtras().getString("review_num");
        listPosition = iT.getExtras().getInt("list_position");

        //초기설정
        initSetting();

        //다른 곳에서 이 액티비티를 조작할 수 있게 함
        CMActiviry = CommentActivity.this;

        //Volley 콜백함수
        initVolleyCallback();

        //리스트뷰 설정
        initList();

        //서버에서 데이터를 받아온다
        connect();

    }


    //자질구래한 것들 초기설정
    public void initSetting(){

        checkIv = (ImageView) findViewById(R.id.cm_check_iv);
        commentEt = (EditText) findViewById(R.id.cm_et);

        Glide.with(this)
                .load( R.drawable.check )
                .into(checkIv);
        checkIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }


    //리스트 초기화
    public void initList(){

        //리스트뷰 Adapter 생성
        adapter = new CommentListViewAdapter();

        //알람를 표시할 리스트뷰와 컨트롤을 위한 어댑터
        listview = (ListView) findViewById(R.id.cm_listview);
        listview.setAdapter(adapter);

        //바닥에 닿는 것을 체크(지금은 일단 사용안함)
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    Log.d("LILI", "인생바닥침");
                }
            }

        });

    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    } //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )


    //상단바의 중간부분 타이틀을 터치하면 스크롤 위로 끌어올림
    public void onClicK_topmenu_title(View v){
        listview.smoothScrollToPosition( 0 );
    }

    public void onClick_comment_up_lr(View v){
        if(commentEt.getText().toString().equals("")){
            Toast.makeText(getApplication(), "댓글을 입력해주세요", Toast.LENGTH_LONG).show();
        }else{
            connect_comment();
            commentEt.setText("");
        }

    }



    public void connect(){

        String url = "http://kumchurk.ivyro.net/app/download_comment.php";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("review_num", reviewNum );


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //데이터 다운로드 volley

    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("ALAL", response);
                jsonToJava(response);

                //변화된 댓글개수 정보를 뉴스피드액티비티에 전해준다
                sendCommentCountToNewsFeedActivity();
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
        rCJ = gson.fromJson(jsonStr, ReviewComment_JSON.class);
        addComment();

    } //다운받은 Json데이터들을 객체화

    public void connect_comment(){

        String url = "http://kumchurk.ivyro.net/app/upload_comment.php";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("review_num", reviewNum );
        params.put("writer_id", GlobalApplication.getUser_id() );
        params.put("comment", commentEt.getText().toString() );
        params.put("updated_at", SimpleFunction.getTodayDate() );


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //댓글업로드 volley

    public void connect_delete(String cm_num){

        String url = "http://kumchurk.ivyro.net/app/upload_comment_delete.php";

        //서버로 메뉴이름, 식당이름, 아이디을 보내서 그에 해당하는 데이터를 받아온다
        Map<String, String> params = new HashMap<>();
        params.put("review_num", reviewNum );
        params.put("comment_num", cm_num);

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //댓글삭제 volley


    public void addComment(){

//        Log.d("ALAL", "카운트: " + cmCount + "개수: " + rCJ.getReviewComment().size());

        //리스트뷰 리셋
        adapter = new CommentListViewAdapter();
        listview.setAdapter(adapter);

        if(rCJ.getReviewComment() != null && rCJ.getReviewComment().size() >0){

            //댓글은 아주 많지는 않을 것이므로 일단 주~욱 띄운다
            //나중에 이 부분이 문제가 된다면 뉴스피드나 소식 부분처럼 끊어서 불러오게 하면 됨

            //아이템추가
            for(int i=0; i<rCJ.getReviewComment().size(); i++){
                adapter.addItem( rCJ.getReviewComment().get(i));
            }
        }

        adapter.notifyDataSetChanged(); //리스트 새로고침
        listview.setSelection(adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운

    }


    //댓글개수 정보를 뉴스피드 액티비티에 전해준다
    public void sendCommentCountToNewsFeedActivity(){
        //이 엑티비티가 만약 Newsfeed에서 실행된 경우, Newsfeed에게 업데이트된 댓글 수를 보내준다(리스트번호, 댓글수)
        NewsFeedActivity nfActivity = (NewsFeedActivity) NewsFeedActivity.NFActiviry;


        int commentCount;

        //댓글이 있으면 개수반환, 댓글이 없으면(list가 null)이면 0반환
        if(rCJ.getReviewComment() != null){
            commentCount = rCJ.getReviewComment().size();
        }else{
            commentCount = 0;
        }

        Log.d("NNII", "newsFeed에게 정보를 전달: " + commentCount);

        //newsFeed에게 정보를 제공한다
        nfActivity.catchComment(listPosition, commentCount);
        }

    }







