package com.xuyurepos.service.manager;

import com.xuyurepos.common.page.PageModel;

/**
 * 短信接口
 * @author yangfei
 *
 */
public interface XuyuMessageLogService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

}
