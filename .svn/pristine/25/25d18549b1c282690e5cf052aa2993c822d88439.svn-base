package com.xuyurepos.service.intergration.cmp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.xuyurepos.common.util.XmlMapUtils;
import com.xuyurepos.common.util.httpclient.HttpTookit;
import com.xuyurepos.service.intergration.telecom.sign.TelecomSign;
import com.xuyurepos.service.intergration.telecom.util.TelecomDesEncrypt;

public class TelecomCMPRequest {

	public static final String request_url="http://api.ct10649.com:9001/m2m_ec/query.do";
	
	public static Map init_shizhang() {
		Map initMap=new HashMap();
    	//具体接口参数需参照自管理门户在线文档
    	String user_id = "shizhang";     //用户名
    	String password = "S727k17l9j7769I4";    //密码
        String key="vgZf2iVzP";
        
        initMap.put("userId",user_id);
        initMap.put("password", password);
        initMap.put("key", key);
        return initMap;
	}

	public static Map init_xiaolu() {
		Map initMap=new HashMap();
    	//具体接口参数需参照自管理门户在线文档
    	String user_id = "szxlkj";     //用户名
    	String password = "9O28IyQjjm7o0Eua";    //密码
        String key="ZmYjw0z0q";
        
        initMap.put("userId",user_id);
        initMap.put("password", password);
        initMap.put("key", key);
        return initMap;
	}
	
	/**
	 * 
	 * @param access_number 物联网卡号(149或10649号段)
	 * @param method 接口名
	 * @param pageIndex 当前页
	 * @return
	 * @throws IOException 
	 */
	public static String telecomCMPRequest(Map map) throws IOException {
		
		Map<String,String> initMap=init_shizhang();
		
		String user_id=initMap.get("userId");		
		String password=initMap.get("password");
		String key=initMap.get("key");
		
    	
		String passwordEnc = TelecomDesEncrypt.strEnc(password,key);  //密码加密 

		Map params=new HashMap();
		params.put("user_id", user_id);
		params.put("password", password);
		
		
		params.putAll(map);
		
		if(map.get("method").equals("queryTrafficByDate")) {
			params.remove("startDate");
			params.remove("endDate");
			params.remove("needDtl");
		}
		
		List<String> tempList=new ArrayList<String>(params.values());
		String[] arr =new String[tempList.size()];
		
		for (int i = 0; i < tempList.size(); i++) {
			arr[i]=tempList.get(i);
		}
		
		

		
		String sign = TelecomSign.sign(arr,"shizhang"); //生成sign加密值
		params.remove("password");
		params.put("passWord",passwordEnc);
		params.put("sign", sign);
		params.putAll(map);
		


	String result=HttpTookit.doGet(request_url, params,"UTF-8");
	System.out.println(result);
	
	return result;
	
	
	}
	
	/**
	 * 
	 * @param access_number 物联网卡号(149或10649号段)
	 * @param method 接口名
	 * @param pageIndex 当前页
	 * @return
	 * @throws IOException 
	 */
	public static String telecomCMPRequestXiaoLu(Map map) throws IOException {
		
		Map<String,String> initMap=init_xiaolu();
		
		String user_id=initMap.get("userId");		
		String password=initMap.get("password");
		String key=initMap.get("key");
		
    	
		String passwordEnc = TelecomDesEncrypt.strEnc(password,key);  //密码加密 

		Map params=new HashMap();
		params.put("user_id", user_id);
		params.put("password", password);
		
		
		params.putAll(map);
		
		if(map.get("method").equals("queryTrafficByDate")) {
			params.remove("startDate");
			params.remove("endDate");
			params.remove("needDtl");
		}
		
		List<String> tempList=new ArrayList<String>(params.values());
		String[] arr =new String[tempList.size()];
		
		for (int i = 0; i < tempList.size(); i++) {
			arr[i]=tempList.get(i);
		}
		
		

		
		String sign = TelecomSign.sign(arr,"xiaolu"); //生成sign加密值
		params.remove("password");
		params.put("passWord",passwordEnc);
		params.put("sign", sign);
		params.putAll(map);
		


	String result=HttpTookit.doGet(request_url, params,"UTF-8");
	System.out.println(result);
	
	return result;
	
	
	}
	
	
	public static void main(String[] args) throws IOException {
//		String sign = TelecomSign.sign(arr,"xiaolu"); //生成sign加密值
//		System.out.println(sign);
		
		SimpleDateFormat sdf=new SimpleDateFormat();
		
		Map map=new HashMap();
		map.put("method", "queryTrafficByDate");
//		map.put("acctCd", "");
//		map.put("orderTypeId", "20");
		map.put("access_number", "1410025378375");
		
		map.put("needDtl","0");
		sdf.applyPattern("yyyyMM");
		map.put("startDate", sdf.format(new Date())+"01");
		sdf.applyPattern("yyyyMMdd");

		map.put("endDate", sdf.format(new Date()));
		
		
     String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		
	String result=xmlHeader+	TelecomCMPRequest.telecomCMPRequestXiaoLu(map);
	
	Map mm=XmlMapUtils.createMapByXml(result);
	String TOTAL_BYTES_CNT=	((Map)(((Map)mm.get("root")).get("NEW_DATA_TICKET_QRsp"))).get("TOTAL_BYTES_CNT")+"";
    System.out.println(TOTAL_BYTES_CNT);

	
	
	}
	
}
