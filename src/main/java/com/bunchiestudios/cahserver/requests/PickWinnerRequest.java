package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public class PickWinnerRequest extends ServerRequest {
    public PickWinnerRequest(DataManager mgr) throws ProcessingException {
        super(mgr, "resource:json/schema/request/PickWinner.json", "resource:json/schema/response/PickWinner.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/game/winner", "GET");
    }
}
