package com.example.proj3os.controllers;

import com.example.proj3os.helper.Common;
import com.example.proj3os.helper.IConstants;
import com.example.proj3os.model.Directory;
import com.example.proj3os.model.File;
import com.example.proj3os.model.FileSystemElement;
import com.example.proj3os.model.SessionInfo;
import com.example.proj3os.views.files.FilesView;
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
import java.util.Set;

import javax.swing.filechooser.FileView;

import static com.example.proj3os.helper.IConstants.DIRECTORY;

public class FileController {
    public FileController() {
    }

    /**
     * Returns a new name if file or folder with same name already exist in current
     * path.
     *
     * @param name the name
     * @return the string
     */
    public static String newNameWithIndex(String name, String directory) {
        ArrayList<String> filesNames = new ArrayList<>();

        getFiles(SessionInfo.getInstance().getUsername(), directory)
                .forEach(fsFile -> filesNames.add(fsFile.getName()));

        boolean found = false;
        String newName = name;
        int index = 0;
        int limit = Integer.MAX_VALUE;
        while (!found) {
            if (!filesNames.contains(newName)) {
                found = true;
            } else {
                newName = name + index;
            }

            index++;

            if (index == limit) {
                break;
            }
        }
        if (!found) {
            return "";
        } else {
            return newName;
        }
    }

    public static int createFile(String fileName, String user, String currentPath, boolean replaceFlag) {
        // String newName = newNameWithIndex(fileName, currentPath);
        try {
            String endpoint = "http://localhost:3000/api/createFile?fileName=" + URLEncoder
                    .encode(fileName + "&user=" + user + "&path=" + currentPath + "&replaceFlag="
                            + Boolean.toString(replaceFlag), StandardCharsets.UTF_8.toString())
                    .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            int res = Common.makeApiCall(new URL(endpoint), "GET");
            if (res == HttpStatus.OK.value()) {
                return 0;
            }
            if (res == HttpStatus.CONFLICT.value()) {
                return 1;
            }
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            return 2;
        }
        return 2;
    }

    public static int createFile(String pFileName, String username, String path, String created, String modified,
            String extension, String size, String content, boolean replaceFlag) {
        try {
            String endpoint = "http://localhost:3000/api/createFile?fileName=" + URLEncoder
                    .encode(pFileName + "&user=" + username + "&path=" + path + "&created=" + created + "&modified="
                            + modified + "&extension=" + extension + "&size=" + size + "&content=" + content
                            + "&replaceFlag=" + Boolean.toString(replaceFlag), StandardCharsets.UTF_8.toString())
                    .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            int res = Common.makeApiCall(new URL(endpoint), "GET");
            if (res == HttpStatus.OK.value()) {
                return 0;
            }
            if (res == HttpStatus.CONFLICT.value()) {
                return 1;
            }
        } catch (MalformedURLException | UnsupportedEncodingException f) {
            return 2;
        }
        return 2;
    }

    public static boolean modifyFile(String pFileName, String username, String path, String created, String modified,
            String extension, String size, String content) {
        try {
            String endpoint = "http://localhost:3000/api/modifyFile?fileName=" + URLEncoder.encode(
                    pFileName + "&user=" + username + "&path=" + path + "&created=" + created + "&modified=" + modified
                            + "&extension=" + extension + "&size=" + size + "&content=" + content,
                    StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                return true;
            }
        } catch (MalformedURLException | UnsupportedEncodingException f) {
            return false;
        }
        return false;
    }

    public static File openFile(String pFileName, String username, String path) {
        String url = "";
        try {
            url = "http://localhost:3000/api/opneFile?username="
                    + URLEncoder.encode(username + "&path=" + path, StandardCharsets.UTF_8.toString())
                            .replaceAll("%26", "&").replaceAll("%3D", "=");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ReadContext json = JsonPath.parse(Common.readJsonFromUrl(url));
        File fileSystemElement = Common.getBuilder().fromJson(json.jsonString(), File.class);

        return fileSystemElement;
    }

    public static List<FileSystemElement> getFiles(String user, String path) {
        String url = "";
        try {
            url = "http://localhost:3000/api/file?username="
                    + URLEncoder.encode(user + "&path=" + path, StandardCharsets.UTF_8.toString())
                            .replaceAll("%26", "&").replaceAll("%3D", "=");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(url);
        ReadContext json = JsonPath.parse(Common.readJsonFromUrl(url));
        FileSystemElement fileSystemElement = Common.getBuilder().fromJson(json.jsonString(), FileSystemElement.class);

        return ((Directory) fileSystemElement).getContents();
    }

    public static ReadContext getFilesReadContext(String user, String path) {
        String url = "";
        try {
            url = "http://localhost:3000/api/file?username="
                    + URLEncoder.encode(user + "&path=" + path, StandardCharsets.UTF_8.toString())
                            .replaceAll("%26", "&").replaceAll("%3D", "=");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(url);
        return JsonPath.parse(Common.readJsonFromUrl(url));
    }

    public static int createDirectory(String folderName, String user, String currentPath, boolean replaceFlag) {
        // String newName = newNameWithIndex(folderName, currentPath);
        try {
            String endpoint = "http://localhost:3000/api/createDirectory?dirName=" + URLEncoder
                    .encode(folderName + "&user=" + user + "&path=" + currentPath + "&replaceFlag="
                            + Boolean.toString(replaceFlag), StandardCharsets.UTF_8.toString())
                    .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
            System.out.println(endpoint);
            int res = Common.makeApiCall(new URL(endpoint), "GET");
            if (res == HttpStatus.OK.value()) {
                return 0;
            }
            if (res == HttpStatus.CONFLICT.value()) {
                return 1;
            }
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            return 2;
        }
        return 2;
    }

    public static boolean deleteItems(String username, String currentPath, Set<FileSystemElement> elements) {
        try {
            StringBuilder str = new StringBuilder();
            str.append("http://localhost:3000/api/");
            str.append("deleteItems");
            str.append("?user=");
            str.append(username);
            str.append("&path=");
            str.append(currentPath);

            URL url = new URL(str.toString());

            Directory dir = new Directory("TEMP", DIRECTORY, new ArrayList<FileSystemElement>(elements));

            return Common.makePOSTCallWithBody(url, dir) == HttpStatus.OK.value();
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean deleteItem(String username, String currentPath, FileSystemElement element) {
        try {
            StringBuilder str = new StringBuilder();
            str.append("http://localhost:3000/api/");
            str.append("deleteItem");
            str.append("?user=");
            str.append(username);
            str.append("&path=");
            str.append(currentPath);
            str.append("&itemName=");
            str.append(element.getName());
            str.append("&itemType=");
            str.append(element.getType());

            URL url = new URL(str.toString());

            return Common.makeApiCall(url, "GET") == HttpStatus.OK.value();
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean shareElement(FileSystemElement element, String targetUser){
        String targetDirectory = IConstants.SHARED_DIR_NAME;

        //String newName = newNameWithIndex(element.getName(), targetDirectory);

        if (element.getType().equals(DIRECTORY)) {

            try {
                String endpoint = "http://localhost:3000/api/createDirectory?dirName="
                        + URLEncoder.encode(
                                newName + "&user=" + targetUser + "&path="
                                        + targetDirectory + "&replaceFlag=" + Boolean.toString(false),
                                StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                    Directory directory = (Directory) element;
                    for (FileSystemElement content : directory.getContents()) {
                        copyFile(content, SessionInfo.getInstance().getCurrentDirectory(), targetDirectory + "/" + newName);
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                return false;
            }

        } else {
            try {
                File file = (File) element;
                String endpoint = "http://localhost:3000/api/createFile?fileName="
                        + URLEncoder
                                .encode(newName + "&user=" + targetUser + "&path="
                                        + targetDirectory + "&created=" + file.getCreation() + "&modified="
                                        + file.getModified() + "&extension=" + file.getExtension() + "&size="
                                        + file.getSize() + "&content=" + file.getContents() + "&replaceFlag="
                                        + Boolean.toString(false), StandardCharsets.UTF_8.toString())
                                .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                    return true;
                }
            } catch (MalformedURLException | UnsupportedEncodingException f) {
                return false;
            }
        }

        return false;
    }

    public static boolean copyFile(FileSystemElement fileToCopy, String currentDirectory, String targetDirectory) {
        String newName = newNameWithIndex(fileToCopy.getName(), targetDirectory);

        if (fileToCopy.getType().equals(DIRECTORY)) {

            try {
                String endpoint = "http://localhost:3000/api/createDirectory?dirName="
                        + URLEncoder.encode(
                                newName + "&user=" + SessionInfo.getInstance().getUsername() + "&path="
                                        + targetDirectory + "&replaceFlag=" + Boolean.toString(false),
                                StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                    Directory directory = (Directory) fileToCopy;
                    for (FileSystemElement content : directory.getContents()) {
                        copyFile(content, currentDirectory, targetDirectory + "/" + newName);
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                return false;
            }

        } else {
            try {
                File file = (File) fileToCopy;
                String endpoint = "http://localhost:3000/api/createFile?fileName="
                        + URLEncoder
                                .encode(newName + "&user=" + SessionInfo.getInstance().getUsername() + "&path="
                                        + targetDirectory + "&created=" + file.getCreation() + "&modified="
                                        + file.getModified() + "&extension=" + file.getExtension() + "&size="
                                        + file.getSize() + "&content=" + file.getContents() + "&replaceFlag="
                                        + Boolean.toString(false), StandardCharsets.UTF_8.toString())
                                .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                    return true;
                }
            } catch (MalformedURLException | UnsupportedEncodingException f) {
                return false;
            }
        }

        return false;
    }

    public static boolean moveFile(FileSystemElement fileToCopy, String currentDirectory, String targetDirectory) {
        FilesView.setMovingFlag(false);

        String newName = newNameWithIndex(fileToCopy.getName(), targetDirectory);

        if (fileToCopy.getType().equals(DIRECTORY)) {

            try {
                String endpoint = "http://localhost:3000/api/createDirectory?dirName="
                        + URLEncoder.encode(
                                newName + "&user=" + SessionInfo.getInstance().getUsername() + "&path="
                                        + targetDirectory + "&replaceFlag=" + Boolean.toString(false),
                                StandardCharsets.UTF_8.toString()).replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") == HttpStatus.OK.value()) {
                    Directory directory = (Directory) fileToCopy;
                    for (FileSystemElement content : directory.getContents()) {
                        copyFile(content, currentDirectory, targetDirectory + "/" + newName);
                    }
                } else {
                    return false;
                }
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                return false;
            }

        } else {
            try {
                File file = (File) fileToCopy;
                String endpoint = "http://localhost:3000/api/createFile?fileName="
                        + URLEncoder
                                .encode(newName + "&user=" + SessionInfo.getInstance().getUsername() + "&path="
                                        + targetDirectory + "&created=" + file.getCreation() + "&modified="
                                        + file.getModified() + "&extension=" + file.getExtension() + "&size="
                                        + file.getSize() + "&content=" + file.getContents() + "&replaceFlag="
                                        + Boolean.toString(false), StandardCharsets.UTF_8.toString())
                                .replaceAll("%26", "&").replaceAll("%3D", "=").trim();
                System.out.println(endpoint);
                if (Common.makeApiCall(new URL(endpoint), "GET") != HttpStatus.OK.value()) {
                    return false;
                }
            } catch (MalformedURLException | UnsupportedEncodingException f) {
                return false;
            }
        }

        deleteItem(SessionInfo.getInstance().getUsername(), currentDirectory, fileToCopy);

        return false;
    }
}
