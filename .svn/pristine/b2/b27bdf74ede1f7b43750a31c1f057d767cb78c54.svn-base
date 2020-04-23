package com.xuyurepos.service.system;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.vo.system.SystemUserVo;
/**
 * 系统管理用户模块
 * @author yangfei
 *
 */
public interface SystemUserService {
	//登陆
	public SystemUser login(SystemUser e)throws Exception  ;
    // 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);
	
	public void delete(String ids);
	
	/**
	 * 保存数据
	 * @param systemUserVo
	 */
	public SystemUser saveInfo(SystemUserVo systemUserVo)throws CustomException ;
	
	/**
	 * 查询用户信息
	 * @param id
	 * @return
	 */
	public SystemUserVo find(String id);
	/**
	 * 修改用户信息
	 * @param cname
	 * @param cname2
	 * @param new_password
	 */
	public void resetName(SystemUser systemUser, String cname2, String new_password);
}
