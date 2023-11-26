package com.example.asynctaskwithapiexample.utilities;

import com.example.asynctaskwithapiexample.parsers.FloatRatesXmlParser;
import com.example.asynctaskwithapiexample.parsers.GunfireHtmlParser;
import com.example.asynctaskwithapiexample.parsers.MeteoLtJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.asynctaskwithapiexample.utilities.Constants.FLOATRATES_API_URL;
import static com.example.asynctaskwithapiexample.utilities.Constants.GUNFIRE_URL;
import static com.example.asynctaskwithapiexample.utilities.Constants.METEOLT_API_URL;

public class ApiDataReader {
    public static String getValuesFromApi(String apiCode) throws IOException {
        InputStream apiContentStream = null;
        String result = "";
        try {
            switch (apiCode) {
                case METEOLT_API_URL:
                    apiContentStream = downloadUrlContent(METEOLT_API_URL);
                    result = MeteoLtJsonParser.getKaunasWeatherForecast(apiContentStream);
                    break;
                case FLOATRATES_API_URL:
                    apiContentStream = downloadUrlContent(FLOATRATES_API_URL);
                    result = FloatRatesXmlParser.getCurrencyRatesBaseUsd(apiContentStream);
                    break;
                case GUNFIRE_URL:
                    apiContentStream = downloadUrlContent(GUNFIRE_URL);
                    result = GunfireHtmlParser.getAmountAndDiscountFromGunfire(apiContentStream);
                    break;
                default:
            }
        }
        finally {
            if (apiContentStream != null) {
                apiContentStream.close();
            }
        }
        return result;
    }

    //Routine that creates and calls GET request to web page
    private static InputStream downloadUrlContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
