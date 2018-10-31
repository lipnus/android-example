package com.kakao.kakaolink.v4.network;

import android.net.Uri;

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.network.RequestConfiguration;
import com.kakao.network.ServerProtocol;

import java.util.Map;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 17..
 */

public class TemplateScrapRequest extends KakaoLinkTemplateRequest {
    private String url;

    public TemplateScrapRequest(RequestConfiguration configuration, final String url, final String templateId, final Map<String, String> tempalateArgs) {
        super(configuration, templateId, tempalateArgs);
        this.url = url;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public String getUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ServerProtocol.SCHEME);
        builder.authority(ServerProtocol.API_AUTHORITY);
        builder.path(ServerProtocol.LINK_TEMPLATE_SCRAP_PATH);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINK_VER, "4.0");
        builder.appendQueryParameter(KakaoTalkLinkProtocol.REQUEST_URL, this.url);

        if (this.templateId != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID, templateId);
        }
        if (this.templateArgs != null && !templateArgs.isEmpty()) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS, getTemplateArgsString());
        }
        return builder.build().toString();
    }

    @Override
    public Map<String, String> getParams() {
        return super.getParams();
    }
}
