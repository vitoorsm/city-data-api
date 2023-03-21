package com.citydata.services;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityTextService {
    private static final String API_KEY = "";
    public String getCityTextFromGPT(String message){
        OpenAiService service = new OpenAiService(API_KEY);
        CompletionRequest request = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(message)
                .temperature(0.7)
                .maxTokens(800)
                .build();
        String response = String.valueOf((service.createCompletion(request)).getChoices());
        return response.substring(response.indexOf("text=")+5, response.indexOf(", index")).trim();
    }
}
