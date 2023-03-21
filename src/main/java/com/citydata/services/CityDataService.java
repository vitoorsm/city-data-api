package com.citydata.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CityDataService {
    private String getCurrentWeatherDataResponse(String city, String countryCode, String API_KEY) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + countryCode + "&appid=" + API_KEY + "&units=metric");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            if(conn.getResponseCode() != 200){
                throw new RuntimeException("Couldn't connect to API");
            }
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
