package com.example.proj.os.proj3.os.pojos;

import java.util.ArrayList;

//A directory is a special kind fo file that has more files inside
public class Directory extends FileSystemElement {
    ArrayList<FileSystemElement> contents;

    public Directory(String name, String type, ArrayList<FileSystemElement> contents) {
        super(name, type);
        this.contents = contents;
    }

    public ArrayList<FileSystemElement> getContents() {
        return contents;
    }

    public void setContents(ArrayList<FileSystemElement> contents) {
        this.contents = contents;
    }
}
