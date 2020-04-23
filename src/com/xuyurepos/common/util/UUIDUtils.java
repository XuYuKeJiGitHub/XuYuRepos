package com.xuyurepos.common.util;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.xuyurepos.common.log.LoggerFactory;

public class UUIDUtils {
	private static Logger log=LoggerFactory.getInstance().getLogger(UUIDUtils.class);
	
	public static String getUUID() {
		String uuid = "";
		uuid =UUID.randomUUID().toString().replaceAll("-","");  
		return uuid;
	}
	public static void main(String[] args) {
		log.info(getUUID());
	}

}
