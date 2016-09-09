package com.bunchiestudios.cahserver;

import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Request;
import com.twitter.server.AbstractTwitterServer;
import com.twitter.util.Future;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Server extends AbstractTwitterServer {
    private ServerService service = new ServerService();
    
    private int CLIENT_THREADS = 100;   //Number of client threads
    private int port;    //Stores the port to be used for comms

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void onInit() {
        ListeningServer server = null;
        try {
            log().info("Java Server initialization...");
            server = Http.serve(":" + port, service);
            server.wait();
        } catch(InterruptedException e) {
            log().warning("Server was interrupted. Closing");
        } finally {
            server.close();
        }

    }

    private class ServerService extends Service<Request, Response> {

        @Override
        public Future<Response> apply(Request request) {
            return null;
        }
    }
}
