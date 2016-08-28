package com.bunchiestudios.cahserver;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * This class contains all the information to redirect messages into
 * their respective as well as the state for a particular client.
 */
public class Protocol {
    Map<String, Request> requests;

    public Protocol() {
    }

    public byte[] receive(byte[] data) {
        String req = new String(data, Charset.forName("UTF-8"));
        JSONObject jsonReq = new JSONObject(req);
        String res = requests.get(jsonReq.getString("reqName")).perform(jsonReq).toString();
        return res.getBytes(Charset.forName("UTF-8"));
    }
}
