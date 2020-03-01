package com.igm1990.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import es.igm1990.consumingwebservice.wsdl.GetCountryRequest;
import es.igm1990.consumingwebservice.wsdl.GetCountryResponse;

public class CountryService extends WebServiceGatewaySupport {
	// Coge el valor del fichero application.properties
	@Value("${service.endpoint}")
	private String serviceEndpoint;
	
	@Value("${service.soap.action}")
	private String serviceSoapAction;

	private static final Logger log = LoggerFactory
			.getLogger(CountryService.class);

	public GetCountryResponse getCountry(String country) {

		GetCountryRequest request = new GetCountryRequest();
		request.setName(country);

		log.info("Requesting location for " + country);

		GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
				.marshalSendAndReceive(serviceEndpoint, request,
						new SoapActionCallback(serviceSoapAction));

		return response;
	}

}