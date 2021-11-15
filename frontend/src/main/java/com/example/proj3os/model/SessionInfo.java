package com.example.proj3os.model;

public class SessionInfo {
    private static final SessionInfo singleton = new SessionInfo();
    private static String username;
    private static String currentDirectory;

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
}
