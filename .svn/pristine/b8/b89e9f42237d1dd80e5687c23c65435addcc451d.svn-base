package com.xuyurepos.controller.payment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.payment.XuyuPayaddress;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.payment.XuyuPayaddressService;
import com.xuyurepos.vo.payment.XuyuPayaddressVo;
/**
 * 充值二维码
 * @author yangfei
 */
@Controller
@RequestMapping(value="xuyupayaddress")
public class XuyuPayaddressControl {

	@Autowired
	private XuyuPayaddressService xuyuPayaddressService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuPayaddressControl.class);
	
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		try {
			 String url=request.getParameter("url");
			 xuyuPayaddressService.outputCaptcha(url, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
       
    }
	
	/**
	 * 生成二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xuyuPayaddressService.saveInfo();
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 二维码查询
	 * @param pageModel
	 * @param xuyuRechargeVo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel, XuyuPayaddressVo xuyuPayaddressVo, HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			xuyuPayaddressVo.setAgencyId(SystemConstants.STRINGEMPTY+systemUser.getOrgId());
		}
		pageModel.setQueryObj(xuyuPayaddressVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		xuyuPayaddressService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}



}
