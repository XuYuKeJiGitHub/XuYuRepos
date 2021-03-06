package com.xuyurepos.controller.aotorulemanager;

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
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.autorule.AotoRuleManagerService;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.system.SystemOrgVo;
@Controller
@RequestMapping(value = "/findgprsyes")
public class AutoRuleGprsYesControl {
	@Autowired
	private AotoRuleManagerService aotoRuleManagerService; 
    @Autowired
    LoggerInfoService loggerInfoService;
    @Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(AotoRuleManagerControl.class);
	
	@Autowired
	private SystemOrgService systemOrgService;
	/**
	 * 昨日用量分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findGprsYesDList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findGprsYesDList(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		
		if(autoRuleLogDTO!=null&&(autoRuleLogDTO.getQueryDate()==null||autoRuleLogDTO.getQueryDate().equals(""))) {
			autoRuleLogDTO.setId("1");
		}
		pageModel.setQueryObj(autoRuleLogDTO);
		aotoRuleManagerService.findGprsYesDList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	
	/**
	 * 满期日期分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findDeadLineList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findDeadLineList(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		pageModel.setQueryObj(autoRuleLogDTO);
		aotoRuleManagerService.findDeadLineList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	
	/**
	 * 流量池分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findGprsMonthList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findGprsMonthList(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			if(autoRuleLogDTO.getAgency()==null||SystemConstants.STRINGEMPTY.equals(autoRuleLogDTO.getAgency())){
				autoRuleLogDTO.setAgency(systemUser.getOrgId());
			}
//			else{
//				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+autoRuleLogDTO.getAgency());
//				autoRuleLogDTO.setAgency(systemOrgVo.getOrgId());
//			}
		}
//		else{
//			if(autoRuleLogDTO.getAgency()==null||SystemConstants.STRINGEMPTY.equals(autoRuleLogDTO.getAgency())){
//			}else{
//				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+autoRuleLogDTO.getAgency());
//				autoRuleLogDTO.setAgency(systemOrgVo.getOrgId());
//			}
//		}
		pageModel.setQueryObj(autoRuleLogDTO);
		
		aotoRuleManagerService.findGprsMonthList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	/**
	 * 公司流量池分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findGprsCompanyList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findGprsCompanyList(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		pageModel.setQueryObj(autoRuleLogDTO);
		aotoRuleManagerService.findGprsCompanyList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	/**
	 * 月度套餐差异最大卡信息分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findComboDiffList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findComboDiffList(PageModel pageModel, AutoRuleLogDTO autoRuleLogDTO, HttpServletRequest request) {
		pageModel.setQueryObj(autoRuleLogDTO);
		aotoRuleManagerService.findComboDiffList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
}
