package com.xuyurepos.service.intergration.unicom;



import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 基于 httpclient 4.3.1版本的 http工具类
 * @author mcSui
 *
 */
public class UnicomHttpTookit {

    private static CloseableHttpClient        httpClient=null;
    public static final String             CHARSET               = "UTF-8";
//    private static final RequestConfig requestConfig;


    /** 
     * 最大连接数 
     */
    public final static int                MAX_TOTAL_CONNECTIONS = 800;
    /** 
     * 获取连接的最大等待时间 
     */
    public final static int                WAIT_TIMEOUT          = 60000;
    /** 
     * 每个路由最大连接数 
     */
    public final static int                MAX_ROUTE_CONNECTIONS = 400;
    /** 
     * 连接超时时间 
     */
    public final static int                CONNECT_TIMEOUT       = 10000;
    /** 
     * 读取超时时间 
     */
    public final static int                READ_TIMEOUT          = 10000;
    static {
     try {        

        SSLContext sc=SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[] {
      		  // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
       new X509TrustManager() {
   

					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
							throws java.security.cert.CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
							throws java.security.cert.CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return null;
					}
                }
        }, new SecureRandom());    
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);    

            
            // set Timeout
            RequestConfig        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(WAIT_TIMEOUT)
                   .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(READ_TIMEOUT).build();
            
            httpClient= HttpClients.custom().setSSLSocketFactory(socketFactory).setDefaultRequestConfig(requestConfig).build();
        } catch (KeyManagementException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } 

        



    }


    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String doGet(String url,Map<String,String>  headers,  String charset) throws ClientProtocolException, IOException{
        if(StringUtils.isBlank(url)){
            return null;
        }

        	HttpGet httpGet = new HttpGet(url);
        	
        	for (String key : headers.keySet()) {
				
        		httpGet.addHeader(key,headers.get(key));
        		
			}
        	
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != 200) {
//                httpGet.abort();
//                throw new RuntimeException("HttpClient,error status code :" + statusCode);
//            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
    }
     
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String doPost(String url,Object objParam,Map<String,String> headers,String charset) throws ClientProtocolException, IOException{
    	
    
    	
        if(StringUtils.isBlank(url)){
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        for (String  key : headers.keySet()) {
        	httpPost.addHeader(key,headers.get(key));
		}
        
    	if(objParam instanceof Map) {
    		
    		Map<String,String> map =(Map)objParam;
//            List<NameValuePair> pairs = null;
//            if(params != null && !params.isEmpty()){
//                pairs = new ArrayList<NameValuePair>(params.size());
//                for(Map.Entry<String,String> entry : params.entrySet()){
//                    String value = entry.getValue();
//                    if(value != null){
//                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
//                    }
//                }
//            }
//            if(pairs != null && pairs.size() > 0){
//                httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
//            }
    		String params=		createLinkString(map);
        	if(StringUtils.isNoneBlank(params)) {
                httpPost.setEntity((new StringEntity(params, CHARSET)));
        	}
        }else if(objParam instanceof String) {
        	String params =(String)objParam;
        	if(StringUtils.isNoneBlank(params)) {
                httpPost.setEntity((new StringEntity(params, CHARSET)));
        	}
        }
    	
    	
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != 200) {
//                httpPost.abort();
//                throw new RuntimeException("HttpClient,error status code :" + statusCode);
//            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
    }
    
    /**  
     * 发送 http put 请求，参数以原生字符串进行提交  
     * @param url  
     * @param encode  
     * @return  
     * @throws IOException 
     * @throws ClientProtocolException 
     */    
    public static String  doPut(String url,Object objParam,Map<String,String> headers,String charset) throws ClientProtocolException, IOException{
    	
    
    	
        if(StringUtils.isBlank(url)){
            return null;
        }
        HttpPut httpPost = new HttpPut(url);
        for (String  key : headers.keySet()) {
        	httpPost.addHeader(key,headers.get(key));
		}
        
    	if(objParam instanceof Map) {
    		
    		Map<String,String> map =(Map)objParam;

    		String params=		createLinkString(map);
        	if(StringUtils.isNoneBlank(params)) {
                httpPost.setEntity((new StringEntity(params, CHARSET)));
        	}
        }else if(objParam instanceof String) {
        	String params =(String)objParam;
        	if(StringUtils.isNoneBlank(params)) {
                httpPost.setEntity((new StringEntity(params, CHARSET)));
        	}
        }
    	
    	
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != 200) {
//                httpPost.abort();
//                throw new RuntimeException("HttpClient,error status code :" + statusCode);
//            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
    }    
    
    
    
    
    
    
    
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

    public static void main(String[] args) {

//        String getData = doGet("https://www.icloud.com/", null);
//        System.out.println(getData);
//        System.out
//            .println("--------------------------------------------------分割线------------------------------------------------------");
//        String postData = doPost("https://www.icloud.com/", null);
//        System.out.println(postData);
//        doPost("http://localhost:1112/httpClientServer/",new HashMap(),"E:/234/20150311170527.xls","123" ,"UTF-8");
    }

}