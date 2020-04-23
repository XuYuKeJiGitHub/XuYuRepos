package com.xuyurepos.service.quartz;

import java.util.Map;

import com.xuyurepos.vo.common.UploadFileDto;
import com.xuyurepos.vo.quartzJob.QuartzJobDto;

public interface QuartzJobServer {
	QuartzJobDto getQuartzJob(String asyncType);
	
	void updateAsyncJobTask(QuartzJobDto quartzJobDto);
	
	void saveAsyncJobTask(QuartzJobDto quartzJobDto);

	void saveUploadFile(UploadFileDto uploadFileDto);

//	void runBatchGprs(Map map);

	void runBatchPercent(Map map);

	void runBatchGprs01(Map map);
	void runBatchGprs02(Map map);
	void runBatchGprs03(Map map);
	void runBatchGprs04(Map map);
}
