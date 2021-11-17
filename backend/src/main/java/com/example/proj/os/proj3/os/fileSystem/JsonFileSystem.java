package com.example.proj.os.proj3.os.fileSystem;

import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JsonFileSystem {
    private static final String FILE_SYSTEM_PATH = "src/main/resources/file_system.json";
    private static JsonFileSystem instance = null ;

    private JSONObject fileSystemBuffer;

    public static JsonFileSystem getInstance(){
        if (instance == null) {
            instance = new JsonFileSystem();
        }        
        return instance;
    }

    // ! Semaphore is needed
    public JSONObject getFileSystemBuffer() {
        if (fileSystemBuffer == null) {
            loadFS();
        }
        return fileSystemBuffer;
    }
    
    public void loadFS(){
        StringBuilder jsonStr = new StringBuilder();
        try {
            File file = new File(FILE_SYSTEM_PATH);

            // ? Un Stream podria ser mejor idea
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                jsonStr.append(scanner.nextLine());
            }

            scanner.close();

            fileSystemBuffer = new JSONObject(jsonStr.toString());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FileSystem");
            fileSystemBuffer = new JSONObject();
        }
    }

    public void saveFS(){
        try {
            FileWriter writer = new FileWriter(FILE_SYSTEM_PATH);
            writer.write(fileSystemBuffer.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving FileSystem");
            e.printStackTrace();
        }

    }
}
