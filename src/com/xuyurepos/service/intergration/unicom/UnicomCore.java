package com.xuyurepos.service.intergration.unicom;

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
import com.xuyurepos.service.intergration.telecom.util.TelecomDesEncrypt;




public class UnicomCore {
	

    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysign() {

        String mysign = UnicomDesEncrypt.authBasic();
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
