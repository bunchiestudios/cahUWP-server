package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Main {
    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        /*
        * PORT IS REQUIRED AS FIRST ARGUMENT
        */
        
        try {
            int port = Integer.parseInt(args[0]);
            Server server = new Server(port);
            server.start();
        } catch (Exception e) {
            log.error("There was an exception!", e);
        }
    }
}
