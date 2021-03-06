package com.xuyurepos.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemUserRole;
import com.xuyurepos.vo.system.SystemUserRoleVo;

public interface SystemUserRoleDao {
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
	public SystemUserRole find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemRoleMenu
	 */
	public void insert(SystemUserRole systemRoleMenu);
    
	/**
	 * 编辑数据
	 * @param systemRoleMenu
	 */
	public void update(SystemUserRole systemRoleMenu);
	
	public List<SystemUserRoleVo> loadUserRoleTree(@Param("userId")String userId,@Param("roleType")String roleType);

}
