package com.xuyurepos.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.util.PropertiesUtil;

public class ProsUtil {
	private static String properiesName = "system.properties";

	private ProsUtil() {

	}

	public static String getProperty(String key) {
		String value = "";
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			Properties p = new Properties();
			p.load(is);
			value = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	public static void main(String[] args) {
		String log4jStr=ProsUtil.getProperty("log4j.path");
		System.out.println(log4jStr);
	}

}
