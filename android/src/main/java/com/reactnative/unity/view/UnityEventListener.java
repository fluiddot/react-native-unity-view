package com.reactnative.unity.view;

/**
 * Created by xzper on 2018-03-08.
 * Edited by fluiddot 2018-06-17
 */

import org.json.JSONObject;

public interface UnityEventListener {
    void onMessage(JSONObject message);
}
