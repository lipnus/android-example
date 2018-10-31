package com.lipnus.kumchurk.submenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;
import com.lipnus.kumchurk.data.NewsFeed_JSON;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

public class NewsFeedActivity extends AppCompatActivity {

    //뉴스피드의 리스트뷰와 어뎁터
    ListView listview;
    NewsFeedListViewAdapter adapter;


    //volley와 리스너 (volly는 독립적인 클래스로 구현)
    IVolleyResult mResultCallback = null;
    IVolleyResult mResultCallback2 = null;
    VolleyConnect volley;

    //뉴스피드 정보
    NewsFeed_JSON nfl;

    //현재 뉴스피드에 표시된 리뷰의 개수
    int nfCount = 0;

    //투표시에 호출한곳의 리스트번호(콜백이라 직접 전달하기 애매해서 여기를 통해서 전달)
    int listPosition;

    //어댑터나 다른 액티비티에서 이 액티비티에 넘겨주는 정보
    int heartCount;
    int fuckCount;
    int commentCount;

    //상단, 서브메뉴 컨트롤
    SubMenuControl smc;

    //다른 곳에서 여기를 finish()할 수 있도록 함.
    public static Activity NFActiviry;

    //리스트뷰 바닥에 닿는거 체크
    boolean lastItemVisibleFlag = false;

    //엑티비티를 호출한 개수
    int resumeCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View thisView = this.getWindow().getDecorView() ;
        smc = new SubMenuControl(this, thisView, "NEWSFEED");

        //다른 곳에서 이 액티비티를 조작할 수 있게 함
        NFActiviry = NewsFeedActivity.this;

        //Volley 콜백함수
        initVolleyCallback();
        initVolleyCallback2();

        //뉴스피드를 표시할 리스트뷰와 컨트롤을 위한 어댑터
        listview = (ListView) findViewById(R.id.nf_listview);

        //리스트뷰 Adapter 생성
        adapter = new NewsFeedListViewAdapter();
        listview.setAdapter(adapter);

        //서버에서 데이터를 받아옴
        connect();


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
                    addMenuReview();//5개추가
                }
            }

        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    } //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )


    //취소버튼
    @Override
    public void onBackPressed() {
        smc.backPress();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


            Log.d("LSLS", "재접속");
            resumeCount++;

        if(resumeCount>1){
            Log.d("NNII", "리셋");
            nfl.newsFeed.get(listPosition).setComment( commentCount );
            adapter.notifyDataSetChanged();
        }

    }

    //상단바의 중간부분 타이틀을 터치하면 스크롤 위로 끌어올림
    public void onClicK_topmenu_title(View v){
        listview.smoothScrollToPosition( 0 );
    }



    public void connect(){
        String url = "http://kumchurk.ivyro.net/app/download_newsfeed.php";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    } //데이터 다운로드 volley

    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("NWNW", response);
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

        Gson gson = new Gson();
        nfl = gson.fromJson(jsonStr, NewsFeed_JSON.class);

        if(resumeCount==1){
            //처음호출
            Log.d("LSLS", "처음리스트그리긔");
            addMenuReview();
        }else{
            //재호출(대부분의 경우 댓글창에서 돌아온 경우)
            Log.d("LSLS", "업데이트");
            Log.d("LSLS", "댓글수: " + nfl.newsFeed.get(0).getComment() );
        }


    } //다운받은 Json데이터들을 객체화

    public void connect_vote(int position, String voted_id, String menuName, String resName, int review_num, int heart, int fuck){

        String url = "http://kumchurk.ivyro.net/app/upload_menureview_vote.php";

        Log.d("ECEC", "내아이디: " + GlobalApplication.getUser_id() );

        listPosition = position;
        heartCount = heart;
        fuckCount = fuck;


        //아이디, 리뷰쓴사람 id, 리뷰글번호, 메뉴이름, 식당이름, 하트, 뻐큐(시간은 웹에서 처리)
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("voted_id", voted_id );
        params.put("review_num", Integer.toString(review_num) );
        params.put("menu_name", menuName );
        params.put("res_name", resName );
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
                listUpdate();
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "(vote)에러: "+ error);
            }
        };
    }//좋아요 빠큐 투표 callback




    //하트랑 빠큐를 눌렀을때 결과보여주기
    public void listUpdate(){

        nfl.newsFeed.get(listPosition).setHeart( nfl.newsFeed.get(listPosition).getHeart() + heartCount );
        nfl.newsFeed.get(listPosition).setFuck( nfl.newsFeed.get(listPosition).getFuck() + fuckCount );
        nfl.newsFeed.get(listPosition).setVote_heart( nfl.newsFeed.get(listPosition).getVote_heart() + heartCount );
        nfl.newsFeed.get(listPosition).setVote_fuck( nfl.newsFeed.get(listPosition).getVote_fuck() + fuckCount );

        //업데이트
        adapter.notifyDataSetChanged();


        Log.d("LSLS", "리스트뷰업데이트");
        Log.d("LSLS", "위치: " + listPosition);
        Log.d("LSLS", "하트: " + nfl.newsFeed.get(listPosition).getHeart());
        Log.d("LSLS", "뻐큐: " + nfl.newsFeed.get(listPosition).getFuck());

        Log.d("LSLS", "투표하트: " + nfl.newsFeed.get(listPosition).getVote_heart());
        Log.d("LSLS", "투표뻐큐: " + nfl.newsFeed.get(listPosition).getVote_fuck());
    }

    //무한스크롤을 이용하여 5개씩 보여준다
    public void addMenuReview(){

        Log.d("LILI", "카운트: " + nfCount + "개수: " + nfl.newsFeed.size());

        if(nfCount+5 > nfl.newsFeed.size()){
            Toast.makeText(getApplicationContext(), "마지막이예요", Toast.LENGTH_SHORT).show();
        }else{

            for(int i=nfCount; i<nfCount+5; i++){
                Log.d("NNWW", "어떤거: " + nfl.newsFeed.get(i).getNo() );
                adapter.addItem( nfl.newsFeed.get(i) );
            }

            nfCount = nfCount+5; //리스트의 개수 5개 추가
            adapter.notifyDataSetChanged(); //리스트 새로고침

        }


    }

    //댓글 개수의 업데이트를 체크해준다(CommentActivity가 끝나면서 호출해줌)
    public void catchComment(int listPosition, int commentCount){
        this.listPosition = listPosition;
        this.commentCount = commentCount;
    }



}
