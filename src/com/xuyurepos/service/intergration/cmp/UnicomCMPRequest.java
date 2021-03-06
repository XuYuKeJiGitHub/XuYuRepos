package com.xuyurepos.service.intergration.cmp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuyurepos.common.util.JsonUtil;
import com.xuyurepos.service.intergration.unicom.UnicomCore;
import com.xuyurepos.service.intergration.unicom.UnicomHttpTookit;

public class UnicomCMPRequest {

	static  Logger logger=LoggerFactory.getLogger(UnicomCMPRequest.class);
	
	public static Map header() {
		String authBasic=	UnicomCore.buildMysign();
		Map header=new HashMap();
		//--header "Accept:application/json" --header "Authorization:Basic ZHB0cmlhbFVzZXIxOmQ3MDNmNTJiLTEyMDAtNDMxOC1hZTBkLTBmNjA5MmIyZTZhYg==
		
		header.put("Accept","application/json");
		header.put("Authorization","Basic "+authBasic);
		
		return header;
	}
	

	public static String unicomCMPRequestGet(String requestURL) throws ClientProtocolException, IOException {
		logger.info("调用联通接口请求URL："+requestURL);
			Map header=header();
			String result=	UnicomHttpTookit.doGet(requestURL, header,"UTF-8");
		logger.info("调用联通接口请求返回结果："+result);	
			return result;
	}
	
	public static String unicomCMPRequestPost(String requestURL,String params) throws ClientProtocolException, IOException {
		logger.info("调用联通接口请求URL："+requestURL);
		logger.info("调用联通接口请求参数："+params);
		Map header=header();
        header.put("Content-Type", "application/json");
		String result=	UnicomHttpTookit.doPost(requestURL,params, header,"UTF-8");
		logger.info("调用联通接口请求返回结果："+result);	
		return result;
}
	
	public static String unicomCMPRequestPut(String requestURL,String params) throws ClientProtocolException, IOException {
		logger.info("调用联通接口请求URL："+requestURL);
		logger.info("调用联通接口请求参数："+params);
		Map header=header();
        header.put("Content-Type", "application/json");
		String result=	UnicomHttpTookit.doPut(requestURL,params, header,"UTF-8");
		logger.info("调用联通接口请求返回结果："+result);	
		return result;
}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Map map=new HashMap();
		//map.put("messageText", "测试用11111");
		map.put("status", "ACTIVATION_READY");
		String params=JsonUtil.parseMap2Json(map);
//		unicomCMPRequestPost("https://api.10646.cn/rws/api/v1/devices/"+"89860619000004899757"+"/smsMessages",params);
//		unicomCMPRequestGet("https://api.10646.cn/rws/api/v1/devices/89860619000004899757/ctdUsages");
		unicomCMPRequestPut("https://api.10646.cn/rws/api/v1/devices/89860619000004899757",params);
//		unicomCMPRequestGet("https://api.10646.cn/rws/api/v1/devices/"+"89860619000004899757");

		
	}
}
