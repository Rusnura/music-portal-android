package org.tumasov.rmusicplayer.helpers.http.entities;

import java.net.HttpURLConnection;

public class HttpResponse {
    private int code;
    private String body;

    public HttpResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public boolean isSuccessful() {
        return code < HttpURLConnection.HTTP_BAD_REQUEST;
    }

    public boolean isAuthorized() {
        return code != HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
