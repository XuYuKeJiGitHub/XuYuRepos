package com.xuyurepos.dao.autorule;

import java.util.List;
import java.util.Map;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.autorule.AutoRuleManagerDTO;

public interface AotoRuleManagerDao {

	void saveRule(AutoRuleManagerDTO autoRuleManagerDTO);

	void findList(PageModel pageModel);

	List selectRuleListWithPage(PageModel pageModel);

	Integer selectRuleCountWithPage(PageModel pageModel);

	AutoRuleManagerDTO getRuleInfo(String id);

	void updateRule(AutoRuleManagerDTO autoRuleManagerDTO);

	void stopRule(String id);

	List selectRuleLogWithPage(PageModel pageModel);

	Integer selectRuleLogCountWithPage(PageModel pageModel);

	List selectRuleYesWithPage(PageModel pageModel);

	Integer selectRuleYesCountWithPage(PageModel pageModel);

	List selectDeadLineWithPage(PageModel pageModel);

	Integer selectDeadLCountWithPage(PageModel pageModel);

	List selectGprsMonthWithPage(PageModel pageModel);

	Integer selectGprsMonthCountWithPage(PageModel pageModel);

	List selectGprsCompanyWithPage(PageModel pageModel);

	Integer selectGprsCompanyCountWithPage(PageModel pageModel);

	List selectComboDiffWithPage(PageModel pageModel);

	Integer selectComboDiffCountWithPage(PageModel pageModel);

	List<AutoRuleLogDTO> getRuleLogInfo(Map map);

	List<AutoRuleLogDTO> getcomboDiffInfo(Map map);

	List<AutoRuleLogDTO> getDeadLineInfo(Map map);

	List<AutoRuleLogDTO> getOwnerInfo(Map map);

	List<AutoRuleLogDTO> getGprsYesterdayInfo(Map map);

}
