package com.uniovi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uniovi.services.util.CreateFolder;

public class LogService {

	private Logger log;

	public LogService(Object object) {
		CreateFolder.createFolder("/log");
		log = LoggerFactory.getLogger(object.getClass());
	}

	public void info(String message) {
		log.info(message);
	}

	public void error(String message) {
		log.error(message);
	}

	public void debug(String message) {
		log.debug(message);
	}

}
