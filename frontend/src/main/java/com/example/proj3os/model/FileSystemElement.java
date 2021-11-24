package com.example.proj3os.model;

public class FileSystemElement {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileSystemElement(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getExtension(){
        if(this instanceof File){
            return ((File) this).getExtension();
        } else {
            return "--";
        }
    }

    public String getCreation() {
        if(this instanceof File){
            return ((File) this).getCreation();
        } else {
            return "--";
        }
    }

    public String getModified() {
        if(this instanceof File){
            return ((File) this).getModified();
        } else {
            return "--";
        }
    }

    public String getSizeString(){
        if(this instanceof File){
            return String.valueOf(((File) this).getSize());
        } else {
            return "--";
        }
    }
}
