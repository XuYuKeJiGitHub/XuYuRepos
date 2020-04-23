package com.xuyurepos.service.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemRole;
import com.xuyurepos.vo.system.SystemRoleVo;
/**
 * 系统角色
 * @author yangfei
 *
 */
public interface SystemRoleService {

	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);
	
	/**
	 * 获取当前用户角色
	 * @param id
	 * @return
	 */
	public List<SystemRole> getRoleById(String userId);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public SystemRole saveInfo(SystemRoleVo systemRoleVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemRoleVo find(String id);

}
