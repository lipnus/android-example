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
package com.kakao.kakaostory.response.model;

import com.kakao.kakaostory.StringSet;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseBody.ResponseBodyException;

import java.util.Collections;
import java.util.List;

/**
 * @author leoshin, created at 15. 7. 31..
 */
public class MyStoryInfo {
    private final String id;
    private final String url;
    private final String mediaType;
    private final String createdAt;
    private final int commentCount;
    private final int likeCount;
    private final String content;
    private final String permission;
    private final List<MyStoryImageInfo> imageInfoList;
    private final List<StoryComment> commentList;
    private final List<StoryLike> likeList;

    public MyStoryInfo(ResponseBody body) throws ResponseBodyException {
        this.id = body.optString(StringSet.id, null);
        this.url = body.optString(StringSet.url, null);
        this.mediaType = body.optString(StringSet.media_type, null);
        this.createdAt = body.optString(StringSet.created_at, null);
        this.commentCount = body.optInt(StringSet.comment_count, 0);
        this.likeCount = body.optInt(StringSet.like_count, 0);
        this.content = body.optString(StringSet.content, null);
        this.permission = body.optString(StringSet.permission, null);
        this.imageInfoList = body.optConvertedList(StringSet.media, MyStoryImageInfo.CONVERTER, Collections.<MyStoryImageInfo>emptyList());
        this.commentList = body.optConvertedList(StringSet.comments, StoryComment.CONVERTER, Collections.<StoryComment>emptyList());
        this.likeList = body.optConvertedList(StringSet.likes, StoryLike.CONVERTER, Collections.<StoryLike>emptyList());
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<MyStoryImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public List<StoryComment> getCommentList() {
        return commentList;
    }

    public List<StoryLike> getLikeList() {
        return likeList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getContent() {
        return content;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MyStoryInfo{");
        sb.append("id='").append(id).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", mediaType='").append(mediaType).append('\'');
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", commentCount=").append(commentCount);
        sb.append(", likeCount=").append(likeCount);
        sb.append(", comments=").append(commentList);
        sb.append(", likes=").append(likeList);
        sb.append(", content='").append(content).append('\'');
        sb.append(", medias=").append(imageInfoList);
        sb.append(", permission='").append(permission).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static final ResponseBody.BodyConverter<MyStoryInfo> CONVERTER = new ResponseBody.BodyConverter<MyStoryInfo>() {
        @Override
        public MyStoryInfo convert(ResponseBody body) throws ResponseBodyException {
            return new MyStoryInfo(body);
        }
    };
}
