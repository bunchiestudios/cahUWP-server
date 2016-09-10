package com.bunchiestudios.cahserver;

/**
 * Created by rdelfin on 9/10/16.
 */
public class PathNotFoundException extends Exception {
    private String path;

    public PathNotFoundException(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "404: \"" + path + "\" not found";
    }
}
