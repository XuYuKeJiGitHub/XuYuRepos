package com.xuyurepos.dao.logger;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.logger.LoggerInfoDto;

public interface LoggerInfoDao {
	@SuppressWarnings("rawtypes")
	List selectLoggerListWithPage(PageModel pageModel);
	@SuppressWarnings("rawtypes")
	Integer selectLoggerCountWithPage(PageModel pageModel);
	
	void saveLogger(LoggerInfoDto loggerInfo);
	
	String getSequence(String seqName);
	
}
