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
public class StoryLike {
    /**
     * 느낌 값
     */
    public enum Emotion {
        /**
         * 좋아요
         */
        LIKE("LIKE"),
        /**
         * 멋져요
         */
        COOL("COOL"),
        /**
         * 기뻐요
         */
        HAPPY("HAPPY"),
        /**
         * 슬퍼요
         */
        SAD("SAD"),
        /**
         * 힘내요
         */
        CHEER_UP("CHEER_UP"),
        /**
         * 정의되지 않은 느낌
         */
        NOT_DEFINED("NOT_DEFINED");

        final String papiEmotion;
        Emotion(final String papiEmotion) {
            this.papiEmotion = papiEmotion;
        }

        public static Emotion getEmotion(final String emotionString){
            for(Emotion emotion : Emotion.values()){
                if(emotion.papiEmotion.equals(emotionString))
                    return emotion;
            }
            return NOT_DEFINED;
        }
    }

    private final Emotion emoticon;
    private final StoryActor actor;

    public StoryLike(ResponseBody body) throws ResponseBodyException {
        this.emoticon = Emotion.getEmotion(body.optString(StringSet.emotion, null));
        this.actor = body.optConverted(StringSet.actor, StoryActor.CONVERTER, null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StoryLike{");
        sb.append("emotion='").append(emoticon).append('\'');
        sb.append(", actor=").append(actor);
        sb.append('}');
        return sb.toString();
    }

    public static final ResponseBody.BodyConverter<StoryLike> CONVERTER = new BodyConverter<StoryLike>() {
        @Override
        public StoryLike convert(ResponseBody body) throws ResponseBodyException {
            return new StoryLike(body);
        }
    };
}
