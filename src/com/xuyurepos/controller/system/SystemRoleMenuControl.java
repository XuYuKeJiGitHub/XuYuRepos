package com.xuyurepos.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.service.system.SystemRoleMenuService;
/**
 * 角色菜单
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/systemrolemenu")
public class SystemRoleMenuControl {
	
	@Autowired
	private SystemRoleMenuService systemRoleMenuService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemRoleMenuControl.class);
	
	/**
	 * 角色菜单
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/loadRoleMenuTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadRoleMenuTree(HttpServletRequest request){
		log.info("loadRoleMenuTree SystemRoleMenuControl start");
		String roleId = request.getParameter("roleId");
		List<Map> list= systemRoleMenuService.loadRoleMenuTree(roleId);
        String result = JSONObject.toJSONString(list);
        return result;
    }
	
	/**
	 * 角色子菜单
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/loadRoleMenuChildrenTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadRoleMenuChildrenTree(HttpServletRequest request){
		log.info("loadRoleMenuChildrenTree SystemRoleMenuControl start");
		String roleId = request.getParameter("roleId");
		String fID = request.getParameter("fID");
		List<Map> list= systemRoleMenuService.loadRoleMenuChildrenTree(roleId,fID);
        String result = JSONObject.toJSONString(list);
        return result;
    }
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String ids = request.getParameter("ids");
			String noids = request.getParameter("noids");
			String roleId=request.getParameter("roleId");
			systemRoleMenuService.saveInfo(ids,noids,roleId);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
