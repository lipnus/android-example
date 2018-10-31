package com.lipnus.kum_kakao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class MainActivity extends AppCompatActivity {
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**카카오톡 로그아웃 요청**/
        //        한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //        테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^

//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            public void onCompleteLogout() {
//                //로그아웃 성공 후 하고싶은 내용 코딩 ~
//                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
//                Log.d("SBKAKAO", "로그아웃");
//            }
//        });



        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    public void onClick_login(View v){
        
    }

    public void onClick_logout(View v){
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Log.d("SBKAKAO", "로그아웃_onFailure");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                super.onSessionClosed(errorResult);
                Log.d("SBKAKAO", "로그아웃_onSessionClosed");
            }

            @Override
            public void onNotSignedUp() {
                super.onNotSignedUp();
                Log.d("SBKAKAO", "로그아웃_onNotSignedUp");
            }

            @Override
            public void onSuccess(Long result) {
                super.onSuccess(result);
                Log.d("SBKAKAO", "로그아웃_onSuccess");
            }

            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
                Log.d("SBKAKAO", "로그아웃");
            }
        });
    }

    public void onClick_unlinked(View v){
        UserManagement.requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("SBKAKAO", "탈퇴_onSessionClosed");
            }

            @Override
            public void onNotSignedUp() {
                Log.d("SBKAKAO", "탈퇴_onNotSignedUp");
            }

            @Override
            public void onSuccess(Long result) {
                Log.d("SBKAKAO", "탈퇴_onSuccess");
            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    Log.d("SBKAKAO", userProfile.getNickname() );
                    Log.d("SBKAKAO", userProfile.getThumbnailImagePath() );
                    Log.d("SBKAKAO", userProfile.getProfileImagePath() );
                    Log.d("SBKAKAO", "" + userProfile.getId() );

//                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
//                    startActivity(intent);
//                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        }
    }

}