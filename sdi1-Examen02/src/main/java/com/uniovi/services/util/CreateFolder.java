package com.uniovi.services.util;

import java.io.File;

/**
 * Clase encargada de crear las carpetas necesarias para nuestra aplicación.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
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
