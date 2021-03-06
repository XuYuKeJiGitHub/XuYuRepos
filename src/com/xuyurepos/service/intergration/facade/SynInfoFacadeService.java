/**
 * Project Name:XuYuRepos
 * File Name:SynInfoFacadeService.java
 * Package Name:com.xuyurepos.service.intergration.facade
 * Date:2019年3月29日下午4:21:31
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.xuyurepos.service.intergration.facade;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.xuyurepos.common.util.JsonUtil;
import com.xuyurepos.common.util.XmlMapUtils;
import com.xuyurepos.service.intergration.cmp.MobileCMPRequest;
import com.xuyurepos.service.intergration.cmp.TelecomCMPRequest;
import com.xuyurepos.service.intergration.cmp.UnicomCMPRequest;

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
public class SynInfoFacadeService {

	static Logger logger=LoggerFactory.getLogger(SynInfoFacadeService.class);
	
    private SynInfoFacadeService() {
        if (SynInfoFacadeInnerService.instance != null) {
            throw new IllegalStateException();
        }
    }
    
    private static class SynInfoFacadeInnerService {
    	private static SynInfoFacadeService instance = new SynInfoFacadeService();
    }

    public static SynInfoFacadeService getInstance() {
        return SynInfoFacadeInnerService.instance;
    }
	
	
	/*******************************************移动接口调用开始***********************************************/
	/**
	 * 
	 * downTimeByMobile:(移动物联网卡停复机). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author z
	 * @param msisdn
	 * @param operType  0-暂停 1-恢复

	 * @return
	 * @since JDK 1.8
	 */
	
	public boolean  mobileChangeCardState(String msisdn, String operType) {
		try {
			Map map = new HashMap();
			map.put("oper_type", operType);
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest("changeCardState", map);
			logger.info(result);

			String code = JsonUtil.parseJson2Map(result).get("code") + "";
			if (code.equals("00000")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
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
	public String mobileGPRSQueryService(String msisdn) {

		try {
			Map map = new HashMap();
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest("gPRSQueryService", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				String used = body.get("used") + "";
			String usedGPRS=	new BigDecimal(used).divide(new BigDecimal(1024)).setScale(0,RoundingMode.UP).toPlainString();
				return usedGPRS;
			}
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
	
	public boolean mobileMessageSendService(String msisdn,String content) {


		try {
			Map map = new HashMap();
			map.put("content",content);
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest( "messageSendService", map);
			logger.info(result);
			return true;
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
	public String mobileUserStatusQuery(String msisdn) {



		try {
			Map map = new HashMap();
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest( "userStatusQuery", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				String status = body.get("STATUS") + "";
				if(StringUtils.isNotBlank(status)&&!"null".equals(status)) {

		          return status;
				}else {
					return "";
				}
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	
	
	
	}
	
	
	
	
	/**
	 * 
	 * userStatusQuery:(卡工作状态). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *  GPRS 在线状态：
 00-离线
 01-在线

	 * @author zhangliang
	 * @param msisdn
	 * @return
	 * @since JDK 1.8
	 */
	public String mobileGPRSStatusQuery(String msisdn) {



		try {
			Map map = new HashMap();
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest( "onlineGPRSRealQuery", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				String status = body.get("GPRSSTATUS") + "";
				if(StringUtils.isNotBlank(status)&&!"null".equals(status)) {

		          return status;
				}else {
					return "";
				}
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	
	
	
	}
	
	
	
	
	
	/**
	 * 
	 * userStatusQuery:(卡工作状态). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *  GPRS 在线状态：
 00-离线
 01-在线

	 * @author zhangliang
	 * @param msisdn
	 * @return
	 * @since JDK 1.8
	 */
	public Map hSSUserStatusInfoService(String msisdn) {
		try {
			Map map = new HashMap();
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeMobileCMPRequest( "hSSUserStatusInfoService", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				return  body;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	
	
	}
	


	

/********************************************************移动接口调用结束**********************************************/	
	
	
	
/*******************************************************联通接口调用开始**************************************************************/
	
	  /**
	    * 
	    * cardWorkState:(中国联通卡工作状态管理 停复机). <br/>
	    * TODO(这里描述这个方法适用条件 – 可选).<br/>
	    * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	    * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	    * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	    *ACTIVATED ACTIVATION_READY DEACTIVATED INVENTORY RETIRED TEST_READY
	    * @author zhangliang
	    * @param iccid
	    * @return
	    * @since JDK 1.8
	    */
	      public boolean  unicomChangeCardState(String iccid,String oper) {
	   		   try {
	   			String requestURL="https://api.10646.cn/rws/api/v1/devices/"+iccid;
	   			Map map=new HashMap();
	   			
	   			if(oper.equals("1")) {
	   				map.put("status", "ACTIVATED");
	   			}else if(oper.equals("0")) {
	   				map.put("status", "DEACTIVATED");
	   			}
	   			
	   			String params=JsonUtil.parseMap2Json(map);
	   			   String result=   UnicomCMPRequest.unicomCMPRequestPut(requestURL,params);
	   				Map resultMap = JsonUtil.parseJson2Map(result);
	   				String id=resultMap.get("iccid")+"";
	   				if(StringUtils.isNotBlank(id)&&!"null".equals(id)) {
	   					return true;
	   					
	   				}else {
	   					return false;
	   				}
	   		} catch (Exception e) {
	   			e.printStackTrace();
	   			return false;
	   		}
	   	   
	      }   
	   
	
	      /**
	  	 * 
	  	 * ctdUsages:(联通同步卡用量). <br/>
	  	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	  	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	  	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	  	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	  	 *
	  	 * @author zhangliang
	  	 * @param iccid
	  	 * @return
	  	 * @since JDK 1.8
	  	 */
	     public String unicomGPRSQueryService(String iccid) {
	  	   try {
	  		String requestURL="https://api.10646.cn/rws/api/v1/devices/"+iccid+"/ctdUsages";
	  		   String result=   UnicomCMPRequest.unicomCMPRequestGet(requestURL);
	  			Map resultMap = JsonUtil.parseJson2Map(result);
	  			String ctdDataUsage=resultMap.get("ctdDataUsage")+"";
	  			if(StringUtils.isNotBlank(ctdDataUsage)&&!"null".equals(ctdDataUsage)) {
	  				return new BigDecimal(ctdDataUsage).divide(new BigDecimal(1024)).divide(new BigDecimal(1024)).setScale(0,RoundingMode.UP).toPlainString();
	  			}else {
	  				return "";
	  			}
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  		return "";
	  	}
	     }
	
	
	
	
	
	/**
	 * 
	 * smsMessages:(联通发送短信接口). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param iccid
	 * @param messageContext
	 * @return
	 * @since JDK 1.8
	 */

	public boolean unicomMessageSendService(String iccid,String messageContext) {
		
		try {
			String requestURL=" https://api.10646.cn/rws/api/v1/devices/"+iccid+"/smsMessages";
			Map params=new HashMap();

			params.put("messageText", messageContext);
			
			String jsonMessageContext=JsonUtil.parseMap2Json(params);
			
			String  result=	UnicomCMPRequest.unicomCMPRequestPost(requestURL, jsonMessageContext);
   
			Map resultMap = JsonUtil.parseJson2Map(result);

			String smsMessageId = resultMap.get("smsMessageId") + "";
			if(StringUtils.isNotBlank(smsMessageId)&&!"null".equals(smsMessageId)) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
/**
 * 
 * cardWorkState:(中国联通卡工作状态查询). <br/>
 * TODO(这里描述这个方法适用条件 – 可选).<br/>
 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
 *ACTIVATED ACTIVATION_READY DEACTIVATED INVENTORY RETIRED TEST_READY
 * @author zhangliang
 * @param iccid
 * @return
 * @since JDK 1.8
 */
   public String unicomUserStatusQuery(String iccid) {
		   try {
			String requestURL="https://api.10646.cn/rws/api/v1/devices/"+iccid;
			   String result=   UnicomCMPRequest.unicomCMPRequestGet(requestURL);
				Map resultMap = JsonUtil.parseJson2Map(result);
				String status=resultMap.get("status")+"";
				if(StringUtils.isNotBlank(status)&&!"null".equals(status)) {
					return status;
					
				}else {
					return "";
				}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	   
   }
   
 
   
/*******************************************************联通接口调用结束**************************************************************/

/*******************************************************电信接口调用开始**************************************************************/
   
	/**
	 * 
	 * downTimeByMobile:(电信物联网卡停复机). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param msisdn
	 * @param operType  0-暂停 1-恢复

	 * @return
	 * @since JDK 1.8
	 */
	
	public boolean  telecomChangeCardState(String msisdn, String operType,String ownerPlace) {
		try {

//			19表示停机保号，20表示停机保号后复机,21表示测试期去激活，22表示测试期去激活后回到测试激活
			
			Map map=new HashMap();
			map.put("method", "disabledNumber");
			map.put("acctCd", "");
			
			if(operType.equals("0")) {
				map.put("orderTypeId", "19");
			}else if(operType.equals("1")) {
				map.put("orderTypeId", "20");
			}
			
			map.put("access_number", msisdn);
			
			
			String resultXML="";
			if("5".equals(ownerPlace)){
				resultXML=TelecomCMPRequest.telecomCMPRequestXiaoLu(map);
			}else{
				resultXML=TelecomCMPRequest.telecomCMPRequest(map);
			}
			
		    String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

			Map  resultMap  = XmlMapUtils.createMapByXml(xmlHeader+resultXML);
			//{businessServiceResponse={result=0, GROUP_TRANSACTIONID=1000000190201904016726465988, RspType=0, resultMsg=处理成功！}}
			String RspType=  ((Map)resultMap.get("businessServiceResponse")).get("RspType")+"";
		if(StringUtils.isNotBlank(RspType)&&RspType.equals("0")) {
			return true;
		}else {
			return false;
		}
		
		} catch (Exception e) {
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
	public String telecomGPRSQueryService(String msisdn,String ownerPlace) {

		try {
			
			SimpleDateFormat sdf=new SimpleDateFormat();
			
			Map map=new HashMap();
			map.put("method", "queryTrafficByDate");
//			map.put("acctCd", "");
//			map.put("orderTypeId", "20");
			map.put("access_number", msisdn);
			
			map.put("needDtl","0");
			sdf.applyPattern("yyyyMM");
			map.put("startDate", sdf.format(new Date())+"01");
			sdf.applyPattern("yyyyMMdd");

			map.put("endDate", sdf.format(new Date()));
			
			
	     String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	     String result="";
		if("5".equals(ownerPlace)){
			result=xmlHeader+TelecomCMPRequest.telecomCMPRequestXiaoLu(map);
		}else{
			result=xmlHeader+TelecomCMPRequest.telecomCMPRequest(map);
		}
		
		
		Map mm=XmlMapUtils.createMapByXml(result);
		String TOTAL_BYTES_CNT=	((Map)(((Map)mm.get("root")).get("NEW_DATA_TICKET_QRsp"))).get("TOTAL_BYTES_CNT")+"";
	    if(StringUtils.isNotBlank(TOTAL_BYTES_CNT)&&!"null".equals(TOTAL_BYTES_CNT)) {
	    	return TOTAL_BYTES_CNT;
	    }else {
	    	return "";
	    }

		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return "";
	}
//	
//	/**
//	 * 
//	 * messageSendService:(提供发送本地号码API。). <br/>
//	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
//	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
//	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
//	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
//	 *
//	 * @author zhangliang
//	 * @param msisdn
//	 * @param content
//	 * @return
//	 * @since JDK 1.8
//	 */
//	
//	public boolean telecomMessageSendService(String msisdn,String content) {
//
//
//		try {
//			Map map = new HashMap();
//			map.put("content",content);
//			map.put("msisdn",msisdn);
//			String result = MobileCMPRequest.nativeMobileCMPRequest( "messageSendService", map);
//			logger.info(result);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	
//	
//	}
//	
	/**
	 * 
	 * userStatusQuery:(卡工作状态). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
卡主状态编码。 取值范围： 1：可激活 2：测试激活 3：测试去激活 4：在用 5：停机 6：运营商管理状态 7：拆机

	 * @author zhangliang
	 * @param msisdn
	 * @return
	 * @since JDK 1.8
	 */
	public String telecomUserStatusQuery(String msisdn,String ownerPlace) {



		try {
//			卡主状态编码。 取值范围： 1：可激活 2：测试激活 3：测试去激活 4：在用 5：停机 6：运营商管理状态 7：拆机
			
			Map map=new HashMap();
			map.put("method", "queryCardMainStatus");
			map.put("access_number",msisdn);
			
			
		String result=	"";
	    if("5".equals(ownerPlace)){
	    	result=TelecomCMPRequest.telecomCMPRequestXiaoLu(map);
	    }else{
	    	result=TelecomCMPRequest.telecomCMPRequest(map);
	    }
		
		
	    Map resultMap=	JsonUtil.parseJson2Map(result);
		
	    JSONArray  jsonArray=JSONArray.parseArray(resultMap.get("productInfo")+"");
	    
	    Map m=   JsonUtil.parseJson2Map(jsonArray.get(0)+"");
	    
	    

	    return m.get("productMainStatusCd")+"";
	    	

		
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	
	
	
	}
   
	
	public static void main(String[] args) throws IOException {
		Map map = new HashMap();
		map.put("oper_type", "0");
		map.put("msisdn", "1440082622432");
		String result = MobileCMPRequest.nativeMobileCMPRequest("changeCardState", map);
		logger.info(result);
	}


	public boolean mobileMessageSendServiceFengXian(String content, String msisdn, String ownerPlace) {
		try {
			Map map = new HashMap();
			map.put("content",content);
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeFengXianMobileCMPRequest( "messageSendService", map);
			logger.info(result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public String mobileUserStatusQueryFengXian(String msisdn, String ownerPlace) {
		try {
			Map map = new HashMap();
			map.put("msisdn",msisdn);
			String result = MobileCMPRequest.nativeFengXianMobileCMPRequest( "userStatusQuery", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				String status = body.get("STATUS") + "";
				if(StringUtils.isNotBlank(status)&&!"null".equals(status)) {

		          return status;
				}else {
					return "";
				}
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	public String mobileGPRSQueryServiceFengXian(String msisdn, String ownerPlace) {
		try {
			Map map = new HashMap();
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.nativeFengXianMobileCMPRequest("gPRSQueryService", map);
			logger.info(result);
			Map resultMap = JsonUtil.parseJson2Map(result);

			String code = resultMap.get("code") + "";
			if (code.equals("00000")) {
				Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
				String used = body.get("used") + "";
			String usedGPRS=	new BigDecimal(used).divide(new BigDecimal(1024)).setScale(0,RoundingMode.UP).toPlainString();
				return usedGPRS;
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			
		}
		return "";
	}


	public boolean mobileChangeCardStateFengXian(String msisdn, String operType, String ownerPlace) {
		try {
			Map map = new HashMap();
			map.put("oper_type", operType);
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.nativeFengXianMobileCMPRequest("changeCardState", map);
			logger.info(result);
			String code = JsonUtil.parseJson2Map(result).get("code") + "";
			if (code.equals("00000")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

}

