package com.example.proj.os.proj3.os.fileSystem;

import com.example.proj.os.proj3.os.IConstants;
import com.example.proj.os.proj3.os.pojos.FileSystem;

public class UserManager implements IConstants {
    
    public boolean checkUser(String pUsername){
        FileSystem fs = JsonFileSystem.getInstance().getFileSystem(pUsername);
        return fs != null;
    }

    public boolean checkCredentials(String pUsername, String pPassword){
        FileSystem fs = JsonFileSystem.getInstance().getFileSystem(pUsername);
        return fs.getUsername().equals(pUsername) && fs.getPassword().equals(pPassword);
    }

    public boolean createUser(String pUsername, String pPassword) {
        JsonFileSystem.getInstance().createFileSyste(pUsername, pPassword);
        return false;
    }
}
