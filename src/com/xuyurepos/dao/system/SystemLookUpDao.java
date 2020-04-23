package com.xuyurepos.dao.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemLookUp;
/**
 * 数据字典类型dao类
 * @author yangfei
 */
public interface SystemLookUpDao {
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
	 * 查询全量数据
	 * @param fLookUpId
	 * @return
	 */
	public List<SystemLookUp> getList();
    
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public SystemLookUp find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemLookUp systemLookUp);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemLookUp systemLookUp);

}
