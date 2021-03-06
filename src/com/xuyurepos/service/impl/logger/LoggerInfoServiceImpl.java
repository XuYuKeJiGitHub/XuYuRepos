package com.xuyurepos.service.impl.logger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.dao.logger.LoggerInfoDao;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.vo.logger.LoggerInfoDto;
@Service
public class LoggerInfoServiceImpl implements LoggerInfoService {
Logger log=LoggerFactory.getInstance().getLogger(LoggerInfoServiceImpl.class);
	
	@Autowired
	private LoggerInfoDao loggerInfoDao;
    
	/**
	 * 分页查询数据列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(loggerInfoDao.selectLoggerListWithPage(pageModel));
	    pageModel.setTotal(loggerInfoDao.selectLoggerCountWithPage(pageModel));
	}

	@Override
	public void saveLogger(LoggerInfoDto loggerInfo) {
		loggerInfoDao.saveLogger(loggerInfo);
	}

	@Override
	public String getSequence(String seqName) {
//		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
//		String time=sf.format(new Date());
//		return time+loggerInfoDao.getSequence();
		String sequence=loggerInfoDao.getSequence(seqName);
		return sequence;
	}
}
