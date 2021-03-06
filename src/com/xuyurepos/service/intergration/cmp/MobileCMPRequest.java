package com.xuyurepos.service.intergration.cmp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.util.JsonUtil;
import com.xuyurepos.common.util.SecretUtils;
import com.xuyurepos.common.util.httpclient.HttpTookit;
import com.xuyurepos.service.intergration.mobile.sign.CMPSign;

public class MobileCMPRequest {

	private static final String request_native_test_uri="http://localhost:8001/api/V1/";
	
	private static final String request_native_pro_uri="http://211.136.110.98:8082/api/V1/";

	
	private static final String request_whole_test_uri="http://localhost:8002/v2/";
	
	private static final String request_whole_pro_uri="http://api.iot.10086.cn/v2/";
	
	private static final String request_whole_tfj_uri="http://221.178.251.182:80/internet_surfing";

	
	private  static  Map  initNative() {
		Map map=new HashMap();
		map.put("appid", "2017053015123929905")	;
		map.put("timestamp",timestamp(new Date(),"yyyyMMddHHmmss"));
		map.put("randomstr",getStringRandom(32));
        return map;
	}
	
	private  static  Map  initNativeFengXian() {
		Map map=new HashMap();
		map.put("appid", "20191107095309173983")	;
		map.put("timestamp",timestamp(new Date(),"yyyyMMddHHmmss"));
		map.put("randomstr",getStringRandom(32));
        return map;
		
	}
	
	private  static  Map  initWholeByHuaiAn() {
		Map map=new HashMap();
		map.put("appid", "GHQUSMDFP1744U")	;
	    map.put("password","XYXX125js");
        return map;
		
	}
	
	
	private  static  Map  initWholeByYanCheng() {
		Map map=new HashMap();
		map.put("appid", "GHS75WIDMEUK7P")	;
	    map.put("password","R1Xt27xJs");
        return map;
		
	}
	
	
	
	private  static String timestamp(Date date,String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);
	 return 	sdf.format(date);
	}
	
	 //生成随机数字和字母,  
    private  static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
//                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + 97);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
	
    /**
     *  移动本地上海
     * @param msisdn 所查询的专网号码
     * @param ability 能力编码，在api应用管理中开通，ability范例：onlineGPRSRealQuery
     * @return
     * @throws IOException 
     */
    public static String nativeMobileCMPRequest(String ability,Map bodyParams) throws IOException {

		Map<String,String> initMap=initNative();
		
	    String 	transationid=initMap.get("appid")+timestamp(new Date(),"yyyyMMddHHmmssSSSSSSSS");
	 
	    initMap.put("ability", ability);
	    initMap.put("transationid", transationid);
	   
		Map body = new HashMap();
		body.putAll(bodyParams);
		initMap.put("body",JSON.toJSONString(body));
		String sign = CMPSign.sign(initMap);
		initMap.put("sign",sign);


		String result=	HttpTookit.doPost(request_native_pro_uri+ability, JsonUtil.parseMap2Json(initMap),"UTF-8");
	//	System.out.println(result);
	//	String result=CMPSubmit.buildRequestPost("","",map, "http://183.230.96.66:8087/v2/gprsrealsingle");
	//	System.out.println(result);
	return result;
	
    }
    
    /**
     *  移动本地上海
     * @param msisdn 所查询的专网号码
     * @param ability 能力编码，在api应用管理中开通，ability范例：onlineGPRSRealQuery
     * @return
     * @throws IOException 
     */
    public static String nativeFengXianMobileCMPRequest(String ability,Map bodyParams) throws IOException {

    	Map<String,String> initMap=initNativeFengXian();
		
	    String transationid	=initMap.get("appid")+timestamp(new Date(),"yyyyMMddHHmmssSSSSSSSS");
	 
	    initMap.put("ability", ability);
	    initMap.put("transationid", transationid);
	   
		Map body=new HashMap();
		body.putAll(bodyParams);
		initMap.put("body",JSON.toJSONString(body));
		String sign = CMPSign.signFengXian(initMap);
		initMap.put("sign",sign);
	
	
		String result=	HttpTookit.doPost(request_native_pro_uri+ability, JsonUtil.parseMap2Json(initMap),"UTF-8");
   return result;
	
    }
    
    /**
     *  移动全国
     * @param ebid
     * @param msisdn 物联卡好
     * @param ability 能力编码 countryType 1淮安 2盐城
     * @return
     * @throws IOException 
     */
   public static String wholeMobileCMPRequest(String ebid,String msisdn,String ability,String ownerPlace) throws IOException {
	   
    Map<String,String> map=null;
    
    if(ownerPlace.equals("1")) {
    	map=initWholeByHuaiAn();
    }else if(ownerPlace.equals("2")) {
    	map=initWholeByYanCheng();

    }
    
    String appid=map.get("appid");
	String transid=  timestamp(new Date(),"yyyyMMddHHmmssSSSSSSSS");
	  map.put("ebid", ebid);
      map.put("msisdn", msisdn);	 
      map.put("transid",transid);
     String password=map.get("password");
     map.remove("password");
     String token= SecretUtils.getSHA256StrJava(  appid+password+transid);
     map.put("token",token);



	String result=	HttpTookit.doGet(request_whole_tfj_uri+ability,map,"UTF-8");
	System.out.println(result);
   return result;
   }
    
	
	public static void main(String[] args) throws IOException {
		String msisdn="1064878208745";
		try {

			Map map = new HashMap();
			map.put("msisdn", msisdn);
			String result = MobileCMPRequest.wholeMobileCMPRequest("0001000000012", msisdn, "gprsusedinfosingle", "2");
			Map resultMap = JsonUtil.parseJson2Map(result);

			
			JSONArray jsonArray=(JSONArray) JSONArray.parseArray(resultMap.get("result")+"");
			
			JSONObject jsonObject=(JSONObject) jsonArray.get(0);
			
			String totalGPRS= jsonObject.get("total_gprs").toString();
			
			String used=new BigDecimal(totalGPRS).divide(new BigDecimal(1024)).setScale(0,RoundingMode.HALF_UP).toPlainString();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		

//		Map map = new HashMap();
//		map.put("msisdn", msisdn);
//		String result = MobileCMPRequest.nativeFengXianMobileCMPRequest("gPRSQueryService", map);
//		System.out.println(result);
//		Map resultMap = JsonUtil.parseJson2Map(result);
//
//		String code = resultMap.get("code") + "";
//		if (code.equals("00000")) {
//			Map body = JsonUtil.parseJson2Map(resultMap.get("body") + "");
//			String used = body.get("used") + "";
//		String usedGPRS=	new BigDecimal(used).divide(new BigDecimal(1024)).setScale(0,RoundingMode.UP).toPlainString();
//			System.out.println(usedGPRS); ;
//		}
	
	}
}
