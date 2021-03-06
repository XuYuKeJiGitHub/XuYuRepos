package com.xuyurepos.controller.payment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.util.UniqueOrderGenerate;
import com.wxpay.MyConfig;
import com.wxpay.WXPay;
import com.wxpay.WXPayConstants.SignType;
import com.wxpay.WXPayUtil;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.entity.payment.PaymentOrder;
import com.xuyurepos.service.payment.PaymentService;
import com.xuyurepos.service.payment.XuyuRechargeService;
/**
 * 三方支付参数获取类
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value="wxpay")
public class WxPayController {
	Logger logger=LoggerFactory.getInstance().getLogger(WxPayController.class);
	
	private static final String wxNotifyUrl="https://iots.shingsou.com/wxpay/notify";
	
	private static final String authorizeRedirectUri="https://iots.shingsou.com/wxpay/jsapiPay.html";
	
	@Autowired
	private XuyuRechargeService xuyuRechargeService; 
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		 String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
	                + "appid=wx115e1f1585ba8a8f&redirect_uri="
	                + URLEncoder.encode(authorizeRedirectUri, "utf-8") + "&response_type=code" + "&"
	                + "&scope=snsapi_base" + "&state=state" + "#wechat_redirect";
		 System.out.println(url);
	}
	
	@Autowired
	private PaymentService paymentService;
	
	/**
	 * 获取微信支付参数
	 * @param systemLookUpVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String wxPay( HttpServletRequest request) throws Exception {
		try {
			logger.info("wxPay PayControl start");
			MyConfig config = new MyConfig();
	        WXPay wxpay = new WXPay(config);
	        UniqueOrderGenerate uog=new UniqueOrderGenerate(0,0);
	        String out_trade_no=request.getParameter("out_trade_no");
	        
//	        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//	        out_trade_no=sdf.format(new Date())+uog.nextId();
	        
	        BigDecimal bd=new BigDecimal(request.getParameter("totalFee"));
	      
	        int total_fee=  bd.multiply(new BigDecimal(100)).intValue();
	        String spbill_create_ip=getInternetIp();
	        String trade_type=request.getParameter("tradeType");
	        String product_id=uog.nextId()+"";
	        String openid=request.getParameter("openid");
	        logger.info("微信JSAPI支付OPENID："+openid);

	        
	        
	        
	        Map<String, String>postData = new HashMap<String, String>();
	        
	        postData.put("body", "旭宇物联网卡充值");
	        postData.put("out_trade_no",out_trade_no );
	        postData.put("device_info", "WEB");
	        postData.put("fee_type", "CNY");
	        postData.put("total_fee", ""+total_fee);
	        postData.put("spbill_create_ip",spbill_create_ip);
	        postData.put("notify_url", wxNotifyUrl);
	        if(trade_type.equals("NATIVE")) {
	        	postData.put("product_id", product_id);
	        }else if(trade_type.equals("JSAPI")) {
	        	postData.put("openid",openid);
	        }
	        postData.put("trade_type", trade_type); 
	        
	        PaymentOrder order= new PaymentOrder();
	        order.setTradeNo(out_trade_no);
	        order.setTotalAmount(new BigDecimal(total_fee));
	        order.setTradeType("WX");
	        paymentService.createOrder(order);
	        logger.info("微信支付JSAPI请求参数:"+postData);
	        Map<String, String> resp = wxpay.unifiedOrder(postData);
	        logger.info("微信支付JSAPI返回参数："+resp);
	        
			Map<String, String> map = new HashMap<String, String>();
			map.put("appId",  resp.get("appid"));
			map.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
			map.put("nonceStr",resp.get("nonce_str"));
			map.put("package", "prepay_id="+resp.get("prepay_id").trim());
			map.put("signType", SignType.HMACSHA256+"");
			map.put("paySign", WXPayUtil.generateSignature(map, config.getKey(),  SignType.HMACSHA256));
			map.put("code_url", resp.get("code_url"));
			map.put("vpackage", "prepay_id="+resp.get("prepay_id").trim());
			String result = JSONObject.toJSONString(map);
			
			logger.info("jsapiPay PayControl end:"+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@RequestMapping(value = "/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("wxpay notify  start");
		InputStream is = request.getInputStream();
		String xml = inputStream2String(is, "UTF-8");
		try {
			logger.info("WxPay回调参数：" + xml);

			MyConfig config = new MyConfig();
			Map map=WXPayUtil.xmlToMap(xml);
			boolean validResult = WXPayUtil.isSignatureValid(map,config.getKey(),SignType.HMACSHA256);
			logger.info("微信支付验签结果："+validResult);
			if (validResult) {
				logger.info("微信支付回调验签成功！");
				String return_code = map.get("return_code") + "";
				if (return_code.equals("SUCCESS")) {
					String out_trade_no = map.get("out_trade_no") + "";
					PaymentOrder order = new PaymentOrder();
					order.setTradeNo(out_trade_no);
					order.setOrderStatus("S");
					paymentService.updateOrderStatus(order);
					
					xuyuRechargeService.payNotify(out_trade_no);

					Map respMap = new HashMap();
					respMap.put("return_code", "SUCCESS");
					respMap.put("return_msg", "OK");

					String respXML = WXPayUtil.mapToXml(respMap);
					response.setContentType("text/xml;charset=UTF-8"); 
					response.getWriter().print(respXML);
				    response.getWriter().flush(); 
				    response.getWriter().close();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	String xml="<xml><appid><![CDATA[wx115e1f1585ba8a8f]]></appid>\r\n" + 
			"<bank_type><![CDATA[CFT]]></bank_type>\r\n" + 
			"<cash_fee><![CDATA[1]]></cash_fee>\r\n" + 
			"<device_info><![CDATA[WEB]]></device_info>\r\n" + 
			"<fee_type><![CDATA[CNY]]></fee_type>\r\n" + 
			"<is_subscribe><![CDATA[N]]></is_subscribe>\r\n" + 
			"<mch_id><![CDATA[1352368801]]></mch_id>\r\n" + 
			"<nonce_str><![CDATA[giRfv67eJQPPRfUl7eQ29Hsl9x0a5p4y]]></nonce_str>\r\n" + 
			"<openid><![CDATA[oVfSkjqkz4ndVKuUgm_Km4CozQ54]]></openid>\r\n" + 
			"<out_trade_no><![CDATA[20190513095100037063]]></out_trade_no>\r\n" + 
			"<result_code><![CDATA[SUCCESS]]></result_code>\r\n" + 
			"<return_code><![CDATA[SUCCESS]]></return_code>\r\n" + 
			"<sign><![CDATA[C23EB709781357BDEF7131C197D2D0A2BDAF7432CE6081824DF8AC1B382D20C8]]></sign>\r\n" + 
			"<time_end><![CDATA[20190513095159]]></time_end>\r\n" + 
			"<total_fee>1</total_fee>\r\n" + 
			"<trade_type><![CDATA[JSAPI]]></trade_type>\r\n" + 
			"<transaction_id><![CDATA[4200000322201905137615348424]]></transaction_id>\r\n" + 
			"</xml>";
	
	private String inputStream2String(InputStream inStream, String encoding) {
		String result = null;
		try {
			if (inStream != null) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] tempBytes = new byte[1024];
				int count = -1;
				while ((count = inStream.read(tempBytes, 0, 1024)) != -1) {
					outStream.write(tempBytes, 0, count);
				}
				tempBytes = null;
				outStream.flush();
				result = new String(outStream.toByteArray(), encoding);
				outStream.close();
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}
	
	/**
	 * 跳转页面
	 * @param req
	 * @param resp
	 * @throws Exception 
	 */
	@RequestMapping(value = "/openIdRedirect")
	@ResponseBody
    protected void openIdRedirect(HttpServletRequest request, HttpServletResponse resp)
            throws Exception {
		logger.info("wxpay openIdRedirect  start");
		MyConfig config = new MyConfig();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid=" + config.getAppID() + "&redirect_uri="
                + URLEncoder.encode(authorizeRedirectUri, "utf-8") + "&response_type=code" + "&"
                + "&scope=snsapi_base" + "&state=state" + "#wechat_redirect";
        resp.sendRedirect(url);
        logger.info("wxpay openIdRedirect  start");
    }
	
	
	@RequestMapping(value = "/wxOpenId", produces = "application/json;charset=UTF-8")
	@ResponseBody
	protected String wxOpenId(HttpServletRequest request, HttpServletResponse response)
	            throws  Exception {
		    logger.info("wxpay wxOpenId  start");
		    MyConfig config = new MyConfig();
		    WXPay wxpay = new WXPay(config);
	        // 正常情况，授权成功页面将跳转至 redirect_uri/?code=CODE&state=STATE。也就是此servlet
	        String code = request.getParameter("code");
	        logger.info(" wxpay wxOpenId  code:"+code);
	        //获取openid和access_token的连接
	        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+config.getAppID()+"&secret=3af1a5e93f5e9fe4f5c405a8810ab20a&code=CODE&grant_type=authorization_code";
	        //获取返回的code
	        String requestUrl = getOpenIdUrl.replace("CODE", code);
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(requestUrl);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        //向微信发送请求并获取response
	        String response1 = httpClient.execute(httpGet,responseHandler);
	        logger.info("=========================获取token===================");
	        logger.info(response);
	        JSONObject jsonObject=JSON.parseObject(response1);
	        String access_token = jsonObject.get("access_token").toString();
	        String openId = jsonObject.get("openid").toString();
	        logger.info("=======================用户access_token==============");
	        logger.info(access_token);
	        logger.info(openId);
	       
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("access_token", access_token);
	        map.put("openId", openId);
	        String result = JSONObject.toJSONString(map);
	        logger.info("wxpay wxOpenId  end");
			return result;
	}


	
	
	
	
	
	
	public static String INTRANET_IP = getIntranetIp(); // 内网IP
	public static String INTERNET_IP = getInternetIp(); // 外网IP


	    /**
	     * 获得内网IP
	     * @return 内网IP
	     */
	    private static String getIntranetIp(){
	        try{
	            return InetAddress.getLocalHost().getHostAddress();
	        } catch(Exception e){
	            throw new RuntimeException(e);
	        }
	    }
	
	 /**
     * 获得外网IP
     * @return 外网IP
     */
    private static String getInternetIp(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
  
}
