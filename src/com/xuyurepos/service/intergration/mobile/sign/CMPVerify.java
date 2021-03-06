package com.xuyurepos.service.intergration.mobile.sign;

import java.util.Map;

import com.xuyurepos.service.intergration.mobile.util.CMPCore;



public class CMPVerify {

    
    /**
     * 验证消息
     * @param params 通知返回来的参数数组
     * @param logger 
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params) {
        
        String inputCharset =  "UTF-8";
        String mysign = getMysign(params,inputCharset);
        String responseTxt = "true";
        
        String sign = "";
        if(params.get("sign") != null) {sign = params.get("sign");}
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param logger 
     * @return 生成的签名结果
     */
    public static String getMysign(Map<String, String> Params,String encode) {
        Map<String, String> sParaNew = CMPCore.paraFilter(Params);//过滤空值、sign与sign_type参数
        String mysign = CMPCore.buildMysign(sParaNew,encode);//获得签名结果
        return mysign;
    }

    


		
	
	
			
			
}
