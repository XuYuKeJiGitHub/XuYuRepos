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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.service.system.XuyuDownService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;
import com.xuyurepos.vo.system.SystemOrgVo;
/**
 * 物联卡信息管理
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/iccidmanager")
public class IccIdManagerControl {
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(IccIdManagerControl.class);
	
	@Autowired
	private IccIdManagerService iccIdManagerService; 
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	@Autowired
	private XuyuDownService xuyuDownService; 
	
	/**
	 * 异步导出数据
	 * @param xuyuContentCardMgrSelfVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syncExportData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String syncExportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> map = null;
		try {
			log.info("exportData() IccIdManagerControl  start");
			// 第一步，生成导出清单 
			Map<String ,Object> mapSet=xuyuDownService.exportData("02");
			map = iccIdManagerService.syncExportData(xuyuContentCardMgrSelfVo,mapSet);
			String result = JSONObject.toJSONString(map);
			log.info("exportData() IccIdManagerControl  end");
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	 }
	
	/**
	 * 导出数据
	 * @param xuyuContentCardMgrSelfVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String exportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> map = null;
		try {
			log.info("exportData() IccIdManagerControl  start");
			map = iccIdManagerService.exportData(xuyuContentCardMgrSelfVo);
			String result = JSONObject.toJSONString(map);
			log.info("exportData() IccIdManagerControl  end");
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	 }
	
	
	// 删除数据
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String delete(HttpServletRequest request) throws Exception {
		log.info("delete() IccIdManagerControl  start");
		String ids = request.getParameter("ids");
		iccIdManagerService.delete(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sucess", true);
		String result = JSONObject.toJSONString(map);
		log.info("delete() IccIdManagerControl  end");
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
	public String findList(PageModel pageModel, XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,HttpServletRequest request) {
		try {
			// 转换
			if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
				xuyuContentCardMgrSelfVo.setAgencyQuery(systemOrgVo.getOrgId());
			}
			String page=request.getParameter("page");
			String rows=request.getParameter("rows");
			if(page!=null){
				pageModel.setPageNumber(Integer.valueOf(page));
				pageModel.setPageSize(Integer.valueOf(rows));
			}
			if(xuyuContentCardMgrSelfVo.getIsowner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIsowner())){
				if("y".equals(xuyuContentCardMgrSelfVo.getIsowner())){
					xuyuContentCardMgrSelfVo.setIsowner("  !(ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
				}
				if("n".equals(xuyuContentCardMgrSelfVo.getIsowner())){
					xuyuContentCardMgrSelfVo.setIsowner(" (ISNULL(OWNER) || LENGTH(trim(OWNER))<1)");
				}
			}
			pageModel.setQueryObj(xuyuContentCardMgrSelfVo);
			iccIdManagerService.findList(pageModel);
			JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
			String result = JSONObject.toJSONString(pageModel,SerializerFeature.WriteDateUseDateFormat);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 调整备注
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setMark", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String setMark(HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			iccIdManagerService.setMark(request);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 发送短信
	 * @param pageModel
	 * @param xuyuContentCardMgrSelfVo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/messageSend", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String messageSend(XuyuMessageLogVo xuyuMessageLogVo,HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			iccIdManagerService.messageSend(xuyuMessageLogVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 实时用户状态查询
	 * @param xuyuMessageLogVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userStatusQuery", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String userStatusQuery(XuyuMessageLogVo xuyuMessageLogVo,HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			iccIdManagerService.userStatusQuery(xuyuMessageLogVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 流量实时查询
	 * @param xuyuMessageLogVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gprsQuery", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String gprsQuery(XuyuMessageLogVo xuyuMessageLogVo,HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			iccIdManagerService.gprsQuery(xuyuMessageLogVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 在线状态查询
	 * @param xuyuMessageLogVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/changeCardState", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String changeCardState(XuyuMessageLogVo xuyuMessageLogVo,HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
//			iccIdManagerService.changeCardState(xuyuMessageLogVo.getAccessNums(),"");
			iccIdManagerService.gprsStatusQuery(xuyuMessageLogVo);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 用户批量停复机
	 * @param xuyuMessageLogVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/changeCardStateAll", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String changeCardStateAll(XuyuMessageLogVo xuyuMessageLogVo,HttpServletRequest request,HttpServletResponse response) {
		try {
			String bool = request.getParameter("bool");
			XuyuMessageLogVo xuyuMessageLogVoStatue=null;
			Map<String, Object> map = new HashMap<String, Object>();
			if(xuyuMessageLogVo.getAccessNums()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuMessageLogVo.getAccessNums())){
				String[] accessNumStr=xuyuMessageLogVo.getAccessNums().split(";");
				if(accessNumStr.length>0){
					for (int i = 0; i < accessNumStr.length; i++) {
					    iccIdManagerService.changeCardState(accessNumStr[i],bool);
					    xuyuMessageLogVoStatue=new XuyuMessageLogVo();
					    xuyuMessageLogVoStatue.setAccessNums(accessNumStr[i]);
					    iccIdManagerService.userStatusQuery(xuyuMessageLogVoStatue);
					}
				}
			}
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}
	
	/**
	 * 导入更新
	 * @return
	 */
	@RequestMapping(value = "/updateByFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateByFile(HttpServletRequest request,HttpServletResponse response){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map=iccIdManagerService.updateByFile();
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	
	}
	
	
	
	
	
	
	
}