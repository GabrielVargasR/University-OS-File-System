package com.example.proj.os.proj3.os.controllers;

import com.jayway.jsonpath.PathNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@RestController
public class UserController {

    @GetMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam("username") String user, @RequestParam("password") String password){
        try {
            ReadContext json = JsonPath.parse(new File("src/main/resources/file_system.json"));
            String pass = json.read("$." + user + ".password");
            if (password.equals(pass)) {
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("Incorrect Password", HttpStatus.UNAUTHORIZED);
        } catch (PathNotFoundException e) {
            return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PostMapping(value = "/api/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestParam("username") String user, @RequestParam("password") String password) {
        StringBuilder jsonStr = new StringBuilder();
        try {
            File file = new File("src/main/resources/file_system.json");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                jsonStr.append(scanner.nextLine());
            }

            JSONObject json = new JSONObject(jsonStr.toString());

            if (json.has(user)) {
                return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
            }

            JSONObject newUser = new JSONObject();
            newUser.put("password", password);
            newUser.put("files", new JSONArray());

            json.put(user, newUser);
            FileWriter writer = new FileWriter("src/main/resources/file_system.json");
            writer.write(json.toString());
            writer.close();

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.FAILED_DEPENDENCY);
        }
    }


}
