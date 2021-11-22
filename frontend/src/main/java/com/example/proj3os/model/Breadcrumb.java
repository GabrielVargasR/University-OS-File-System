package com.example.proj3os.model;

public class Breadcrumb {
    private final String dirName;
    private final int index;

    public Breadcrumb(String dirName, int index) {
        this.dirName = dirName;
        this.index = index;
    }

    public String getDirName() {
        return dirName;
    }

    public int getIndex() {
        return index;
    }
}
