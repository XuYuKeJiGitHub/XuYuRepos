package com.xuyurepos.controller.aotorulemanager;

import java.util.HashMap;
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
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.autorule.AotoRuleManagerService;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.autorule.AutoRuleManagerDTO;
import com.xuyurepos.vo.logger.LoggerInfoDto;

@Controller
@RequestMapping(value = "/aotorule")
public class AotoRuleManagerControl {
	@Autowired
	private AotoRuleManagerService aotoRuleManagerService; 
    @Autowired
    LoggerInfoService loggerInfoService;
    @Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(AotoRuleManagerControl.class);
	/**
	 * 分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel, AutoRuleManagerDTO autoRuleManagerDTO, HttpServletRequest request) {
		pageModel.setQueryObj(autoRuleManagerDTO);
		aotoRuleManagerService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	/**
	 * 分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findListLog", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findListLog(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		pageModel.setQueryObj(autoRuleLogDTO);
		aotoRuleManagerService.findListLog(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	// 保存
	@RequestMapping(value = "/saveRule", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveRule(AutoRuleManagerDTO autoRuleManagerDTO, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(systemUser!=null) {
				autoRuleManagerDTO.setCreateUser(systemUser.getUserName());
			}
			String ruleId=loggerInfoService.getSequence("jobTaskSeq");
			if (autoRuleManagerDTO!=null) {
				String id=autoRuleManagerDTO.getId();
				LoggerInfoDto loggerInfo=new LoggerInfoDto();
				loggerInfo.setId(loggerInfoService.getSequence("seq"));
				loggerInfo.setModelId("defineRule");
				loggerInfo.setModelName("定义规则");
				loggerInfo.setOpreate("add");
				loggerInfo.setMark(ruleId);
				loggerInfo.setCreateUser(systemUser.getUserName());
				if(id!=null&&!id.equals("")) {
					aotoRuleManagerService.updateRule(autoRuleManagerDTO);
					loggerInfo.setOpreate("update");
				}else {
					autoRuleManagerDTO.setRuleId(ruleId);
				}
				aotoRuleManagerService.saveRule(autoRuleManagerDTO);
				loggerInfoService.saveLogger(loggerInfo);
				map.put("sucess", true);
			}else {
				map.put("sucess", false);
			}
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 更新规则数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/stopRule", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String stopRule(HttpServletRequest request) {
		try {
			String id=request.getParameter("id");
			Map<String,Object> map = new HashMap<String,Object>();
			aotoRuleManagerService.stopRule(id);
			map.put("sucess", true);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 编辑查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/editInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String editRuleInfo(HttpServletRequest request) {
		try {
			String id=request.getParameter("id");
			Map<String,Object> map = new HashMap<String,Object>();
			AutoRuleManagerDTO autoRuleManagerInfo=aotoRuleManagerService.find(id);
			map.put("sucess", true);
			map.put("model", autoRuleManagerInfo);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
