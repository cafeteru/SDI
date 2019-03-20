package com.uniovi.test.util;

public class Random {
	public static Integer integer(int min, int max) {
		return (int) (new java.util.Random().nextFloat() * (max - min) + min);
	}

	public static String string(int length) {
		String res = "";
		for (int i = 0; i < length; i++) {
			res += (char) Random.integer('a', 'z').intValue();
		}
		return res;
	}

	public static String email() {
		return Random.string(12) + "@gmail.es";
	}
}
