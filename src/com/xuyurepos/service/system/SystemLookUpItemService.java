package com.xuyurepos.service.system;
import java.util.List;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.ComboBoxUtils;
import com.xuyurepos.entity.system.SystemLookUpItem;
import com.xuyurepos.vo.system.SystemLookUpItemVo;
/**
 * 系统管理数据字典
 * @author yangfei
 *
 */
public interface SystemLookUpItemService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * @param systemUserVo
	 */
	public SystemLookUpItem saveInfo(SystemLookUpItemVo systemLookUpVo)throws CustomException;

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public SystemLookUpItemVo find(String id);
    /**
     * 动态获取下拉列表框数据
     * @param fLookUpId
     * @return
     */
	public List<ComboBoxUtils> findList(String fLookUpId);
}
