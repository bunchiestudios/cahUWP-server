package com.bunchiestudios.cahserver.database;

/**
 * Created by rdelfin on 9/11/16.
 */
public class InvalidQueryException extends Exception {
    public InvalidQueryException(Exception e) {
        super("There was an error executing a query", e);
    }

    public InvalidQueryException(String query, Exception e) {
        super("There was an error executing query: " + query, e);
    }
}
