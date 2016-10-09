package com.bunchiestudios.cahserver;

import com.bunchiestudios.cahserver.database.DataManager;
import com.bunchiestudios.cahserver.database.HerokuDatabase;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Request;
import com.twitter.server.AbstractTwitterServer;
import com.twitter.util.*;
import scala.runtime.AbstractFunction0;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by rdelfin on 8/27/16.
 */
public class Server extends AbstractTwitterServer {
    private Protocol protocol;
    private FuturePool pool;
    private DataManager mgr;
    
    private int CLIENT_THREADS = 100;   //Number of client threads
    private int port;    //Stores the port to be used for comms

    public Server(int port) throws ProcessingException, IOException {
        this.mgr = new DataManager(new HerokuDatabase());
        this.port = port;
        protocol = new Protocol(mgr);
        pool = new ExecutorServiceFuturePool(Executors.newFixedThreadPool(CLIENT_THREADS));
    }

    @Override
    public void onInit() {
        ListeningServer server = null;
        log().info("Java Server initialization...");
        server = Http.serve(":" + port, new Service<Request, Response>() {
            @Override
            public Future<Response> apply(Request request) {
                return pool.apply(new AbstractFunction0<Response>() {
                    @Override
                    public Response apply() {
                        return protocol.receive(request);
                    }
                });

            }
        });
        try {
            Await.ready(server);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            server.close();
        }

    }
}
