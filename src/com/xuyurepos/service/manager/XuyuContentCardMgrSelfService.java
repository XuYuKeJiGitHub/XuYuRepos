package com.xuyurepos.service.manager;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuContentCardMgrVo;

/**
 * 卡出库管理
 * @author yangfei
 *
 */
public interface XuyuContentCardMgrSelfService {

	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public XuyuContentCardMgrVo find(String id);

	public void selfAll(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo)throws CustomException;

	public void setOwner(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo)throws CustomException;

}
