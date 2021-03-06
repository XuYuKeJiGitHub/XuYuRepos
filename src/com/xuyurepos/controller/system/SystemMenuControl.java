package com.xuyurepos.controller.system;

import java.util.ArrayList;
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
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemMenu;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemMenuService;
import com.xuyurepos.vo.system.SystemMenuVo;
/**
 * 系统菜单表
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/systemmenu")
public class SystemMenuControl {


	@Autowired
	private SystemMenuService systemMenuService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemMenuControl.class);
	
	/**
	 * 加载当前用户第一级菜单
	 * @return
	 */
	@RequestMapping(value="/loadUserFirstMenu",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadUserFirstMenu(HttpServletRequest request){
		try {
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
	        List<SystemMenuVo> OTList = systemMenuService.loadUserFirstMenu(systemUser);
	        String result = JSONObject.toJSONString(OTList);
	        return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
    }
	
	/**
	 * 记载用户子节点菜单
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loadUserChildrenMenu",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadUserChildrenMenu(HttpServletRequest request,String FID){
		try {
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
	        List<SystemMenuVo> OTList = systemMenuService.loadUserChildrenMenu(systemUser,FID);
	        Map<String,Object> mapUrl=systemUser.getMap();
	        if(mapUrl==null){
	        	mapUrl=new HashMap<String,Object>();
	        }
	        List<Map> listChildren=new ArrayList<Map>();
	        Map map=null;
	        Map attr=null;
	        for (SystemMenuVo orgTree : OTList) {
	        	map=new HashMap<String, Object>();
	        	attr=new HashMap<String, Object>();
	            map.put("id", orgTree.getId());
	            map.put("text", orgTree.getName());
	            map.put("state", "open");
	            attr.put("href", orgTree.getAction());
	            mapUrl.put(orgTree.getAction(), orgTree.getAction());
	            map.put("attributes", attr);
	        	listChildren.add(map);
	        }
	        systemUser.setMap(mapUrl);
	        request.getSession().setAttribute("systemUser",systemUser);
	        String result = JSONObject.toJSONString(listChildren);
	        return result;         
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loadMenuTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadOrgTree(){
        List<SystemMenuVo> OTList = systemMenuService.loadMenuTree();
        List<Map> list=new ArrayList<Map>();
        List<Map> listChildren=new ArrayList<Map>();
        Map map=new HashMap<String, Object>();
        map.put("id", "0");
        map.put("text", "全部目录");
        map.put("state", "closed");
        map.put("children", listChildren);
        list.add(map);
        for (SystemMenuVo orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getName());
            map.put("state", "closed");
        	listChildren.add(map);
        }
        String result = JSONObject.toJSONString(list);
        return result;
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="/loadMenuChildrenTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadOrgChildrenTree(String FID){
        List<SystemMenuVo> OTList = systemMenuService.loadMenuChildrenTree(FID);
        List<Map> listChildren=new ArrayList<Map>();
        Map map=null;
        for (SystemMenuVo orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getName());
            map.put("state", "open");
        	listChildren.add(map);
        } 
        String result = JSONObject.toJSONString(listChildren);
        return result;           
    }
	
	// 保存
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemMenuVo systemRoleVo, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SystemMenu item = systemMenuService.saveInfo(systemRoleVo);
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
			SystemMenuVo systemMenuVo = systemMenuService.find(id);
			map.put("sucess", true);
			map.put("model", systemMenuVo);
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
		systemMenuService.delete(ids);
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
	public String findList(PageModel pageModel, SystemMenuVo systemMenuVo, HttpServletRequest request) {
		pageModel.setQueryObj(systemMenuVo);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		systemMenuService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
}
