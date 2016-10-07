package com.bunchiestudios.cahserver.json;

import org.json.JSONObject;

/**
 * Created by rdelfin on 10/7/16.
 */
public class StandardResponses {
    public static JSONObject messageFormatInvalidError() {
        JSONObject result = new JSONObject();
        result.put("error", "Message format invalid. Check API documentation.");
        result.put("error_code", 100);

        return result;
    }

    public static JSONObject userUnauthenticatedError() {
        JSONObject result = new JSONObject();
        result.put("error", "User token is invalid or user does not exist.");
        result.put("error_code", 101);

        return result;
    }

    public static JSONObject createGameError() {
        JSONObject result = new JSONObject();
        result.put("error", "There was an error creating the game.");
        result.put("error_code", 102);

        return result;

    }

    public static JSONObject unknownError(String action) {
        JSONObject result = new JSONObject();
        result.put("error", "There was an unknown error when " + action + ".");
        result.put("error_code", 103);

        return result;
    }
}
