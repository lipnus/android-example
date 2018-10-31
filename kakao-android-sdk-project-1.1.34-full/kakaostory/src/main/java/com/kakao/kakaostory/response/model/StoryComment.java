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
 * 카카오 스토리 덧글
 * @author leoshin, created at 15. 8. 3..
 */
public class StoryComment {
    private final String text;
    private final StoryActor writer;

    public StoryComment(ResponseBody body) throws ResponseBodyException {
        this.text = body.optString(StringSet.text, null);
        this.writer = body.optConverted(StringSet.writer, StoryActor.CONVERTER, null);
    }

    public String getText() {
        return text;
    }

    public StoryActor getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StoryComment{");
        sb.append("text='").append(text).append('\'');
        sb.append(", writer=").append(writer);
        sb.append('}');
        return sb.toString();
    }

    public static final ResponseBody.BodyConverter<StoryComment> CONVERTER = new BodyConverter<StoryComment>() {
        @Override
        public StoryComment convert(ResponseBody body) throws ResponseBodyException {
            return new StoryComment(body);
        }
    };
}
