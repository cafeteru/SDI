package com.uniovi.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
