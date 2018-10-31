package com.kakao.kakaolink.v4;

import com.kakao.kakaolink.v4.network.KakaoLinkTemplateResponse;
import com.kakao.kakaolink.v4.network.TemplateScrapRequest;
import com.kakao.kakaolink.v4.network.TemplateScrapResponse;
import com.kakao.kakaolink.v4.network.TemplateValidateRequest;
import com.kakao.network.NetworkTask;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseData;

import java.io.IOException;

/**
 * Template validation을 하는 API를 실행하는 클래스.
 * Created by kevin.kang on 2016. 11. 25..
 */

class KakaoLinkApi {
    private NetworkTask networkTask;

    KakaoLinkApi(final NetworkTask networkTask) {
        this.networkTask = networkTask;
    }

    KakaoLinkTemplateResponse requestTemplateValidate(final TemplateValidateRequest request) throws IOException, ResponseBody.ResponseBodyException {
        ResponseData responseData =  networkTask.request(request);
        return new KakaoLinkTemplateResponse(getResponseBody(responseData));
    }

    TemplateScrapResponse requestTemplateScrap(final TemplateScrapRequest request) throws IOException, ResponseBody.ResponseBodyException {
        ResponseData responseData =  networkTask.request(request);
        return new TemplateScrapResponse(getResponseBody(responseData));
    }

    @SuppressWarnings("WeakerAccess")
    ResponseBody getResponseBody(final ResponseData responseData) throws ResponseBody.ResponseBodyException{
        return new ResponseBody(responseData.getHttpStatusCode(), responseData.getData());
    }
}
