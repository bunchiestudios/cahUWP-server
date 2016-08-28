package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Server {
    private static Logger log = Logger.getLogger(Server.class);
    private int CLIENT_THREADS = 100;

    public void start() {
        ExecutorService clientPool = Executors.newFixedThreadPool(100);
        
        Runnable runnable = () -> {
            try {
                ServerSocket server = new ServerSocket(4242);
                while(true) {
                    Socket client = server.accept();
                    clientPool.submit(new ClientHandler(client));
                    log.info("Client connected");
                }
            } catch(IOException e) {
                log.error("Error establishing socket!", e);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
