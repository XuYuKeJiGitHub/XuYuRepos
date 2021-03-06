package com.xuyurepos.controller.system;

import java.util.HashMap;
import java.util.List;
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
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.ComboBoxUtils;
import com.xuyurepos.entity.system.SystemLookUp;
import com.xuyurepos.service.system.SystemLookUpService;
import com.xuyurepos.vo.system.SystemLookUpVo;

/**
 * 系统数据字典类型
 * @author yangfei
 */
@Controller
@RequestMapping(value = "/systemlookup")
public class SystemLookUpControl {
	
	@Autowired
	private SystemLookUpService systemLookUpService; 
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemLookUpControl.class);
	
	/**
	 * 提供数据字典类型的所有数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lookUp",produces="application/json;charset=UTF-8")
	@ResponseBody
    public String lookUp(HttpServletRequest request,HttpServletResponse response){
        try{
        	List<ComboBoxUtils> dataList =systemLookUpService.findList();
        	String result=JSONObject.toJSONString(dataList);
			return result;
        }catch(Exception ex){
        	ex.printStackTrace();
        	bizException.resolveException(request, response, 1011, ex);
			return null;
        }
    }
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemLookUpVo systemLookUpVo, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SystemLookUp systemLookUp=systemLookUpService.saveInfo(systemLookUpVo);
			map.put("sucess", true);
			map.put("model", systemLookUp);
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
			SystemLookUpVo systemLookUpVo = systemLookUpService.find(id);
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
		systemLookUpService.delete(ids);
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
	public String findList(PageModel pageModel, SystemLookUpVo systemLookUpVo, HttpServletRequest request) {
		pageModel.setQueryObj(systemLookUpVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		systemLookUpService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}

}
