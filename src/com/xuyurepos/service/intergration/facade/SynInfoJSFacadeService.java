/**
 * Project Name:XuYuRepos
 * File Name:SynInfoFacadeService.java
 * Package Name:com.xuyurepos.service.intergration.facade
 * Date:2019年3月29日下午4:21:31
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.xuyurepos.service.intergration.facade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.util.JsonUtil;
import com.xuyurepos.common.util.XmlMapUtils;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.service.intergration.cmp.MobileCMPRequest;
import com.xuyurepos.service.intergration.cmp.TelecomCMPRequest;
import com.xuyurepos.util.HttpClientUtil;

/**
 * ClassName:SynInfoFacadeService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019年3月29日 下午4:21:31 <br/>
 * @author   zhangliang
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class SynInfoJSFacadeService {

	static Logger logger=LoggerFactory.getLogger(SynInfoJSFacadeService.class);
	private final static String ccwlwbatchcontrolsr = "cc_wlw_batchcontrolsr";
	
    private SynInfoJSFacadeService() {
        if (SynInfoJSFacadeInnerService.instance != null) {
            throw new IllegalStateException();
        }
    }
    
    private static class SynInfoJSFacadeInnerService {
    	private static SynInfoJSFacadeService instance = new SynInfoJSFacadeService();
    }

    public static SynInfoJSFacadeService getInstance() {
        return SynInfoJSFacadeInnerService.instance;
        
    }
	
    @Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	/*******************************************移动接口调用开始***********************************************/
	/**
	 * 
	 * downTimeByMobile:(移动物联网卡停复机). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param msisdn		卡编号
	 * @param operType  1 停机  ；2复机
	 * @param ownerPlace  地区编号 1淮安 2盐城 3闵行4奉贤
	 * @return
	 * @throws Exception 
	 * @since JDK 1.8
	 */
    public static void main(String[] args) {
    	Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("telnum", "123");
		map.put("oprtype", "1");
		map.put("reason", "7");
		Map<String, Object> mapParam=new HashMap<String, Object>();
		
		mapParam.put("content", map);
		mapParam.put("appid", "2222");
		String createXmlByMap = XmlMapUtils.createXmlByMap2(mapParam, "GBK", "operation_in");
		Map<String, Object> createMapByXml = XmlMapUtils.createMapByXml(createXmlByMap);
		Map<String, Object> operation_in = (Map<String, Object>) createMapByXml.get("operation_in");
		
		Map<String, Object> content = (Map<String, Object>) operation_in.get("content");
		String oprtype = (String) content.get("oprtype");
		
		System.out.println(oprtype);
		System.out.println(createMapByXml);
		
	}
    private  static  Map<String, Object>  initNativeTFJ() {
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("process_code", "cc_wlw_controlsr")	;
			params.put("route_type","1");
			params.put("sign", "");
			params.put("verify_code", "");
			params.put("req_type", "");
			params.put("terminal_id", "");
			params.put("accept_seq", "");
			params.put("req_time", "");
			params.put("req_seq", "");
			params.put("request_type", "");
			params.put("sysfunc_id", "");
			params.put("operator_id", "");
			params.put("request_time", "");
			params.put("request_seq", "");
			params.put("request_source", "");
			params.put("request_target", "");
			params.put("msg_version", "");
			params.put("user_passwd", "");
			params.put("operator_ip", "");
			params.put("channelid", "");
			params.put("login_msisdn", "");
			params.put("channelid", "");
	        return params;
	}
    private static final String request_whole_tfj_uri="http://221.178.251.182:80/internet_surfing";
	public String  mobileChangeCardState(String msisdn, String operType,String ownerPlace) {
		try {


			String reson = "";
			if ("1".equals(operType)) {
				reson = "6";
			} else {
				reson = "7";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("telnum", msisdn);
			map.put("oprtype", operType);
			map.put("reason", reson);
			Map<String, Object> params =initNativeTFJ();
			if("1".equals(ownerPlace)){//淮安
				map.put("groupid", "51734332192");
				params.put("content",map);
				params.put("app_id","109000000207");
				params.put("access_token","TvqcYsb82w1tYdaZKYZ7");
				params.put("route_value","12");
				
				
			}else if("2".equals(ownerPlace)){//盐城
				map.put("groupid", "51533317364");
				params.put("content",map);
				params.put("app_id","109000000305");
				params.put("access_token","n3McSu8o0McpQwsM67Ez");
				params.put("route_value","22");
			}
			
			String xmlFileName = XmlMapUtils.createXmlByMap2(params, "GBK", "operation_in");
			String url = request_whole_tfj_uri;
			logger.info("停复机接口请求参数："+xmlFileName);
			String postXML = HttpClientUtil.postXML(url, xmlFileName);
			logger.info("停复机接口返回参数："+postXML);
			int indexbegin = postXML.indexOf("<ret_code>");
			int indexend = postXML.indexOf("</ret_code>");
			String statusCode = "";
			if(indexbegin>0 && indexend>0){
				statusCode = postXML.substring(indexbegin+10, indexend);
			}
	        logger.info("淮安返回状态码："+statusCode);
	        
	    	if ("0".equals(statusCode)) { //0:成功；1:失败  
	    		return SystemConstants.STATE_CG;
			} else if("1".equals(statusCode)){
				return SystemConstants.STATE_SB;
			}else{
				return SystemConstants.STATE_PF;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SystemConstants.STRING_NO;
	}
	
	/**
	 * 
	 * cardStatusService:(同卡用量). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *  卡状态：0 未激活 1 正常 2 停机 3 测试期 4 沉默期 5 其他
	 * @author zhangliang
	 * @param msisdn
	 * @since JDK 1.8
	 */
	public String mobileGPRSQueryService(String msisdn,String ownerPlace) {

		try {
			Map map = new HashMap();
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.wholeMobileCMPRequest("0001000000012", msisdn, "gprsusedinfosingle", ownerPlace);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			
			JSONArray jsonArray=(JSONArray) JSONArray.parseArray(resultMap.get("result")+"");
			
			JSONObject jsonObject=(JSONObject) jsonArray.get(0);
			
			if(null==jsonObject) {
				return "";
			}
			
			String totalGPRS= jsonObject.get("total_gprs").toString();
			
			String used=new BigDecimal(totalGPRS).divide(new BigDecimal(1024)).setScale(0,RoundingMode.HALF_UP).toPlainString();
		return used;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return "";
	}
	
	/**
	 * 
	 * messageSendService:(提供发送本地号码API。). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param msisdn
	 * @param content
	 * @return
	 * @since JDK 1.8
	 */
	
	public boolean mobileMessageSendService(String msisdn,String content,String ownerPlace) {

		
		boolean flag=false;

		try {

			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
	
	}
	
	/**
	 * 
	 * userStatusQuery:(卡工作状态). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *  00-正常；
 01-单向停机；
 02-停机；
 03-预销号；
 04-销号；
 05-过户；
 06-休眠；
 07-待激活；
 99-号码不存在

	 * @author zhangliang
	 * @param msisdn
	 * @return
	 * @since JDK 1.8
	 */
	
	private  static  Map<String, Object>  initNativeZTCX() {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("process_code", "cc_wlw_qrystatus")	;
		params.put("route_type","1");
		params.put("sign", "");
		params.put("verify_code", "");
		params.put("req_type", "");
		params.put("terminal_id", "");
		params.put("accept_seq", "");
		params.put("req_time", "");
		params.put("req_seq", "");
		params.put("request_type", "");
		params.put("sysfunc_id", "");
		params.put("operator_id", "");
		params.put("request_time", "");
		params.put("request_seq", "");
		params.put("request_source", "");
		params.put("request_target", "");
		params.put("msg_version", "");
		params.put("user_passwd", "");
		params.put("operator_ip", "");
		params.put("channelid", "");
		params.put("login_msisdn", "");
		params.put("channelid", "");
        return params;
	}
	
	public String mobileUserStatusQuery(String msisdn,String ownerPlace) {

		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("telnum", msisdn);
			Map<String, Object> params =initNativeZTCX();
			if("1".equals(ownerPlace)){//淮安
				map.put("groupid", "51734332192");
				params.put("content",map);
				params.put("app_id","109000000207");
				params.put("access_token","TvqcYsb82w1tYdaZKYZ7");
				params.put("route_value","12");
			}else if("2".equals(ownerPlace)){//盐城
				map.put("groupid", "51533317364");
				params.put("content",map);
				params.put("app_id","109000000305");
				params.put("access_token","n3McSu8o0McpQwsM67Ez");
				params.put("route_value","22");
			}
			
			String xmlFileName = XmlMapUtils.createXmlByMap2(params, "GBK", "operation_in");
			String url = request_whole_tfj_uri;
			logger.info("查询状态接口请求参数："+xmlFileName);
			String postXML = HttpClientUtil.postXML(url, xmlFileName);
			logger.info("查询状态接口返回参数："+postXML);
			
			Map<String, Object> createMapByXml = XmlMapUtils.createMapByXml(postXML);
			Map<String, Object> operation_out = (Map<String, Object>) createMapByXml.get("operation_out");
			
			Map<String, Object> content = (Map<String, Object>) operation_out.get("content");
			String ret_code = (String) content.get("ret_code");
			String status = (String) content.get("status");
			
			
	        logger.info("查询状态接口返回状态码："+ret_code);
	        logger.info("查询状态接口返回卡状态码："+status);
			
			
	        if ("0".equals(ret_code)) { //0:成功；1:失败  
	        	logger.info("*******success**ret_code*****："+ret_code);
	        	logger.info("*******success**status*****："+status);
	    		return status;
			}else{
				logger.info("*******error**ret_code*****："+ret_code);
	        	logger.info("*******error**status*****："+status);
				return SystemConstants.STRING_NO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SystemConstants.STRING_NO;
		}
	
	
	
	}
/********************************************************移动接口调用结束**********************************************/	
	
	

}

