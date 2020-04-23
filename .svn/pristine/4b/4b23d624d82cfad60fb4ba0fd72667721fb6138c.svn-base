package com.xuyurepos.common.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
	   /**
     * 将请求信息转换为JSON格式
  * @param params 
  * @author lvyiguang
  * @return
  */
  public static String parseMap2Json(Map<String, String> params) {
	   
	return JSONObject.toJSONString(params,SerializerFeature.WriteMapNullValue);
 }
  /**
   * 将返回信息JSON转换为Map格式
* @param params
* @author lvyiguang
* @return
*/
  public static Map parseJson2Map(String str) {
  	JSONObject json=JSON.parseObject(str);
  	Map map=new HashMap();
  	for(String key:json.keySet()) {
  		map.put(key, json.get(key));
  	}
  	return map;
  }
}
