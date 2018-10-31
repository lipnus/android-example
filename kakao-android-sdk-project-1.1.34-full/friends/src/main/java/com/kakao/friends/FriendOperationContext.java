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
package com.kakao.friends;

import com.kakao.auth.common.PageableContext;
import com.kakao.friends.request.FriendsOperationRequest.Operation;
import com.kakao.util.KakaoParameterException;

/**
 * @author leoshin on 15. 9. 4.
 */
public class FriendOperationContext extends PageableContext {
    private final String firstId;
    private final String secondId;
    private final Operation operation;
    private final boolean secureResource;
    private final int offset;
    private final int limit;
    private final String order;

    private FriendOperationContext(FriendContext firstFriendContext, FriendContext secondFriendContext, Operation operation, boolean secureResource, int offset, int limit, String order) {
        this.firstId = firstFriendContext.getId();
        this.secondId = secondFriendContext.getId();
        this.operation = operation;
        this.secureResource = secureResource;
        this.offset = offset;
        this.limit = limit;
        this.order = order;
    }

    /**
     *
     * @param firstFriendContext
     * @param secondFriendContext
     * @param operation
     * @param secureResource
     * @param offset
     * @param limit
     * @param order
     * @return
     * @throws KakaoParameterException
     */
    public static FriendOperationContext createContext(final FriendContext firstFriendContext,
                                                       final FriendContext secondFriendContext,
                                                       final Operation operation,
                                                       final boolean secureResource,
                                                       final int offset,
                                                       final int limit,
                                                       final String order) throws KakaoParameterException {
        if (firstFriendContext.getId() == null || secondFriendContext.getId() == null) {
            throw new KakaoParameterException("Friend context Id is missing. Id is getting after requestFriends");
        }
        return new FriendOperationContext(firstFriendContext, secondFriendContext, operation, secureResource, offset, limit, order);
    }

    public String getFirstId() {
        return firstId;
    }

    public String getSecondId() {
        return secondId;
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean isSecureResource() {
        return secureResource;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public String getOrder() {
        return order;
    }
}
