package com.xuyurepos.controller.cardmanager;

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
import com.xuyurepos.service.manager.XuyuContentCardMgrSelfService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuContentCardMgrVo;
import com.xuyurepos.vo.system.SystemOrgVo;

/**
 * 卡出库管理
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyucontentcardmgrself")
public class XuyuContentCardMgrSelfControl {
	
	@Autowired
	private XuyuContentCardMgrSelfService xuyuContentCardMgrSelfService; 
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuContentCardMgrSelfControl.class);
	
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
			XuyuContentCardMgrVo xuyuContentCardMgrVo = xuyuContentCardMgrSelfService.find(id);
			map.put("sucess", true);
			map.put("model", xuyuContentCardMgrVo);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
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
	public String findList(PageModel pageModel, XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,HttpServletRequest request) {
		// 转换
		if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
			SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
			xuyuContentCardMgrSelfVo.setAgencyQuery(systemOrgVo.getOrgId());
		}
		pageModel.setQueryObj(xuyuContentCardMgrSelfVo);
		xuyuContentCardMgrSelfService.findList(pageModel);
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
		String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
		return result;
	}
	
	/**
	 * 批量出库
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selfAll", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String selfAll(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xuyuContentCardMgrSelfService.selfAll(xuyuContentCardMgrVo);
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
	 * 划卡
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setOwner", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String setOwner(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xuyuContentCardMgrSelfService.setOwner(xuyuContentCardMgrVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}

}
