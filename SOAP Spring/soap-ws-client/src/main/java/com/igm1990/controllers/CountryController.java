package com.igm1990.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.igm1990.services.CountryService;

import es.igm1990.consumingwebservice.wsdl.Country;

@RestController
public class CountryController {
	@Autowired
	private CountryService countryService;

	@GetMapping("/countries")
	public String getMarks(@RequestParam String name) {
		Country country = countryService.getCountry(name).getCountry();
		if (country == null) {
			country = new Country();
		}
		return country.toString();
	}
}
