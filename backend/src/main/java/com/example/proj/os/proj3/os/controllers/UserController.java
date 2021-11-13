package com.example.proj.os.proj3.os.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

    @GetMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("username") String user, @RequestParam("password") String password){
        return "qué pa " + user + " " + password;
    }

    @PostMapping(value = "/api/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestParam("username") String user, @RequestParam("password") String password) {
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


}
