package com.xuyurepos.service.intergration.mobile.util;

import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;

import com.xuyurepos.common.util.httpclient.HttpProtocolHandler;
import com.xuyurepos.common.util.httpclient.HttpRequest;
import com.xuyurepos.common.util.httpclient.HttpResponse;
import com.xuyurepos.common.util.httpclient.HttpResultType;




public class CMPSubmit {

	 

    public static String buildRequestPost(String strParaFileName, String strFilePath,Map<String, String> sParaTemp,
            String posturl) throws Exception {

    			String inputCharset ="UTF-8";

    			HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

    			HttpRequest request = new HttpRequest(HttpResultType.BYTES);
    			request.setCharset(inputCharset);

    			request.setParameters(generatNameValuePair(sParaTemp));
    			request.setUrl(posturl);

    			HttpResponse  response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
    			if (response == null) {
    				return null;
    			}

    			return response.getStringResult(inputCharset);
    }
	
    public static String buildRequestGet(Map<String, String> sParaTemp,String posturl) throws Exception {
        
        String inputCharset = "UTF-8";

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(inputCharset);
        
        String queryStr = CMPCore.createLinkString(sParaTemp);
        request.setUrl(posturl);
        request.setMethod(HttpRequest.METHOD_GET);
        request.setQueryString(posturl+"?"+queryStr);
        request.setHeader("application/x-www-form-urlencoded");
       
        HttpResponse  response = httpProtocolHandler.execute(request,"","");
        if (response == null) {
            return null;
        }
      
        return response.getStringResult(inputCharset);
    }



    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }




}
