package com.xuyurepos.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemLookUpItem;

public interface SystemLookUpItemDao {
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
	public SystemLookUpItem find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
	
	/**
	 * 查询全量数据
	 * @param fLookUpId
	 * @return
	 */
	public List<SystemLookUpItem> getList(@Param("fLookUpId")String  fLookUpId);
	/**
	 * 
	 * @param systemLookUpItem
	 */
	public void update(SystemLookUpItem systemLookUpItem);
	/**
	 * 
	 * @param systemLookUpItem
	 */
	public void insert(SystemLookUpItem systemLookUpItem);

}
