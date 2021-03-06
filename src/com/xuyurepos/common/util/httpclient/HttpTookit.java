package com.xuyurepos.common.util.httpclient;



import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xuyurepos.common.util.XmlMapUtils;
import com.xuyurepos.util.HttpClientUtil;

/**
 * 基于 httpclient 4.5.3版本的 http工具类
 * @author mcSui
 *
 */
public class HttpTookit {
	
	static Logger logger=LoggerFactory.getLogger(HttpTookit.class);
	
	private static PoolingHttpClientConnectionManager connMgr;
    private static CloseableHttpClient        httpClient=null;
//    public static final String             CHARSET               = "UTF-8";
    private static  RequestConfig requestConfig;


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
    		// 设置连接池
    		connMgr = new PoolingHttpClientConnectionManager();
    		// 设置连接池大小
    		connMgr.setMaxTotal(100);
    		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

      SSLContext sc=SSLContext.getInstance("SSLv3");
      sc.init(null, new TrustManager[] {
    		  // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
     new X509TrustManager() {
                  @Override
                  public X509Certificate[] getAcceptedIssuers() {
                      return null;
                  }
                  @Override
                  public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                  }
                  @Override
                  public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                  }
              }
      }, new SecureRandom());    
      SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);    

    requestConfig = RequestConfig.custom().setConnectionRequestTimeout(WAIT_TIMEOUT)
               .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(READ_TIMEOUT).build();
        
        httpClient= HttpClients.custom().setSSLSocketFactory(socketFactory).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
//    public static String doGet(String url, Map<String, String> params) {
//        return doGet(url, params, CHARSET);
//    }
//
//    public static String doPost(String url, Map<String, String> params) {
//        return doPost( url, params, null,null, CHARSET);
//    }

    //    public static String doPost(String url, String params){
    //        return doPost(url, params,CHARSET);
    //    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     * @throws IOException 
     */
    public static String doGet(String url,Object objParam,String charset) throws IOException{
        if(StringUtils.isBlank(url)){
            return null;
        }
        	if(objParam instanceof Map) {
        	Map<String,String> params=(Map)objParam;
        		
//            if(params != null && !params.isEmpty()){
//                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
//                for(Map.Entry<String,String> entry : params.entrySet()){
//                    String value = entry.getValue();
//                    if(value != null){
//                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
//                    }
//                }
//                
//
//                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
//            }
        String param=	createLinkString(params);
        url +="?"+param;
        	}else if(objParam instanceof String) {
                url += "?" + ( URLEncoder.encode((String) objParam, charset));
        	}
        	
        	logger.info("请求完整路径："+url);
        	HttpGet httpGet = new HttpGet(url);
        	httpGet.setHeader("Content-Type","application/x-www-form-urlencoded");
        	httpGet.setConfig(requestConfig);
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
            logger.info("请求返回完整参数："+result);
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
     */
    public static String doPost(String url,Object objParam,String charset) throws IOException{
    	
    
    	
        if(StringUtils.isBlank(url)){
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
    	if(objParam instanceof Map) {
    		
    		Map<String,String> params =(Map)objParam;
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,charset));
            }
//    		String params=		createLinkString(map);
//        	if(StringUtils.isNoneBlank(params)) {
//                httpPost.setEntity((new StringEntity(params, CHARSET)));
//        	}
        }else if(objParam instanceof String) {
        	String params =(String)objParam;
        	if(StringUtils.isNoneBlank(params)) {
                httpPost.setEntity((new StringEntity(params, charset)));
        	}
        }
    	
    	logger.info("请求路径完整："+url+EntityUtils.toString(httpPost.getEntity()));
    	
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("请求返回结果："+result);
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

    public static void main(String[] args) throws IOException {

//        String getData = doGet("https://www.icloud.com/", null);
//        System.out.println(getData);
//        System.out
//            .println("--------------------------------------------------分割线------------------------------------------------------");
    	TreeMap treeMap=new TreeMap();
    	treeMap.put("agencyCode","test1");
    	treeMap.put("accessNum","1440042671250");
    	treeMap.put("provider","01");
    	String str=getSign(treeMap);
    	Map map=new TreeMap();
    	map.put("agencyCode","test1");
    	map.put("accessNum","1440042671250");
    	map.put("provider","01");
    	map.put("sign",str);
        try {
        	String postData = doPost("https://iots.shingsou.com/facade/singleGprsInfoQuery", JSON.toJSONString(map),"utf-8");
			System.out.println("返回参数"+postData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
//        doPost("http://localhost:1112/httpClientServer/",new HashMap(),"E:/234/20150311170527.xls","123" ,"UTF-8");
    	
    	String str2="{\r\n" + 
    			"	\"body\": {\r\n" + 
    			"		\"encryData\": \"F02D43B38A0163817F56963AF1047ADB1D91325CF603E7D9A16E064A9737070DAA7F40940FAD71CA8BBE9DFD26CE6DC6BA71730461FA8C6719EC751F3C7409FDF68FDF289D3F75560E53317A297C9350372C81DA4718D1AFE03136B4B666127E0F598A86E456F46D50672D9CF9BF3D182B5870DFCD4C463739E87F6282A214753ACE3DF598E47E9AC4101154B583973BF356700EAAA189CB7D7CF5386A5668084D47833A5E202EC65130FD3EB43B0041FA045C85A4CAE68F42DE415D24F60A22428E5673E88C9663043E1E31F752068592D0514A53531C30FC92E979D218B51D0F0B434061F457186F2A39C7CBEC814A1AA00F7D623F9085FA32A45B5D7C75033190B8144B9B0C49405DF80971CAC63B7583DDA79C0322DDEDDD04D7B86104F9EC8F9E43331A545A87ADDD8652D55C3EABE0309F1AC3AA0567BA0C2EC440929BC99ED3C3CF8D02108D9F840B49FF8B0ADADBAFCE6DDA6ACE848D90FF6BBFB5762F0BA368A36785090333794779397BCB26433206B3A845D350928A8D1A585ED7E827F2BECF2CD6D031A6AA04A3147CDA8B80FE4E8816C27A88081D8A5CC4CE698EA28BACB7FE814FB02BB7DA6B54E5A92BAE60772E5CCEADB02C27BFA81F416766EF8729C09BCB26F21A3E14D15F526E92DBC9F0723394BD39EFE52A2CF2785B76329BA2DEBD7EFB6169FB3E8CE7B16AF70B909D14E98507EEF4100DE475680B3A9968B61347B70CFF3B363BE990C0C2D3C3BCA4BADCB0C5A3E6EB83242DCFE0814A796C0AF2360001111E7C66FE05F3BB722E23364A994A952D30B2656A41D796FC164BC1882DE5B825BB85C626410FDC7A1AFB32D3081934317CCDA9A38B88CDFD4F0BD9153557798D394358BD4EA5028E0B1A9A4F0412B54292FF8B0B49E7E94139898430B737701A92E049CE6D1FA63FEA9F6B5D28EA370827095D2C10E5EE4047705193BBD8B74BFD7E4A06A73E857E2F3113E417E824FB8057177F09692BAE60772E5CCEADB02C27BFA81F416766EF8729C09BCB26F21A3E14D15F526E436BE4D5045FE83C3827DB264C28E153DA6A44569D3AA071DB0A53B896A4AFD9E2ACFB64B6BFE6B8DC75B1F21D7AAAB3B069FC0E48C80A3245BECB113AD794AA3E5C3005D9DB16DF64515DC9D5578127E033AFFA34D4E97EAF668AF893691556760DC81D2D8E271307410C8BC0DDDD8FFC14FB475604BAEC47FD6AE444EE8AB0B32B581AE6822755144F113225F1CD5969BA3DBA6FF40E95951A46D54F8E2A87E2412D4C4BC6B52D9FE4F84BC93F556AACC6ACF4CD31A47D0F8C993DEEEA25D709B6CC4C1D1704CC41385627C638434BBBD193F9C45F494698215FC06998370251831377B518975065A59C786132F891592F6A4D43FF5D64DEB0A35E9616903D45586F62A46D99C091FF9FFBA95560C0C1F6ACF0ED05817F833646918333C24B6175CF333A6023AA486432397061D8853C5925D6F48E16C69F454120E551227AA435A4A146905E45042913A10FD6DA408D0C1043284381F2C6A2A5405C072105DC7EE9D75683238CB5D6DC6B39F260E7F7BD335A3F8A898057E3F39AB2034FCEB368215D2C643B88AD520A78E6196F5BD3118C875C8F25DE3466565DEE17DE3C024F3F071E91AF1B24ABE00C549E4C71D55DED6AE46DC3858A7D8C0AC0F2DC8C189B67F6B8FD1521628757353A8001A9E90CE0A474F0F9A3D98759CC907C6467F99D61BE1B352D99F78CC4A82537C2E210BEC8507FF26DD2FA2CF5EA908F910825BE02BF1E7535E21ECBC2C870025398FB6BA63729C1BBDC701518ED66FC976137E808E09E62745DAC1155BF4624DDBDD612E2EA20C706DA77A710FC3DF9543F412706BBD1808FED22DA4C28A82557CD2FB7E30F9D221089415BE9708C2D0BD2400FAC8B387EEA83A4443155A7098ACA51D0DCDA483E02FA63C52842B00BC6E6B138C0304ED057553505011661353A98A6B5311DD7539356832A7D925A8C4C3D963DC76C3877E8DFE4997DC88BD4B93D4FA32B6A2BCD36F218AC4B17A099C4A368399DD0FDDB57A965913FB429CF062E6BBD571B3B17D63EFCCBB4B2F660F49682CEAC24D8D48DC13882C4BEC64636D2B677A1BDE338A2CC03B2FA5A8F7D347DE566AA45EA61E1668857C138306F8AFE\",\r\n" + 
    			"		\"signData\": \"4ea77665d50ada4375e4234a3018609c\"\r\n" + 
    			"	},\r\n" + 
    			"	\"header\": {\r\n" + 
    			"		\"agencyCode\": \"P0000001\",\r\n" + 
    			"		\"key\": \"hXlGfkkNDoQR7TOHNZ6y9VfGO+fW487u8LqMp8UCytEB6vhDilL2Aq0B8SUBj8NL8AO3ci2Nbva9hJwNLfPMzLm8ydpwzjSuslSgPc25HI+LLolw8B7Fa4mfR3+/7gVxstiRRQ+TIk6+ncTYoHX6RaOoHNMh4Wbo6bd3vjp0/Ts=\",\r\n" + 
    			"		\"orgCode\": \"kg\",\r\n" + 
    			"		\"orgPwd\": \"qtcq67FY*9ntbyb6\",\r\n" + 
    			"		\"orgUser\": \"kg\",\r\n" + 
    			"		\"serviceCode\": \"QDB_AMS_001\"\r\n" + 
    			"	},\r\n" + 
    			"	\"message\": {\r\n" + 
    			"		\"returnCode\": \"000000\",\r\n" + 
    			"		\"returnMsg\": \"成功\"\r\n" + 
    			"	}\r\n" + 
    			"}";
    	
    	doPost("http://localhost:8080/api/outter/maizi/callback/withdraw.htm",str2,"UTF-8");
    }
   
	public static String getSign(TreeMap treeMap) {
		String str="";
		Iterator iter = treeMap.entrySet().iterator(); 
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next(); 
		     if(entry.getValue()!=null&&!entry.getValue().equals("")) {
		    	 str=str+entry.getKey()+"="+entry.getValue();
//		    	 if(iter.hasNext()) {
		    		 str=str+"&";
//		    	 }
		     }
		}
		return getMD5(str);
	}
	/**
	 * 获取MD5字符串
	 * @param str
	 * @return
	 */
	private static String getMD5(String str) {
		String key="xuYuRepos2019";
		String signStr=str+"key="+key;//&key
		String sign=DigestUtils.md5Hex(signStr).toUpperCase();
		return sign;
	}
	
	public static void main2(String[] args) {
		HashMap<String,Object> content = new HashMap<>();
		
		content.put("groupid", "51533317364");
		content.put("telnum", "89860445101971348018");
		content.put("oprtype", "2");
		content.put("reason", "7");
		HashMap<String, Object> operation_in = new HashMap<>();
		operation_in.put("content", content);
		
		String url = "http://221.178.251.182:80/internet_surfing";
		
		String createXmlByMap = XmlMapUtils.createXmlByMap(operation_in, "utf-8");
		String postXML = HttpClientUtil.postXML(url, createXmlByMap);
		
	}
	
}