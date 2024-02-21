package com.kev;


import com.kev.utils.RequestMaker;

public class App {
    public static void main(String[] args) {
        RequestMaker maker = new RequestMaker(args);
        try {
            maker.makeRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
