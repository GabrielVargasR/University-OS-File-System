package com.example.proj.os.proj3.os.pojos;

import java.util.ArrayList;

import com.example.proj.os.proj3.os.IConstants;

//A directory is a special kind fo file that has more files inside
public class Directory extends FileSystemElement implements IConstants {
    ArrayList<FileSystemElement> contents;

    public Directory(String name, ArrayList<FileSystemElement> contents) {
        super(name, DIRECTORY);
        this.contents = contents;
    }

    public Directory(String name) {
        super(name, DIRECTORY);
        this.contents = new ArrayList<FileSystemElement>();
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
            if (element.getName().equals(pFileName) && element.getType().equals(FILE)) {
                return (File) element;
            }
        }
        return null;
    }

    // ? Es medio contradictorio tener esta funcion y el getDirectory arriba.
    public Directory findDir(String pDirName){
        for (FileSystemElement element : getContents()) {
            if (element.getName().equals(pDirName) && element.getType().equals(DIRECTORY)) {
                return (Directory) element;
            }
        }
        return null;
    }

    // ? Es medio contradictorio tener esta funcion y el getDirectory arriba.
    public FileSystemElement find(String pDirName, String pType){
        for (FileSystemElement element : getContents()) {
            if (element.getName().equals(pDirName) && element.getType().equals(pType)) {
                return element;
            }
        }
        return null;
    }
}
