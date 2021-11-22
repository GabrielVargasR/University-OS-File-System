package com.example.proj.os.proj3.os.pojos;

import java.util.ArrayList;

public class FileSystem {
    private ArrayList<User> users;

    public FileSystem(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
