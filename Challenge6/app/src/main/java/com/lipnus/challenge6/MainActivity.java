package com.lipnus.challenge6;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //슬라이드로 내려오는 주소입력창 레이아웃의 이동상태
    boolean isSlideDown = false;

    Animation moveDown;
    Animation moveUp;

    Button btn;
    Button btnSearch;

    EditText editText;

    LinearLayout SlideLayout;

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //주소입력창 레이아웃을 객체화
        SlideLayout = (LinearLayout) findViewById(R.id.SlideLayout);

        //웹뷰 객체 설정
        //==========================================================================================
        webView = (WebView) findViewById(R.id.webView);

        //밑에 세줄은 직접 페이지를 띄울 거 아니면 필요없는건가??
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebBrowserClient());
        //==========================================================================================

        //버튼 객체 설정
        btn = (Button)findViewById(R.id.btn);
        btnSearch = (Button)findViewById(R.id.serchBtn);

        //에디트텍스트 객체설정
        editText = (EditText)findViewById(R.id.edittext);

        //애니매이션 설정
        moveUp = AnimationUtils.loadAnimation(this, R.anim.move_up);
        moveDown = AnimationUtils.loadAnimation(this, R.anim.move_down);

        //각 애니매이션들을 감시하는 리스너와 연결
        SlideMovingListener slideMovingListener = new SlideMovingListener();
        moveUp.setAnimationListener(slideMovingListener);
        moveDown.setAnimationListener(slideMovingListener);

    }


    final class WebBrowserClient extends WebChromeClient{
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    public void onClick(View v){
        if(isSlideDown==false){
            //열기의 시작
            SlideLayout.setVisibility(View.VISIBLE);
            SlideLayout.startAnimation(moveDown);
        }else{
            //닫기의 시작
            SlideLayout.startAnimation(moveUp);
            SlideLayout.setVisibility(View.INVISIBLE);
        }

    }


    public void onClick_Search(View v){
        webView.loadUrl(editText.getText().toString());
    }

    /**
    애니매이션의 움직임을 감지하는 리스너이다.
     */
    private class SlideMovingListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            if(isSlideDown==false){

                btn.setText("▲");
                isSlideDown=true;
                Toast.makeText(getApplicationContext(), "주소 입력창을 열었다", Toast.LENGTH_LONG).show();
                //열기의 끝
            }else{

                btn.setText("▼");
                isSlideDown=false;
                Toast.makeText(getApplicationContext(), "주소 입력창을 닫았다", Toast.LENGTH_LONG).show();
                //닫기의 끝
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
