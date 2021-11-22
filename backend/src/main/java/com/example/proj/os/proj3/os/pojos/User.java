package com.example.proj.os.proj3.os.pojos;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<FileSystemElement> files;

    public User(String username, String password, ArrayList<FileSystemElement> files) {
        this.username = username;
        this.password = password;
        this.files = files;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<FileSystemElement> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileSystemElement> files) {
        this.files = files;
    }

    public void appendToFiles(ArrayList<FileSystemElement> files) {
        this.files.addAll(files);
    }
}
