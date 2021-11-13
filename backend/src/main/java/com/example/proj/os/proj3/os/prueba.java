package com.example.proj.os.proj3.os;

import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import com.jayway.jsonpath.*;

public class prueba {
    public static void main(String[] args) {
        StringBuilder str = new StringBuilder();
        ReadContext json;
        try {
//            File file = new File("src/main/resources/file_system.json");
//            Scanner scanner = new Scanner(file);
//
//            while (scanner.hasNextLine()) {
//                str.append(scanner.nextLine());
//            }
//            System.out.println(str.toString());
            json = JsonPath.parse(new File("src/main/resources/file_system.json"));
            System.out.println((String) json.read("$.gabo.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        JSONObject obj = new JSONObject(str.toString());
//        System.out.println(((JSONObject) obj.get("gabo")).get("password"));
    }
}
