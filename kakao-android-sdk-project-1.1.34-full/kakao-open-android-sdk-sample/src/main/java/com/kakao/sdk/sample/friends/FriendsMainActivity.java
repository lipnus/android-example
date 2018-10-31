package com.kakao.sdk.sample.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.friends.FriendContext;
import com.kakao.friends.FriendsService;
import com.kakao.friends.request.FriendsRequest.FriendFilter;
import com.kakao.friends.request.FriendsRequest.FriendOrder;
import com.kakao.friends.request.FriendsRequest.FriendType;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.friends.response.model.FriendInfo;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.BaseActivity;
import com.kakao.sdk.sample.common.widget.KakaoDialogSpinner;
import com.kakao.sdk.sample.common.widget.KakaoToast;
import com.kakao.sdk.sample.friends.FriendsListAdapter.IFriendListCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leo.shin
 */
public class FriendsMainActivity extends BaseActivity implements OnClickListener, IFriendListCallback {
    public enum MSG_TYPE {
        NORMAL(0),
        INVITE(1),
        FEED(2);

        private int value;
        MSG_TYPE(int value) {
            this.value = value;
        }

        public static MSG_TYPE valueOf(int i) {
            for(MSG_TYPE type : values()) {
                if (type.getValue() == i) {
                    return type;
                }
            }

            return NORMAL;
        }

        public int getValue() {
            return value;
        }
    }
    private abstract class FriendsResponseCallback extends ApiResponseCallback<FriendsResponse> {

        @Override
        public void onFailure(ErrorResult errorResult) {
            KakaoToast.makeToast(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onDidStart() {
            showWaitingDialog();
        }

        @Override
        public void onDidEnd() {
            cancelWaitingDialog();
        }
    }

    private static class FriendsInfo {
        private final List<FriendInfo> friendInfoList = new ArrayList<FriendInfo>();
        private int totalCount;
        private String id;

        public FriendsInfo() {
        }

        public List<FriendInfo> getFriendInfoList() {
            return friendInfoList;
        }

        public void merge(FriendsResponse response) {
            this.id = response.getId();
            this.totalCount = response.getTotalCount();
            this.friendInfoList.addAll(response.getFriendInfoList());
        }

        public String getId() {
            return id;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static final String EXTRA_KEY_SERVICE_TYPE = "KEY_FRIEND_TYPE";

    protected ListView list = null;
    private FriendsListAdapter adapter = null;
    private List<FriendType> friendTypeList = new ArrayList<FriendType>();
    private Button talkButton = null;
    private Button storyButton = null;
    private Button talkStoryButton = null;
    private FriendContext friendContext = null;
    private FriendsInfo friendsInfo = null;
    protected KakaoDialogSpinner msgType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_friends_main);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY_SERVICE_TYPE)) {
            String[] serviceTypes = intent.getStringArrayExtra(EXTRA_KEY_SERVICE_TYPE);
            for (int i = 0; i < serviceTypes.length; i++) {
                friendTypeList.add(FriendType.valueOf(serviceTypes[i]));
            }
        } else {
            friendTypeList.add(FriendType.KAKAO_TALK);
            friendTypeList.add(FriendType.KAKAO_STORY);
            friendTypeList.add(FriendType.KAKAO_TALK_AND_STORY);
        }

        list = (ListView)findViewById(R.id.friend_list);
        talkButton = (Button)findViewById(R.id.all_talk_friends);
        storyButton = (Button)findViewById(R.id.all_story_friends);
        talkStoryButton = (Button)findViewById(R.id.all_talk_and_story_friends);
        msgType = (KakaoDialogSpinner)findViewById(R.id.message_type);

        talkButton.setVisibility(View.GONE);
        storyButton.setVisibility(View.GONE);
        talkStoryButton.setVisibility(View.GONE);

        for(FriendType friendType : friendTypeList) {
            switch (friendType) {
                case KAKAO_TALK:
                    talkButton.setVisibility(View.VISIBLE);
                    talkButton.setOnClickListener(this);
                    break;
                case KAKAO_STORY:
                    storyButton.setVisibility(View.VISIBLE);
                    storyButton.setOnClickListener(this);
                    break;
                case KAKAO_TALK_AND_STORY:
                    talkStoryButton.setVisibility(View.VISIBLE);
                    talkStoryButton.setOnClickListener(this);
                    break;
            }
        }

        if (friendTypeList.size() == 1) {
            friendsInfo = new FriendsInfo();
            requestFriends(friendTypeList.get(0));
        }

        findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        FriendType type = FriendType.KAKAO_TALK;
        switch (v.getId()) {
            case R.id.all_talk_friends:
                type = FriendType.KAKAO_TALK;
                break;
            case R.id.all_story_friends:
                type = FriendType.KAKAO_STORY;
                break;
            case R.id.all_talk_and_story_friends:
                type = FriendType.KAKAO_TALK_AND_STORY;
                break;
        }
        requestFriends(type);
    }

    private void requestFriends(FriendType type) {
        adapter = null;
        friendsInfo = new FriendsInfo();
        friendContext = FriendContext.createContext(type, FriendFilter.NONE, FriendOrder.NICKNAME, false, 0, 100, "asc");
        requestFriendsInner();
    }

    private void requestFriendsInner() {
        final IFriendListCallback callback = this;
        FriendsService.requestFriends(new FriendsResponseCallback() {
            @Override
            public void onNotSignedUp() {
                redirectSignupActivity();
            }

            @Override
            public void onSuccess(FriendsResponse result) {
                if (result != null) {
                    friendsInfo.merge(result);

                    if (adapter == null) {
                        adapter = new FriendsListAdapter(friendsInfo.getFriendInfoList(), callback);
                        list.setAdapter(adapter);
                    } else {
                        adapter.setItem(friendsInfo.getFriendInfoList());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, friendContext);
    }

    @Override
    public void onItemSelected(int position, FriendInfo friendInfo) {
    }

    @Override
    public void onPreloadNext() {
        if (friendContext.hasNext()) {
            requestFriendsInner();
        }
    }
}
