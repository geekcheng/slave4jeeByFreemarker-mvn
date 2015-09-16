package com.billy.jee.slavebyfreemarker.util;

public class StringExUtil {

	/**
	 * 大写一个字符串的第一个字符
	 *
	 * @param s
	 * @return
	 */
	public static String capitaliseFirst(String s) {
		if(s == null){
			return null;
		}
		if(s.length() == 0){
			return s;
		}
		StringBuilder cap = new StringBuilder(s.substring(0, 1).toUpperCase());
		if(s.length() > 1) {
			cap.append(s.substring(1));
		}
		return cap.toString();
	}
}
