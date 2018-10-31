package com.kakao.sdk.link.sample.kakaolink;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.kakaolink.v4.KakaoLinkResponse;
import com.kakao.kakaolink.v4.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.sdk.link.sample.R;
import com.kakao.util.helper.log.Logger;

public class KakaoLink40MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_link40_main);

        final String[] methods = new String[] {
                "Feed template",
                "List template",
                "Scrap template"
        };

        ListView listView = (ListView) findViewById(R.id.link40_method_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, methods));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sendFeedTemplate();
                        break;
                    case 1:
                        sendListTemplate();
                        break;
                    case 2:
                        sendScrapMessage();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void sendFeedTemplate() {
        KakaoLinkService.getInstance().send(this, "2669", null, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private void sendListTemplate() {
        KakaoLinkService.getInstance().send(this, "2670", null, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private void sendScrapMessage() {
        KakaoLinkService.getInstance().sendUrl(this, "https://dev.kakao.com", new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }
}
