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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.payment.XuyuRechargeService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.payment.XuyuRechargeResultVo;
import com.xuyurepos.vo.payment.XuyuRechargeVo;
import com.xuyurepos.vo.system.SystemOrgVo;
/**
 * 充值相关处理类
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value="xuyurecharge")
public class XuyuRechargeControl {
	@Autowired
	private XuyuRechargeService xuyuRechargeService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuRechargeControl.class);
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	/**
	 * 检查卡类型
	 * @param xuyuRechargeVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkType(XuyuRechargeVo xuyuRechargeVo, HttpServletRequest request,HttpServletResponse response){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map=xuyuRechargeService.checkType(xuyuRechargeVo);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 群组充值
	 * @param xuyuRechargeVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(XuyuRechargeVo xuyuRechargeVo, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xuyuRechargeService.saveInfo(xuyuRechargeVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 后台充值
	 * @param xuyuRechargeVo
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/backOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String backOrder(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String a=request.getParameter("a");
			System.out.println("a:"+a);
			XuyuRechargeVo xuyuRechargeVo=JSONObject.parseObject(a,XuyuRechargeVo.class);
			xuyuRechargeService.backOrder(xuyuRechargeVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	
	
	/**
	 * 创建代理商充值订单
	 * @return
	 */
	@RequestMapping(value = "/createOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createOrder(HttpServletRequest request,HttpServletResponse response){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			XuyuRechargeResultVo xuyuRechargeResultVo=xuyuRechargeService.createOrder();
			map.put("order", xuyuRechargeResultVo);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	
	/**
	 * 充值成功后回调接口测试
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/payNotify", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String payNotify(HttpServletRequest request,HttpServletResponse response){
		try {
			String out_trade_no = request.getParameter("out_trade_no");
			xuyuRechargeService.payNotify(out_trade_no);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
		
	}
	
	/**
	 * 充值记录查询
	 * @param pageModel
	 * @param xuyuPackagesVo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel, XuyuRechargeVo xuyuRechargeVo, HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
			}else{
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemUser.getOrgCode());
			}
		}else{
			if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
			}
		}
		pageModel.setQueryObj(xuyuRechargeVo);
		
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		xuyuRechargeService.findList(pageModel);
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
		return result;
	}
	
	/**
	 * 代理商充值记录
	 * @param pageModel
	 * @param xuyuRechargeVo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/customerFindList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String customerFindList(PageModel pageModel, XuyuRechargeVo xuyuRechargeVo, HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
			}else{
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemUser.getOrgCode());
			}
		}else{
			if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
				xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
			}
		}
		pageModel.setQueryObj(xuyuRechargeVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		xuyuRechargeService.customerFindList(pageModel);
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
		return result;
	}
	
	/**
	 * 后台充值记录导出
	 * @param xuyuRechargeVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exporData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String exporData(XuyuRechargeVo xuyuRechargeVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> map = null;
		try {
			log.info("exporData() XuyuRechargeControl  start");
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(!"1000".equals(systemUser.getOrgCode())){
				if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
					SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
				}else{
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemUser.getOrgCode());
				}
			}else{
				if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
					SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
				}
			}
			map = xuyuRechargeService.exporData(xuyuRechargeVo);
			String result = JSONObject.toJSONString(map);
			log.info("exporData() XuyuRechargeControl  end");
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	 }
	
	/**
	 * 客户充值记录导出
	 * @param xuyuContentCardMgrSelfVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exporCtustomerData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String exporCtustomerData(XuyuRechargeVo xuyuRechargeVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> map = null;
		try {
			log.info("exporCtustomerData() XuyuRechargeControl  start");
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(!"1000".equals(systemUser.getOrgCode())){
				if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
					SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
				}else{
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemUser.getOrgCode());
				}
			}else{
				if(xuyuRechargeVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAgencyQuery())){
					SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuRechargeVo.getAgencyQuery());
					xuyuRechargeVo.setCustomer(SystemConstants.STRINGEMPTY+systemOrgVo.getOrgId());
				}
			}
			map = xuyuRechargeService.exporCtustomerData(xuyuRechargeVo);
			String result = JSONObject.toJSONString(map);
			log.info("exporCtustomerData() XuyuRechargeControl  end");
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	 }
	
	
	

}
