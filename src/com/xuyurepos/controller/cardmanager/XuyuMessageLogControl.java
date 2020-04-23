package com.xuyurepos.controller.cardmanager;

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
import com.xuyurepos.entity.manager.XuyuMessageLog;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.manager.XuyuMessageLogService;
/**
 * 短信查询数据
 * @author yangfei
 */
@Controller
@RequestMapping(value = "/xuyumessagelog")
public class XuyuMessageLogControl {
	
	@Autowired
	private XuyuMessageLogService xuyuMessageLogService; 
	@Autowired
	private BizException bizException; 
	
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuMessageLogControl.class);
	/**
	 * 分页查询数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/findList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel,XuyuMessageLog xuyuMessageLog,HttpServletRequest request){
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		xuyuMessageLog.setCreateUser(systemUser.getUserName());
		pageModel.setQueryObj(xuyuMessageLog);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(page!=null){
			pageModel.setPageNumber(Integer.valueOf(page));
		}
		if(page!=null){
			pageModel.setPageSize(Integer.valueOf(rows));
		}
		xuyuMessageLogService.findList(pageModel);
		String result=JSONObject.toJSONString(pageModel);
		return result;
	}

}
