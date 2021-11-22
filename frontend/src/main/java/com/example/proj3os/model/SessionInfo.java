package com.example.proj3os.model;

import java.util.ArrayList;

public class SessionInfo {
    private static final SessionInfo singleton = new SessionInfo();
    private static String username;
    private static String currentDirectory;
    private static ArrayList<Breadcrumb> breadCrumbs = new ArrayList<>();

    private SessionInfo() { }

    public static SessionInfo getInstance() {
        return singleton;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        SessionInfo.username = username;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(String currentDirectory) {
        SessionInfo.currentDirectory = currentDirectory;
    }

    public ArrayList<Breadcrumb> getBreadCrumbs() {
        return breadCrumbs;
    }

    public void setBreadCrumbs(ArrayList<Breadcrumb> breadCrumbs) {
        SessionInfo.breadCrumbs = breadCrumbs;
    }
}
