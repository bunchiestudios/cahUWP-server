package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.twitter.finagle.http.Method;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by rdelfin on 8/27/16.
 */
public class CreateGameRequest extends ServerRequest {

    public CreateGameRequest(DataManager mgr) throws ProcessingException, IOException {
        super(mgr, "json/schema/request/CreateGame.json", "json/schema/response/CreateGame.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/game", "POST");
    }
}
