package com.example.proj.os.proj3.os.fileSystem;


import java.util.Arrays;

import com.example.proj.os.proj3.os.IConstants;
import com.example.proj.os.proj3.os.pojos.*;

public class JsonFileSystem implements IConstants {

    private FileSystem fileSystem;

    private final JsonManager jsonManager = new JsonManager();

    private static final JsonFileSystem jsonFileSystem = new JsonFileSystem();

    public static JsonFileSystem getInstance(){
        return jsonFileSystem;
    }

    public FileSystem getFileSystem(String pUsername) {
        if (jsonManager.checkIfFsExist(pUsername)) {
            loadFileSystem(pUsername);
            return fileSystem;  
        }
        return null;
    }

    public Directory getDirectory(String pUsername, String pPath) {
        loadFileSystem(pUsername);
        return getDirectoryAux(fileSystem.getFiles(), pPath.split(FILEPATH_SEPARATOR), 1);
    }

    public File getFile(String pUsername, String pPath) {
        loadFileSystem(pUsername);
        String[] pathArray = pPath.split(FILEPATH_SEPARATOR);
        String[] dirPath = Arrays.copyOfRange(pathArray, 0, (pathArray.length-1));
        String fileName = pathArray[pathArray.length-1];

        Directory dir = getDirectoryAux(fileSystem.getFiles(), dirPath, 0);

        if (dir != null) {
            for (FileSystemElement element : dir.getContents()) {
                if (element.getName().equals(fileName) && element.getType().equals(FILE)) {
                    return (File) element;
                }
            }
        }

        // Not Found
        return null;
    }

    public boolean saveFileSystem() {
        return jsonManager.writeToFileSystem(fileSystem);
    }

    public boolean createFileSystem(String pUsername, String pPassword) {
        Directory rootDir = new Directory(ROOT_NAME);
        rootDir.getContents().add(new Directory(SHARED_DIR_NAME));
        FileSystem fs = new FileSystem(pUsername, pPassword, rootDir);
        return jsonManager.createFileSystem(fs);
    }

    private Directory getDirectoryAux(Directory pRoot, String[] pPath, int pPathIndex){
        if (pPath.length <= pPathIndex) {
            return pRoot;
        }
        
        for (FileSystemElement element : pRoot.getContents()) {
            if (element.getName().equals(pPath[pPathIndex]) && element.getType().equals(DIRECTORY)) {
                return getDirectoryAux( (Directory) element, pPath, pPathIndex+1);
            }
        }

        // Not found
        return null;

    }

    private void loadFileSystem(String pUsername) {
        // En caso de que se quiera editar en memoria se puede modificar aqui.
        fileSystem = jsonManager.getFileSystem(pUsername);
    }

    private JsonFileSystem(){}
}
