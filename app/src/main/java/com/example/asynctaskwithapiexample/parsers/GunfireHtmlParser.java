package com.example.asynctaskwithapiexample.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GunfireHtmlParser {
    public static String getAmountAndDiscountFromGunfire(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        for (String line; (line = r.readLine()) != null; ) {
            Matcher m = Pattern.compile("id=\"projector_status_description\">(.*)").matcher(line);
            if(m.find()){
                return  m.group(1);
            }
        }
        return "";
    }
}
