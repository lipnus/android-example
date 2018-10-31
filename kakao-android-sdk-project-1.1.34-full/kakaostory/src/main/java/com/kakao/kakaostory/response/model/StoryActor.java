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
import com.kakao.network.response.ResponseBody.BodyConverter;
import com.kakao.network.response.ResponseBody.ResponseBodyException;

/**
 * @author leoshin, created at 15. 8. 3..
 */
public class StoryActor {
    private final String displayName;
    private final String profileThumbnailUrl;

    public StoryActor(ResponseBody body) {
        this.displayName = body.optString(StringSet.display_name, null);
        this.profileThumbnailUrl = body.optString(StringSet.profile_thumbnail_url, null);
    }

    public static final ResponseBody.BodyConverter<StoryActor> CONVERTER = new BodyConverter<StoryActor>() {
        @Override
        public StoryActor convert(ResponseBody body) throws ResponseBodyException {
            return new StoryActor(body);
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileThumbnailUrl() {
        return profileThumbnailUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StoryActor{");
        sb.append("displayName='").append(displayName).append('\'');
        sb.append(", profileThumbnailUrl='").append(profileThumbnailUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
