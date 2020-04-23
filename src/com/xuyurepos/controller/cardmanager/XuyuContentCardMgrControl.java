package com.xuyurepos.controller.cardmanager;

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
import com.xuyurepos.service.manager.XuyuContentCardMgrService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrVo;

/**
 * 卡管理
 * @author yangfei
 *
 */
@Controller
@RequestMapping(value = "/xuyucontentcardmgr")
public class XuyuContentCardMgrControl {
	
	@Autowired
	private XuyuContentCardMgrService xuyuContentCardMgrService; 
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuContentCardMgrControl.class);
	
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
			XuyuContentCardMgrVo xuyuContentCardMgrVo = xuyuContentCardMgrService.find(id);
			map.put("sucess", true);
			map.put("model", xuyuContentCardMgrVo);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
			map = xuyuContentCardMgrService.exportModel();
			String result = JSONObject.toJSONString(map);
			log.info("exportModel() XuyuContentCardMgrControl  end");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出模板失败" + e.getMessage());
			throw e;
		}
	 }

}
