package com.xuyurepos.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemRoleMenu;
import com.xuyurepos.vo.system.SystemRoleMenuVo;

public interface SystemRoleMenuDao {
	/**
	 * 分页查询
	 * @param pageModel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);
	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);
    
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public SystemRoleMenu find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemRoleMenu
	 */
	public void insert(SystemRoleMenu systemRoleMenu);
    
	/**
	 * 编辑数据
	 * @param systemRoleMenu
	 */
	public void update(SystemRoleMenu systemRoleMenu);
	
	public List<SystemRoleMenuVo> loadRoleMenuTree(@Param("roleId")String roleId);
	
	public List<SystemRoleMenuVo> loadRoleMenuChildrenTree(@Param("roleId")String roleId,@Param("fID")String fID);

}
