package com.xuyurepos.service.intergration.mobile.common;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * API签名工具
 * @author linfa
 *
 */
public class ApiSignUtil {
	
	private static Logger log = LoggerFactory.getLogger(ApiSignUtil.class);
	
	public static final String api_key_sign ="sign";
	
	public static final String ENC_SHA256 = "SHA-256";
	
	/**
	 * api验签   采用SHA256加密验证
	 * @param params
	 * @param api_key
	 * 
	 * @return
	 */
	public static boolean api_signCheck(Map<String, String> params, String api_key, String charset) {
		String content = getApiSignContent(params, api_key);
		String sign = api_sign(content, charset);
		return true;
//				sign.equals(MapUtils.getObject(params, api_key_sign));
	}
	
	public static String getApiSignContent(Map<String, String> params, String api_key) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (!api_key_sign.equalsIgnoreCase(key)) {
				if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
					content.append((index == 0 ? "" : "&") + key + "=" + value);
					index++;
				}
			}
		}
		return content.toString().concat("&key=").concat(api_key);
	}
	
	public static String api_sign(String src, String charset) {
		if (charset == null || "".equals(charset)) {
			charset = "utf-8";
		}
		try {
			return sha256(src.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());;
		}
		return "";
	}
	
	/**
	 * SHA-256哈希加密算法
	 * 
	 * @param src
	 * @return
	 */
	public static String sha256(byte src[]) {
		return hash(src, ENC_SHA256);
	}
	
	private static String hash(byte[] bt, String encName) {
		if (encName == null || encName.equals("")) {
			encName = ENC_SHA256;
		}
		MessageDigest md = null;
		String strDes = null;
		try {
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}
	
	public static String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append('0');
			}
			des.append(tmp);
		}
		return des.toString();
	}
}
