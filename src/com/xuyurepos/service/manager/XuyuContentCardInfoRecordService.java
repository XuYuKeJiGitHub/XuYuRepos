package com.xuyurepos.service.manager;

import com.xuyurepos.entity.manager.XuyuContentCardInfoRecord;
import com.xuyurepos.vo.manager.XuyuContentCardInfoRecordVo;
/**
 * 物联网卡划卡记录表
 * @author yangfei
 *
 */
public interface XuyuContentCardInfoRecordService {
	
	
	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public XuyuContentCardInfoRecord saveInfo(XuyuContentCardInfoRecordVo xuyuContentCardInfoRecordVo);

}
