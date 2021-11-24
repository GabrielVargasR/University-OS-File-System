package com.example.proj.os.proj3.os.pojos;

import java.util.ArrayList;

import com.example.proj.os.proj3.os.IConstants;

//A directory is a special kind fo file that has more files inside
public class Directory extends FileSystemElement implements IConstants {
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

    // ? Es medio contradictorio tener esta funcion y el getDirectory arriba.
    public File findFile(String pFileName){
        for (FileSystemElement element : getContents()) {
            if (element.getName() == pFileName && element.getType() == FILE) {
                return (File) element;
            }
        }
        return null;
    }
}
