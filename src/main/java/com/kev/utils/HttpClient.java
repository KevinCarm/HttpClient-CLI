package com.kev.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.net.ssl.HttpsURLConnection;


public class HttpClient {
    private final String url;
    private final String method;
    private final String body;
    private final List<String> headers;

    private HttpClient(HttpClientBuilder builder) {
        this.body = builder.body;
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers;
    }

    public static class HttpClientBuilder {
        private String url;
        private String method;
        private String body;
        private List<String> headers;

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

        public HttpClientBuilder setHeaders(List<String> headers) {
            this.headers = headers;
            return this;
        }
    }

    public String sendRequest() throws IOException, InterruptedException {
        URL _url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) _url.openConnection();
        connection.setRequestMethod(!this.method.isEmpty() ? this.method : "GET");
        connection.setDoOutput(!this.method.equals("GET"));
        if (!this.headers.isEmpty()) {
            for (String header : this.headers) {
                String[] values = header.split(":");
                connection.setRequestProperty(values[0], values[1]);
            }
        }

        CompletableFuture<String> future = CompletableFuture.supplyAsync(getSupplier(connection));
        while (!future.isDone()) {
            Thread.sleep(50);
            System.out.print(".");
        }
        System.out.println();
        return future.join();
    }

    private Supplier<String> getSupplier(HttpURLConnection connection) {
        return () -> {
            try {
                if (!body.isEmpty()) {
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = this.body.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

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

    @Override
    public String toString() {
        return "HttpClient{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                '}';
    }
}
