package com.xuyurepos.service.intergration.mobile.sign;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xuyurepos.service.intergration.mobile.util.CMPCore;



@Service
public class CMPSign  {

static	Logger logger=Logger.getLogger(CMPSign.class);

	public static String sign(Map params) {
		if (logger.isInfoEnabled()) {
			logger.info("CMP-->签名请求数据：" + params);
			
		}
		
		String sign="";
		
		try {

			
		    sign=	CMPCore.buildMysign(params, "UTF-8");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
      return  sign;
	}
	
	public static String signFengXian(Map params) {
		if (logger.isInfoEnabled()) {
			logger.info("CMP-->签名请求数据：" + params);
			
		}
		
		String sign="";
		
		try {

			
		    sign=	CMPCore.buildMysignFengXian(params, "UTF-8");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
      return  sign;
	}

}
