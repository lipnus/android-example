/**
 * Copyright 2014-2015 Kakao Corp.
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
package com.kakao.auth.authorization;

import android.net.Uri;

import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;

/**
* @author MJ
*/
public class AuthorizationResult {
    public enum RESULT_CODE {
        SUCCESS,
        CANCEL,
        PASS,   // 다음 방법으로 진행하라
        OAUTH_ERROR,
        ERROR
    }

    private final int requestCode;
    private final RESULT_CODE resultCode;
    private String redirectURL;
    private AccessToken accessToken;
    private String resultMessage;
    private Exception exception;

    public static AuthorizationResult createSuccessAuthCodeResult(final String redirectURL) {
        return new AuthorizationResult(Session.AUTHORIZATION_CODE_REQUEST, RESULT_CODE.SUCCESS).setRedirectURL(redirectURL);
    }

    public static AuthorizationResult createSuccessAccessTokenResult(final AccessToken accessToken) {
        return new AuthorizationResult(Session.ACCESS_TOKEN_REQUEST, RESULT_CODE.SUCCESS).setAccessToken(accessToken);
    }

    public static AuthorizationResult createCancelResult(int requestType, final String resultMessage) {
        return new AuthorizationResult(requestType, RESULT_CODE.CANCEL).setResultMessage(resultMessage);
    }

    public static AuthorizationResult createAuthCodeCancelResult(final String resultMessage) {
        return new AuthorizationResult(Session.AUTHORIZATION_CODE_REQUEST, RESULT_CODE.CANCEL).setResultMessage(resultMessage);
    }

    public static AuthorizationResult createAccessTokenOAuthErrorResult(final String resultMessage) {
        return new AuthorizationResult(Session.ACCESS_TOKEN_REQUEST, RESULT_CODE.OAUTH_ERROR).setResultMessage(resultMessage);
    }

    public static AuthorizationResult createAccessTokenErrorResult(final String resultMessage) {
        return new AuthorizationResult(Session.ACCESS_TOKEN_REQUEST, RESULT_CODE.ERROR).setResultMessage(resultMessage);
    }

    public static AuthorizationResult createAuthCodeOAuthErrorResult(final String resultMessage) {
        return new AuthorizationResult(Session.AUTHORIZATION_CODE_REQUEST, RESULT_CODE.OAUTH_ERROR).setResultMessage(resultMessage);
    }

    public static AuthorizationResult createAuthCodeOAuthErrorResult(final Exception e) {
        return new AuthorizationResult(Session.AUTHORIZATION_CODE_REQUEST, RESULT_CODE.OAUTH_ERROR, e).setResultMessage(e.getMessage());
    }

    public static AuthorizationResult createAuthCodePassResult() {
        return new AuthorizationResult(Session.AUTHORIZATION_CODE_REQUEST, RESULT_CODE.PASS);
    }

    public boolean isSuccess() {
        return resultCode == RESULT_CODE.SUCCESS;
    }

    public boolean isCanceled() {
        return resultCode == RESULT_CODE.CANCEL;
    }

    public boolean isPass() {
        return resultCode == RESULT_CODE.PASS;
    }

    public boolean isAuthError() {
        return resultCode == RESULT_CODE.OAUTH_ERROR;
    }

    public boolean isError() {
        return resultCode == RESULT_CODE.ERROR;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public boolean isAuthorizationCodeRequest(){
        return requestCode == Session.AUTHORIZATION_CODE_REQUEST;
    }

    public boolean isAccessTokenRequest(){
        return requestCode == Session.ACCESS_TOKEN_REQUEST;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public Uri getRedirectUri() {
        if(redirectURL == null)
            return null;
        else
            return Uri.parse(redirectURL);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public Exception getException() {
        return exception;
    }

    private AuthorizationResult(final int requestCode, final RESULT_CODE result) {
        this.requestCode = requestCode;
        this.resultCode = result;
    }

    private AuthorizationResult(final int requestCode, final RESULT_CODE result, Exception e) {
        this.requestCode = requestCode;
        this.resultCode = result;
        this.exception = e;
    }

    private AuthorizationResult setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
        return this;
    }

    private AuthorizationResult setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    private AuthorizationResult setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }
}
