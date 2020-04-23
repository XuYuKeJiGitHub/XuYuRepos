package com.xuyurepos.dao.manager;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.XuyuMessageLog;
/**
 * 短信接口
 * @author yangfei
 *
 */
public interface XuyuMessageLogDao {
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
	public XuyuMessageLog find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(XuyuMessageLog xuyuMessageLog);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(XuyuMessageLog xuyuMessageLog);

}
