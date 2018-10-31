package com.kakao.network;

import android.net.Uri;
import android.os.Build;

import com.kakao.network.multipart.Part;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.Utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로그인이 필요하지 않을 때 사용하는 Request 클래스. 앱키 정보만 보낸다.
 * Created by kevin.kang on 2016. 11. 25..
 */

public abstract class KakaoRequest implements IRequest {
    protected static final String POST = "POST";
    protected static final String GET = "GET";
    protected static final String DELETE = "DELETE";

    protected String kaHeader;
    protected String extras;
    private String appKey;
    private String appVer;

    protected KakaoRequest(final RequestConfiguration configuration) {
        this.appKey = configuration.getAppKey();
        this.kaHeader = configuration.getKaHeader();
        this.extras = configuration.getExtras();
        this.appVer = configuration.getAppVer();
    }

    @Override
    public abstract String getMethod();

    @Override
    public abstract String getUrl();

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>();
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<String, String>();
        header.put(CommonProtocol.KA_HEADER_KEY, kaHeader);

        if (!header.containsKey("Content-Type")) {
            header.put("Content-Type", "application/x-www-form-urlencoded");
        }

        if (!header.containsKey("Accept")) {
            header.put("Accept", "*/*");
        }

        if (!header.containsKey("User-Agent")) {
            header.put("User-Agent", getHttpUserAgentString());
        }

        header.put(CommonProtocol.KA_AUTH_HEADER_KEY, CommonProtocol.KAKAO_AK_HEADER_KEY + " " + getAppKey());

        return header;
    }

    @Override
    public List<Part> getMultiPartList() {
        return Collections.emptyList();
    }

    @Override
    public String getBodyEncoding() {
        return "UTF-8";
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppVer() {
        return appVer;
    }

    public static String createBaseURL(final String authority, final String requestPath) {
        Uri uri = Utility.buildUri(authority, requestPath);
        return uri.toString();
    }

    String getHttpUserAgentString() {
        return new StringBuilder().append("os/").append(CommonProtocol.OS_ANDROID).append("-").append(Build.VERSION.SDK_INT).append(" ").toString();
    }
}
