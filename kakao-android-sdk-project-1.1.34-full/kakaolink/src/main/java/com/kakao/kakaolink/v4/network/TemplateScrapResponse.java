package com.kakao.kakaolink.v4.network;

import com.kakao.network.response.ResponseBody;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 19..
 */

public class TemplateScrapResponse extends KakaoLinkTemplateResponse {
    private String templateId;
    private JSONObject templateArgs;

    public TemplateScrapResponse(ResponseBody responseBody) throws ResponseBody.ResponseBodyException {
        super(responseBody);
        JSONObject resJson = responseBody.getJson();
        try {
            templateId = resJson.getString("template_id");
            templateArgs = resJson.getJSONObject("template_args");
        } catch (JSONException e) {
            throw new KakaoException(KakaoException.ErrorType.JSON_PARSING_ERROR, "There was an error parsing template scrap response");
        }
    }

    public String getTemplateId() {
        return templateId;
    }
    public JSONObject getTemplateArgs() {
        return templateArgs;
    }
}
