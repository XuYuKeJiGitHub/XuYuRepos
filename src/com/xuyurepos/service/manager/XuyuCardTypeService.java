package com.xuyurepos.service.manager;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.XuyuCardType;
import com.xuyurepos.vo.manager.XuyuCardTypeVo;

public interface XuyuCardTypeService {

    // 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);
	
	public void delete(String ids);
	
	/**
	 * 保存数据
	 * @param systemUserVo
	 */
	public XuyuCardType saveInfo(XuyuCardTypeVo xuyuCardTypeVo);
	
	/**
	 * 查询机构信息
	 * @param id
	 * @return
	 */
	public XuyuCardTypeVo find(String id);

	public List<XuyuCardType> loadOrgTree();

	public List<XuyuCardType> loadOrgChildrenTree(String fID);



}
