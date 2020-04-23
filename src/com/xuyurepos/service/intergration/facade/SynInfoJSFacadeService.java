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
import com.xuyurepos.common.util.JsonUtil;
import com.xuyurepos.common.util.XmlMapUtils;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.service.intergration.cmp.MobileCMPRequest;
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
	 * @since JDK 1.8
	 */
    
	public boolean  mobileChangeCardState(String msisdn, String operType,String ownerPlace) {
		try {
			if ("1".equals(ownerPlace)) {//淮安
				String reson = "";
				if ("1".equals(operType)) {
					reson = "6";
				} else {
					reson = "7";
				}
				String url = "http://221.178.251.182:80/internet_surfing";
				logger.info("淮安停复机接口路径："+url);
				String xmlFileName = "<?xml version=\"1.0\" encoding=\"GBK\" ?>"+
							"<operation_in>"+
							"<process_code>cc_wlw_controlsr</process_code>"+
							"<app_id>109000000207</app_id>"+
							"<access_token>TvqcYsb82w1tYdaZkYZ7</access_token>"+
							"<sign></sign>"+
							"<verify_code></verify_code>"+
							"<req_type></req_type>"+
							"<terminal_id></terminal_id>"+
							"<accept_seq></accept_seq>"+
							"<req_time></req_time>"+
							"<req_seq></req_seq>"+
							"<request_type/>"+
							"<sysfunc_id></sysfunc_id>"+
							"<operator_id/>"+
							"<organ_id></organ_id>"+
							"<route_type>1</route_type>"+
							"<route_value>12</route_value>"+
							"<request_time></request_time>"+
							"<request_seq></request_seq>"+
							"<request_source></request_source>"+
							"<request_target/>"+
							"<msg_version></msg_version>"+
							"<cont_version></cont_version>"+
							"<user_passwd></user_passwd>"+
							"<operator_ip></operator_ip>"+
							"<channelid></channelid>"+
							"<login_msisdn/>"+
							"<content>"+
							"<groupid>51734332192</groupid>"+ //groupid
							"<telnum>"+msisdn+"</telnum>"+  //接入号
							"<oprtype>"+operType+"</oprtype>"+ //1、 停机；2、 复机
							"<reason>"+reson+"</reason>"+ //操作原因
							"</content>"+
							"</operation_in>";
				
				String postXML = HttpClientUtil.postXML(url, xmlFileName);
				logger.info("淮安停复机接口返回参数："+postXML);
				int indexbegin = postXML.indexOf("<ret_code>");
				int indexend = postXML.indexOf("</ret_code>");
				String statusCode = "";
				if(indexbegin>0 && indexend>0){
					statusCode = postXML.substring(indexbegin+10, indexend);
				}
		        logger.info("淮安返回状态码："+statusCode);
		        
		    	if ("0".equals(statusCode)) { //0:成功；1:失败
		    		//接口修改成功之后修改数据库
			        /*if("1".equals(operType)){//停机
			        	xuyuContentCardInfoDao.updateCardState(msisdn, "02");
			        }
			        if("2".equals(operType)){//复机
			        	xuyuContentCardInfoDao.updateCardState(msisdn, "00");
			        }*/
		    		return true;
				} else {
					return false;
				}
			} else if("2".equals(ownerPlace)){//盐城
				String reson = "";
				if ("1".equals(operType)) {
					reson = "6";
				} else {
					reson = "7";
				}
				String url = "http://221.178.251.182:80/internet_surfing";
				logger.info("盐城停复机接口路径："+url);
				String xmlFileName = "<?xml version=\"1.0\" encoding=\"GBK\" ?>"+
							"<operation_in>"+
							"<process_code>cc_wlw_controlsr</process_code>"+
							"<app_id>109000000305</app_id>"+
							"<access_token>n3McSu8o0McpQwsM67Ez</access_token>"+
							"<sign></sign>"+
							"<verify_code></verify_code>"+
							"<req_type></req_type>"+
							"<terminal_id></terminal_id>"+
							"<accept_seq></accept_seq>"+
							"<req_time></req_time>"+
							"<req_seq></req_seq>"+
							"<request_type/>"+
							"<sysfunc_id></sysfunc_id>"+
							"<operator_id/>"+
							"<organ_id></organ_id>"+
							"<route_type>1</route_type>"+
							"<route_value>22</route_value>"+
							"<request_time></request_time>"+
							"<request_seq></request_seq>"+
							"<request_source></request_source>"+
							"<request_target/>"+
							"<msg_version></msg_version>"+
							"<cont_version></cont_version>"+
							"<user_passwd></user_passwd>"+
							"<operator_ip>47.101.207.177</operator_ip>"+
							"<channelid></channelid>"+
							"<login_msisdn/>"+
							"<content>"+
							"<groupid>51533317364</groupid>"+ //groupid
							"<telnum>"+msisdn+"</telnum>"+  //接入号
							"<oprtype>"+operType+"</oprtype>"+ //1、 停机；2、 复机
							"<reason>"+reson+"</reason>"+ //操作原因
							"</content>"+
							"</operation_in>";
				
				String postXML = HttpClientUtil.postXML(url, xmlFileName);
				logger.info("盐城停复机接口返回参数："+postXML);
				int indexbegin = postXML.indexOf("<ret_code>");
				int indexend = postXML.indexOf("</ret_code>");
				String statusCode = "";
				if(indexbegin>0 && indexend>0){
					statusCode = postXML.substring(indexbegin+10, indexend);
				}
		        logger.info("盐城返回状态码："+statusCode);
		        
		    	if ("0".equals(statusCode)) { //0:成功；1:失败
		    		//接口修改成功之后修改数据库
		    		String accessNum = msisdn;
			        /*if("1".equals(operType)){//停机
			        	String state = "02";
			        	xuyuContentCardInfoDao.updateCardState(accessNum, state);
			        }
			        if("2".equals(operType)){//复机
			        	String state = "00";
			        	xuyuContentCardInfoDao.updateCardState(accessNum, state);
			        }*/
		    		return true;
				} else {
					return false;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return false;
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
	public String mobileUserStatusQuery(String msisdn,String ownerPlace) {



		try {
			Map map = new HashMap();
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.wholeMobileCMPRequest("0001000000009", msisdn, "userstatusrealsingle", ownerPlace);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

//			String code = resultMap.get("code") + "";
//			System.out.println("代码返回："+code);
//			if (code.equals("00000")) {
//				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
//				String status = body.get("status") + "";
//				if(StringUtils.isNotBlank(status)&&!"null".equals(status)) {
//
//		          return status;
//				}else {
//					return "";
//				}
//			}
			
			String status = resultMap.get("status") + "";
			System.out.println("接口返回："+status);
			if (status.equals("0")) {
				Map result1 = JsonUtil.parseJson2Map((resultMap.get("result") + "").replace("[","").replace("]",""));
				String status1 = result1.get("STATUS") + "";
				return status1;
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	
	
	
	}
/********************************************************移动接口调用结束**********************************************/	
	
	

}

