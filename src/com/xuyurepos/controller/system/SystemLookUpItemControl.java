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
import com.xuyurepos.entity.system.SystemLookUpItem;
import com.xuyurepos.service.system.SystemLookUpItemService;
import com.xuyurepos.vo.system.SystemLookUpItemVo;
/**
 * 数据字典
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/systemlookupitem")
public class SystemLookUpItemControl {
	
	@Autowired
	private SystemLookUpItemService itemService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemLookUpItemControl.class);
	
	/**
	 * 提供下拉框的数据查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lookUp",produces="application/json;charset=UTF-8")
	@ResponseBody
    public String lookUp(HttpServletRequest request,HttpServletResponse response){
        try{
        	String fLookUpId=request.getParameter("fLookUpId");
        	List<ComboBoxUtils> dataList =itemService.findList(fLookUpId);
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
	public String save(SystemLookUpItemVo systemLookUpVo, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SystemLookUpItem item = itemService.saveInfo(systemLookUpVo);
			map.put("sucess", true);
			map.put("model", item);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception ex) {
        	ex.printStackTrace();
        	bizException.resolveException(request, response, 1011, ex);
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
			SystemLookUpItemVo systemLookUpVo = itemService.find(id);
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
		itemService.delete(ids);
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
	public String findList(PageModel pageModel, SystemLookUpItemVo systemLookUpVo, HttpServletRequest request) {
		pageModel.setQueryObj(systemLookUpVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		itemService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}

}
