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

/**
 * 내스토리의 이미지를 크기별로 얻을 수 있는 객체.
 * @author leoshin, created at 15. 8. 3..
 */
public class MyStoryImageInfo {
    private final String xlarge;
    private final String large;
    private final String medium;
    private final String small;
    private final String original;
    public MyStoryImageInfo(ResponseBody body) {
        this.xlarge = body.optString(StringSet.xlarge, null);
        this.large = body.optString(StringSet.large, null);
        this.medium = body.optString(StringSet.medium, null);
        this.small = body.optString(StringSet.small, null);
        this.original = body.optString(StringSet.original, null);
    }

    public String getXlarge() {
        return xlarge;
    }

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public String getSmall() {
        return small;
    }

    public String getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KakaoStoryActivityImage{");
        sb.append("original='").append(original).append('\'');
        sb.append(", xlarge='").append(xlarge).append('\'');
        sb.append(", large='").append(large).append('\'');
        sb.append(", medium='").append(medium).append('\'');
        sb.append(", small='").append(small).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static final ResponseBody.BodyConverter<MyStoryImageInfo> CONVERTER = new ResponseBody.BodyConverter<MyStoryImageInfo>() {
        @Override
        public MyStoryImageInfo convert(ResponseBody body) throws ResponseBodyException {
            return new MyStoryImageInfo(body);
        }
    };
}
