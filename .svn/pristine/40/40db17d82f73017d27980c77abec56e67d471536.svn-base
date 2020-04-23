package com.xuyurepos.service.autorule;

import java.util.List;
import java.util.Map;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.autorule.AutoRuleManagerDTO;

public interface AotoRuleManagerService {
	/**
	 * 保存自动化规则
	 * @param autoRuleManagerDTO
	 */
	void saveRule(AutoRuleManagerDTO autoRuleManagerDTO);

	void findList(PageModel pageModel);

	AutoRuleManagerDTO find(String id);

	void updateRule(AutoRuleManagerDTO autoRuleManagerDTO);

	void stopRule(String id);

	void findListLog(PageModel pageModel);

	void findGprsYesDList(PageModel pageModel);

	void findDeadLineList(PageModel pageModel);

	void findGprsMonthList(PageModel pageModel);

	void findGprsCompanyList(PageModel pageModel);

	void findComboDiffList(PageModel pageModel);

	List<AutoRuleLogDTO> getRuleLogInfo(Map map);

	List<AutoRuleLogDTO> getcomboDiffInfo(Map map);

	List<AutoRuleLogDTO> getDeadLineInfo(Map map);

	List<AutoRuleLogDTO> getOwnerInfo(Map map);

	List<AutoRuleLogDTO> getGprsYesterdayInfo(Map map);

}
