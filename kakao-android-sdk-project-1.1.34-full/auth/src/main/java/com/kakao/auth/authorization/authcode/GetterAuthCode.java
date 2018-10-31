package com.kakao.auth.authorization.authcode;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.kakao.auth.AuthType;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.StringSet;
import com.kakao.auth.api.AuthApi;
import com.kakao.auth.authorization.AuthorizationResult;
import com.kakao.auth.authorization.Authorizer;
import com.kakao.auth.authorization.authcode.AuthCodeRequest.Command;
import com.kakao.network.ServerProtocol;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.KakaoServiceProtocol;
import com.kakao.util.helper.TalkProtocol;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author leo.shin
 */
public class GetterAuthCode extends Authorizer {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private AuthCodeRequest authCodeRequest;
    final private Activity callerActivity;
    final private Queue<Command> commandQueue = new LinkedList<Command>();

    public static GetterAuthCode newInstance(final AuthCodeRequest authCodeRequest, final AuthType authType, final Activity callerActivity) {
        return new GetterAuthCode(authCodeRequest, authType, callerActivity);
    }

    public static GetterAuthCode newInstanceForScopesUpdate(final AuthCodeRequest authCodeRequest, final Activity callerActivity) {
        return new GetterAuthCode(authCodeRequest, callerActivity);
    }

    private GetterAuthCode(final AuthCodeRequest authCodeRequest, final Activity callerActivity) {
        this.authCodeRequest = authCodeRequest;
        commandQueue.add(Command.WEBVIEW_AUTH);
        this.callerActivity = callerActivity;
    }

    private GetterAuthCode(AuthCodeRequest authCodeRequest, final AuthType authType, final Activity callerActivity) {
        this.authCodeRequest = authCodeRequest;
        AuthType type = authType == null ? AuthType.KAKAO_TALK : authType;
        this.callerActivity = callerActivity;

        switch (type) {
            case KAKAO_TALK:
                commandQueue.add(Command.LOGGED_IN_TALK);
                commandQueue.add(Command.LOGGED_OUT_TALK);
                break;
            case KAKAO_STORY:
                commandQueue.add(Command.LOGGED_IN_STORY);
                break;
            case KAKAO_TALK_EXCLUDE_NATIVE_LOGIN:
                commandQueue.add(Command.LOGGED_IN_TALK);
                break;
            case KAKAO_LOGIN_ALL:
                commandQueue.add(Command.LOGGED_IN_TALK);
                commandQueue.add(Command.LOGGED_OUT_TALK);
                commandQueue.add(Command.LOGGED_IN_STORY);
                break;
        }

        commandQueue.add(Command.WEBVIEW_AUTH);
    }

    @Override
    protected void doneOnOAuthError(String errorMessage) {
        Logger.e("GetterAuthorizationCode : " + errorMessage);

        done(AuthorizationResult.createAuthCodeOAuthErrorResult(errorMessage));
        clear();
    }

    @Override
    protected void done(AuthorizationResult result) {
        super.done(result);
        clear();
    }

    public void clear() {
        commandQueue.clear();
    }

    public void start() {
        Command command = null;
        while ((command = commandQueue.poll()) != null) {
            if (authCodeRequest.needsInternetPermission() && !checkInternetPermission()) {
                continue;
            }

            if (request(command)) {
                return;
            }
        }

        // handler를 끝까지 돌았는데도 authorization code를 얻지 못했으면 error
        doneOnOAuthError("Failed to get Authorization Code.");
    }

    public void startActivityForResult(final Intent intent, final int requestCode) {
        if (callerActivity != null) {
            callerActivity.startActivityForResult(intent, requestCode);
        }
    }

    public boolean handleActivityResult(final int requestCode, final int resultCode, final Intent data) {
        AuthorizationResult outcome;

        if (data == null) {
            // This happens if the user presses 'Back'.
            outcome = AuthorizationResult.createAuthCodeCancelResult("pressed back button or cancel button during requesting auth code.");
        } else if (KakaoServiceProtocol.isCapriProtocolMatched(data)) {
            outcome = AuthorizationResult.createAuthCodeOAuthErrorResult("TalkProtocol is mismatched during requesting auth code through KakaoTalk.");
        } else if (resultCode == Activity.RESULT_CANCELED) {
            outcome = AuthorizationResult.createAuthCodeCancelResult("pressed cancel button during requesting auth code.");
        } else if (resultCode != Activity.RESULT_OK) {
            outcome = AuthorizationResult.createAuthCodeOAuthErrorResult("got unexpected resultCode during requesting auth code. code=" + requestCode);
        } else {
            outcome = handleResultOk(data);
        }

        if(outcome.isPass()) {
            start();
        } else {
            done(outcome);
        }

        return true;
    }

    private AuthorizationResult handleResultOk(final Intent data) {
        Bundle extras = data.getExtras();
        String errorType = extras.getString(TalkProtocol.EXTRA_ERROR_TYPE);
        String rediretURL = extras.getString(TalkProtocol.EXTRA_REDIRECT_URL);
        if (errorType == null && rediretURL != null) {
            return AuthorizationResult.createSuccessAuthCodeResult(rediretURL);
        } else {
            if(errorType != null && errorType.equals(TalkProtocol.NOT_SUPPORT_ERROR))
                return AuthorizationResult.createAuthCodePassResult();
            String errorDes = extras.getString(TalkProtocol.EXTRA_ERROR_DESCRIPTION);
            return AuthorizationResult.createAuthCodeOAuthErrorResult("redirectURL=" + rediretURL + ", " + errorType + " : " + errorDes);
        }
    }

    public boolean request(Command command) {
        boolean result = true;
        if (command != Command.WEBVIEW_AUTH) {
            Intent intent = authCodeRequest.getIntent(command);
            if (intent == null) {
                return false;
            }

            try {
                startActivityForResult(intent, authCodeRequest.getRequestCode());
            } catch (ActivityNotFoundException e) {
                Logger.e(e);
                return false;
            }
        } else {
            result = requestWebviewAuth();
        }

        return result;
    }

    private boolean requestWebviewAuth() {
        ResultReceiver resultReceiver = new ResultReceiver(sHandler) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                String redirectUrl = null;
                KakaoException kakaoException = null;
                switch (resultCode) {
                    case KakaoWebViewActivity.RESULT_SUCCESS:
                        redirectUrl =  resultData.getString(KakaoWebViewActivity.KEY_REDIRECT_URL);
                        break;
                    case KakaoWebViewActivity.RESULT_ERROR:
                        kakaoException = (KakaoException) resultData.getSerializable(KakaoWebViewActivity.KEY_EXCEPTION);
                        break;
                }
                onWebViewCompleted(redirectUrl, kakaoException);
            }
        };

        try {
            final Bundle parameters = new Bundle();
            parameters.putString(StringSet.client_id, authCodeRequest.getAppKey());
            parameters.putString(StringSet.redirect_uri, authCodeRequest.getRedirectURI());
            parameters.putString(StringSet.response_type, StringSet.code);

            final Bundle extraParams = authCodeRequest.getExtraParams();
            if(extraParams != null && !extraParams.isEmpty()){
                for(String key : extraParams.keySet()){
                    String value = extraParams.getString(key);
                    if(value != null){
                        parameters.putString(key, value);
                    }
                }
            }

            AuthApi.synchronizeCookies(callerActivity);

            Uri uri = Utility.buildUri(ServerProtocol.AUTH_AUTHORITY, ServerProtocol.AUTHORIZE_CODE_PATH, parameters);

            Intent intent = KakaoWebViewActivity.newIntent(callerActivity);
            intent.putExtra(KakaoWebViewActivity.KEY_URL, uri.toString());
            intent.putExtra(KakaoWebViewActivity.KEY_EXTRA_HEADERS, authCodeRequest.getExtraHeaders());
            intent.putExtra(KakaoWebViewActivity.KEY_USE_WEBVIEW_TIMERS, KakaoSDK.getAdapter().getSessionConfig().isUsingWebviewTimer());
            intent.putExtra(KakaoWebViewActivity.KEY_RESULT_RECEIVER, resultReceiver);

            callerActivity.startActivity(intent);

        } catch (Throwable t) {
            Logger.e("WebViewAuthHandler is failed", t);
            return false;
        }
        return true;
    }

    void onWebViewCompleted(final String redirectURL, final KakaoException exception) {
        AuthorizationResult result;
        if (redirectURL != null) {
            Uri redirectedUri = Uri.parse(redirectURL);
            final String code = redirectedUri.getQueryParameter(StringSet.code);
            if (!TextUtils.isEmpty(code)) {
                result = AuthorizationResult.createSuccessAuthCodeResult(redirectURL);
            } else {
                String error = redirectedUri.getQueryParameter(StringSet.error);
                String errorDescription = redirectedUri.getQueryParameter(StringSet.error_description);
                if (error != null && error.equalsIgnoreCase(StringSet.access_denied)) {
                    result = AuthorizationResult.createAuthCodeCancelResult("pressed back button or cancel button during requesting auth code.");
                } else {
                    result = AuthorizationResult.createAuthCodeOAuthErrorResult(errorDescription);
                }
            }
        } else {
            if (exception.isCancledOperation()) {
                result = AuthorizationResult.createAuthCodeCancelResult(exception.getMessage());
            } else {
                result = AuthorizationResult.createAuthCodeOAuthErrorResult(exception.getMessage());
            }
        }
        done(result);
    }
}
