package com.xuyurepos.controller.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.service.facade.FacadeService;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;
/**
 * 外部接口
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/facade")
public class FacadeControl {
	
	Logger logger=LoggerFactory.getInstance().getLogger(FacadeControl.class);
	
	@Autowired
	private FacadeService facadeService; 
	
	@Autowired
	private IccIdManagerService iccIdManagerService; 
	
	/**
	 * 五、用户批量停复机
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return 
	 */
	@RequestMapping(value = "/changeCardStateAll", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String changeCardStateAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalid2(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					writeResult(response,obj);
					return null;
				}
			}
			String[] accessNumArr = paramMap.get("accessNums").toString().split(";");
			String state = paramMap.get("state").toString();//停复机状态 ：00正常 ；02停机
			for (int i = 0; i < accessNumArr.length; i++) {
			    iccIdManagerService.changeCardState(accessNumArr[i],state);
			}
			obj.put("requestCode", "0");
			obj.put("requestMsg", "停复机操作成功");
			writeResult(response,obj);
			return null;
		}catch (Exception e) {
			
			return null;
		}
	}
	
	/**
	 * 一、单卡GPRS用量信息查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/singleGprsInfoQuery", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String singleGprsInfoQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			 paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalid(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			resultMap=facadeService.singleGprsInfoQuery(paramMap);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "单卡用量信息查询成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "单卡用量信息查询成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "单卡用量信息查询结果为空");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "单卡用量信息查询结果为空");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}catch (Exception e) {
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	/**
	 * 一、Iccid单卡GPRS用量信息查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/singleGprsInfoQueryIccid", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String singleGprsInfoQueryIccid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			 paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalidIccid(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			resultMap=facadeService.singleGprsInfoQueryIccid(paramMap);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "单卡用量信息查询成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "单卡用量信息查询成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "单卡用量信息查询结果为空");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "单卡用量信息查询结果为空");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}catch (Exception e) {
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	private Map<String, Object> invalidIccid(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String iccid=(String) paramMap.get("iccid");
		String queryDate=(String) paramMap.get("queryDate");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(iccid==null||"".equals(iccid)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;iccid为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		treeMap.put("iccid",iccid);
		treeMap.put("queryDate",queryDate);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 二、批量GPRS用量信息查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchGprsInfoQuery")
	public String batchGprsInfoQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalidBatch(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			List<Map<String, Object>> resultMapList=facadeService.batchGprsInfoQuery(paramMap);
			resultMap.put("accessNumList", resultMapList);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "批量用量信息查询成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "批量用量信息查询成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "批量用量信息查询结果为空");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "批量用量信息查询结果为空");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		} catch (Exception e) {
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	/**
	 * 二、iccid批量GPRS用量信息查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchGprsInfoQueryIccid")
	public String batchGprsInfoQueryIccid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalidBatchIccid(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			List<Map<String, Object>> resultMapList=facadeService.batchGprsInfoQueryIccid(paramMap);
			resultMap.put("iccidList", resultMapList);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "批量用量信息查询成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "批量用量信息查询成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "批量用量信息查询结果为空");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "批量用量信息查询结果为空");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	private Map<String, Object> invalidBatchIccid(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String iccidList=(String) paramMap.get("iccidList");
		String queryDate=(String) paramMap.get("queryDate");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(iccidList==null||"".equals(iccidList)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;iccidList为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		treeMap.put("iccidList",iccidList);
		treeMap.put("queryDate",queryDate);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 充值套餐查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/rechargeQuery")
	public String rechargeQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			 paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalidRecharge(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			List<Map<String, Object>> resultMap1=facadeService.rechargeQuery(paramMap);
			resultMap.put("rechargeList", resultMap1);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "充值套餐信息查询成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "充值套餐信息查询成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "充值套餐信息查询结果为空");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "充值套餐信息查询结果为空");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}catch (Exception e) {
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	/**
	 * 充值接口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay")
	public String pay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj=new JSONObject();
		try {
			Map<String, Object> resultMap=new HashMap();
			Map paramMap;
			String requestParam=getRequestParam(request);
			 paramMap=parseJson2Map(requestParam);
			if(paramMap==null||paramMap.size()==0) {
				obj.put("requestCode", "-1");
				obj.put("requestMsg", "参数异常,未获取到请求参数");
				writeResult(response,obj);
				return null;
			}else {
				resultMap=invalidPay(paramMap);
				if(resultMap!=null&&resultMap.size()>0) {
					obj.put("requestCode", "0");
					obj.put("requestMsg", "参数校验未通过");
					obj.put("resultMap", resultMap);
					writeResult(response,obj);
					return null;
				}
			}
			// 校验供应商代码暂时不做
			resultMap=facadeService.pay(paramMap);
			if(resultMap!=null&&resultMap.size()>0) {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "充值成功");
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", "充值成功");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}else {
				obj.put("requestCode", "0");
				obj.put("requestMsg", "充值失败");
				resultMap=new HashMap<>();
				resultMap.put("resultCode", "-1");
				resultMap.put("resultMsg", "充值失败");
				obj.put("resultMap", resultMap);
				writeResult(response,obj);
				return null;
			}
		}catch (CustomException e) { 
			obj.put("resultCode", "-1");
			obj.put("resultMsg", "接口异常,"+e.getMessage());
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}catch (Exception e) {
			obj.put("requestCode", "-1");
			obj.put("requestMsg", "接口异常,解析参数报错");
			logger.info("接口异常"+e.getMessage());
			writeResult(response,obj);
			return null;
		}
	}
	
	/**
	 * 充值接口参数校验
	 * @param paramMap
	 * @return
	 */
	private Map<String, Object> invalidPay(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String payId=(String) paramMap.get("payId");
		String yc=(String) paramMap.get("yc");
		String accessNum=(String) paramMap.get("accessNum");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(accessNum==null||"".equals(accessNum)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;accessNum为必填");
			return resultMap;
		}
		if(payId==null||"".equals(payId)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;payId为必填");
			return resultMap;
		}
		if(yc==null||"".equals(yc)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;yc为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		treeMap.put("payId",payId);
		treeMap.put("yc",yc);
		treeMap.put("accessNum",accessNum);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}

	
	
	/**
	 * 将http字节流转换为String
	 * @param request
	 * @return
	 */
	public String getRequestParam(HttpServletRequest request) {
 		try {
			StringBuilder sb=new StringBuilder("");
			BufferedReader br=request.getReader();
			String line=null;
			while((line=br.readLine())!=null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
 			e.printStackTrace();
 			return null;
		}
	}
	/**
	 * 将JSON String转换为Map
	 * @param requestParam
	 * @return
	 */
	private Map parseJson2Map(String requestParam) {
		JSONObject json=JSON.parseObject(requestParam);
		Map paramMap =new HashMap();
		for(String key:json.keySet()) {
			paramMap.put(key, json.get(key));
		}
		return  paramMap;
	}
	/**
	 * 将Map转换成Json返回
	 * @param map
	 * @return json string
	 */
	private String parseMap2Json(Map<String,Object> map) {
		String str=JSON.toJSONString(map,SerializerFeature.WriteMapNullValue);
		return str;
	}
	private void writeResult(HttpServletResponse response ,JSONObject obj) {
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(obj.toString());
			response.getWriter().flush();
		} catch (Exception e) {
			logger.info("接口异常，请联系系统管理员"+e.getMessage());
		}
	}
	
	/**
	 * 参数校验
	 * @param paramMap
	 * @return
	 */
	private Map invalidBatch(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNumList=(String) paramMap.get("accessNumList");
		String queryDate=(String) paramMap.get("queryDate");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(accessNumList==null||"".equals(accessNumList)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;accessNumList为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		treeMap.put("accessNumList",accessNumList);
		treeMap.put("queryDate",queryDate);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}
	
	/**
	 * 校验充值查询参数
	 * @param paramMap
	 * @return
	 */
	private Map invalidRecharge(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}
	
	private Map invalid(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNum=(String) paramMap.get("accessNum");
		String queryDate=(String) paramMap.get("queryDate");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(accessNum==null||"".equals(accessNum)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;accessNum为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
 			return resultMap;
		}
		TreeMap treeMap=new TreeMap();
		String str="";
		treeMap.put("agencyCode",agencyCode);
		treeMap.put("accessNum",accessNum);
		treeMap.put("queryDate",queryDate);
		String signTemp=getSign(treeMap);
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
 			return resultMap;
		}
		return resultMap;
	}
	/**
	 * 停复机接口验证
	 * @param paramMap
	 * @return
	 */
	private Map invalid2(Map paramMap) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNums=(String) paramMap.get("accessNums");
		String state=(String) paramMap.get("state");
		String sign=(String) paramMap.get("sign");
		// 非空校验
		if(agencyCode==null||"".equals(agencyCode)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;agencyCode为必填");
			return resultMap;
		}
		if(accessNums==null||"".equals(accessNums)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;accessNums为必填");
			return resultMap;
		}
		if(state==null||"".equals(state)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;state为必填");
			return resultMap;
		}
		if(sign==null||"".equals(sign)){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;sign为必填");
			return resultMap;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append("agencyCode="+agencyCode);
		sbf.append("&accessNums="+accessNums);
		sbf.append("&state="+state+"&");
		String signTemp = getMD5(sbf.toString());
		if(!sign.equals(signTemp)) {
			resultMap.put("resultCode", "-1");
			resultMap.put("resultMsg", "参数异常;签名错误");
			return resultMap;
		}
		return resultMap;
	}
	/**
	 * 
	 * @param treeMap
	 * @return
	 */
	private String getSign(TreeMap treeMap) {
		String str="";
		Iterator iter = treeMap.entrySet().iterator(); 
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next(); 
		     if(entry.getValue()!=null&&!entry.getValue().equals("")) {
		    	 str=str+entry.getKey()+"="+entry.getValue();
		    	 str=str+"&";
		     }
		}
		return getMD5(str);
	}
	/**
	 * 获取MD5字符串
	 * @param str
	 * @return
	 */
	private String getMD5(String str) {
		String key="xuYuRepos2019";
		String signStr=str+"key="+key;//&key
		String sign=DigestUtils.md5Hex(signStr).toUpperCase();
		return sign;
	}
}
