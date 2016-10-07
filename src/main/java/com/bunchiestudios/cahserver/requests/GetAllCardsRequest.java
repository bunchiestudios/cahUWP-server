package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.bunchiestudios.cahserver.datamodel.Card;
import com.bunchiestudios.cahserver.datamodel.Player;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        // Parsing json into appropriate variables
        JSONObject user = message.getJSONObject("user");
        long id = user.getLong("id");
        String token = user.getString("token");

        DataManager mgr = getMgr();

        Player player = mgr.authenticateUser(id, token);
        if(player == null)
            return new JSONObject("{\"error\"}: \"Could not authenticate user\"");

        List<Card> cards = new ArrayList<>();
        JSONObject result = new JSONObject();
        JSONObject userResponse = new JSONObject();
        userResponse.put("id", player.getId());
        JSONArray cardArray = new JSONArray();


        for(Card c : cards) {
            // Construct card JSON object
            JSONObject jsonCard = new JSONObject();
            jsonCard.put("id", c.getId());
            jsonCard.put("message", c.getMessage());
            jsonCard.put("pick_n", c.getPickN());
            jsonCard.put("black", c.isBlack());

            // Append to the list of cards
            cardArray.put(jsonCard);
        }

        result.put("user", userResponse);
        result.put("cards", cardArray);

        return result;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/cards", "GET");
    }
}
