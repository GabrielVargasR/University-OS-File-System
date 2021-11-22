package com.example.proj.os.proj3.os.controllers;

import com.example.proj.os.proj3.os.fileSystem.JsonFileSystem;
import com.example.proj.os.proj3.os.pojos.FileSystem;
import com.example.proj.os.proj3.os.pojos.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UserController {

    @GetMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password){

        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem();
        if(fileSystem.getUsers().stream().anyMatch(user -> user.getUsername().equals(username))
                && fileSystem.getUsers().stream().anyMatch(user -> user.getPassword().equals(password))){
            return new ResponseEntity<>(username, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Incorrect Password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/api/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestParam("username") String username, @RequestParam("password") String password) {
        FileSystem fileSystem = JsonFileSystem.getInstance().getFileSystem();

        if(fileSystem.getUsers().stream().anyMatch(user -> user.getUsername().equals(username))){
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        User newUser = new User(username, password, new ArrayList<>());
        fileSystem.getUsers().add(newUser);

        return new ResponseEntity<>(username, HttpStatus.CREATED);
    }


}
