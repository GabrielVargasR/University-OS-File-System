package com.example.proj.os.proj3.os.pojos;

public class File extends FileSystemElement {

    private String extension;
    private String creation;
    private String modified;
    private int size;
    private String contents;

    public File(String name, String fileType, String extension, String creation, String modified, int size, String contents) {
        super(name, fileType);
        this.extension = extension;
        this.creation = creation;
        this.modified = modified;
        this.size = size;
        this.contents = contents;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
