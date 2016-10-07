package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public class GetAllCardsRequest extends ServerRequest {
    public GetAllCardsRequest(DataManager mgr) throws ProcessingException {
        super(mgr, "resource:json/schema/request/GetAllCards.json", "resource:json/schema/response/GetAllCards.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        if(!requestValid(message))
            return new JSONObject("{\"error\": \"The request format is invalid\"}");

        DataManager mgr = getMgr();

        return null;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/cards", "GET");
    }
}
