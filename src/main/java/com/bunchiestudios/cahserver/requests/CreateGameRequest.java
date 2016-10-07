package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.twitter.finagle.http.Method;
import org.json.JSONObject;

/**
 * This is the request made by the user when they want to create a new game. Provided a game name, an optional password,
 * and some authentication, the appropriate data structures should be set up.
 */
public class CreateGameRequest extends ServerRequest {

    public CreateGameRequest(DataManager mgr) throws ProcessingException {
        super(mgr, "resource:json/schema/request/CreateGame.json", "resource:json/schema/response/CreateGame.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        JSON

        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/game", "POST");
    }
}
