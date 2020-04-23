package com.xuyurepos.service.operamanager;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuOwnerInfo;
import com.xuyurepos.vo.operamanager.XuyuOwnerInfoVo;

public interface XuyuOwnerInfoService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param xuyuOwnerInfoVo
	 */
	public XuyuOwnerInfo saveInfo(XuyuOwnerInfoVo xuyuOwnerInfoVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public XuyuOwnerInfoVo find(String id);

}
