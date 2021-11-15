package com.example.proj.os.proj3.os;

import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;

public class prueba {
    public static void main(String[] args) {
        StringBuilder str = new StringBuilder();
        try {
            File file = new File("src/main/resources/file_system.json");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                str.append(scanner.nextLine());
            }

            JSONObject obj = new JSONObject(str.toString());
            obj.put("nuevo", new JSONObject());

            FileWriter writer = new FileWriter("src/main/resources/file_system.json");

            writer.write(obj.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
