package org.tumasov.rmusicplayer.helpers.http.entities;

import java.util.LinkedList;
import java.util.List;

public class HttpRequest {
    private final String URL;
    private final String method;
    private final List<HttpParameter> headers;
    private final String body;
    private final boolean useCache;
    private final int readTimeout;
    private final int connectionTimeout;
    private boolean doInput;
    private boolean doOutput;

    private HttpRequest(HttpBuilder httpBuilder) {
        this.URL = httpBuilder.URL;
        this.method = httpBuilder.method;
        this.headers = httpBuilder.headers;
        this.useCache = httpBuilder.useCache;
        this.readTimeout = httpBuilder.readTimeout;
        this.connectionTimeout = httpBuilder.connectionTimeout;
        this.doInput = httpBuilder.doInput;
        this.doOutput = httpBuilder.doOutput;
        this.body = httpBuilder.body;
    }

    public static class HttpBuilder {
        private final String URL;
        private final String method;
        private final List<HttpParameter> headers = new LinkedList<>();
        private int connectionTimeout = 0;
        private int readTimeout = 0;
        private boolean useCache = false;
        private boolean doInput = true;
        private boolean doOutput = false;
        private String body;

        public HttpBuilder(String URL, String method) {
            this.URL = URL;
            this.method = method;
        }

        public HttpBuilder addHeader(HttpParameter header) {
            this.headers.add(header);
            return this;
        }

        public HttpBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpBuilder useCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public HttpBuilder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public HttpBuilder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public HttpBuilder doInput(boolean doInput) {
            this.doInput = doInput;
            return this;
        }

        public HttpBuilder doOutput(boolean doOutput) {
            this.doOutput = doOutput;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public String getURL() {
        return URL;
    }

    public String getMethod() {
        return method;
    }

    public List<HttpParameter> getHeaders() {
        return headers;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public boolean isDoInput() {
        return doInput;
    }

    public boolean isDoOutput() {
        return doOutput;
    }

    public String getBody() {
        return body;
    }
}
