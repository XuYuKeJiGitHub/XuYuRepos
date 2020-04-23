package com.xuyurepos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuyurepos.service.intergration.facade.SynInfoJSFacadeService;

/* 
 * 利用HttpClient进行post请求的工具类 
 */  
//调用短信发送接口的工具类
public class HttpClientUtil {  
	static Logger logger=LoggerFactory.getLogger(SynInfoJSFacadeService.class);
	//static 代表第一次调用的时候创建，不会再重复创建
  	static CloseableHttpClient client = null;
  	static {
  		//1、创建httpClient实例
  		client = HttpClients.createDefault();
  	}
  	
  	//get请求方式
  	public static String get(String url,HashMap<String, Object> params){
  		try {
  			//2、创建连接方式（get）实例
  			HttpGet httpGet = new HttpGet();
  			
  			//3、拼接（get）请求路径
  			Set<String> keySet = params.keySet();
  			StringBuffer stringBuffer = new StringBuffer();
  			stringBuffer.append(url).append("?t=").append(System.currentTimeMillis());
  			for (String key : keySet) {
  				stringBuffer.append("&").append(key).append("=").append(params.get(key));
  			}
  			
  			httpGet.setURI(new URI(stringBuffer.toString()));
  			//4、执行execute方法，发送请求
  			CloseableHttpResponse execute = client.execute(httpGet);
  				//获取返回的对象
  			HttpEntity httpEntity = execute.getEntity();
  			//5、获取请求的状态码  --200:登录成功
  			int statusCode = execute.getStatusLine().getStatusCode();
  			//判断请求是否成功
//  				如果不成功--返回 “”值
  			if (200 != statusCode) {
  				return "";
  			}
//  				如果成功--返回服务端传递回来的字符串
  			return EntityUtils.toString(execute.getEntity(), "utf-8");
  		}catch (Exception e) {
  			//6、关闭连接
  			try {
  				client.close();
  			} catch (IOException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			}
  			e.printStackTrace();
  			return null;
  		}
  	}
  	
  	//post请求方式
  	public static String post(String url,HashMap<String, Object> params) {
  		try {
  			HttpPost httpPost = new HttpPost();
  			httpPost.setURI(new URI(url));
  			
  			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
  			Set<String> keySet = params.keySet();
  			for (String key : keySet) {
  				NameValuePair e = new BasicNameValuePair(key, params.get(key).toString());
  				parameters.add(e);
  			}
  			
  			HttpEntity entity = new UrlEncodedFormEntity(parameters , "utf-8");
  			httpPost.setEntity(entity);
  			
  			CloseableHttpResponse execute = client.execute(httpPost);
  			
  			//获取返回的对象
  			HttpEntity httpEntity = execute.getEntity();
  			
  			int statusCode = execute.getStatusLine().getStatusCode();
  			if (200 != statusCode) {
  				return "";
  			}
  			return EntityUtils.toString(execute.getEntity(), "utf-8");
  		}catch (Exception e) {
  			//6、关闭连接
  			try {
  				client.close();
  			} catch (IOException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			}			
  			e.printStackTrace();
  			return null;
  		}
  	}
  	
  	//post2请求--删除，批删，新增，修改时使用
  	public static String post2(String url,String params) {
  		try {
  			HttpPost httpPost = new HttpPost();
  			httpPost.setURI(new URI(url));
  			httpPost.setHeader("Content-Type", "application/json");
  			
  			StringEntity stringEntity = new StringEntity(params,"utf-8");
  			httpPost.setEntity(stringEntity);
  			
  			CloseableHttpResponse execute = client.execute(httpPost);
  			
  			int statusCode = execute.getStatusLine().getStatusCode();
  			if (200 != statusCode) {
  				return "";
  			}
  			return EntityUtils.toString(execute.getEntity(), "utf-8");
  		}catch (Exception e) {
  			//6、关闭连接
  			try {
  				client.close();
  			} catch (IOException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			}			
  			e.printStackTrace();
  			return null;
  		}
  	}
  	//httpclient 发送xml参数格式的post请求
  	public static String postXML(String url,String xmlFileName){
  	//创建httpclient工具对象   
        HttpClient client = new HttpClient();    
        //创建post请求方法   
        PostMethod myPost = new PostMethod(url);    
        //设置请求超时时间   
        client.setConnectionTimeout(3000*1000);  
        String responseString = null;    
        try{    
            //设置请求头部类型   
            /*myPost.setRequestHeader("Content-Type","text/xml");  
            myPost.setRequestHeader("charset","GBK");  */
            //设置请求体，即xml文本内容，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式   
            myPost.setRequestBody(xmlFileName);   
            System.out.println(myPost.toString()+"0000000000000000000000000000000000000000000000000000000000000000000000000000000");
            logger.info("停复机接口请求头："+myPost.toString());
            int statusCode = client.executeMethod(myPost);  
            logger.info("请求状态码："+statusCode);
            //只有请求成功200了，才做处理
            if(statusCode == HttpStatus.SC_OK){    
            	InputStream inputStream = myPost.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                responseString = stringBuffer.toString();
            }    
        }catch (Exception e) { 
            e.printStackTrace();    
        }finally{
        	 myPost.releaseConnection(); 
        }
        return responseString;   

  	}
  	/*public static String postXML(String url,String xmlFileName){
  		CloseableHttpClient client = null;
  		CloseableHttpResponse resp = null;
  		try{
  			HttpPost httpPost = new HttpPost(url);
  			httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
  			client = HttpClients.createDefault();
  			StringEntity entityParams = new StringEntity(xmlFileName,"utf-8");
  			httpPost.setEntity(entityParams);
  			client = HttpClients.createDefault();
  			resp = client.execute(httpPost);
  			String resultMsg = EntityUtils.toString(resp.getEntity(),"utf-8");
  			return resultMsg;
  		}catch (Exception e){
  			e.printStackTrace();
  		}finally {
  			try {
  				if(client!=null){
  					client.close();
  				}
  				if(resp != null){
  					resp.close();
  				}
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  		}
  		return null;
  		
  	}*/
  	
  	
    
}