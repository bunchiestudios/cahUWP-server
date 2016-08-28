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
    
    private int CLIENT_THREADS = 100;   //Number of client threads
    private int port;    //Stores the port to be used for comms
    
    private String dburl;  //Stored the URL of the DB to be used
    
    public Server(){
        try{
            //Read PORT system variable
            int port = Integer.parseInt(System.getenv("PORT"));

            //Read DATABASE_URL system variable
            dburl = System.getenv("DATABASE_URL");
            
            //Debug data
            System.out.println("Creating server.\nListening on: " + port);
            System.out.println("Using DB URL: " + dburl);
            
        }catch(NumberFormatException ex){
            System.err.println("Error reading PORT variable!\n" + ex.getMessage());
        }catch(Exception ex){
            System.err.println("There was an exception in server constructor!\n" + ex.getMessage());
        }
    }

    public void start() {
        ExecutorService clientPool = Executors.newFixedThreadPool(CLIENT_THREADS);
        
        Runnable runnable = () -> {
            try {
                ServerSocket server = new ServerSocket(port);
                while(true) {
                    Socket client = server.accept();
                    clientPool.submit(new ClientHandler(client));
                    System.out.println("Client connected");
                }
            } catch(IOException e) {
                System.err.println("Error establishing socket!\n" + e.getMessage());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
