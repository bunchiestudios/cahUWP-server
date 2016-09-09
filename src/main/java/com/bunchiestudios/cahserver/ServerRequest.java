package com.bunchiestudios.cahserver;

import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public interface ServerRequest {
    public JSONObject perform(JSONObject message);
    public String getName();
}
