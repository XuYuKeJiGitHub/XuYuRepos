package com.xuyurepos.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemFunction;
import com.xuyurepos.service.system.SystemFunctionService;
import com.xuyurepos.vo.system.SystemFunctionVo;
/**
 * 后台模块
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/systemfunction")
public class SystemFunctionControl {

	
	@Autowired
	private SystemFunctionService systemFunctionService; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemFunctionControl.class);
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemFunctionVo systemLookUpVo, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SystemFunction item = systemFunctionService.saveInfo(systemLookUpVo);
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
			SystemFunctionVo systemLookUpVo = systemFunctionService.find(id);
			map.put("sucess", true);
			map.put("model", systemLookUpVo);
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
		systemFunctionService.delete(ids);
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
	public String findList(PageModel pageModel, SystemFunctionVo systemFunctionVo, HttpServletRequest request) {
		pageModel.setQueryObj(systemFunctionVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		systemFunctionService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
}
