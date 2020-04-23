package com.xuyurepos.service.intergration.telecom.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuyurepos.service.intergration.common.XuYuKey;




public class TelecomCore {
	

    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysign(String[] strArray,String protal) {
    	String desKey="";
    	if(protal.equals("shizhang")) {
    		desKey=XuYuKey.shizhang_Des_key;
    	}else if(protal.equals("xiaolu")) {
    		desKey=XuYuKey.xiaolu_Des_key;
    	}

        String mysign = TelecomDesEncrypt.strEnc(TelecomDesEncrypt.naturalOrdering(strArray), desKey);
        return mysign;
    }
    
    /**
     * 解码
     * @param uri
     * @param encode
     * @return
     */
    public static String decodeUri(String uri,String encode){
        
        String decodedUri = uri;
        try{
            decodedUri = URLDecoder.decode(uri,encode);
        }catch(UnsupportedEncodingException ex){
            decodedUri = uri;
        }
        return decodedUri;
    }
    
   



    
    

   
}
