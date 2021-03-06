package com.xuyurepos.service.impl.autorule;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.dao.autorule.AotoRuleManagerDao;
import com.xuyurepos.service.autorule.AotoRuleManagerService;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.autorule.AutoRuleManagerDTO;
@Service
public class AotoRuleManagerServiceImpl implements AotoRuleManagerService {
	Logger log=LoggerFactory.getInstance().getLogger(AotoRuleManagerServiceImpl.class);
	@Autowired
	private AotoRuleManagerDao aotoRuleManagerDao;
	@Override
	public void saveRule(AutoRuleManagerDTO autoRuleManagerDTO) {
		aotoRuleManagerDao.saveRule(autoRuleManagerDTO);
	}
	/**
	 * 分页查询数据列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectRuleListWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectRuleCountWithPage(pageModel));
	}
	@Override
	public AutoRuleManagerDTO find(String id) {
		return aotoRuleManagerDao.getRuleInfo(id);
	}
	@Override
	public void updateRule(AutoRuleManagerDTO autoRuleManagerDTO) {
		aotoRuleManagerDao.updateRule(autoRuleManagerDTO);
	}
	@Override
	public void stopRule(String id) {
		aotoRuleManagerDao.stopRule(id); 
	}
	@Override
	public void findListLog(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectRuleLogWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectRuleLogCountWithPage(pageModel));
	}
	@Override
	public void findGprsYesDList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectRuleYesWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectRuleYesCountWithPage(pageModel));
	}
	@Override
	public void findDeadLineList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectDeadLineWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectDeadLCountWithPage(pageModel));
	}
	@Override
	public void findGprsMonthList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectGprsMonthWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectGprsMonthCountWithPage(pageModel));
	}
	@Override
	public void findGprsCompanyList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectGprsCompanyWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectGprsCompanyCountWithPage(pageModel));
	}
	@Override
	public void findComboDiffList(PageModel pageModel) {
		pageModel.setRows(aotoRuleManagerDao.selectComboDiffWithPage(pageModel));
	    pageModel.setTotal(aotoRuleManagerDao.selectComboDiffCountWithPage(pageModel));
	}
	@Override
	public List<AutoRuleLogDTO> getRuleLogInfo(Map map) {
 		return aotoRuleManagerDao.getRuleLogInfo(map);
	}
	@Override
	public List<AutoRuleLogDTO> getcomboDiffInfo(Map map) {
		return aotoRuleManagerDao.getcomboDiffInfo(map);
	}
	@Override
	public List<AutoRuleLogDTO> getDeadLineInfo(Map map) {
		return aotoRuleManagerDao.getDeadLineInfo(map);
	}
	@Override
	public List<AutoRuleLogDTO> getOwnerInfo(Map map) {
		return aotoRuleManagerDao.getOwnerInfo(map);
	}
	@Override
	public List<AutoRuleLogDTO> getGprsYesterdayInfo(Map map) {
		return aotoRuleManagerDao.getGprsYesterdayInfo(map);
	}
}
