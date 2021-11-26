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
        if (fs != null ) {
            return fs.getUsername().equals(pUsername) && fs.getPassword().equals(pPassword);
        }
        return false;
    }

    public boolean createUser(String pUsername, String pPassword, int pMaxSize) {
        return JsonFileSystem.getInstance().createFileSystem(pUsername, pPassword, pMaxSize);
    }
}
