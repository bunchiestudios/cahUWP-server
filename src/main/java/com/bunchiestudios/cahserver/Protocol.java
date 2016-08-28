package com.bunchiestudios.cahserver;

import com.bunchiestudios.cahserver.requests.*;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all the information to redirect messages into
 * their respective as well as the state for a particular client.
 */
public class Protocol {
    Map<String, Request> requests;

    public Protocol() {
        requests = new HashMap<>();
        List<Request> tempList = new ArrayList<>();

        tempList.add(new CreateGameRequest());
        tempList.add(new GetAllCardsRequest());
        tempList.add(new GetUsersRequest());
        tempList.add(new GetWinnerRequest());
        tempList.add(new JoinGameRequest());
        tempList.add(new LeaveRequest());
        tempList.add(new LoginRequest());
        tempList.add(new PickWinnerRequest());
        tempList.add(new PlayRequest());

        for(Request req : tempList)
            requests.put(req.getName(), req);
    }

    public byte[] receive(byte[] data) {
        String req = new String(data, Charset.forName("UTF-8"));
        JSONObject jsonReq = new JSONObject(req);
        String res = requests.get(jsonReq.getString("reqName")).perform(jsonReq).toString();
        return res.getBytes(Charset.forName("UTF-8"));
    }
}
