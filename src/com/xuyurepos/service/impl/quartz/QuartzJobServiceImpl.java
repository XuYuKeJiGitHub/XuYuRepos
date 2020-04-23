package com.xuyurepos.service.impl.quartz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuyurepos.dao.quartz.QuartzJobDao;
import com.xuyurepos.service.quartz.QuartzJobServer;
import com.xuyurepos.vo.common.UploadFileDto;
import com.xuyurepos.vo.quartzJob.QuartzJobDto;
@Service
//Transactional
public class QuartzJobServiceImpl implements QuartzJobServer {
	@Autowired
	QuartzJobDao quartzJobDao;
	
	@Override
	public QuartzJobDto getQuartzJob(String asyncType) {
		return quartzJobDao.getQuartzJob(asyncType);
	}
	@Override
	public void updateAsyncJobTask(QuartzJobDto quartzJobDto) {
		quartzJobDao.updateAsyncJobTask(quartzJobDto);
	}
	@Override
	public void saveAsyncJobTask(QuartzJobDto quartzJobDto) {
		quartzJobDao.saveAsyncJobTask(quartzJobDto);
	}
	@Override
	public void saveUploadFile(UploadFileDto uploadFileDto) {
		quartzJobDao.saveUploadFile(uploadFileDto);
	}
//	@Override
//	public void runBatchGprs(Map map) {
//		quartzJobDao.runBatchGprs(map);
//	}
	@Override
	public void runBatchPercent(Map map) {
		quartzJobDao.runBatchPercent(map);
	}
	@Override
	public void runBatchGprs01(Map map) {
		quartzJobDao.runBatchGprs01(map);
	}
	@Override
	public void runBatchGprs02(Map map) {
		quartzJobDao.runBatchGprs02(map);
	}
	@Override
	public void runBatchGprs03(Map map) {
		quartzJobDao.runBatchGprs03(map);
	}
	@Override
	public void runBatchGprs04(Map map) {
		quartzJobDao.runBatchGprs04(map);
	}
}
