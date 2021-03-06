package com.xuyurepos.common.util.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuyurepos.service.manager.XuyuContentCardInfoClearService;

/**
 * 处理预充值和清空数据
 * @author yangfei
 *
 */
public class QuartzCleanJob {
	
	static Logger logger=LoggerFactory.getLogger(QuartzCleanJob.class);
	
	@Autowired
	private XuyuContentCardInfoClearService xuyuContentCardInfoClearService; 
	
	public void execute() {
		logger.info("清空到期数据和处理预充值信息");
		xuyuContentCardInfoClearService.clear();
	    logger.info("本月批量结束：");
	}


}
