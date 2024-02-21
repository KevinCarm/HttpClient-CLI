package com.kev.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.net.ssl.HttpsURLConnection;


public class HttpClient {
    private final String url;
    private final String method;
    private final String body;

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

    public String sendRequest() throws IOException, InterruptedException {
        URL _url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) _url.openConnection();
        connection.setRequestMethod(!this.method.isEmpty() ? this.method : "GET");
        connection.setDoOutput(!this.method.equals("GET"));
        connection.setRequestProperty("Content-Type", "application/json");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(getSupplier(connection));
        while (!future.isDone()) {
            Thread.sleep(100);
            System.out.print(".");
        }
        System.out.println();
        return future.join();
    }

    private Supplier<String> getSupplier(HttpURLConnection connection) {
        return () -> {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                return content.toString();
            } catch (IOException e) {
                return e.getMessage();
            }
        };
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
