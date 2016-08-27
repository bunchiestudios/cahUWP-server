package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Main {
    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            log.error("There was an exception!", e);
        }
    }
}
