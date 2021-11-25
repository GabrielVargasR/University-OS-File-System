package com.example.proj3os.helper;

import com.example.proj3os.model.Directory;
import com.example.proj3os.model.File;
import com.example.proj3os.model.FileSystemElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.example.proj3os.helper.IConstants.*;

public class Common {

    /**
     * Read json from url string.
     *
     * @param url is the url
     * @return a string with the json response from the API
     */
    //~Kendall P
    public static String readJsonFromUrl(String url) {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            return readAll(rd);
        } catch (IOException e) {
            return "Could not read the json from the url";
        }
    }

    /**
     * Read all string.
     *
     * @param rd the rd
     * @return a string
     * @throws IOException Build strings and read all
     */
    //~Kendall P
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Make api call int.
     *
     * @param url      the url
     * @param httpType Make an api call
     * @return the int
     */
    public static int makeApiCall(URL url, String httpType) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(httpType);
            int responseCode = con.getResponseCode();
            con.disconnect();
            return responseCode;
        } catch (IOException ignored) {}
        return 404;
    }

    public static Gson getBuilder(){
        final RuntimeTypeAdapterFactory<FileSystemElement> typeFactory = RuntimeTypeAdapterFactory
                .of(FileSystemElement.class, TYPE, true) // Here you specify which is the parent class and what field particularizes the child class.
                .registerSubtype(Directory.class, DIRECTORY) // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
                .registerSubtype(File.class, FILE);

        return new GsonBuilder().registerTypeAdapterFactory(typeFactory).setPrettyPrinting().serializeNulls().create();
    }

}
