package com.xuyurepos.service.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemMenu;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.vo.system.SystemMenuVo;

public interface SystemMenuService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public SystemMenu saveInfo(SystemMenuVo systemMenuVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemMenuVo find(String id);
    
	/**
	 * 查询第一级的数据
	 * @return
	 */
	public List<SystemMenuVo> loadMenuTree();
    
	/**
	 * 查询子级数据
	 * @param fID
	 * @return
	 */
	public List<SystemMenuVo> loadMenuChildrenTree(String fID);
    
	/**
	 * 加载用户第一级菜单
	 * @return
	 */
	public List<SystemMenuVo> loadUserFirstMenu(SystemUser systemUser);
    
	/**
	 * 加载用户子节点菜单
	 * @return
	 */
	public List<SystemMenuVo> loadUserChildrenMenu(SystemUser systemUser,String FID);

}
