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
package com.kakao.auth.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.webkit.CookieSyncManager;
import com.kakao.auth.AgeAuthParamBuilder;
import com.kakao.auth.AuthService.AgeAuthStatus;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.SingleNetworkTask;
import com.kakao.auth.StringSet;
import com.kakao.auth.authorization.AuthorizationResult;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.authorization.accesstoken.AccessTokenRequest;
import com.kakao.auth.authorization.authcode.KakaoWebViewActivity;
import com.kakao.auth.network.request.AccessTokenInfoRequest;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ServerProtocol;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseData;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

/**
 * Bloking으로 동작하며, 인증관련 내부 API콜을 한다.
 * @author leoshin
 */
public class AuthApi {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public static void synchronizeCookies(Context context) {
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        syncManager.sync();
    }

    public static AuthorizationResult requestAccessToken(Context context, String appKey, String redirectUri, String authCode, String refreshToken, String approvalType) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseBody result = networkTask.requestAuth(new AccessTokenRequest(context, appKey, redirectUri, authCode, refreshToken, approvalType));
        final AccessToken accessToken = new AccessToken(result);
        return AuthorizationResult.createSuccessAccessTokenResult(accessToken);
    }

    private static boolean requestWebviewAuth(Context context, Bundle ageAuthParams, boolean useSmsReceiver, ResultReceiver resultReceiver) {
        synchronizeCookies(context);

        boolean isUsingTimer = KakaoSDK.getAdapter().getSessionConfig().isUsingWebviewTimer();
        Uri uri = Utility.buildUri(ServerProtocol.AGE_AUTH_AUTHORITY, ServerProtocol.ACCESS_AGE_AUTH_PATH, ageAuthParams);
        Logger.d("AgeAuth request Url : " + uri);

        Intent intent = KakaoWebViewActivity.newIntent(context);
        intent.putExtra(KakaoWebViewActivity.KEY_URL, uri.toString());
        intent.putExtra(KakaoWebViewActivity.KEY_USE_WEBVIEW_TIMERS, isUsingTimer);
        intent.putExtra(KakaoWebViewActivity.KEY_USE_SMS_RECEIVER, useSmsReceiver);
        intent.putExtra(KakaoWebViewActivity.KEY_RESULT_RECEIVER, resultReceiver);

        context.startActivity(intent);
        return true;
    }

    /**
     * {@link com.kakao.auth.ErrorCode} NEED_TO_AGE_AUTHENTICATION(-405)가 발생하였을때 연령인증을 시도한다.
     * 이때 연령인증중 발생할 수 있는 sms수신여부를 해당앱의 permission이 존재하는지 여부를 보고 판단하도록 한다.
     * @param context 현재 화면의 topActivity의 context
     * @param ageAuthParams {@link AgeAuthParamBuilder}를 통해 만든 연령인증에 필요한 파람들
     * @return status code
     */
    public static int requestShowAgeAuthDialog(final Context context, final Bundle ageAuthParams) {
        return requestShowAgeAuthDialog(ageAuthParams, Utility.isUsablePermission(context, permission.RECEIVE_SMS));
    }

    public static int requestShowAgeAuthDialog(final Context context) {
        return requestShowAgeAuthDialog(context, new Bundle());
    }

    public static int requestShowAgeAuthDialog(final Bundle ageAuthParams, final boolean useSmsReceiver) {
        final Context context = KakaoSDK.getCurrentActivity();
        if (useSmsReceiver && !Utility.isUsablePermission(context, permission.RECEIVE_SMS)) {
            throw new SecurityException("Don't have permission RECEIVE_SMS");
        }

        final AgeAuthResult result = new AgeAuthResult();
        final CountDownLatch lock = new CountDownLatch(1);
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultReceiver resultReceiver = new ResultReceiver(sHandler) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            int status = AgeAuthStatus.CLIENT_ERROR.getValue();
                            if (resultCode == KakaoWebViewActivity.RESULT_SUCCESS) {
                                String redirectUrl = resultData.getString(KakaoWebViewActivity.KEY_REDIRECT_URL);
                                if (redirectUrl != null) {
                                    if (Uri.parse(redirectUrl).getQueryParameter(StringSet.status) != null) {
                                        status = Integer.valueOf(Uri.parse(redirectUrl).getQueryParameter(StringSet.status));
                                    }
                                }
                            } else if (resultCode == KakaoWebViewActivity.RESULT_ERROR) {
                                result.setException((KakaoException) resultData.getSerializable(KakaoWebViewActivity.KEY_EXCEPTION));
                            }
                            result.getResult().set(status);
                            lock.countDown();
                        }
                    };
                    requestWebviewAuth(context, ageAuthParams, useSmsReceiver, resultReceiver);
                } catch (Exception e) {
                    result.getResult().set(AgeAuthStatus.CLIENT_ERROR.getValue());
                    result.setException(new KakaoException(e));
                    lock.countDown();
                }
            }
        });

        // 사용자가 취소를 하여도 종료.
        try {
            lock.await();
        } catch (InterruptedException ignor) {
            Logger.e(ignor.toString());
        }

        if (result.getException() != null) {
            throw result.getException();
        }
        return result.getResult().get();
    }

    /**
     * @deprecated  replaced by {@link #requestShowAgeAuthDialog(Context,Bundle)}
     */
    @Deprecated
    public static int requestShowAgeAuthDialog(final Context context, final AgeAuthParamBuilder builder) {
        return requestShowAgeAuthDialog(builder, Utility.isUsablePermission(context, permission.RECEIVE_SMS));
    }

    /**
     * @deprecated  replaced by {@link #requestShowAgeAuthDialog(Bundle,boolean)}
     */
    @Deprecated
    public static int requestShowAgeAuthDialog(final AgeAuthParamBuilder builder, final boolean useSmsReceiver) {
        return requestShowAgeAuthDialog(builder.build(), useSmsReceiver);
    }

    public static AccessTokenInfoResponse requestAccessTokenInfo() throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new AccessTokenInfoRequest());
        return new AccessTokenInfoResponse(result);
    }

    static class AgeAuthResult {
        private AtomicInteger result;
        private KakaoException exception;

        public AgeAuthResult() {
            this.result = new AtomicInteger();
        }

        public AtomicInteger getResult() {
            return result;
        }

        public void setResult(AtomicInteger result) {
            this.result = result;
        }

        public KakaoException getException() {
            return exception;
        }

        public void setException(KakaoException exception) {
            this.exception = exception;
        }
    }
}
