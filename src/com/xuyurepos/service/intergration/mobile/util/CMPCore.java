package com.xuyurepos.service.intergration.mobile.util;

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




public class CMPCore {
	

    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysign(Map<String, String> sArray,String encode) {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        
        //解码uri
        String realUri = decodeUri(prestr,encode);
        
        String mysign = CMPMd5Encrypt.sign( realUri,XuYuKey.MD5_key,encode);
        return mysign;
    }
    
    /**
     * 生成奉贤的数据
     * @param sArray
     * @param encode
     * @return
     */
    public static String buildMysignFengXian(Map<String, String> sArray,String encode) {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        
        //解码uri
        String realUri = decodeUri(prestr,encode);
        
        String mysign = CMPMd5Encrypt.sign( realUri,XuYuKey.MD5_key_fengXian,encode);
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
    
   

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    
    

   
}
