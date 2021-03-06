package com.xuyurepos.dao.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.GPRSDosageInfo;
import com.xuyurepos.entity.system.SystemOrg;


public interface SystemOrgDao {
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
	public SystemOrg find(Integer id);
	
	public List<SystemOrg> getList(String upOrgId);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemOrg systemOrg);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemOrg systemOrg);
	
	
	public List<SystemOrg> loadOrgTree();
	public List<SystemOrg> loadOrgChildrenTree(@Param("FID") String FID);
	/**
	 * 获取当前机构最大ID
	 * @param upOrgId
	 * @param orgLevel
	 * @return
	 */
	public String getMaxOrgId(@Param("upOrgId")String upOrgId, @Param("orgLevel")Integer orgLevel,@Param("dept")String dept);
	/**
	 * 批量上报GPRS用量信息
	 * @return
	 */
	public ArrayList<GPRSDosageInfo> findAll();

}
