package com.xuyurepos.service.logger;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.logger.LoggerInfoDto;

public interface LoggerInfoService {
		// 分页查询
		@SuppressWarnings("rawtypes")
		public void findList(PageModel pageModel);
		
		//保存操作日志
		public void saveLogger(LoggerInfoDto loggerInfo);
		
		//获取sequence
		public String getSequence(String seqName);
}
