package com.xuyurepos.service.impl.manager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.dao.manager.XuyuMessageLogDao;
import com.xuyurepos.service.manager.XuyuMessageLogService;
/**
 * 短信接口实现类
 * @author yangfei
 *
 */
@Service
@Transactional
public class XuyuMessageLogServiceImpl implements XuyuMessageLogService{
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuMessageLogServiceImpl.class);

	@Autowired
	private XuyuMessageLogDao xuyuMessageLogDao;
	
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(xuyuMessageLogDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuMessageLogDao.selectUserCountWithPage(pageModel));
	}

}
