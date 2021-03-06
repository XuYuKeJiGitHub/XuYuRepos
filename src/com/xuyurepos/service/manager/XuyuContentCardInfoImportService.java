package com.xuyurepos.service.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;

public interface XuyuContentCardInfoImportService {

	public Map<String, Object> exportModel();

	public void setMark(HttpServletRequest request)throws CustomException ;

	public Map<String, Object> syncExportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,Map<String ,Object> mapSet);

	public Map<String, Object> exportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo);

}
