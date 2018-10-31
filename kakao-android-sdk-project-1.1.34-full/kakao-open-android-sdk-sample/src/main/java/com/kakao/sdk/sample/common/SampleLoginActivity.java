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
package com.kakao.sdk.sample.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.widget.KakaoToast;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.
 *
 * @author MJ
 */
public class SampleLoginActivity extends BaseActivity {
    private SessionCallback callback;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            setContentView(R.layout.layout_common_kakao_login);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }

            setContentView(R.layout.layout_common_kakao_login);
        }
    }
}
