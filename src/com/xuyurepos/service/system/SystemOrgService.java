package com.xuyurepos.service.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemOrg;
import com.xuyurepos.vo.system.SystemOrgVo;


public interface SystemOrgService {
    // 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);
	
	public void delete(String ids);
	
	/**
	 * 保存数据
	 * @param systemUserVo
	 */
	public SystemOrg saveInfo(SystemOrgVo systemOrgVo);
	
	/**
	 * 查询机构信息
	 * @param id
	 * @return
	 */
	public SystemOrgVo find(String id);
	
	public SystemOrg findById(String id);

	public List<SystemOrg> loadOrgTree();

	public List<SystemOrg> loadOrgChildrenTree(String fID);

}
