package com.kakao.network;

import android.content.Context;
import android.text.TextUtils;

import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.SystemInfo;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * API request를 보내기 위해 필요한 다양한 값들을 관리하는 클래스.
 * Created by kevin.kang on 2016. 11. 29..
 */

public class RequestConfiguration {
    private String appKey;
    private String kaHeader;
    private String keyHash;
    private String extras;
    private String appVer;
    private String packageName;

    public RequestConfiguration(String appKey, String kaHeader, String keyHash, String extras, String appVer, String packageName) {
        this.appKey = appKey;
        if (TextUtils.isEmpty(this.appKey)) {
            throw new IllegalStateException("Kakao app key should be defined in AndroidManifest.xml.");
        }
        this.keyHash = keyHash;
        if (TextUtils.isEmpty(this.keyHash)) {
            throw new IllegalStateException("Key hash is null.");
        }
        this.kaHeader = kaHeader;
        if (TextUtils.isEmpty(this.kaHeader)) {
            throw new IllegalStateException("KA Header is null.");
        }
        this.extras = extras;
        this.appVer = appVer;
        this.packageName = packageName;

    }

    public static RequestConfiguration createRequestConfiguration(final Context context) {
        String appKey = Utility.getMetadata(context, CommonProtocol.APP_KEY_PROPERTY);
        SystemInfo.initialize(context);
        String kaHeader = SystemInfo.getKAHeader();
        String keyHash = Utility.getKeyHash(context);
        String packageName = context.getPackageName();
        String extrasString;
        JSONObject extras = new JSONObject();
        try {
            extras.put(CommonProtocol.APP_PACKAGE, context.getPackageName());
            extras.put(CommonProtocol.KA_HEADER_KEY, SystemInfo.getKAHeader());
            extras.put(CommonProtocol.APP_KEY_HASH, keyHash);
            extrasString = extras.toString();
        } catch (JSONException e) {
            throw new IllegalArgumentException("JSON parsing error. Malformed parameters were provided. Detailed error message: " + e.toString());
        }
        String appVer = String.valueOf(Utility.getAppVersion(context));
        return new RequestConfiguration(appKey, kaHeader, keyHash, extrasString, appVer, packageName);
    }


    public String getAppKey() {
        return appKey;
    }
    public String getKaHeader() {
        return kaHeader;
    }
    public String getKeyHash() {
        return keyHash;
    }
    public String getExtras() {
        return extras;
    }
    public String getAppVer() {
        return appVer;
    }
    public String getPackageName() {
        return packageName;
    }
}
