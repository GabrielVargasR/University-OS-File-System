package com.example.proj3os.controllers;

import com.example.proj3os.model.FsFile;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    public FileController() {}

    public List<FsFile> getFiles(String user, String path) {
        StringBuilder str = new StringBuilder();
        str.append("http://localhost:3000/api/file?username=");
        str.append(user);

        try {
            URL url = new URL(str.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            ReadContext json = JsonPath.parse(content.toString());
            ArrayList<?> ls = json.read("$", ArrayList.class);

            ReadContext jsonObj;
            FsFile file;
            ArrayList<FsFile> directoryContents = new ArrayList<FsFile>();
            for (int i = 0; i < ls.size(); i++) {
                jsonObj = JsonPath.parse(ls.get(i));
                file = jsonObj.read("$.type").equals("file") ?
                        this.buildFsFile(jsonObj) : new FsFile(jsonObj.read("$.name"));
                directoryContents.add(file);
            }
            return directoryContents;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FsFile buildFsFile(ReadContext json) {
        FsFile file = new FsFile(
                json.read("$.name"),
                json.read("$.extension"),
                json.read("$.creation"),
                json.read("$.modified"),
                json.read("$.size")
        );
        return file;
    }
}
