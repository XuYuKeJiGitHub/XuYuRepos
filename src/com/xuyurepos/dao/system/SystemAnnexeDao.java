package com.xuyurepos.dao.system;

import java.util.List;

import com.xuyurepos.entity.system.SystemAnnexe;

public interface SystemAnnexeDao {
	
	public List<SystemAnnexe> getList(SystemAnnexe systemAnnexe);
	
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public SystemAnnexe find(String id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(String id);
    
	/**
	 * 添加数据
	 * @param systemUser
	 */
	public void insert(SystemAnnexe systemAnnexe);
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(SystemAnnexe systemAnnexe);
	


}
