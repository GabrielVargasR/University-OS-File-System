package com.example.proj.os.proj3.os.controllers;

import com.example.proj.os.proj3.os.fileSystem.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserManager userManager = new UserManager();

    @GetMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password){

        if(userManager.checkCredentials(username, password)){
            return new ResponseEntity<>(username, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Incorrect Password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/api/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestParam("username") String username, @RequestParam("password") String password) {

        if(userManager.checkUser(username)){
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        userManager.createUser(username, password);

        return new ResponseEntity<>(username, HttpStatus.CREATED);
    }


}
