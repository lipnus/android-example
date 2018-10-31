package com.kakao.kakaolink.v4;

import java.util.Map;

/**
 * 카카오링크 4.0 메시지를 구성하는 templateId와 templateArgs를 포함하는 value class.
 * Created by kevin.kang on 2016. 11. 25..
 */

class KakaoLinkMessage {
    private String templateId;
    private Map<String, String> templateArgs;

    KakaoLinkMessage(final String templateId, final Map<String, String> templateArgs) {
        if (templateId == null) {
            throw new NullPointerException("Template_id cannot be null.");
        }
        this.templateId = templateId;
        this.templateArgs = templateArgs;
    }

    String getTempalteId() {
        return templateId;
    }

    Map<String, String> getTemplateArgs() {
        return templateArgs;
    }
}
