package com.lipnus.webtest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private WebView webview = null;
    private Handler mHandler = new Handler();

    Button loadButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadButton = (Button)findViewById(R.id.loadBtn);
        final EditText edit = (EditText)findViewById(R.id.urlEdit);

        //웹뷰 객체 참조
        webview = (WebView)findViewById(R.id.webview);

        //웹뷰에 WebSettings지정
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webview.setWebChromeClient(new WebBrowserClient());
        webview.addJavascriptInterface(new JavaScriptMethods(), "sample");

        //웹뷰에 샘플페이지 로딩
        webview.loadUrl("file:///android_asset/www/sample.html");


        //열기를 누르면 사용자가 입력한 url페이지 로딩
        loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 입력한 URL의 페이지 로딩
                webview.loadUrl(edit.getText().toString());
            }
        });
    }

    final class JavaScriptMethods {

        JavaScriptMethods() {

        }

        @android.webkit.JavascriptInterface
        public void clickOnFace() {
            mHandler.post(new Runnable() {
                public void run() {
                    // 버튼의 텍스트 변경
                    loadButton.setText("클릭후열기");
                    // 자바스크립트 함수 호출
                    webview.loadUrl("javascript:changeFace()");
                }
            });

        }
    }

    final class WebBrowserClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
    }
}
