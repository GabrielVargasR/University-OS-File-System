package com.example.proj.os.proj3.os.fileSystem;

import com.example.proj.os.proj3.os.pojos.*;

import static com.example.proj.os.proj3.os.IConstants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileModifier {

    public static boolean createFile(String pFileName, String pUsername, String pPath) throws Exception {

        Directory currentDir = JsonFileSystem.getInstance().getDirectory(pUsername, pPath);

        File currentFile = currentDir.findFile(pFileName);

        if (currentFile != null) {
            // ! FALTA

            // Archivo ya existe
        }


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();



        com.example.proj.os.proj3.os.pojos.File newFile = new com.example.proj.os.proj3.os.pojos.File(
                pFileName,
                FILE,
                EXTENSION,
                formatter.format(date),
                formatter.format(date),
                0,
                ""
        );

        fileSystem.getFiles();


        String[] pathArray = pPath.split(ROOT);
        if (pathArray.length!=0){
            String lastValue = pathArray[pathArray.length-1];
            ArrayList<FileSystemElement> targetDirectory = searchDirectory(userFiles, lastValue);
            ((Directory) targetDirectory.stream().filter(fileSystemElement -> fileSystemElement.getName().equals(lastValue)).findFirst().orElseThrow()).getContents().add(newFile);
        } else{
            userFiles.add(newFile);
        }

        fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(pUsername)
                ).findFirst()
                .orElseThrow()
                .setFiles(userFiles);

        return JsonFileSystem.getInstance().writeToFileSystem(fileSystem);
    }

    private static ArrayList<FileSystemElement> searchDirectory(ArrayList<FileSystemElement> userFiles, String lastValue) {
        if(userFiles==null){
            return null;
        }

        //List<FileSystemElement> directoriesOnLevel = userFiles.stream().filter(fileSystemElement -> fileSystemElement.getType().equals(DIRECTORY)).collect(Collectors.toList());
        ArrayList<FileSystemElement> directoriesOnLevel = new ArrayList<>();
        for (FileSystemElement userFile : userFiles) {
            if(userFile.getType().equals(DIRECTORY)){
                directoriesOnLevel.add(userFile);
            }
        }

        ArrayList<FileSystemElement> returnLevel = null;
        for (FileSystemElement fileSystemElement : directoriesOnLevel) {
           if(fileSystemElement.getName().equals(lastValue)){
               returnLevel = directoriesOnLevel;
               break;
           } else {
               returnLevel = searchDirectory(((Directory) fileSystemElement).getContents(), lastValue);
               if(returnLevel!=null){
                   break;
               }
           }
        }
        if(returnLevel==null || returnLevel.size() == 0){
            return null;
        } else {
            return returnLevel;
        }
    }

    public static boolean createDirectory(String folderName, String pUsername, String path) throws Exception {
        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem(pUsername);


        com.example.proj.os.proj3.os.pojos.Directory newDirectory = new com.example.proj.os.proj3.os.pojos.Directory(
                folderName,
                DIRECTORY,
                new ArrayList<>()
        );

        ArrayList<FileSystemElement> userFiles = fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(pUsername)
                ).findFirst()
                .orElseThrow()
                .getFiles();


        String[] pathArray = path.split(ROOT);
        if (pathArray.length!=0){
            String lastValue = pathArray[pathArray.length-1];
            ArrayList<FileSystemElement> targetDirectory = searchDirectory(userFiles, lastValue);
            ((Directory) targetDirectory.stream().filter(fileSystemElement -> fileSystemElement.getName().equals(lastValue)).findFirst().orElseThrow()).getContents().add(newDirectory);
        } else{
            userFiles.add(newDirectory);
        }

        fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(pUsername)
                ).findFirst()
                .orElseThrow()
                .setFiles(userFiles);

        return JsonFileSystem.getInstance().writeToFileSystem(fileSystem);
    }

    public static FileSystemElement getDirectoryContents(String pUsername, String path) throws Exception {
        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem(pUsername);

        ArrayList<FileSystemElement> userFiles = fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(pUsername)
                ).findFirst()
                .orElseThrow()
                .getFiles();

        String[] pathArray = path.split(ROOT);
        if (pathArray.length!=0){
            String lastValue = pathArray[pathArray.length-1];
            userFiles = searchDirectory(userFiles, lastValue);
            userFiles = ((Directory) userFiles.stream().filter(fileSystemElement -> fileSystemElement.getName().equals(lastValue)).findFirst().orElseThrow()).getContents();
            //userFiles = ((Directory) userFiles.get(0)).getContents();
        }

        return new Directory(ROOT, DIRECTORY, userFiles);
    }
}
