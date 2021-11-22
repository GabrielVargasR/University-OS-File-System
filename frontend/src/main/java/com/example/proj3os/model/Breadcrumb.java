package com.example.proj3os.model;

public class Breadcrumb {
    private final String dirName;
    private final String pathForDir;
    private final int index;

    public Breadcrumb(String dirName, String pathForDir, int index) {
        this.dirName = dirName;
        this.pathForDir = pathForDir;
        this.index = index;
    }

    public String getDirName() {
        return dirName;
    }

    public int getIndex() {
        return index;
    }

    public String getPathForDir() {
        return pathForDir;
    }
}
