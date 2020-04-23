package com.xuyurepos.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.xuyurepos.common.constants.SystemConstants;

/**
 * 字符串出列类
 * 
 * @author yangfei
 *
 */
public class StringUtil extends StringUtils {

	/**
	 * 判定字符串是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static Boolean isEmpty(String val) {
		Boolean boolean1 = true;
		if (val != null && !SystemConstants.STRINGEMPTY.equals(val)) {
			boolean1 = false;
		}
		return boolean1;
	}

	/**
	 * 将返回信息InputStream转为String
	 * 
	 * @param is
	 * @author lvyiguang
	 * @return
	 */
	public String convertStreanToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
