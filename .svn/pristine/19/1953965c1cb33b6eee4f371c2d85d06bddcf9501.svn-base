package com.xuyurepos.controller.operamanager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuOwnerInfo;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.operamanager.XuyuOwnerInfoService;
import com.xuyurepos.vo.operamanager.XuyuOwnerInfoVo;
/**
 * 群组管理后台模块
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyuownerinfo")
public class XuyuOwnerInfoControl {
	@Autowired
	private XuyuOwnerInfoService xuyuOwnerInfoService; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuOwnerInfoControl.class);
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(XuyuOwnerInfoVo xuyuOwnerInfoVo) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			XuyuOwnerInfo xuyuOwnerInfo = xuyuOwnerInfoService.saveInfo(xuyuOwnerInfoVo);
			map.put("sucess", true);
			map.put("model", xuyuOwnerInfo);
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
			XuyuOwnerInfoVo xuyuOwnerInfoVo = xuyuOwnerInfoService.find(id);
			map.put("sucess", true);
			map.put("model", xuyuOwnerInfoVo);
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
		xuyuOwnerInfoService.delete(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sucess", true);
		String result = JSONObject.toJSONString(map);
		return result;
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
	public String findList(PageModel pageModel, XuyuOwnerInfoVo xuyuOwnerInfoVo,HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
		}else{
			xuyuOwnerInfoVo.setCompanyId("");
		}
		pageModel.setQueryObj(xuyuOwnerInfoVo);
		xuyuOwnerInfoService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}

}
