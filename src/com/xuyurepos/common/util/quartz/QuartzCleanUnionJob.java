package com.xuyurepos.common.util.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuyurepos.service.manager.XuyuContentCardInfoClearService;

/**
 * 处理预充值和清空数据
 * 专门处理联通的数据
 * @author yangfei
 *
 */
public class QuartzCleanUnionJob {

	
	static Logger logger=LoggerFactory.getLogger(QuartzCleanUnionJob.class);
	
	@Autowired
	private XuyuContentCardInfoClearService xuyuContentCardInfoClearService; 
	
	public void execute() {
		logger.info("联通清空到期数据和处理预充值信息");
		xuyuContentCardInfoClearService.clearUnion();
	    logger.info("本月批量结束：");
	}



}
