package com.kakao.kakaolink.v4.network;

import android.net.Uri;

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.network.RequestConfiguration;
import com.kakao.network.ServerProtocol;

import java.util.Map;

/**
 * Template validation request class.
 * Created by kevin.kang on 2016. 11. 25..
 */

public class TemplateValidateRequest extends KakaoLinkTemplateRequest {
    public TemplateValidateRequest(final RequestConfiguration configuration, final String templateId, final Map<String, String> tempalteArgs) {
        super(configuration, templateId, tempalteArgs);
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
        builder.path(ServerProtocol.LINK_TEMPLATE_VALIDATE_PATH);

        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINK_VER, "4.0");
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
