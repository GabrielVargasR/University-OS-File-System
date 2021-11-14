package com.example.proj3os.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UserController {
    public UserController() {
    }

    public int login(String username, String password) {
        try {
            URL url = new URL(this.urlBuilder("login", username, password));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            return con.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int signup(String username, String password) {
        try {
            URL url = new URL(this.urlBuilder("signup", username, password));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            return con.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String urlBuilder(String endpoint, String username, String password) {
        StringBuilder str = new StringBuilder();
        str.append("http://localhost:3000/api/");
        str.append(endpoint);
        str.append("?username=");
        str.append(username);
        str.append("&password=");
        str.append(password);

        return str.toString();
    }
}
