package com.xuyurepos.dao.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.XuyuCardType;

public interface XuyuCardTypeDao {
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
	public XuyuCardType find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(XuyuCardType systemOrg);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(XuyuCardType systemOrg);
	
	
	public List<XuyuCardType> loadOrgTree();
	public List<XuyuCardType> loadOrgChildrenTree(@Param("FID") String FID);
	
	public List<XuyuCardType> getList(String string);

}
