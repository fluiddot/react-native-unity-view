package com.reactnative.unity.view;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.json.JSONObject;

/**
 * Created by xzper on 2018-03-08.
 * Edited by fluiddot 2018-06-17
 */

public class UnityMessageEvent extends Event<UnityMessageEvent> {

    public static final String EVENT_NAME = "unityMessage";
    private final JSONObject mData;

    public UnityMessageEvent(int viewId, JSONObject data) {
        super(viewId);
        mData = data;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        WritableMap data = null;
        try {
            data = JsonHelper.convertJsonToMap(mData);
        }
        catch(org.json.JSONException exception) {
          System.out.println("JSON parse exception: " + exception);
        }

        rctEventEmitter.receiveEvent(getViewTag(), EVENT_NAME, data);
    }
}
