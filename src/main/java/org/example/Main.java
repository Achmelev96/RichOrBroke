package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the desired currency code in capital letters");
        Scanner sc = new Scanner(System.in);
        String currency = sc.nextLine();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String monthStr = String.valueOf(month);
        String dayStr = String.valueOf(day);

        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        if (day < 10) {
            dayStr = "0" + dayStr;
        }

        String yesterday = year + "-" + monthStr + "-" + dayStr;
        double yesterdayCurrency = getCurrencyValue(yesterday, currency);
        double todayCurrency = getCurrencyValue(null, currency);

        String url;
        if (yesterdayCurrency > todayCurrency) {
            url = getGif("rich");
        } else {
            url = getGif("broke");
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static double getCurrencyValue(String date, String currency) {
        double currencyValue;
        final String APP_ID = "ad1c3a856f32428db34c670bfefe84c1";
        String spec = "https://openexchangerates.org/api/latest.json?app_id=" + APP_ID;

        if (date != null){
            spec= "https://openexchangerates.org/api/historical/" + date + ".json?app_id=" + APP_ID;
        }

        try {
            URL url = new URL(spec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null){
                result.append(line);
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject jsonRates = (JSONObject) jsonObject.get("rates");
            currencyValue = (double) jsonRates.get(currency);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return currencyValue;
    }

    static String getGif(String search) {
        String gifUrl;
        final String APP_ID = "MGR9scZeBiM84H88nbtqoLxVvdN8vxzl";
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/search?q=" + search + "&api_key=" + APP_ID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null){
                result.append(line);
            }


            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONArray jsonArray = (JSONArray)  jsonObject.get("data");
            JSONObject gifNum = (JSONObject) jsonArray.get((int)(Math.random() * jsonArray.size()));
            gifUrl = (String) gifNum.get("id");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return getGifFullUrl(gifUrl);
    }

    static String getGifFullUrl(String gifUrl) {
        String fullUrl = "https://i.giphy.com/" + gifUrl + ".gif";
        return fullUrl;
    }
}
