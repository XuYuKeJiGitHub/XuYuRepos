package com.xuyurepos.common.util.quartz;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.entity.manager.GPRSDosageInfo;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.util.HttpClientUtil;

/**
 * 上报卡信息
 * @author zhangyanpeng
 *
 */
public class QuartzReportedCardInfoJob {
	static Logger logger=LoggerFactory.getLogger(QuartzJob.class);
	private static final String ReportedCardInfoUrl="https://admin.ibdnchina.com/api/iotcard/update";
	@Autowired
	private IccIdManagerService iccIdManagerService; 
	private String getMD5(String str) {
		String key="ibdn20";
		String signStr=str+"key="+key;//&key
		String sign=DigestUtils.md5Hex(signStr).toUpperCase();
		return sign;
	}
	public void execute() throws Exception {
		logger.info("定时批量上报GPRS用量信息");
		ArrayList<GPRSDosageInfo> cardInfoList = iccIdManagerService.findGPRSInfo();
		String dateStr = Long.toString(System.currentTimeMillis()/1000L);
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("agencyCode=xuyu");
		sbf.append("&timestamp="+dateStr+"&");
		
		String sign = getMD5(sbf.toString());
		
		HashMap<String, Object> reqMap = new HashMap<>();
		reqMap.put("agencyCode", "xuyu");
		reqMap.put("timestamp", dateStr);
		reqMap.put("sign", sign);
		reqMap.put("list", cardInfoList);
		
		String url = ReportedCardInfoUrl;
		String doPost = HttpClientUtil.doPost(url, reqMap);
		JSONObject parseObject = JSONObject.parseObject(doPost);
		Integer status = (Integer) parseObject.get("errorCode");
		logger.info("定时批量上报GPRS用量信息状态码"+status);
	}
}
