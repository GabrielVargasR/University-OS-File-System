package com.example.proj.os.proj3.os.controllers;

import com.example.proj.os.proj3.os.IConstants;
import com.example.proj.os.proj3.os.fileSystem.FileManager;
import com.example.proj.os.proj3.os.fileSystem.JsonFileSystem;
import com.example.proj.os.proj3.os.fileSystem.JsonManager;
import com.example.proj.os.proj3.os.pojos.Directory;
import com.example.proj.os.proj3.os.pojos.FileSystemElement;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @GetMapping(value = "/api/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDirectory(@RequestParam("username") String username,
            @RequestParam("path") String path) {
        try {
            Object files = FileManager.getDirectoryContents(username, path);
            if (files == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/createFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFile(@RequestParam("fileName") String pFileName, @RequestParam("user") String user,
            @RequestParam("path") String path, @RequestParam("replaceFlag") Boolean replaceFlag,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "created", required = false) String created,
            @RequestParam(value = "modified", required = false) String modified,
            @RequestParam(value = "extension", required = false) String extension,
            @RequestParam(value = "size", required = false) String size) {
        try {
            if (FileManager.createFile(pFileName, user, path, created, modified, extension, size, content,
                    replaceFlag)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e.getMessage().equals(IConstants.EXISTING_ELEMENT_MESSAGE)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/modifyFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyFile(@RequestParam("fileName") String pFileName, @RequestParam("user") String user,
            @RequestParam("path") String path, @RequestParam(value = "content") String content,
            @RequestParam(value = "created", required = false) String created,
            @RequestParam(value = "modified", required = false) String modified,
            @RequestParam(value = "extension", required = false) String extension,
            @RequestParam(value = "size", required = false) String size) {
        try {
            if (FileManager.modifyFile(pFileName, user, path, created, modified, extension, size, content)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/createDirectory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDirectory(@RequestParam("dirName") String dirName, @RequestParam("user") String user,
            @RequestParam("replaceFlag") Boolean replaceFlag, @RequestParam("path") String path) {
        try {
            if (FileManager.createDirectory(dirName, user, path, replaceFlag)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e.getMessage().equals(IConstants.EXISTING_ELEMENT_MESSAGE)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/deleteItems", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteItems(@RequestBody String directoryJson, @RequestParam("user") String user,
            @RequestParam("path") String path) {
        try {
            Directory directory = JsonManager.getBuilder().fromJson(directoryJson, Directory.class);
            FileManager.deleteItems(user, path, directory.getContents());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/deleteItem", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteItem(@RequestParam("itemName") String itemName,
            @RequestParam("itemType") String itemType, @RequestParam("user") String user,
            @RequestParam("path") String path) {
        try {
            if (FileManager.deleteItem(user, path, itemName, itemType)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
