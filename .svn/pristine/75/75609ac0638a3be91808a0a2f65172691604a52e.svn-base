package com.xuyurepos.controller.loggermanager;

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
import com.xuyurepos.controller.system.SystemUserControl;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.vo.logger.LoggerInfoDto;

@Controller
@RequestMapping(value = "/logger")
public class SystemLoggerInfo {
Logger logger=LoggerFactory.getInstance().getLogger(SystemUserControl.class);
	@Autowired
	private LoggerInfoService loggerInfoService; 	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemLoggerInfo.class);
	
	/**
	 * 分页查询数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loggerList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel, LoggerInfoDto loggerInfoDto, HttpServletRequest request) {
		pageModel.setQueryObj(loggerInfoDto);
		loggerInfoService.findList(pageModel);
		String result = JSONObject.toJSONString(pageModel);
		return result;
	}
	
	public String saveLogger(HttpServletRequest request) {
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			  if(systemUser!=null) {
				  LoggerInfoDto loggerInfo=new LoggerInfoDto();
					loggerInfo.setId(loggerInfoService.getSequence("seq"));
					loggerInfo.setModelId("logger");
					loggerInfo.setModelName("日志审计");
					loggerInfo.setOpreate("search");
					loggerInfo.setCreateUser(systemUser.getUserName());
					loggerInfoService.saveLogger(loggerInfo);
			  }
		return null;
	}
}
