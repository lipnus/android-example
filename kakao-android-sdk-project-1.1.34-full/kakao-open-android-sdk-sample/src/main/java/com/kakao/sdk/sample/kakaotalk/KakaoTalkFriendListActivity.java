package com.kakao.sdk.sample.kakaotalk;

import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.auth.common.MessageSendable;
import com.kakao.friends.StringSet;
import com.kakao.friends.response.model.FriendInfo;
import com.kakao.kakaotalk.KakaoTalkService;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.GlobalApplication;
import com.kakao.sdk.sample.common.log.Logger;
import com.kakao.sdk.sample.common.widget.KakaoToast;
import com.kakao.sdk.sample.friends.FriendsMainActivity;
import com.kakao.usermgmt.response.model.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author leo.shin
 */
public class KakaoTalkFriendListActivity extends FriendsMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UserProfile userProfile = UserProfile.loadFromCache();
        if (userProfile != null) {
            View headerView = getLayoutInflater().inflate(R.layout.view_friend_item, null, false);

            NetworkImageView profileView = (NetworkImageView) headerView.findViewById(R.id.profile_image);
            profileView.setDefaultImageResId(R.drawable.thumb_story);
            profileView.setErrorImageResId(R.drawable.thumb_story);
            TextView nickNameView = (TextView) headerView.findViewById(R.id.nickname);

            String profileUrl = userProfile.getThumbnailImagePath();
            Application app  = GlobalApplication.getGlobalApplicationContext();
            if (profileUrl != null && profileUrl.length() > 0) {
                profileView.setImageUrl(profileUrl, ((GlobalApplication) app).getImageLoader());
            } else {
                profileView.setImageResource(R.drawable.thumb_story);
            }

            String nickName = getString(R.string.text_send_to_me) + " " + userProfile.getNickname();
            nickNameView.setText(nickName);

            list.addHeaderView(headerView);

            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "Test for send Memo";
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("''yy년 MM월 dd일 E요일");
                    KakaoTalkMessageBuilder builder = new KakaoTalkMessageBuilder();
                    builder.addParam("MESSAGE", message);
                    builder.addParam("DATE", sdf.format(date));

                    requestSendMemo(builder);
                }
            });
        }
    }

    @Override
    public void onItemSelected(final int position, final FriendInfo friendInfo) {
        if (!friendInfo.isAllowedMsg()) {
            return;
        }

        TalkMessageHelper.showSendMessageDialog(this, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MSG_TYPE type = MSG_TYPE.valueOf(msgType.getSelectedItemPosition());
                requestSendMessage(type, friendInfo, makeMessageBuilder(type, friendInfo.getProfileNickname()));
            }
        });
    }

    private KakaoTalkMessageBuilder makeMessageBuilder(MSG_TYPE type, String nickName) {
        KakaoTalkMessageBuilder builder = new KakaoTalkMessageBuilder();

        if (type == MSG_TYPE.NORMAL) {
            builder.addParam("username", "Your name is " + nickName);
            builder.addParam("labelMsg", "Hi " + nickName + ". this is test messagae");
            builder.addParam("linkMsg", "Go to website");
        }

        return builder;
    }

    private void requestSendMessage(MSG_TYPE type, MessageSendable friendInfo, KakaoTalkMessageBuilder builder) {
        KakaoTalkService.requestSendMessage(new TalkResponseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Logger.d("++ send message result : " + result);
                KakaoToast.makeToast(getApplicationContext(), "Send message success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNotKakaoTalkUser() {
                KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                KakaoToast.makeToast(getApplicationContext(), "onNotSignedUp : " + "User Not Registed App", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDidStart() {
                showWaitingDialog();
            }

            @Override
            public void onDidEnd() {
                cancelWaitingDialog();
            }
        }, friendInfo, TalkMessageHelper.getSampleTemplateId(type), builder.build());
    }

    private void requestSendMemo(KakaoTalkMessageBuilder builder) {
        KakaoTalkService.requestSendMemo(new TalkResponseCallback<Boolean>() {
            @Override
            public void onNotKakaoTalkUser() {
                KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                KakaoToast.makeToast(getApplicationContext(), "onNotSignedUp : " + "User Not Registed App", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Boolean result) {
                KakaoToast.makeToast(getApplicationContext(), "Send message success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDidStart() {
                showWaitingDialog();
            }

            @Override
            public void onDidEnd() {
                cancelWaitingDialog();
            }
        }, TalkMessageHelper.getMemoTemplateId(), builder.build());
    }

}
