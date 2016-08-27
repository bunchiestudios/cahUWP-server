package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

import java.net.Socket;

/**
 * Created by rdelfin on 8/27/16.
 */
public class ClientHandler implements Runnable {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        log.info("ClientHandler called");
    }
}
