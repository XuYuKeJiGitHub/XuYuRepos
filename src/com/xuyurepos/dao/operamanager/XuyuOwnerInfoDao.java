package com.xuyurepos.dao.operamanager;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuOwnerInfo;
/**
 * 群组相关dao
 * @author yangfei
 *
 */
public interface XuyuOwnerInfoDao {
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
	public XuyuOwnerInfo find(Integer id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param xuyuOwnerInfo
	 */
	public void insert(XuyuOwnerInfo xuyuOwnerInfo);
    
	/**
	 * 编辑数据
	 * @param xuyuOwnerInfo
	 */
	public void update(XuyuOwnerInfo xuyuOwnerInfo);

}
