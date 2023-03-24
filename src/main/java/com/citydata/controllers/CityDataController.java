package com.citydata.controllers;

import com.citydata.dtos.CityDto;
import com.citydata.models.CityData;
import com.citydata.services.CityDataService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@AllArgsConstructor
public class CityDataController {

    private CityDataService cityDataService;

    @GetMapping("/")
    public ModelAndView getInfo(ModelMap model){
        model.addAttribute("cityDto", new CityDto());
        return new ModelAndView("/cityform", model);
    }

    @PostMapping("/data")
    public ModelAndView receiveInfoAndReturnData(@ModelAttribute @Valid CityDto cityDto, ModelMap model, BindingResult result) throws Exception {
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());
            return new ModelAndView("/error");
        }
        CityData cityData = cityDataService.getCurrentWeather(cityDto.getCity(), cityDto.getCountry());
        cityData.setText(cityDataService.getCityTextFromGPT("Basic information about the city of " + cityDto.getCity()));
        model.addAttribute("cityData", cityData);
        return new ModelAndView("/citydata", model);
    }
}