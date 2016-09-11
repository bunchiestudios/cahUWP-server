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
    private String host, port, path, username, password;

    public HerokuDatabase(String host, String port, String path, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.path = path;
    }


    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:postgresql://" + host + ':' + port + path;

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public ResultSet getQuery(String sql, List<Object> params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement statement = con.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i, params.get(i));
        }

        return statement.executeQuery();
    }

    public void executeQuery(String sql, List<Object> params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement statement = con.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i, params.get(i));
        }

        statement.execute();
    }
}
