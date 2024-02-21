package com.kev.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestMaker {
    private final List<String> parameters;

    public RequestMaker(String[] args) {
        this.parameters = Arrays.asList(args);
    }

    public void makeRequest() throws Exception {
        if (!parameters.contains("-u")) {
            throw new Exception("Url is required");
        }

        List<String> headers = new ArrayList<>();
        for (String parameter : this.parameters) {
            if (parameter.equals("-h")) {
                headers.add(
                        this.parameters.get(this.parameters.indexOf(parameter) + 1)
                );
            }
        }

        HttpClient client = new HttpClient.HttpClientBuilder()
                .setMethod(
                        parameters.contains("-m")
                                ? parameters.get(parameters.indexOf("-m") + 1)
                                : "")
                .setUrl(parameters.get(parameters.indexOf("-u") + 1))
                .setHeaders(headers)
                .build();
        System.out.println(client.sendRequest());
    }
}
