package com.xuyurepos.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemMenu;
import com.xuyurepos.vo.system.SystemMenuVo;

public interface SystemMenuDao {
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
	public SystemMenu find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemMenu systemMenu);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemMenu systemMenu);
	
	public List<SystemMenuVo> loadMenuTree();
	
	public List<SystemMenuVo> loadMenuChildrenTree(String fID);
	
	/**
	 * 查询当前用户角色下的一级菜单
	 * @param userId
	 * @return
	 */
    public List<SystemMenuVo> loadUserMenuTree(@Param("userId")String userId);
	
    /**
     * 查询当前用户角色下的子菜单
     * @param userId
     * @param fID
     * @return
     */
	public List<SystemMenuVo> loadUserMenuChildrenTree(@Param("userId")String userId,@Param("fID")String fID);



}
