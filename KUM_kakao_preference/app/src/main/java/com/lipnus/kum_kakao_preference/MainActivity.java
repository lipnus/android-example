package com.lipnus.kum_kakao_preference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class MainActivity extends AppCompatActivity {

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    SessionCallback callback;
    EditText eT;
    TextView tV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        eT = (EditText) findViewById(R.id.edittext);
        tV = (TextView) findViewById(R.id.textview);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();


        //두번째 파라미터는 값이 없을경우 나오는 값
        String str = setting.getString("text", "no");
        tV.setText(str);

        Log.d("KAKAKA", "세션상태: "+ Session.getCurrentSession().isClosed() );

    }


    public void onClick(View v){
        String str;
        str = eT.getText().toString();

        editor.putString("text", str);
        editor.commit();

        Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "자동로그인", Toast.LENGTH_LONG).show();
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
