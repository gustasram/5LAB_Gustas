package com.example.asynctaskwithapiexample.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MeteoLtJsonParser {
    public static String getKaunasWeatherForecast(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        String data = "";
        while(line != null){
            line = bufferedReader.readLine();
            data = data + line;
        }

        String result = "";
        try {
            JSONObject jData = new JSONObject(data);

            JSONObject placeNode = jData.getJSONObject("place");
            JSONObject coordinatesNodes = placeNode.getJSONObject("coordinates");
            String lat = coordinatesNodes.getString("latitude");
            String lon = coordinatesNodes.getString("longitude");
            String administrativeDivision = placeNode.getString("administrativeDivision");
            result = String.format("location name: %s,\n lat: %s,\n lon: %s", administrativeDivision, lat, lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
