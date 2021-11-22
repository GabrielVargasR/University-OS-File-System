package com.example.proj.os.proj3.os.controllers;

import com.example.proj.os.proj3.os.fileSystem.FileModifier;
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
            Object files = FileModifier.getDirectoryContents(username, path);
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/createFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFile(@RequestParam("fileName") String pFileName, @RequestParam("user") String user, @RequestParam("path") String path){
        try {
            if(FileModifier.createFile(pFileName, user, path)){
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
            if(FileModifier.createDirectory(dirName, user, path)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
