package com.dob.telegrambotformsocial.utils;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class JsonParser {
    public static String getJSONFromURL(String strUrl) {
        StringBuilder jsonText = new StringBuilder();
        try {
            URL url = new URI(strUrl).toURL();
            InputStream is = url.openStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText.append(line).append("\n");
            }
            is.close();
            bufferedReader.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonText.toString();
    }

    public List<String> parseJsonArrayToObject(String url,String key) {
        String strJson = getJSONFromURL(url);
        List<String> list = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray a = (JSONArray) parser.parse(strJson);
            for (Object o:a
                 ) {
                JSONObject jsonObject = (JSONObject) o;
                var jsonKey = jsonObject.get(key);
                list.add(String.valueOf(jsonKey));
            }
        }
        catch(Exception e) {
            //todo logger
            log.error(e.getMessage());
        }
        return list;
    }
}
