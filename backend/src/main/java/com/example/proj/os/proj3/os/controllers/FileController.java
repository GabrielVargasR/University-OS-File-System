 package com.example.proj.os.proj3.os.controllers;

 import com.jayway.jsonpath.JsonPath;
 import com.jayway.jsonpath.ReadContext;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.MediaType;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.bind.annotation.GetMapping;

 import java.io.File;
 import java.io.IOException;

 @RestController
 public class FileController {

     @GetMapping(value = "/api/file", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<?> listDirectory(@RequestParam("username") String user){
         try {
             ReadContext json = JsonPath.parse(new File("src/main/resources/file_system.json"));
             return new ResponseEntity<>((Object) json.read("$." + user + ".files"), HttpStatus.OK);
         } catch (IOException e) {
             return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NOT_FOUND);
         }
     }

 }
