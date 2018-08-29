package com.uniovi.util;

/**
 * Clase que permite validar un DNI. Se crea un objeto del tipo ValidadorDNI y
 * se le pasa un String a validar.
 * 
 * @return true si DNI es correcto. Desarrollado por Manuel Mato.
 */
public class ValidatorDNI {

	public static boolean validate(String dni) {
		String letraMayuscula = "";
		if (dni.length() != 9 || Character.isLetter(dni.charAt(8)) == false)
			return false;
		letraMayuscula = (dni.substring(8)).toUpperCase();
		if (onlyNumbers(dni) == true && letterDNI(dni).equals(letraMayuscula))
			return true;
		return false;
	}

	private static boolean onlyNumbers(String dni) {
		int i, j = 0;
		String numero = "", miDNI = "";
		for (i = 0; i < dni.length() - 1; i++) {
			numero = dni.substring(i, i + 1);
			for (j = 0; j < 9; j++) {
				if (numero.equals("" + (j + 1))) {
					miDNI += numero;
				}
			}
		}
		if (miDNI.length() != 8)
			return false;
		return true;
	}

	private static String letterDNI(String dni) {
		int miDNI = Integer.parseInt(dni.substring(0, 8));
		int resto = 0;
		String miLetra = "";
		String[] asignacionLetra = { "T", "R", "W", "A", "G", "M", "Y", "F",
				"P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C",
				"K", "E" };
		resto = miDNI % 23;
		miLetra = asignacionLetra[resto];
		return miLetra;
	}
}
