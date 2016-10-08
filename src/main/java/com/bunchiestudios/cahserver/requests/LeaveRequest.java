package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by rdelfin on 8/27/16.
 */
public class LeaveRequest extends ServerRequest {
    public LeaveRequest(DataManager mgr) throws ProcessingException, IOException {
        super(mgr, "json/schema/request/Leave.json", "json/schema/response/Leave.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/game/users", "DELETE");
    }
}
