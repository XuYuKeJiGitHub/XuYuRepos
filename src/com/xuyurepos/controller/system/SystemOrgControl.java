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
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemOrg;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.system.SystemOrgVo;

@Controller
@RequestMapping(value = "/org")
public class SystemOrgControl {
	@Autowired
	private SystemOrgService systemOrgService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemOrgControl.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loadOrgTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadOrgTree(HttpServletRequest request){
		// 判断是否为总部
		List<SystemOrg> OTList =new ArrayList<SystemOrg>();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			SystemOrg systemOrg=systemOrgService.findById(systemUser.getOrgId());
			OTList.add(systemOrg);
		}else{
			OTList = systemOrgService.loadOrgTree();
		}
        List<Map> list=new ArrayList<Map>();
        List<Map> listChildren=new ArrayList<Map>();
        Map map=new HashMap<String, Object>();
        map.put("id", "0");
        map.put("text", "根目录");
        map.put("state", "closed");
        map.put("children", listChildren);
        list.add(map);
        for (SystemOrg orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getOrgName());
            map.put("state", "closed");
        	listChildren.add(map);
        }
        String result = JSONObject.toJSONString(listChildren);
        return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loadOrgChildrenTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadOrgChildrenTree(String FID){
        List<SystemOrg> OTList = systemOrgService.loadOrgChildrenTree(FID);
        List<Map> listChildren=new ArrayList<Map>();
        Map map=null;
        for (SystemOrg orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getOrgName());
            map.put("state", "closed");
        	listChildren.add(map);
        }
        String result = JSONObject.toJSONString(listChildren);
        return result;              
    }

	
	// 保存
	@RequestMapping(value="/save",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemOrgVo systemOrgVo,HttpServletRequest request) throws Exception{
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			// 判断是否为总部
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(!"1000".equals(systemUser.getOrgCode())){
				systemOrgVo.setIsDept(SystemConstants.STRING_NO);
			}
			SystemOrg systemOrg=systemOrgService.saveInfo(systemOrgVo);
			map.put("sucess", true);
			map.put("model", systemOrg);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取用户对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/find",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String find(HttpServletRequest request) throws Exception{
		try {
			String id=request.getParameter("id");
			Map<String,Object> map = new HashMap<String,Object>();
			SystemOrgVo systemUserVo=systemOrgService.find(id);
			map.put("sucess", true);
			map.put("model", systemUserVo);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 删除数据
	@RequestMapping(value="/delete",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String delete(HttpServletRequest request) throws Exception{
		String ids=request.getParameter("ids");
		systemOrgService.delete(ids);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sucess", true);
		String result=JSONObject.toJSONString(map);
		return result;
	}
	
	/**
	 * 分页查询数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/findList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel,SystemOrgVo systemOrg,HttpServletRequest request){
		try {
			// 判断是否为总部
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(!"1000".equals(systemUser.getOrgCode())){
				if(systemOrg.getUpOrgId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemOrg.getUpOrgId())){
					SystemOrgVo systemUserVo=systemOrgService.find(systemOrg.getUpOrgId());
					systemOrg.setUpOrgId(systemUserVo.getOrgId());
				}else{
					systemOrg.setUpOrgId(systemUser.getOrgCode());
				}
			}else{
				if(systemOrg.getUpOrgId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemOrg.getUpOrgId())){
					SystemOrgVo systemUserVo=systemOrgService.find(systemOrg.getUpOrgId());
					systemOrg.setUpOrgId(systemUserVo.getOrgId());
				}
			}
			pageModel.setQueryObj(systemOrg);
//			String page=request.getParameter("page");
//			String rows=request.getParameter("rows");
//			if(page!=null){
//				pageModel.setPageNumber(Integer.valueOf(page));
//			}
//			if(page!=null){
//				pageModel.setPageSize(Integer.valueOf(rows));
//			}
			systemOrgService.findList(pageModel);
			String result=JSONObject.toJSONString(pageModel);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
			

}
