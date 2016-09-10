package com.bunchiestudios.cahserver;

import com.bunchiestudios.cahserver.requests.*;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.JSONException;
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
    Map<RequestIdentifier, ServerRequest> requests;

    public Protocol() {
        requests = new HashMap<>();
        List<ServerRequest> tempList = new ArrayList<>();

        tempList.add(new CreateGameRequest());
        tempList.add(new GetAllCardsRequest());
        tempList.add(new GetUsersRequest());
        tempList.add(new GetWinnerRequest());
        tempList.add(new JoinGameRequest());
        tempList.add(new LeaveRequest());
        tempList.add(new LoginRequest());
        tempList.add(new PickWinnerRequest());
        tempList.add(new PlayRequest());

        for(ServerRequest req : tempList)
            requests.put(req.getIdentifier(), req);
    }

    public Response receive(Request req) {
        try {
            JSONObject jsonReq = new JSONObject(req.getContentString());
            RequestIdentifier id = new RequestIdentifier(req.path(), req.method().toString());

            if(!requests.containsKey(id)) {
                throw new PathNotFoundException(req.path());
            }
            String resString = requests.get(id).perform(jsonReq).toString();
            Response res = new Response.Ok();
            res.setContentType("application/json", "utf-8");
            res.setContentString(resString);
            return res;
        } catch(JSONException e) {
            System.err.println("There was an error when processing the incoming JSON request:");
            Response res = new Response.Ok();
            res.setStatus(HttpResponseStatus.BAD_REQUEST);
            res.setContentString("{\"error\":\"Invalid JSON format request\"}");
            return res;
        } catch (PathNotFoundException e) {
            System.err.println("requests.get() returned null for the path. Throw 404");
            System.err.println(e.toString());
            Response res = new Response.Ok();
            res.setStatus(HttpResponseStatus.NOT_FOUND);
            res.setContentString("{\"error\":\"Resource not found\"}");
            return res;
        }
    }
}
