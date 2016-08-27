package com.bunchiestudios.cahserver;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch(Exception e) {

        }
    }
}
