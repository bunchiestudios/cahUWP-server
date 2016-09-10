package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public class GetAllCardsRequest implements ServerRequest {
    @Override
    public JSONObject perform(JSONObject message) {
        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/cards", "GET");
    }
}
