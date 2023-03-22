package com.citydata.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityData {
    private String city;
    private String country;
    private String temp;
    private String feels_like;
    private String temp_min;
    private String temp_max;
    private String humidity;
    private String description;
    private String text;
}
