package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Main {
    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            new Server(80).main(args);
        } catch (Exception e) {
            System.err.println("There was an exception!\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
