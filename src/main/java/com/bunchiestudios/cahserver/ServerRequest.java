package com.bunchiestudios.cahserver;

import com.bunchiestudios.cahserver.database.DataManager;
import org.json.JSONObject;

/**
 * Created by rdelfin on 8/27/16.
 */
public abstract class ServerRequest {
    private DataManager mgr;

    public ServerRequest(DataManager mgr) {
        this.mgr = mgr;
    }

    protected DataManager getMgr() { return mgr; }

    public abstract JSONObject perform(JSONObject message);
    public abstract RequestIdentifier getIdentifier();
}
