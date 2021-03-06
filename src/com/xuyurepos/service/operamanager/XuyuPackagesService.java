package com.xuyurepos.service.operamanager;

import java.util.Map;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuPackages;
import com.xuyurepos.vo.operamanager.XuyuPackagesVo;

public interface XuyuPackagesService {
	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public XuyuPackages saveInfo(XuyuPackagesVo xuyuPackagesVo);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public XuyuPackagesVo find(String id);

	public void setState(String ids);

	public void findListH5(PageModel pageModel) throws CustomException;

	public void packageSend(String ids, String agency);

	public Map<String, Object> select(String accessNum);

}
