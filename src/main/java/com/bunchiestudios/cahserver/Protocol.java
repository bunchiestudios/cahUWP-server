package com.bunchiestudios.cahserver;

/**
 * This class contains all the information to redirect messages into
 * their respective as well as the state for a particular client.
 */
public class Protocol {
    long userId;
    String userToken;
    boolean loggedIn;

    public Protocol() {
        userId = 0;
        userToken = null;
        loggedIn = false;
    }

    public void recieve(byte[] data) {
        
    }
}
