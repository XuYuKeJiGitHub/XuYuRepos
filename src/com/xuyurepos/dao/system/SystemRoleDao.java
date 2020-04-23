package com.xuyurepos.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemRole;

public interface SystemRoleDao {


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
	public SystemRole find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemRole systemRole);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemRole systemRole);
	
	/**
	 * 根据用户获取角色
	 * @param userId
	 * @return
	 */
	public List<SystemRole> getRoleById(@Param("userId")String userId);

}
