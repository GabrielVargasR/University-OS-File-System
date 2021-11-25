package com.example.proj.os.proj3.os.controllers;

import com.example.proj.os.proj3.os.fileSystem.FileManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @GetMapping(value = "/api/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDirectory(@RequestParam("username") String username, @RequestParam("path") String path){
        try {
            Object files = FileManager.getDirectoryContents(username, path);
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/createFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFile(@RequestParam("fileName") String pFileName,
                                        @RequestParam("user") String user,
                                        @RequestParam("path") String path,
                                        @RequestParam(value = "created", required = false) String created,
                                        @RequestParam(value = "modified", required = false) String modified,
                                        @RequestParam(value = "extension", required = false) String extension,
                                        @RequestParam(value = "size", required = false) String size,
                                        @RequestParam(value = "content", required = false) String content){
        try {
            if(FileManager.createFile(pFileName, user, path, created, modified, extension, size, content)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/createDirectory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDirectory(@RequestParam("dirName") String dirName, @RequestParam("user") String user, @RequestParam("path") String path){
        try {
            if(FileManager.createDirectory(dirName, user, path)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
