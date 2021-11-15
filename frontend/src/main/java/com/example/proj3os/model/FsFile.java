package com.example.proj3os.model;

public class FsFile {
    private final String name;
    private final String extension;
    private final String type;
    private final String creation;
    private final String modification;
    private final String size;

    public FsFile(String name, String extension, String creation, String modification, String size) {
        this.name = name;
        this.extension = extension;
        this.type = "file";
        this.creation = creation;
        this.modification = modification;
        this.size = size;
    }

    public FsFile(String name) {
        this.name = name;
        this.type = "directory";
        this.extension = "--";
        this.creation = "--";
        this.modification = "--";
        this.size = "--";
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }

    public String getCreation() {
        return creation;
    }

    public String getModification() {
        return modification;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "FsFile{" +
                "name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                ", type='" + type + '\'' +
                ", creation='" + creation + '\'' +
                ", modification='" + modification + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
