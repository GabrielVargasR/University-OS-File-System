package com.example.proj.os.proj3.os.fileSystem;

import com.example.proj.os.proj3.os.IConstants;
import com.example.proj.os.proj3.os.other.RuntimeTypeAdapterFactory;
import com.example.proj.os.proj3.os.pojos.Directory;
import com.example.proj.os.proj3.os.pojos.File;
import com.example.proj.os.proj3.os.pojos.FileSystem;
import com.example.proj.os.proj3.os.pojos.FileSystemElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// Loads and saves the File System from JSON File
public class JsonManager implements IConstants {

    private static final String EXTENSION = ".json";

    public Gson getBuilder(){
        final RuntimeTypeAdapterFactory<FileSystemElement> typeFactory = RuntimeTypeAdapterFactory
                .of(FileSystemElement.class, TYPE, true) // Here you specify which is the parent class and what field particularizes the child class.
                .registerSubtype(Directory.class, DIRECTORY) // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
                .registerSubtype(File.class, FILE);

        return new GsonBuilder().registerTypeAdapterFactory(typeFactory).setPrettyPrinting().serializeNulls().create();
    }

    public String getFileSystemFileAsString(String pName) throws IOException {
        return Files.readString(Paths.get(FILE_SYSTEM_PATH + FILEPATH_SEPARATOR + pName + EXTENSION), StandardCharsets.UTF_8);
    }

    public FileSystem getFileSystem(String pName){
        try {
            return getBuilder().fromJson(getFileSystemFileAsString(pName), FileSystem.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean writeToFileSystem(FileSystem fileSystem){
        try{
            Writer writer = new FileWriter(FILE_SYSTEM_PATH + FILEPATH_SEPARATOR + fileSystem.getUsername() + EXTENSION);
            getBuilder().toJson(fileSystem, writer);
            writer.flush(); //flush data to file   <---
            writer.close(); //close write          <---
        } catch (IOException ioException){
            return false;
        }
        return true;
    }

    public boolean createFileSystem(FileSystem fileSystem){
        try{
            java.io.File writer = new java.io.File(FILE_SYSTEM_PATH + FILEPATH_SEPARATOR + fileSystem.getUsername() + EXTENSION);
            writer.createNewFile();
            writeToFileSystem(fileSystem);
        } catch (IOException ioException){
            return false;
        }
        return true;
    }

    public boolean checkIfFsExist(String pName) {
        return Files.exists(Paths.get(FILE_SYSTEM_PATH + FILEPATH_SEPARATOR + pName + EXTENSION));
        
    }
}
