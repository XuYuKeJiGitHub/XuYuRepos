package com.xuyurepos.common.util.quartz;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuyurepos.service.quartz.QuartzJobServer;

public class QuartzAuToRuleJob {
	 @Autowired
	 QuartzJobServer quartzJobServer;
	 static Logger logger=LoggerFactory.getLogger(QuartzAuToRuleJob.class);
	  @SuppressWarnings("unused")
		public void execute(){
	        logger.info("this is my second scheduler!");
			try {
				String address = InetAddress.getLocalHost().getHostAddress();
				logger.info("当前服务器IP地址：" + address);
//					if(!address.equals("172.19.182.120")) {
//						return ;
//					}

			} catch (Exception e) {
				e.printStackTrace();
			}
	        Map map=new HashMap();
	        map.put("errCode", null);
	        map.put("errMsg", null);
	        quartzJobServer.runBatchGprs01(map);
	        quartzJobServer.runBatchGprs02(map);
	        quartzJobServer.runBatchGprs03(map);
	        //quartzJobServer.runBatchGprs04(map);
	        System.out.println("自动化规则执行结果"+map);
	  }
}
