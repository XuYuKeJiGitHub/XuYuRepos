package com.xuyurepos.common.util.quartz;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuyurepos.service.intergration.batch.SynCardInfoBatchService;
import com.xuyurepos.service.intergration.batch.SynInfoBatchService;
import com.xuyurepos.service.quartz.QuartzJobServer;

public class BatchSynStateQuartzJob{
	
	static Logger logger=LoggerFactory.getLogger(BatchSynStateQuartzJob.class);
	

	 
	 @Autowired
	 private SynCardInfoBatchService  batchService; 
	 
	
	public void execute(){
        logger.info("同步卡状态开始：");
        batchService.sync();
        logger.info("同步卡状态结束：");

    }

}
