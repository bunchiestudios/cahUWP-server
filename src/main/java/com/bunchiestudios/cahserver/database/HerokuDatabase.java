package com.bunchiestudios.cahserver.database;

import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Future;
import com.twitter.util.FuturePool;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;

import java.sql.*;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by rdelfin on 9/10/16.
 */
public class HerokuDatabase {
    private FuturePool pool;
    private String host, port, path, username, password;

    private final static int THREADS = 20;

    public HerokuDatabase(String host, String port, String path, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.path = path;

        pool = new ExecutorServiceFuturePool(Executors.newFixedThreadPool(THREADS));
    }


    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:postgresql://" + host + ':' + port + path;

        return DriverManager.getConnection(dbUrl, username, password);
    }

    private Future<ResultSet> getQuery(String sql, List<Object> params) throws SQLException {
        return pool.apply(new AbstractFunction0<Future<ResultSet>>() {
            @Override
            public Future<ResultSet> apply() {
                try {
                    Connection con = getConnection();
                    PreparedStatement statement = con.prepareStatement(sql);
                    for (int i = 0; i < params.size(); i++) {
                        statement.setObject(i, params.get(i));
                    }

                    return Future.value(statement.executeQuery());
                } catch(Exception e) {
                    return Future.exception(e);
                }
            }
        }).flatMap(new AbstractFunction1<Future<ResultSet>, Future<ResultSet>>() {
            @Override
            public Future<ResultSet> apply(Future<ResultSet> resultSetFuture) {
                return resultSetFuture;
            }
        });
    }

    private Future<Void> executeQuery(String sql, List<Object> params) {
        return pool.apply(new AbstractFunction0<Future<Void>>() {
            @Override
            public Future<Void> apply() {
                try {
                    Connection con = getConnection();
                    PreparedStatement statement = con.prepareStatement(sql);
                    for (int i = 0; i < params.size(); i++) {
                        statement.setObject(i, params.get(i));
                    }

                    statement.execute();
                    return Future.value(null);
                } catch(Exception e) {
                    return Future.exception(e);
                }
            }
        }).flatMap(new AbstractFunction1<Future<Void>, Future<Void>>() {
            @Override
            public Future<Void> apply(Future<Void> resultSetFuture) {
                return resultSetFuture;
            }
        });
    }
}
