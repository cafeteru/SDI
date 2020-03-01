package com.igm1990;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.igm1990.services.CountryService;

@Configuration
public class CountryConfiguration {
	@Value("${service.endpoint}")
	private String serviceEndpoint;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage>
		// specified in
		// pom.xml
		marshaller.setContextPath("es.igm1990.consumingwebservice.wsdl");
		return marshaller;
	}

	@Bean
	public CountryService countryClient(Jaxb2Marshaller marshaller) {
		CountryService client = new CountryService();
		client.setDefaultUri(serviceEndpoint);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}