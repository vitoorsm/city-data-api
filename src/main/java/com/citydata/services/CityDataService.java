package com.citydata.services;

import com.citydata.exception.BadRequestException;
import com.citydata.models.CityData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CityDataService {
    private static final String OPEN_WEATHER_MAP_API_KEY = "033f7ce0c6c033a9d9281bec0785a275";
    private static final String OPEN_AI_API_KEY = "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";
    public CityData getCurrentWeather(String city, String countryCode){
        String response = getCurrentWeatherDataResponse(city, countryCode);
        return formatResponseToCurrentWeatherClass(response);
    }
    private String getCurrentWeatherDataResponse(String city, String countryCode) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + countryCode + "&appid=" + OPEN_WEATHER_MAP_API_KEY + "&units=metric");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException("There was an error with OpenWeatherMap API");
        }
    }
    private CityData formatResponseToCurrentWeatherClass(String response) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject dataObj = (JSONObject) parse.parse(response);
            JSONObject mainData = (JSONObject) dataObj.get("main");
            JSONObject sysData = (JSONObject) dataObj.get("sys");
            JSONArray weatherData = (JSONArray) dataObj.get("weather");

            String weatherDescription = String.valueOf(weatherData);
            weatherDescription = weatherDescription.substring(weatherDescription.indexOf("description")+14, weatherDescription.indexOf("main")-3);

            Gson gson = new GsonBuilder().create();
            CityData cityData = gson.fromJson(String.valueOf(mainData), CityData.class);
            cityData.setCity((String) dataObj.get("name"));
            cityData.setCountry(gson.fromJson(String.valueOf(sysData.get("country")), String.class));
            cityData.setDescription(StringUtils.capitalize(weatherDescription));
            return cityData;

        }
        catch (Exception e){
            throw new BadRequestException("Error while parsing");
        }
    }

    public String getCityTextFromGPT(String message){
        try {
            OpenAiService service = new OpenAiService(OPEN_AI_API_KEY);
            CompletionRequest request = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt(message)
                    .temperature(0.7)
                    .maxTokens(800)
                    .build();
            String response = String.valueOf((service.createCompletion(request)).getChoices());
            String responseCut = response.substring(response.indexOf("text=")+5, response.indexOf(", index")).trim();
            Pattern pattern = Pattern.compile("\\n");
            Matcher matcher = pattern.matcher(responseCut);
            while (matcher.find()){
                responseCut = responseCut.replace(matcher.group()," ");
            }
            return responseCut;
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException("Couldn't connect to OpenAI API, please try again");
        }
    }
}
