package com.kakao.kakaolink.v4.network;

import com.kakao.network.response.ResponseBody;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Template validate response class.
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkTemplateResponse {
    private JSONObject templateMsg;
    private JSONObject warningMsg;

    public KakaoLinkTemplateResponse(ResponseBody responseBody) throws ResponseBody.ResponseBodyException {
        if (responseBody.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new KakaoException(KakaoException.ErrorType.ILLEGAL_ARGUMENT, responseBody.toString());
        }

        JSONObject resJson = responseBody.getJson();
        try {
            templateMsg = resJson.getJSONObject("template_msg");
            warningMsg = resJson.getJSONObject("warning_msg");
        } catch (JSONException e) {
            throw new KakaoException(KakaoException.ErrorType.JSON_PARSING_ERROR, "There was an error parsing response");
        }
    }

    public JSONObject getTemplateMsg() {
        return templateMsg;
    }

    public JSONObject getWarningMsg() {
        return warningMsg;
    }
}
