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
package com.kakao.kakaotalk.api;

import java.util.Map;

import com.kakao.auth.SingleNetworkTask;
import com.kakao.auth.common.MessageSendable;
import com.kakao.auth.network.response.ApiResponse.BlankApiResponse;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.request.ChatListRequest;
import com.kakao.kakaotalk.request.ChatRoomListRequest;
import com.kakao.kakaotalk.request.SendMemoRequest;
import com.kakao.kakaotalk.request.SendMessageRequest;
import com.kakao.kakaotalk.request.TalkProfileRequest;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.TalkProfileResponse;
import com.kakao.network.response.ResponseData;

/**
 * 카카오톡 API 요청을 담당한다.
 * @author leo.shin
 */
public class KakaoTalkApi {

    /**
     * 카카오톡 프로필 요청
     */
    public static void requestProfile() throws Exception {
        requestProfile(false);
    }

    /**
     * 카카오톡 프로필 요청
     * @param secureResource 이미지 url을 https로 반환할지 여부.
     */
    public static KakaoTalkProfile requestProfile(boolean secureResource) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new TalkProfileRequest(secureResource));
        return new TalkProfileResponse(result).getProfile();
    }

    /**
     * 카카오톡 메시지 전송하며, 리치메시지 3.5 spec으로 구성된 template으로 카카오톡 메시지 전송.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param receiverInfo 메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    public static boolean requestSendMessage(MessageSendable receiverInfo, String templateId, Map<String, String> args) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new SendMessageRequest(receiverInfo, templateId, args));
        new BlankApiResponse(result);
        return true;
    }

    /**
     * 카카오톡에 나에게 메세지를 전송한다.
     * 퍼미션 불필요. 수신자별/발신자별 쿼터 제한 없음.
     * 초대 메시지는 나에게 전송 불가.
     * 카카오톡에 가입이 되어있어야함.
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    public static boolean requestSendMemo(String templateId, Map<String, String> args) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new SendMemoRequest(templateId, args));
        new BlankApiResponse(result);
        return true;
    }

    @Deprecated
    public static ChatListResponse requestChatList(ChatListContext context) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new ChatListRequest(context));
        ChatListResponse response = new ChatListResponse(result);
        context.setAfterUrl(response.getAfterUrl());
        context.setBeforeUrl(response.getBeforeUrl());
        return response;
    }

    /**
     * 톡의 그룹챗방 리스트 정보
     * 해당 유저의 group 챗방을 가져온다.
     * 기본 정렬은 asc로 최근 대화 순으로 정렬한다. (desc는 반대로 가장 오래된 대화 순으로 정렬한다.)
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     *
     * {@link }
     * @param context {@link ChatListContext} 챗방리스트 요청정보를 담고있는 context
     */
    public static ChatListResponse requestChatRoomList(ChatListContext context) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new ChatRoomListRequest(context));
        ChatListResponse response = new ChatListResponse(result);
        context.setAfterUrl(response.getAfterUrl());
        context.setBeforeUrl(response.getBeforeUrl());
        return response;
    }
}
