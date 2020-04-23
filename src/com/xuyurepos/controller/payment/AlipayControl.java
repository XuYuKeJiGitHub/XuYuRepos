package com.xuyurepos.controller.payment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.service.payment.AlipayService;

@Controller
@RequestMapping(value="alipay")
public class AlipayControl {
	
	Logger logger=LoggerFactory.getInstance().getLogger(AlipayControl.class);
	
	@Autowired
	private AlipayService alipayService;
	
	@Autowired
	private BizException bizException;
	
	/**
	 * 校验数据有效性
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkNums", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkNums(HttpServletRequest request,HttpServletResponse response){
		try {
			logger.info("checkNums AlipayControl start");
			String accessNums=request.getParameter("accessNums");
			Map<String, Object> mapCheck=alipayService.checkNums(accessNums);
			Map<String, Object> map = new HashMap<String, Object>();
			if(mapCheck==null){
				map.put("result",false);
			}else{
				// 先要看运营商
				map.put("provider",mapCheck.get("PROVIDER"));
				map.put("accessNum",mapCheck.get("ACCESS_NUM"));
				map.put("iccid",mapCheck.get("ICCID"));
				map.put("result",true);
			}
			String result = JSONObject.toJSONString(map);
			logger.info("checkNums AlipayControl end");
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 创建订单信息
	 * @return
	 */
	@RequestMapping(value = "/createOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createOrder(HttpServletRequest request,HttpServletResponse response){
		try {
			logger.info("createOrder AlipayControl start");
			String accessNums=request.getParameter("accessNums");
			String payId=request.getParameter("payId");
			Map<String, Object> map =alipayService.createOrder(accessNums,payId);
			String result = JSONObject.toJSONString(map);
			logger.info("createOrder AlipayControl end");
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}

}
