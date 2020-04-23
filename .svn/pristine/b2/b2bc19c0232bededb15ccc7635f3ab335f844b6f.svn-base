package com.xuyurepos.service.system;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.ComboBoxUtils;
import com.xuyurepos.entity.system.SystemLookUp;
import com.xuyurepos.vo.system.SystemLookUpVo;
/**
 * 系统数据字典类型
 * @author yangfei
 *
 */
public interface SystemLookUpService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public SystemLookUp saveInfo(SystemLookUpVo systemLookUpVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemLookUpVo find(String id);

	public List<ComboBoxUtils> findList();

}
