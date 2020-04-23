package com.xuyurepos.controller.system;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.entity.system.XuyuDown;
import com.xuyurepos.service.system.XuyuDownService;
/**
 * 异步导出类
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyudowncontrol")
public class XuyuDownControl {

	
	@Autowired
	private XuyuDownService xuyuDownService; 
	
	/**
	 * 分页查询数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel,XuyuDown xuyuDown, HttpServletRequest request) {
		try {
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			xuyuDown.setCreateUser(systemUser.getUserName());
			pageModel.setQueryObj(xuyuDown);
			xuyuDownService.findList(pageModel);
			JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
			String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}


}
