package com.kakao.s2;

import com.kakao.network.KakaoRequest;
import com.kakao.network.RequestConfiguration;
import com.kakao.network.ServerProtocol;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 2..
 */

class PublishAdidEventsRequest extends KakaoRequest {
    private Event rootEvent;
    private List<Event> events;

    PublishAdidEventsRequest(RequestConfiguration configuration, Event rootEvent, List<Event> events) {
        super(configuration);
        this.rootEvent = rootEvent;
        this.events = events;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getUrl() {
        return Utility.buildUri(ServerProtocol.API_AUTHORITY, ServerProtocol.EVENTS_PUBLISH_ADID_PATH).toString();
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();

        if (events != null && !events.isEmpty()) {
            String eventsString = events.toString();
            if (eventsString.length() > Event.MAX_BODY_SIZE) {
                throw new KakaoException(KakaoException.ErrorType.ILLEGAL_ARGUMENT, "Event's bulk size is too large (over " + Event.MAX_BODY_SIZE + " bytes).");
            }
            params.put(Event.EVENTS, eventsString);
        }

        if (rootEvent == null) {
            return params;
        }

        if (rootEvent.getTimestamp() != null) {
            params.put(Event.TIMESTAMP, String.valueOf(rootEvent.getTimestamp()));
        }
        if (rootEvent.getFrom() != null)
            params.put(Event.ADID, rootEvent.getFrom());
        if (rootEvent.getAdidEnabled() != null)
            params.put(Event.AD_TRACKING_ENABLED, String.valueOf(rootEvent.getAdidEnabled()));
        if (rootEvent.getTo() != null)
            params.put(Event.TO, rootEvent.getTo());
        if (rootEvent.getAction() != null)
            params.put(Event.ACTION, rootEvent.getAction());
        if (rootEvent.getProps() != null && !rootEvent.getProps().isEmpty()) {
            try {
                JSONObject jsonObject = this.rootEvent.propsToJson();
                if (jsonObject != null) {
                    params.put(Event.PROPS, jsonObject.toString());
                }
            } catch (JSONException e) {
                Logger.e("failed to jsonify properties for event: " + e.toString());
            }
        }
        return params;
    }
}
