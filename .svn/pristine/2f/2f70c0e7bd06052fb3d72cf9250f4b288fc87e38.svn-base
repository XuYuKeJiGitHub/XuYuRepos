package com.xuyurepos.controller.cardmanager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.service.manager.XuyuContentCardInfoImportService;
import com.xuyurepos.service.system.XuyuDownService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;

@Controller
@RequestMapping(value = "/cardinfoimport")
public class XuyuContentCardInfoImportControl {
	
	@Autowired
	private XuyuContentCardInfoImportService xuyuContentCardInfoImportService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuContentCardInfoImportControl.class);
	
	@Autowired
	private XuyuDownService xuyuDownService; 
	/**
	  * 导出模板
	  * @return String
	  * @throws Exception
	*/
	@RequestMapping(value = "/exportModel", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String exportModel(HttpServletRequest request) throws Exception {
		Map<String, Object> map = null;
		try {
			log.info("exportModel() XuyuContentCardMgrControl  start");
			map = xuyuContentCardInfoImportService.exportModel();
			String result = JSONObject.toJSONString(map);
			log.info("exportModel() XuyuContentCardMgrControl  end");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出模板失败" + e.getMessage());
			throw e;
		}
	 }
	
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
			log.info("exportData() XuyuContentCardInfoImportControl  start");
			// 第一步，生成导出清单
			Map<String ,Object> mapSet=xuyuDownService.exportData("01");
			map = xuyuContentCardInfoImportService.syncExportData(xuyuContentCardMgrSelfVo,mapSet);
			String result = JSONObject.toJSONString(map);
			log.info("exportData() XuyuContentCardInfoImportControl  end");
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
			log.info("exportData() XuyuContentCardInfoImportControl  start");
			// 直接导出
			map = xuyuContentCardInfoImportService.exportData(xuyuContentCardMgrSelfVo);
			String result = JSONObject.toJSONString(map);
			log.info("exportData() XuyuContentCardInfoImportControl  end");
			return result;
		} catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	 }
	
	

}
