package com.example.proj.os.proj3.os.pojos;

import com.google.gson.annotations.Expose;

public abstract class FileSystemElement {
    private String name;
    @Expose
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
}
