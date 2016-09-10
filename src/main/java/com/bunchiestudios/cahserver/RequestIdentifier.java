package com.bunchiestudios.cahserver;

import io.netty.handler.codec.http.HttpMethod;

/**
 * Used to uniquely identify any uncomming request. One will be associated per child of ServerRequest class.
 */
public class RequestIdentifier {
    private String path;
    private String method;

    public RequestIdentifier(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestIdentifier that = (RequestIdentifier) o;

        if (!path.equals(that.path)) return false;
        return method.equals(that.method);

    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
