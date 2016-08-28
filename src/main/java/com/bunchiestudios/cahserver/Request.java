package com.bunchiestudios.cahserver;

import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public interface Request {
    public JSONObject perform(JSONObject message);
    public String getName();
}
