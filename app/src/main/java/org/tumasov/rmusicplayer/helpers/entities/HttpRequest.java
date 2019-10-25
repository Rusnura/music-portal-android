package org.tumasov.rmusicplayer.helpers.entities;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class HttpRequest {
    private final java.net.URL URL;
    private final String method;
    private final List<HttpParameter> headers;
    private final List<HttpParameter> body;
    private final boolean useCache;
    private final int readTimeout;
    private boolean doInput;
    private boolean doOutput;

    public HttpRequest(HttpBuilder httpBuilder) {
        this.URL = httpBuilder.URL;
        this.method = httpBuilder.method;
        this.headers = httpBuilder.headers;
        this.useCache = httpBuilder.useCache;
        this.readTimeout = httpBuilder.readTimeout;
        this.doInput = httpBuilder.doInput;
        this.doOutput = httpBuilder.doOutput;
        this.body = httpBuilder.bodyParameters;
    }

    public static class HttpBuilder {
        private final URL URL;
        private final String method;
        private final List<HttpParameter> headers = new LinkedList<>();
        private final List<HttpParameter> bodyParameters = new LinkedList<>();
        private int readTimeout = 0;
        private boolean useCache = false;
        private boolean doInput = true;
        private boolean doOutput = false;

        public HttpBuilder(URL URL, String method) {
            this.URL = URL;
            this.method = method;
        }

        public HttpBuilder addHeader(HttpParameter header) {
            this.headers.add(header);
            return this;
        }

        public HttpBuilder addBodyParameter(HttpParameter bodyParameter) {
            this.bodyParameters.add(bodyParameter);
            return this;
        }

        public HttpBuilder useCache(boolean useCache) {
            this.useCache = useCache;
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

    public java.net.URL getURL() {
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

    public int getReadTimeout() {
        return readTimeout;
    }

    public boolean isDoInput() {
        return doInput;
    }

    public boolean isDoOutput() {
        return doOutput;
    }
}
