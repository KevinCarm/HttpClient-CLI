package com.kev.utils;

public class HttpClient {
    private String url;
    private String method;
    private String body;


    private HttpClient(HttpClientBuilder builder) {
        this.body = builder.body;
        this.method = builder.method;
        this.url = builder.url;
    }

    public static class HttpClientBuilder {
        private String url;
        private String method;
        private String body;

        public HttpClient build() {
            return new HttpClient(this);
        }

        public HttpClientBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public HttpClientBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpClientBuilder setMethod(String method) {
            this.method = method;
            return this;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpClient [url=" + url + ", method=" + method + ", body=" + body + "]";
    }
    
}
