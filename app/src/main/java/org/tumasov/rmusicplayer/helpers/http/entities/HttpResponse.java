package org.tumasov.rmusicplayer.helpers.http.entities;

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

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
