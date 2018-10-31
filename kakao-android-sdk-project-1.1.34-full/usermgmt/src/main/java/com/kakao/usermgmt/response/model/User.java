package com.kakao.usermgmt.response.model;

/**
 * @author leoshin on 15. 9. 17.
 */
public interface User {
    /**
     * 사용자 ID
     */
    long getId();

    /**
     * 해당 앱에서 유일한 친구의 code
     * 가변적인 데이터.
     */
    String getUUID();

    /**
     * 친구의 카카오 회원번호. 앱의 특정 카테고리나 특정 권한에 한해 내려줌
     */
    long getServiceUserId();
}
