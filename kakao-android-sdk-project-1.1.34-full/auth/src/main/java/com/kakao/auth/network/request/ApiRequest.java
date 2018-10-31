/**
 * Copyright 2014-2016 Kakao Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.auth.network.request;

import android.net.Uri;
import android.os.Build;

import com.kakao.auth.Session;
import com.kakao.network.IRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.network.multipart.Part;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.SystemInfo;
import com.kakao.util.helper.Utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leo.shin
 */
public abstract class ApiRequest implements IRequest {
    protected static final String POST = "POST";
    protected static final String GET = "GET";
    protected static final String DELETE = "DELETE";

    public abstract String getMethod();

    public abstract String getUrl();

    public Map<String, String> getParams() {
        return Collections.emptyMap();
    }

    public Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<String, String>();
        header.put(CommonProtocol.KA_HEADER_KEY, SystemInfo.getKAHeader());
        header.put(ServerProtocol.AUTHORIZATION_HEADER_KEY, getTokenAuthHeaderValue());

        if (!header.containsKey("Content-Type")) {
            header.put("Content-Type", "application/x-www-form-urlencoded");
        }

        if (!header.containsKey("Accept")) {
            header.put("Accept", "*/*");
        }

        if (!header.containsKey("User-Agent")) {
            header.put("User-Agent", getHttpUserAgentString());
        }
        return header;
    }

    @Override
    public List<Part> getMultiPartList() {
        return Collections.emptyList();
    }

    public String getBodyEncoding() {
        return "UTF-8";
    }

    private static String getTokenAuthHeaderValue() {
        final StringBuilder autorizationValue = new StringBuilder();
        autorizationValue.append(ServerProtocol.AUTHORIZATION_BEARER).append(ServerProtocol.AUTHORIZATION_HEADER_DELIMITER).append(Session.getCurrentSession().getAccessToken());
        return autorizationValue.toString();
    }

    public static String createBaseURL(final String authority, final String requestPath) {
        Uri uri = Utility.buildUri(authority, requestPath);
        return uri.toString();
    }

    protected String getHttpUserAgentString() {
        return new StringBuilder().append("os/").append(CommonProtocol.OS_ANDROID).append("-").append(Build.VERSION.SDK_INT).append(" ").toString();
    }
}
