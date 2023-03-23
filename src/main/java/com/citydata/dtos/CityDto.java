package com.citydata.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityDto {
    @NotBlank(message = "city must not be blank")
    private String city;
    @NotBlank(message = "country must not be blank")
    @Size(min = 2, max = 2)
    private String country;
}
