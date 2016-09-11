package com.bunchiestudios.cahserver.database;

import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Future;
import com.twitter.util.FuturePool;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by rdelfin on 9/10/16.
 */
public class HerokuDatabase {
    private URI dbUri;
    private String username, password;

    public HerokuDatabase() {
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));

            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        } catch(URISyntaxException e) {
            System.err.println("DATABASE_URL environment variable could not be parsed: " + e);
        }

    }


    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
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
