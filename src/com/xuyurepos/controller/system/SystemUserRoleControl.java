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
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemUserRoleService;
/**
 * 用户角色
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/systemuserrole")
public class SystemUserRoleControl {
	@Autowired
	private SystemUserRoleService systemUserRoleService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemUserRoleControl.class);
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/loadUserRoleTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadRoleMenuTree(HttpServletRequest request){
		log.info("loadUserRoleTree SystemUserRoleControl start");
		String userId = request.getParameter("userId");
		String fID=request.getParameter("fID");
		if(fID==null||"".equals(fID)){
			String roleType="";
			// 判断是否为总部
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(!"1000".equals(systemUser.getOrgCode())){
				roleType="02";
			}
			List<Map> list= systemUserRoleService.loadUserRoleTree(userId,roleType);
	        String result = JSONObject.toJSONString(list);
	        return result;
		}else{
			return "{}";
		}
		
        
    }
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String ids = request.getParameter("ids");
			String noids = request.getParameter("noids");
			String userId=request.getParameter("userId");
			systemUserRoleService.saveInfo(ids,noids,userId);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
