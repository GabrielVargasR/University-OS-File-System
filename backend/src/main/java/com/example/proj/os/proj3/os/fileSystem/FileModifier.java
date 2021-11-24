package com.example.proj.os.proj3.os.fileSystem;

import com.example.proj.os.proj3.os.pojos.Directory;
import com.example.proj.os.proj3.os.pojos.File;
import com.example.proj.os.proj3.os.pojos.FileSystem;
import com.example.proj.os.proj3.os.pojos.FileSystemElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.proj.os.proj3.os.fileSystem.IConstants.*;

public class FileModifier {

    public static boolean createFile(String pFileName, String username, String path, String created, String modified, String extension, String size, String content) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem();
        File newFile;

        if(content.equals("")){
            newFile = new File(
                    pFileName,
                    FILE,
                    EXTENSION,
                    formatter.format(date),
                    formatter.format(date),
                    0,
                    ""
            );
        } else {
            newFile = new File(
                    pFileName,
                    FILE,
                    extension,
                    created,
                    modified,
                    Integer.parseInt(size),
                    content
            );
        }


        ArrayList<FileSystemElement> userFiles = fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(username)
                ).findFirst()
                .orElseThrow()
                .getFiles();


        String[] pathArray = path.split(ROOT);
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
                        user -> user.getUsername().equals(username)
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

    public static boolean createDirectory(String folderName, String username, String path) throws Exception {
        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem();


        com.example.proj.os.proj3.os.pojos.Directory newDirectory = new com.example.proj.os.proj3.os.pojos.Directory(
                folderName,
                DIRECTORY,
                new ArrayList<>()
        );

        ArrayList<FileSystemElement> userFiles = fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(username)
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
                        user -> user.getUsername().equals(username)
                ).findFirst()
                .orElseThrow()
                .setFiles(userFiles);

        return JsonFileSystem.getInstance().writeToFileSystem(fileSystem);
    }

    public static FileSystemElement getDirectoryContents(String username, String path) throws Exception {
        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem();

        ArrayList<FileSystemElement> userFiles = fileSystem.getUsers()
                .stream()
                .filter(
                        user -> user.getUsername().equals(username)
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
