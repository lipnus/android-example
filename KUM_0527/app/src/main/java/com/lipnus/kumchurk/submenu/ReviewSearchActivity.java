package com.lipnus.kumchurk.submenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.VolleyConnect;
import com.lipnus.kumchurk.data.MenuRes_Info_JSON;
import com.lipnus.kumchurk.data.main.Main_Data;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * SearchActivity를 거의 그대로 차용하였다
 */

public class ReviewSearchActivity extends AppCompatActivity {



    EditText searchEt;
    View searchV;

    //volley, 리스너
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    //리스트뷰와 어댑터
    ListView listview;
    SearchListViewAdapter adapter;

    //검색으로 나온 결과가 저장되는 객체
    Main_Data md;

    //검색을 요청하여 서버에서 받아온 메뉴와 식당정보가 들어간 리스트가 저장되는 객체
    MenuRes_Info_JSON mrIJ;

    //다른곳에서 이 액티비티를 종료할 수 있게
    public static Activity RVActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //volley callback
        initVolleyCallback();

        //리스트뷰와 어댑터
        listview = (ListView) findViewById(R.id.sl_list);
        adapter = new SearchListViewAdapter();
        listview.setAdapter(adapter);

        //아래의 공간. 검색결과나 필터가 표시
        searchV = findViewById(R.id.include_search);

        //EditText 조작
        searchEditText();

        //ScrollActivity가 Task에 쌓이지 않게 하기 위해
        RVActivity = ReviewSearchActivity.this;


    }

    //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.hide_to_right);
    }

    //왼쪽 상단의 뒤로가기
    public void onClick_BacktoMain(View v){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.hide_to_right);
    }

    //아래부분의 검은필터
    public void onClick_rv_interceptTouchFilter(View v){
        Toast.makeText(getApplicationContext(), "먼저 리뷰하실 메뉴를 검색해주세요", Toast.LENGTH_LONG).show();
    }









    //search EditText 조작
    public void searchEditText(){

        //포커스를 얻었을 때
        searchEt = (EditText) findViewById(R.id.search_et);

        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == true){
                    Log.d("FFCC", "포커스 얻음");
                    searchV.setVisibility(View.VISIBLE);
                }
            }
        });


        //텍스트에 변화가 있을 때
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String searchCode = searchEt.getText().toString();
                Log.d("SCLI", "검색어길이: " + searchCode.length());
                if(searchCode.length() > 1){
                    connect( searchCode );
                }else{
                    //1글자 이하이면 리스트뷰를 초기화 시켜버린다
                    adapter = new SearchListViewAdapter();
                    listview.setAdapter(adapter);
                }

            }
        });



        //검색버튼 눌렀을 때
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                //검색어가져오기, 키보드끄기
                String searchCode = searchEt.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:

                        //검색버튼

                        connect( searchCode ); //검색요청
                        imm.hideSoftInputFromWindow( searchEt.getWindowToken(), 0); //키보드끄기
                        break;
                    default:

                        //엔터키

                        connect( searchCode ); //검색요청
                        imm.hideSoftInputFromWindow( searchEt.getWindowToken(), 0); //키보드끄기
                        return false;
                }
                return true;


            }
        });

    }



    //volley 접속(검색정보 요청)
    public void connect(String searchCode){
        Log.d("VOVO", "접속...");
        String url = "http://kumchurk.ivyro.net/app/download_search2.php";

        Map<String, String> params = new HashMap<>();
        params.put("search_code", searchCode);


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵, [다이얼로그 사용하지 않음(3)]
        volley = new VolleyConnect(mResultCallback, this, url, params, 3);
    }

    //volley 콜백(회원정보 업로드)
    public void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                //전송의 결과를 받는 부분
                Log.d("SCLI", response);
                jsonToJava(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분

                Log.d("VOVO", "에러: "+ error);
            }
        };
    }

    //받은 json을 객체로 전환
    void jsonToJava(String jsonStr){
        Log.d("VOVO", "jsonToJava()");

        //==============================================================================
        // 검색으로 나온 결과를 저장.
        // 해당 객체는 여기 SearchActivity내부에서만 사용
        //==============================================================================

        Gson gson = new Gson();
        mrIJ =  gson.fromJson(jsonStr, MenuRes_Info_JSON.class);

        addSearchList();


    }



    //스크롤에 리스트를 다 띄워줌
    public void addSearchList(){

        Log.d("LISTER", "리스트에 추가");

        if(mrIJ.getMenuresInfo() != null && mrIJ.getMenuresInfo().size() >0){

            //리스트뷰 리셋
            adapter = new SearchListViewAdapter();
            listview.setAdapter(adapter);

            //아이템추가
            for(int i=0; i<mrIJ.getMenuresInfo().size(); i++){
                adapter.addItem( mrIJ.getMenuresInfo().get(i), "review" );
                Log.d( "LISTER", "" + mrIJ.getMenuresInfo().get(i).getMenu_name() );
            }

            adapter.notifyDataSetChanged(); //리스트 새로고침
        }

    }


}
