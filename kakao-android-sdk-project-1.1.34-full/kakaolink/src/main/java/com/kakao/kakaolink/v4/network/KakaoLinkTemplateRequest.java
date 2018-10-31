package com.kakao.kakaolink.v4.network;

import com.kakao.network.KakaoRequest;
import com.kakao.network.RequestConfiguration;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 17..
 */

public abstract class KakaoLinkTemplateRequest extends KakaoRequest {
    String templateId;
    protected Map<String, String> templateArgs;

    KakaoLinkTemplateRequest(RequestConfiguration configuration, final String templateId, final Map<String, String> templateArgs) {
        super(configuration);
        this.templateId = templateId;
        this.templateArgs = templateArgs;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Map getTemplateArgs() {
        return templateArgs;
    }

    public String getTemplateArgsString() {
        if (templateArgs == null || templateArgs.isEmpty()) {
            return null;
        }
        JSONObject argsJson = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
                argsJson.put(entry.getKey(), entry.getValue());
            }
            return argsJson.toString();
        } catch (JSONException e) {
            Logger.e(e.toString());
        }
        return null;
    }
}
