package com.uniovi.services.util;

import java.io.File;

public class CreateFolder {
	/**
	 * Crea una carpeta por código si esta no existe previamente.
	 * 
	 * @param url
	 */
	public static void createFolder(String url) {
		File file = new File(url);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
