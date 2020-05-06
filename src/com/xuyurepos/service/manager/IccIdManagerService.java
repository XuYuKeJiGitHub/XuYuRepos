package com.xuyurepos.service.manager;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.GPRSDosageInfo;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;

public interface IccIdManagerService {

	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void messageSend(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException;

	public String userStatusQuery(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException ;

	public void gprsQuery(XuyuMessageLogVo xuyuMessageLogVo)throws CustomException;
	
	public String getGPRS(XuyuContentCardInfo contentCardInfo);

	public String changeCardState(String accessNums,String bool)throws CustomException;

	public void setMark(HttpServletRequest request)throws CustomException;

	public Map<String, Object> exportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo);

	public void delete(String ids);

	public Map<String ,Object> updateByFile()throws CustomException;

	public Map<String, Object> syncExportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,
			Map<String, Object> mapSet);

	public void gprsStatusQuery(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException ;

	public XuyuContentCardInfo findCardOwnerPlace(String accessNums);
	
	public ArrayList<GPRSDosageInfo> findGPRSInfo();

}
