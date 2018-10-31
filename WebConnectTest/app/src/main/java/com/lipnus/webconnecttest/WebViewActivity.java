package com.lipnus.webconnecttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URISyntaxException;

public class WebViewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);



    }

    //
    class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("app")) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            view.loadUrl(url);
            return false;
        }


    }

    //버튼클릭시
    public void goURL(View view){
        TextView tvURL = (TextView)findViewById(R.id.txtURL);
        String url = tvURL.getText().toString();
        Log.i("URL","Opening URL :"+url);

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebClient()); // 이걸 안해주면 새창이 뜸
        webView.loadUrl(url);

    }




}