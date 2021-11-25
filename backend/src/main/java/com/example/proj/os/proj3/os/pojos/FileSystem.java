package com.example.proj.os.proj3.os.pojos;

public class FileSystem {
    private String username;
    private String password;
    private Directory files;

    public FileSystem (String username, String password, Directory files) {
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

    public Directory getFiles() {
        return files;
    }

    public void setFiles(Directory files) {
        this.files = files;
    }

}
