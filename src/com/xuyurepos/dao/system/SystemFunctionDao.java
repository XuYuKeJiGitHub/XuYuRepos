package com.xuyurepos.dao.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemFunction;

public interface SystemFunctionDao {

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
	public SystemFunction find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemFunction systemLookUp);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemFunction systemLookUp);


}
