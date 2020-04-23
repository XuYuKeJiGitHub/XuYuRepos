package com.xuyurepos.service.intergration.mobile.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.util.httpclient.HttpUtils;
import com.xuyurepos.service.intergration.mobile.common.ApiSignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: create by panlinkai
 * @version: v1.0
 * @date:2018/11/22
 */
public class ApiTest {
    public static void main(String args[]){
        ApiTest test = new ApiTest();
        try{
//            test.sendAPI("18770817086","jaker");
            test.sendOnlineGPRSRealQuery("1440082636750");
        }catch(Exception e){

        }

    }
    
    
    /**
     * 4.1.1	SH_CMP_API_000011 在线信息实时查询
     * @param telphone_num
     * @param send_content
     * @throws Exception
     */
    private Map sendOnlineGPRSRealQuery(String telphone_num) throws Exception {
        // api服务调用测试
        String url = "http://localhost:8001/api/V1/onlineGPRSRealQuery";
        String ctype = "application/json;charset=UTF-8";
        String appid = "2017053015123929905";// 应用编码  20161116161121401478
        String ability = "onlineGPRSRealQuery";// 能力编码
        String timestamp = "20181122121111";// 请求时间戳 yyyyMMddHHmmss格式
        String transationid = appid+timestamp+"12345678";// API能力接口请求流水
        // 20位APPID+YYYYMMDDHHMISS+8
        // 位数字序列
        String randomstr = "12345678901234567890123456789012";// 32位随机字符串（字母或数字）
        String sign = "";// 数据签名

        Map<String, Object> busi_params = new HashMap<String, Object>();
        busi_params.put("msisdn", telphone_num);
        JSONObject busi_json = new JSONObject();
        busi_json.putAll(busi_params);
        String body = busi_json.toString();// 业务请求数据，json格式

        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("ability", ability);
        params.put("transationid", transationid);
        params.put("timestamp", timestamp);
        params.put("randomstr", randomstr);
        params.put("sign", sign);
        params.put("body", body);

        String api_key = "204F6F767DC3C423A909FBD3243999100F656EDD800F70667ED4D344AA05DCD3"; // api秘钥
        // 签名数据
        String sourceData = ApiSignUtil.getApiSignContent(params, api_key);
        sign = ApiSignUtil.api_sign(sourceData, "utf-8");
        params.put("sign", sign);

        JSONObject params_json = new JSONObject();
        params_json.putAll(params);

        byte[] content = {};
        content = params_json.toString().getBytes("UTF-8");
        System.out.println("===================API 调用开始==========================");
        System.out.println(params_json);
        String respose = HttpUtils.doPost(url, ctype, content, 60000, 60000);
        //String respose = "{\"res_code\":\"00000\",\"res_message\":\"sucess\"}";
        System.out.println(respose);
        System.out.println("===================API 调用结束==========================");

        Map map=(Map) JSON.parse(respose);
        return map;
    }
    
    
    
    
    /**
     * GPRS暂停恢复
     * @param telphone_num
     * @param send_content
     * @throws Exception
     */
    private Map sendAPI(String telphone_num, String send_content) throws Exception {
        // api服务调用测试
        String url = "http://211.136.110.98:8082/api/V1/messageSendService";
        String ctype = "application/json;charset=UTF-8";
        String appid = "20190125151718145922";// 应用编码  20161116161121401478
        String ability = "messageSendService";// 能力编码
        String timestamp = "20181122121111";// 请求时间戳 yyyyMMddHHmmss格式
        String transationid = appid+timestamp+"12345678";// API能力接口请求流水
        // 20位APPID+YYYYMMDDHHMISS+8
        // 位数字序列
        String randomstr = "12345678901234567890123456789012";// 32位随机字符串（字母或数字）
        String sign = "";// 数据签名

        Map<String, Object> busi_params = new HashMap<String, Object>();
        busi_params.put("msisdn", telphone_num);
        busi_params.put("content", send_content);
        JSONObject busi_json = new JSONObject();
        busi_json.putAll(busi_params);
        String body = busi_json.toString();// 业务请求数据，json格式

        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("ability", ability);
        params.put("transationid", transationid);
        params.put("timestamp", timestamp);
        params.put("randomstr", randomstr);
        params.put("sign", sign);
        params.put("body", body);

        String api_key = "A3AC76A48B98B87D13091D221ECD12DAC9BCE08B757B9461DFD6D35C90B9459C"; // api秘钥
        // 签名数据
        String sourceData = ApiSignUtil.getApiSignContent(params, api_key);
        sign = ApiSignUtil.api_sign(sourceData, "utf-8");
        params.put("sign", sign);

        JSONObject params_json = new JSONObject();
        params_json.putAll(params);

        byte[] content = {};
        content = params_json.toString().getBytes("UTF-8");
        System.out.println("===================API 调用开始==========================");
        System.out.println(params_json);
        String respose = HttpUtils.doPost(url, ctype, content, 60000, 60000);
        //String respose = "{\"res_code\":\"00000\",\"res_message\":\"sucess\"}";
        System.out.println(respose);
        System.out.println("===================API 调用结束==========================");

        Map map=(Map) JSON.parse(respose);
        return map;
    }
}
