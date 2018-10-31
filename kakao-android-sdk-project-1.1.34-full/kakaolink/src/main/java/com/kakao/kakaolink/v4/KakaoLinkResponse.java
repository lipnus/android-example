package com.kakao.kakaolink.v4;

import org.json.JSONObject;

/**
 * Template validation에 성공하였을 때 반환되는 response.
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkResponse {
    private JSONObject templateMsg;
    private JSONObject warningMsg;

    KakaoLinkResponse(final JSONObject templateMsg, final JSONObject warningMsg) {
        this.templateMsg = templateMsg;
        this.warningMsg = warningMsg;
    }

    @SuppressWarnings("unused") // 써드 앱들이 사용하는 메소드.
    public JSONObject getTemplateMsg() {
        return templateMsg;
    }

    @SuppressWarnings("unused") // 써드 앱들이 사용하는 메소드.
    public JSONObject getWarningMsg() {
        return warningMsg;
    }
}
