package com.xuyurepos.controller.cardmanager;

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
import com.xuyurepos.controller.system.SystemOrgControl;
import com.xuyurepos.entity.manager.XuyuCardType;
import com.xuyurepos.service.manager.XuyuCardTypeService;
import com.xuyurepos.vo.manager.XuyuCardTypeVo;
/**
 * 卡分类
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyucardtype")
public class XuyuCardTypeControl {

	@Autowired
	private  XuyuCardTypeService xuyuCardTypeService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemOrgControl.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loadOrgTree",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String loadOrgTree(){
		List<XuyuCardType> OTList = xuyuCardTypeService.loadOrgTree();
        List<Map> list=new ArrayList<Map>();
        List<Map> listChildren=new ArrayList<Map>();
        Map map=new HashMap<String, Object>();
        map.put("id", "0");
        map.put("text", "根目录");
        map.put("state", "closed");
        map.put("children", listChildren);
        list.add(map);
        for (XuyuCardType orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getCardName());
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
        List<XuyuCardType> OTList = xuyuCardTypeService.loadOrgChildrenTree(FID);
        List<Map> listChildren=new ArrayList<Map>();
        Map map=null;
        for (XuyuCardType orgTree : OTList) {
        	map=new HashMap<String, Object>();
            map.put("id", orgTree.getId());
            map.put("text", orgTree.getCardName());
            map.put("state", "closed");
        	listChildren.add(map);
        }
        String result = JSONObject.toJSONString(listChildren);
        return result;              
    }

	
	// 保存
	@RequestMapping(value="/save",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String save(XuyuCardTypeVo systemOrgVo,HttpServletRequest request) throws Exception{
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			if("".equals(systemOrgVo.getCardOrgId())){
				systemOrgVo.setCardOrgId("0");// 默认层级
			}
			XuyuCardType systemOrg=xuyuCardTypeService.saveInfo(systemOrgVo);
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
			XuyuCardTypeVo systemUserVo=xuyuCardTypeService.find(id);
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
		xuyuCardTypeService.delete(ids);
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
	public String findList(PageModel pageModel,XuyuCardTypeVo systemOrg,HttpServletRequest request){
		try {
			pageModel.setQueryObj(systemOrg);
			String page=request.getParameter("page");
			String rows=request.getParameter("rows");
			if(page!=null){
				pageModel.setPageNumber(Integer.valueOf(page));
			}
			if(page!=null){
				pageModel.setPageSize(Integer.valueOf(rows));
			}
			xuyuCardTypeService.findList(pageModel);
			String result=JSONObject.toJSONString(pageModel);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
			



}
