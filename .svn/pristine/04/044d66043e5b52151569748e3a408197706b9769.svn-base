package com.xuyurepos.service.system;

import java.util.Map;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.system.XuyuDown;
/**
 * 异步任务处理接口
 * @author yangfei
 *
 */
public interface XuyuDownService {

	// 分页查询
	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void delete(String ids);

	/**
	 * 保存数据
	 * 
	 * @param systemUserVo
	 */
	public XuyuDown saveInfo(XuyuDown xuyuDown);

	/**
	 * 查询信息
	 * 
	 * @param id
	 * @return
	 */
	public XuyuDown find(String id);

	public Map<String, Object> exportData(String downType);

}
