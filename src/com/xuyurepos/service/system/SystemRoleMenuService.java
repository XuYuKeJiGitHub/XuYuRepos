package com.xuyurepos.service.system;

import java.util.List;
import java.util.Map;

public interface SystemRoleMenuService {
    /**
     * 加载角色菜单
     * @param roleId
     * @return
     */
	@SuppressWarnings("rawtypes")
	public List<Map> loadRoleMenuTree(String roleId);
	
	/**
	 * 加载角色子菜单
	 * @param roleId
	 * @param FID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> loadRoleMenuChildrenTree(String roleId,String FID);

	public void saveInfo(String ids,String noids,String roleId);

}
