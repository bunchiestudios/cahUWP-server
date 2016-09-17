package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import org.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by rdelfin on 8/27/16.
 */
public class LoginRequest implements ServerRequest {
    @Override
    public JSONObject perform(JSONObject message) {
        //TODO: query the next available id 
        // generate token
        // insert new user with id and token
        // reply with info
        JSONObject response = new JSONObject();
        int ID = 42;
        String token = generateToken(64);
        
        response.put("id", ID);
        response.put("token", token);
        return response;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/users", "POST");
    }
    
    private String generateToken(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
