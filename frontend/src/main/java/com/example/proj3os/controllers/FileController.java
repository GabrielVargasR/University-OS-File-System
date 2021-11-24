package com.example.proj3os.controllers;

import com.example.proj3os.helper.Common;
import com.example.proj3os.model.Directory;
import com.example.proj3os.model.FileSystemElement;
import com.example.proj3os.model.SessionInfo;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    public FileController() {}

    /**
     * Returns a new name if file or folder with same name already exist in current path.
     *
     * @param name the name
     * @return the string
     */
    public static String newNameWithIndex(String name){
        ArrayList<String> filesNames = new ArrayList<>();

        getFiles(SessionInfo.getInstance().getUsername(), SessionInfo.getInstance().getCurrentDirectory())
                .forEach(fsFile -> filesNames.add(fsFile.getName()));

        boolean found = false;
        String newName = name;
        int index = 0;
        int limit = Integer.MAX_VALUE;
        while (!found){
            if(!filesNames.contains(newName)){
                found = true;
            } else {
                newName = name + index;
            }

            index++;

            if(index == limit){
                break;
            }
        }
        if(!found){
            return "";
        } else {
            return newName;
        }
    }

    public static boolean createFile(String fileName, String user, String currentPath){
        String newName = newNameWithIndex(fileName);
        try {
            String endpoint = "http://localhost:3000/api/createFile?fileName=" + URLEncoder.encode(newName+"&user="+user+"&path="+currentPath, StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            if(Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()){
                return true;
            }
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            return false;
        }
        return false;
    }

    public static List<FileSystemElement> getFiles(String user, String path) {
        String url = "";
        try {
            url = "http://localhost:3000/api/file?username=" + URLEncoder.encode(user + "&path="+path, StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(url);
        ReadContext json = JsonPath.parse(Common.readJsonFromUrl(url));
        FileSystemElement fileSystemElement = Common.getBuilder().fromJson(json.jsonString(), FileSystemElement.class);


        return ((Directory) fileSystemElement).getContents();
    }

    public static ReadContext getFilesReadContext(String user, String path){
        String url = "";
        try {
            url = "http://localhost:3000/api/file?username=" + URLEncoder.encode(user + "&path="+path, StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(url);
        return JsonPath.parse(Common.readJsonFromUrl(url));
    }

    public static boolean createDirectory(String folderName, String user, String currentPath) {
        String newName = newNameWithIndex(folderName);
        try {
            String endpoint = "http://localhost:3000/api/createDirectory?dirName=" + URLEncoder.encode(newName+"&user="+user+"&path="+currentPath, StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            if(Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()){
                return true;
            }
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            return false;
        }
        return false;
    }
}
