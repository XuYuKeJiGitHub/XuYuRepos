package com.xuyurepos.service.system;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.SystemFunction;
import com.xuyurepos.vo.system.SystemFunctionVo;
/**
 * 后台模块
 * @author yangfei
 *
 */
public interface SystemFunctionService {

	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public SystemFunction saveInfo(SystemFunctionVo systemLookUpVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemFunctionVo find(String id);




}
