package com.xuyurepos.controller.operamanager;

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
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuPackages;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.operamanager.XuyuPackagesService;
import com.xuyurepos.vo.operamanager.XuyuPackagesVo;
/**
 * 充值套餐
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyupackages")
public class XuyuPackagesControl {
	
	@Autowired
	private XuyuPackagesService xuyuPackagesService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuPackagesControl.class);
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(XuyuPackagesVo xuyuPackagesVo, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			XuyuPackages item = xuyuPackagesService.saveInfo(xuyuPackagesVo);
			map.put("sucess", true);
			map.put("model", item);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取用户对象
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/find", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String find(HttpServletRequest request) throws Exception {
		try {
			String id = request.getParameter("id");
			Map<String, Object> map = new HashMap<String, Object>();
			XuyuPackagesVo xuyuPackagesVo = xuyuPackagesService.find(id);
			map.put("sucess", true);
			map.put("model", xuyuPackagesVo);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除数据
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String delete(HttpServletRequest request) throws Exception {
		String ids = request.getParameter("ids");
		xuyuPackagesService.delete(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sucess", true);
		String result = JSONObject.toJSONString(map);
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setState", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String setState(HttpServletRequest request) throws Exception {
		String ids = request.getParameter("ids");
		xuyuPackagesService.setState(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sucess", true);
		String result = JSONObject.toJSONString(map);
		return result;
	}
	
	/**
	 * 发送套餐给代理商
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/packageSend", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String packageSend(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String ids = request.getParameter("ids");
		String agency=request.getParameter("agency");
		try {
			xuyuPackagesService.packageSend(ids,agency);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception ex) {
        	ex.printStackTrace();
        	bizException.resolveException(request, response,ex.getMessage(), ex);
			return null;
		}
		
	}
	
	
	
	/**
	 * 分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel, XuyuPackagesVo xuyuPackagesVo, HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			xuyuPackagesVo.setAgencyId(systemUser.getOrgId());
		}
		pageModel.setQueryObj(xuyuPackagesVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		xuyuPackagesService.findList(pageModel);
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
		String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
		return result;
	}
	
	/**
	 * 查询套餐数据
	 * @param pageModel
	 * @param xuyuPackagesVo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/views", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String views(PageModel pageModel, XuyuPackagesVo xuyuPackagesVo, HttpServletRequest request,HttpServletResponse response) {
		try {
			pageModel.setQueryObj(xuyuPackagesVo);
			
			xuyuPackagesService.findListH5(pageModel);
			JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
			String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
			return result;
		} catch (Exception ex) {
        	ex.printStackTrace();
        	bizException.resolveException(request, response, 1011, ex);
			return null;
		}
		
	}
	
	/**
	 * 查询套餐数据
	 * @param pageModel
	 * @param xuyuPackagesVo
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/select", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String select(PageModel pageModel, XuyuPackagesVo xuyuPackagesVo, HttpServletRequest request,HttpServletResponse response) {
		try {
			pageModel.setQueryObj(xuyuPackagesVo);
			String accessNum = request.getParameter("accessNum");
			Map<String, Object> map=xuyuPackagesService.select(accessNum);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception ex) {
        	ex.printStackTrace();
        	bizException.resolveException(request, response, 1011, ex);
			return null;
		}
		
	}
	
	

}
