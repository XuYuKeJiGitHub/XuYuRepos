package com.xuyurepos.dao.operamanager;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.operamanager.XuyuPackages;

public interface XuyuPackagesDao {

	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);

	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);

	public void del(Integer id);

	public XuyuPackages find(Integer valueOf);

	public void insert(XuyuPackages xuyuPackages);

	public void update(XuyuPackages xuyuPackages);

	@SuppressWarnings("rawtypes")
	public List selectH5Page(PageModel pageModel);

	@SuppressWarnings("rawtypes")
	public Integer selectH5CountPage(PageModel pageModel);

}
