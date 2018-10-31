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
package com.kakao.auth;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.kakao.auth.AuthService.AgeAuthStatus;
import com.kakao.auth.Session.RequestType;
import com.kakao.auth.api.AuthApi;
import com.kakao.auth.authorization.AuthorizationResult;
import com.kakao.auth.authorization.Authorizer.OnAuthorizationListener;
import com.kakao.auth.authorization.authcode.AuthCodeRequest;
import com.kakao.auth.authorization.authcode.AuthorizationCode;
import com.kakao.auth.authorization.authcode.GetterAuthCode;
import com.kakao.auth.network.response.ApiResponse.InsufficientScopeException;
import com.kakao.auth.network.response.ApiResponse.SessionClosedException;
import com.kakao.network.INetwork;
import com.kakao.network.IRequest;
import com.kakao.network.KakaoNetworkImpl;
import com.kakao.network.NetworkTask;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseBody.ResponseBodyException;
import com.kakao.network.response.ResponseData;
import com.kakao.util.helper.log.Logger;

/**
 * @author leo.shin
 */
public class SingleNetworkTask extends NetworkTask {
    final private INetwork network;

    public SingleNetworkTask() {
        this.network = new KakaoNetworkImpl();
    }

    public SingleNetworkTask(INetwork network) {
        this.network = network;
    }

    private static AuthCodeRequest createAuthCodeRequest(Context context, String appKey, String redirectUri, String refreshToken, String scopeParam) {
        AuthCodeRequest request = new AuthCodeRequest(context, appKey, redirectUri);
        request.putExtraHeader(StringSet.RT, refreshToken);
        request.putExtraParam(StringSet.scope, scopeParam);
        return request;
    }

    /**
     * Session의 상태를 체크하여 open상태로 만들어주는 역할을 수행.
     * @return true is succeed, false is otherwise.
     */
    private static boolean checkApiSession() {
        Session session = Session.getCurrentSession();
        // 1. session check.
        if (session.isOpened()) {
            return true;
        }

        // 2. request accessToken with refreshToken.
        // 3. update original request authorization accessToken value on header.
        if (session.isAvailableOpenByRefreshToken()) {
            try {
                AuthorizationResult result = session.requestAccessToken(RequestType.REFRESHING_ACCESS_TOKEN).get();
                return result.isSuccess();
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private static AuthorizationResult requestScopesUpdateBlocking(final Activity topActivity, final ResponseBody result) throws Exception {
        String scopeParam = null;
        if (result.has(StringSet.required_scopes)) {
            try {
                List<String> requiredScopes = result.optConvertedList(StringSet.required_scopes, ResponseBody.STRING_CONVERTER, Collections.<String>emptyList());
                StringBuilder builder = null;
                for (String scope : requiredScopes) {
                    if (builder != null) {
                        builder.append(",");
                    } else {
                        builder = new StringBuilder("");
                    }

                    builder.append(scope);
                }

                if (builder != null) {
                    scopeParam = builder.toString();
                }
            } catch (ResponseBodyException e) {
                throw new InsufficientScopeException(result);
            }
        }

        return requestScopesUpdate(topActivity, scopeParam);
    }

    private static AuthorizationResult requestScopesUpdate(final Activity topActivity, String scopeParam) throws Exception {
        final Session session = Session.getCurrentSession();
        final String refreshToken = session.getRefreshToken();
        final String appKey = session.getAppKey();
        final String redirectUri = session.getRedirectUri();
        final AuthCodeRequest authCodeRequest = createAuthCodeRequest(topActivity, appKey, redirectUri, refreshToken, scopeParam);
        final GetterAuthCode getter = GetterAuthCode.newInstanceForScopesUpdate(authCodeRequest, topActivity);
        final AtomicReference<AuthorizationResult> authorizationResult = new AtomicReference();
        final CountDownLatch lock = new CountDownLatch(1);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    // 4. Scope update Webview Dialog띄움.(사용자 동의) - AuthCode 요청.
                    getter.setOnAuthorizationListener(new OnAuthorizationListener() {
                        @Override
                        public void onAuthorizationCompletion(AuthorizationResult result) {
                            authorizationResult.set(result);
                            lock.countDown();
                        }
                    });
                    getter.start();
                } catch (Exception e) {
                    authorizationResult.set(AuthorizationResult.createAuthCodeOAuthErrorResult(e));
                    lock.countDown();
                }
            }
        });

        // scope을 갱신할때까지 기다린다.
        // 사용자가 취소를 하여도 종료.
        try {
            lock.await();
        } catch (InterruptedException ignor) {
        }

        AuthorizationResult authResult = authorizationResult.get();
        if (authResult != null && authResult.isSuccess()) {
            // 5. 새로운 authCode assign되어서 accessToken을 새로 갱신.
            final String resultRedirectURL = authResult.getRedirectURL();
            if (resultRedirectURL != null && resultRedirectURL.startsWith(authCodeRequest.getRedirectURI())) {
                AuthorizationCode authCode = AuthorizationCode.createFromRedirectedUri(authResult.getRedirectUri());
                // authorization code가 포함되지 않음
                if (!authCode.hasAuthorizationCode()) {
                    Logger.e("the result of authorization code request does not have authorization code.");
                    return authResult;
                }

                try {
                    // 3. request accessToken use new AuthCode
                    authResult = session.requestAccessTokenByAuthCode(authCode);
                    session.onAccessTokenCompleted(authResult);
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }

        if (authResult.getException() != null) {
            throw authResult.getException();
        }

        return authResult;
    }

    private static ErrorCode getErrorCode(ResponseBody responseBody) {
        try {
            // 1. check scopes error.
            if (responseBody.has(StringSet.code)) {
                return ErrorCode.valueOf(responseBody.getInt(StringSet.code));
            }
        } catch (ResponseBodyException e) {
        }
        return null;
    }

    private static Activity getTopActivity() {
        Activity topActivity = KakaoSDK.getCurrentActivity();
        if (topActivity == null) {
            // 3번까지만 retry해서 타이밍 이슈에대한 방어를 어느정도 해준다.
            int retryCount = 0;
            while(topActivity == null && retryCount < 3) {
                try {
                    retryCount++;
                    Thread.sleep(500);
                    topActivity = KakaoSDK.getCurrentActivity();
                } catch (InterruptedException e) {
                }
            }
        }
        return topActivity;
    }

    private boolean handleApiError(ResponseData result) {
        boolean retry = false;
        try {
            ResponseBody errResponseBody = new ResponseBody(result.getHttpStatusCode(), result.getData());
            if (getErrorCode(errResponseBody) == ErrorCode.INVALID_TOKEN_CODE) {
                Session session = Session.getCurrentSession();
                session.removeAccessToken();

                if (session.isAvailableOpenByRefreshToken()) {
                    AuthorizationResult refreshAccessTokenResult = session.requestAccessToken(RequestType.REFRESHING_ACCESS_TOKEN).get();
                    retry = refreshAccessTokenResult.isSuccess();
                }
            } else if (getErrorCode(errResponseBody) == ErrorCode.INVALID_SCOPE_CODE) {
                Activity topActivity = getTopActivity();
                if (topActivity != null) {
                    retry = requestScopesUpdateBlocking(topActivity, errResponseBody).isSuccess();
                }
            } else if (getErrorCode(errResponseBody) == ErrorCode.NEED_TO_AGE_AUTHENTICATION) {
                Activity topActivity = getTopActivity();
                if (topActivity != null) {
                    int state = AuthApi.requestShowAgeAuthDialog(topActivity);
                    retry = state == AgeAuthStatus.SUCCESS.getValue() || state == AgeAuthStatus.ALREADY_AGE_AUTHORIZED.getValue();
                }
            }
        } catch (Exception ignore) {
            Logger.w(ignore);
        }
        return retry;
    }

    /**
     * 모든 API 요청에 대해서 아래의 동작을 수행한다.
     * Background Thread
     * 1. accessToken 유효한지 체크 후 유효하지 않다면 refreshToken으로 accessToken갱신.
     * 2. 요청한 API 호출.
     *
     * UI Thread
     * 1. result중 scope error 체크.
     * 2. Scope update Webview Dialog띄움.(사용자 동의)
     * 3. AuthCode요청 후 accessToken갱신.
     *
     * Backgound Thread
     * 1. 갱신된 accessToken을 가지고 다시 retry.
     *
     * @param request API 요청.
     * @return request에 대한 responseBody
     * @throws Exception
     */
    public synchronized ResponseData requestApi(final IRequest request) throws Exception {
        // 1. accessToken 유효한지 체크 후 유효하지 않다면 refreshToken으로 accessToken갱신.
        if (!checkApiSession()) {
            throw new SessionClosedException("Application Session is Closed.");
        }

        // 2. 요청한 API 호출.
        ResponseData result = request(request);
        Logger.d("++ [%s]response : %s", result.getHttpStatusCode(), result.getStringData());
        if (result.getHttpStatusCode() != HttpURLConnection.HTTP_OK) {
            if (handleApiError(result)) {
                // 갱신된 accessToken을 가지고 다시 retry.
                return requestApi(request);
            }
        }

        return result;
    }

    public synchronized ResponseBody requestAuth(IRequest request) throws Exception {
        ResponseData result = request(request);
        Logger.d("++ [%s]response : %s", result.getHttpStatusCode(), result.getStringData());
        return new ResponseBody(result.getHttpStatusCode(), result.getData());
    }
}
