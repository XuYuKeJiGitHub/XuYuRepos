package com.xuyurepos.service.system;

import java.util.List;

import com.xuyurepos.entity.system.SystemAnnexe;
import com.xuyurepos.vo.system.SystemAnnexeVo;

public interface SystemAnnexeService {
	/**
	 * 根据条件查询附件数据
	 * @param annexe
	 * @return
	 */
	public List<SystemAnnexe> getList(SystemAnnexe annexe);
	
	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public SystemAnnexe saveInfo(SystemAnnexeVo systemAnnexeVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemAnnexeVo find(String id);
	

}
