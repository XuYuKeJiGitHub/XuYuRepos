package com.xuyurepos.common.util.quartz;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuyurepos.service.manager.ImportTempService;
import com.xuyurepos.service.quartz.QuartzJobServer;
import com.xuyurepos.vo.quartzJob.QuartzJobDto;

/**
 * 更新卡状态
 * @author yangfei
 *
 */
public class QuartzUpdateJob {
	
	static Logger logger=LoggerFactory.getLogger(QuartzUpdateJob.class);
	@Autowired
	QuartzJobServer quartzJobServer;
	
	@Autowired
	private ImportTempService importTempService; 
	 
	public void execute() {
		logger.info("开始解析更新文件");
		QuartzJobDto quartzJobDto = quartzJobServer.getQuartzJob("cardUpdate");
		if (quartzJobDto != null) {
			logger.info("正在解析更新文件");
			try {
				String address = InetAddress.getLocalHost().getHostAddress();
				if (!address.equals(quartzJobDto.getIp())) {
					logger.info("本机ip:" + address + ";任务ip:" + quartzJobDto.getIp() + ";非本机的任务不解析");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info(quartzJobDto.getAsyncBatchNo() + quartzJobDto.getAsyncFlag());
			quartzJobDto.setAsyncFlag("02");// 待处理
			quartzJobServer.updateAsyncJobTask(quartzJobDto);
			logger.info(quartzJobDto.getAsyncBatchNo() + quartzJobDto.getAsyncFlag());
			// 业务处理 开始
			importTempService.anaLysisUpdateData(quartzJobDto.getAsyncBatchNo());
			// 业务处理 结束
			quartzJobDto.setAsyncFlag("03");// 处理完成
			quartzJobServer.updateAsyncJobTask(quartzJobDto);
		}else{
			logger.info("本次没有更新文件");
		}
	}

}
