package com.xml.parsing.demo.activities.utils;

/**
 * @author dipenp
 *
 */
public class StringUtils {

	/**
	 * checkin if string is empty
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmpty(String string) {
		return (null == string || "".equals(string.trim())) ? true : false;
	}
}
