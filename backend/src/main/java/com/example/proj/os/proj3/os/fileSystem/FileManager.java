package com.example.proj.os.proj3.os.fileSystem;

import com.example.proj.os.proj3.os.pojos.*;

import static com.example.proj.os.proj3.os.IConstants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileManager {

    public static boolean createFile(String fileName, String username, String path, String created, String modified,
            String extension, String size, String content) throws Exception {

        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);

        if (currentDir != null) {
            if (currentDir.findFile(fileName) == null) {
                File newFile;

                if (content.equals("")) {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    newFile = new File(fileName, FILE, EXTENSION, formatter.format(date), formatter.format(date), 0,
                            "");
                } else {
                    newFile = new File(fileName, FILE, extension, created, modified, Integer.parseInt(size), content);
                }

                currentDir.getContents().add(newFile);
            } else {
                // ! FALTA
                // Archivo ya existe se podria tirar exception
                return false;
            }

            return fileSystem.saveFileSystem();
        } else {
            // ! FALTA
            // Directorio no existe
            return false;
        }

    }

    public static boolean createDirectory(String folderName, String username, String path) throws Exception {
        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);

        if (currentDir != null) {
            if (currentDir.findDir(folderName) == null) {

                Directory newDirectory = new Directory(folderName, new ArrayList<>());
                currentDir.getContents().add(newDirectory);
                
            } else{
                // ! FALTA
                // Folder already exist
                return false;
            }
        } else { 
            // ! FALTA
            // Dir not found
            return false;
        }

        return fileSystem.saveFileSystem();
    }

    public static FileSystemElement getDirectoryContents(String username, String path) throws Exception {
        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);

        if (currentDir == null){
            // ! Falta
            // Dir not found
        }
        return currentDir;
    }

    public boolean deleteFile(String username, String path, String fileName) {
        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);
        File fileToDelete = currentDir.findFile(fileName);
        boolean res = currentDir.getContents().remove(fileToDelete);

        return fileSystem.saveFileSystem() && res;
    }

    public static boolean deleteItems(String username, String path, ArrayList<FileSystemElement> elements ) {
        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);

        boolean res = true;
        for (FileSystemElement element : elements) {
            FileSystemElement fileToDelete = currentDir.find(element.getName(),element.getType());
            res = currentDir.getContents().remove(fileToDelete) && res;          
        }

        return fileSystem.saveFileSystem() && res;
    }

    public boolean deleteDir(String username, String path, String dirName){
        JsonFileSystem fileSystem = JsonFileSystem.getInstance();
        Directory currentDir = fileSystem.getDirectory(username, path);
        Directory dirToDelete = currentDir.findDir(dirName);
        boolean res = currentDir.getContents().remove(dirToDelete);

        return fileSystem.saveFileSystem() && res;
    }
}
