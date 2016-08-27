package com.bunchiestudios.cahserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Server {
    ServerSocket socket;

    public Server() throws IOException {
        socket = new ServerSocket(4242);
    }

    public void start() {

    }
}
