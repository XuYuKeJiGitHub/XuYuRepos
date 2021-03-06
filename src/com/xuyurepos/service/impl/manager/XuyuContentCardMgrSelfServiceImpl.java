package com.xuyurepos.service.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.dao.manager.XuyuContentCardInfoRecordDao;
import com.xuyurepos.dao.manager.XuyuContentCardMgrDao;
import com.xuyurepos.entity.comm.Params;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.entity.manager.XuyuContentCardInfoRecord;
import com.xuyurepos.entity.manager.XuyuContentCardMgr;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.intergration.batch.SynInfoBatchService;
import com.xuyurepos.service.manager.XuyuContentCardMgrSelfService;
import com.xuyurepos.service.operamanager.XuyuOwnerInfoService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuContentCardMgrVo;
import com.xuyurepos.vo.operamanager.XuyuOwnerInfoVo;
import com.xuyurepos.vo.system.SystemOrgVo;
@Transactional
@Service
public class XuyuContentCardMgrSelfServiceImpl implements XuyuContentCardMgrSelfService{
	
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuContentCardMgrSelfServiceImpl.class);

	@Autowired
	private XuyuContentCardMgrDao xuyuContentCardMgrDao;
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	@Autowired
	private XuyuContentCardInfoRecordDao xuyuContentCardInfoRecordDao;
	@Autowired
	private SystemOrgService systemOrgService;
	@Autowired
	private SynInfoBatchService synInfoBatchService;
	@Autowired
	private CommonMapper commonMapper;
	@Autowired
	private XuyuOwnerInfoService xuyuOwnerInfoService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findList(PageModel pageModel) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		String firstQuery=request.getParameter("firstQuery");
		String orgId=systemUser.getOrgId();
		// 查找机构信息
		SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
		if(systemOrgVo.getOrgLevel().equals("1")){
			if(SystemConstants.STRING_YES.equals(firstQuery)){
				// 总部用户不需要划卡
				pageModel.setRows(xuyuContentCardMgrDao.selectListWithPage(pageModel));
			    pageModel.setTotal(xuyuContentCardMgrDao.selectCountWithPage(pageModel));
			    logger.info("总部用户查询");
			}else{
				// 总部用户开始划卡
				logger.info("总部用户查询");
				XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo=(XuyuContentCardMgrSelfVo)pageModel.getQueryObj();
				String agency=systemOrgVo.getOrgId();
				logger.info("agency:"+agency);
				xuyuContentCardMgrSelfVo.setAgency(agency);
				pageModel.setRows(xuyuContentCardInfoDao.selectUserListWithPage(pageModel));
			    pageModel.setTotal(xuyuContentCardInfoDao.selectUserCountWithPage(pageModel));
			}
			
		}else{
			// 代理商用户开始划卡
			logger.info("代理商划卡查询");
			XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo=(XuyuContentCardMgrSelfVo)pageModel.getQueryObj();
			String agency=systemOrgVo.getOrgId();
			logger.info("agency:"+agency);
			xuyuContentCardMgrSelfVo.setAgency(agency);
			pageModel.setRows(xuyuContentCardInfoDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(xuyuContentCardInfoDao.selectUserCountWithPage(pageModel));
		}
		
	}

	@Override
	public XuyuContentCardMgrVo find(String id) {
		XuyuContentCardMgrVo xuyuContentCardMgrVo=new XuyuContentCardMgrVo();
		if(!StringUtil.isEmpty(id)){
			XuyuContentCardMgr xuyuContentCardMgr=xuyuContentCardMgrDao.find(id);
			BeanUtils.copyProperties(xuyuContentCardMgr, xuyuContentCardMgrVo);
			xuyuContentCardMgrVo.setId(id);
		}
		return xuyuContentCardMgrVo;
	}
    
	/**
	 * 批量出库
	 * @throws CustomException 
	 */
	@Override
	public synchronized void selfAll(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException {
		// 先判断是否为卡段处理
		if(xuyuContentCardMgrVo!=null){
			if(SystemConstants.STRING_YES.equals(xuyuContentCardMgrVo.getYesNo())){
				useAccessNum(xuyuContentCardMgrVo);
			}else{
				noUseAccessNum(xuyuContentCardMgrVo);
			}
		}
	}
	
	/**
	 * 使用卡段出库
	 * @param xuyuContentCardMgrVo
	 * @throws CustomException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized void useAccessNum(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		String orgId=systemUser.getOrgId();
		// 查找机构信息
		SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
		String firstQuery=request.getParameter("firstQuery");
		logger.info("firstQuery:"+firstQuery);
		if(systemOrgVo.getOrgLevel().equals("1")&&SystemConstants.STRING_YES.equals(firstQuery)){
			// 校验出库卡段是否超过10W
			// 首先判断类型
			int result=0;
			if("01".equals(xuyuContentCardMgrVo.getNumType())){
				result=xuyuContentCardMgrDao.findCount( xuyuContentCardMgrVo.getAccessNumStart(), xuyuContentCardMgrVo.getAccessNumEnd());
			}else{
				result=xuyuContentCardMgrDao.findCountIccId( xuyuContentCardMgrVo.getAccessNumStart(), xuyuContentCardMgrVo.getAccessNumEnd());
			}
			if(result>100000){
				throw new CustomException("目前系统支持的出库数据一次最多为100000");
			}else{
				try {
					// 类型判定是否为总部出库
					XuyuContentCardMgr xuyuContentCardMgr=new XuyuContentCardMgr();
					xuyuContentCardMgr.setManagerFlag(SystemConstants.STRING_YES);
					xuyuContentCardMgr.setAgency(xuyuContentCardMgrVo.getAgency());
					xuyuContentCardMgr.setAgencyName(xuyuContentCardMgrVo.getAgencyName());
					xuyuContentCardMgr.setEstablishDate(new Date());
					HashMap map=new HashMap();
					map.put("xuyuContentCardMgr", xuyuContentCardMgr);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardMgrDao.updateInfo(map);
					// 插入info表
					XuyuContentCardInfo xuyuContentCardInfo=new XuyuContentCardInfo();
					BeanUtils.copyProperties(xuyuContentCardMgr, xuyuContentCardInfo);
					map=new HashMap();
					map.put("xuyuContentCardInfo", xuyuContentCardInfo);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoDao.insertInfo(map);
					// 插入明细表
					XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
					xuyuContentCardInfoRecord.setPrice(BigDecimal.valueOf(Double.valueOf(xuyuContentCardMgrVo.getUnitCost())));
					BeanUtils.copyProperties(xuyuContentCardMgr, xuyuContentCardInfoRecord);
					map=new HashMap();
					map.put("xuyuContentCardInfoRecord", xuyuContentCardInfoRecord);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoRecordDao.insertInfo(map);
					
					map=new HashMap();
					map.put("xuyuContentCardMgr", xuyuContentCardMgr);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardMgrDao.updateInfoManager(map);
					
					// 调用批量
//					synInfoBatchService.setMobileStartFlag("1");
				} catch (DuplicateKeyException e) {
					throw new CustomException("卡已经出库，不能重复出库");
				}
			}
		}else{
			// 首先判断所有卡是否都属于本代理商
			HashMap map=new HashMap();
			map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
			map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
			map.put("agency", orgId);
			int j=xuyuContentCardInfoDao.checkAgency(map);
			if(j==0){
				// 校验出库卡段是否超过10W
				// 首先判断类型
				int result=xuyuContentCardInfoDao.findCount( xuyuContentCardMgrVo.getAccessNumStart(), xuyuContentCardMgrVo.getAccessNumEnd(),xuyuContentCardMgrVo.getNumType());
				if(result>100000){
					throw new CustomException("目前系统支持的出库数据一次最多为100000");
				}else{
					// 下级代理商出库
					logger.info("下级代理商出库");
					// 修改当前卡段区间的info的agency
					XuyuContentCardInfo xuyuContentCardInfo=new XuyuContentCardInfo();
					xuyuContentCardInfo.setAgency(xuyuContentCardMgrVo.getAgency());
					xuyuContentCardInfo.setAgencyName(xuyuContentCardMgrVo.getAgencyName());
					map=new HashMap();
					map.put("xuyuContentCardInfo", xuyuContentCardInfo);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoDao.updateInfo(map);
					// 插入明细表
					XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
					xuyuContentCardInfoRecord.setPrice(BigDecimal.valueOf(Double.valueOf(xuyuContentCardMgrVo.getUnitCost())));
					BeanUtils.copyProperties(xuyuContentCardInfo, xuyuContentCardInfoRecord);
					map=new HashMap();
					map.put("xuyuContentCardInfoRecord", xuyuContentCardInfoRecord);
					map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
					map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoRecordDao.insertInfo(map);
				
				}
			}else{
				if(systemOrgVo.getOrgLevel().equals("1")){
					// 校验出库卡段是否超过10W
					// 首先判断类型
					int result=xuyuContentCardInfoDao.findCount( xuyuContentCardMgrVo.getAccessNumStart(), xuyuContentCardMgrVo.getAccessNumEnd(),xuyuContentCardMgrVo.getNumType());
					if(result>100000){
						throw new CustomException("目前系统支持的出库数据一次最多为100000");
					}else{
						// 下级代理商出库
						logger.info("下级代理商出库");
						// 修改当前卡段区间的info的agency
						XuyuContentCardInfo xuyuContentCardInfo=new XuyuContentCardInfo();
						xuyuContentCardInfo.setAgency(xuyuContentCardMgrVo.getAgency());
						xuyuContentCardInfo.setAgencyName(xuyuContentCardMgrVo.getAgencyName());
						map=new HashMap();
						map.put("xuyuContentCardInfo", xuyuContentCardInfo);
						map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
						map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
						map.put("numType", xuyuContentCardMgrVo.getNumType());
						xuyuContentCardInfoDao.updateInfo(map);
						// 插入明细表
						XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
						xuyuContentCardInfoRecord.setPrice(BigDecimal.valueOf(Double.valueOf(xuyuContentCardMgrVo.getUnitCost())));
						BeanUtils.copyProperties(xuyuContentCardInfo, xuyuContentCardInfoRecord);
						map=new HashMap();
						map.put("xuyuContentCardInfoRecord", xuyuContentCardInfoRecord);
						map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
						map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
						map.put("numType", xuyuContentCardMgrVo.getNumType());
						xuyuContentCardInfoRecordDao.insertInfo(map);
					
					}
				}else{
					throw new CustomException("当前卡段中的卡存在不属于本代理商的，不能出库");
				}
				
			}
		}
		
	}
	/**
	 * 不使用卡段出库
	 * @throws CustomException 
	 */
	private synchronized void noUseAccessNum(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException{
		// 类型判定是否为总部出库
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		String orgId=systemUser.getOrgId();
		// 查找机构信息
		SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
		String firstQuery=request.getParameter("firstQuery");
		logger.info("firstQuery:"+firstQuery);
		if(systemOrgVo.getOrgLevel().equals("1")&&SystemConstants.STRING_YES.equals(firstQuery)){
			String accessNums=xuyuContentCardMgrVo.getAccessNums();
			if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
				String[] idsStr=null;
				if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
					accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
					idsStr=accessNums.split(SystemConstants.STRING_SENT);
				}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
					idsStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
				}else{
					idsStr=accessNums.split(SystemConstants.STRING_ENTER);
				}
				
			    List<Map<String, Object>> bacthInsertMap=null;
				Map<String, Object> valuesMap=null;  
				StringBuilder sb=new StringBuilder();
				sb.append(" delete from  MSG_TEMP ");
				commonMapper.executeAction(sb.toString());
				// 首先循环插入临时表
				for (int i = 0; i < idsStr.length; i++) {
						if(i==0){
							bacthInsertMap=new ArrayList<Map<String, Object>>();
							valuesMap=new HashMap<String, Object>();
							valuesMap.put("values", idsStr[i]);
							bacthInsertMap.add(valuesMap);
						}else{
							valuesMap=new HashMap<String, Object>();
							valuesMap.put("values", idsStr[i]);
							bacthInsertMap.add(valuesMap);
						}
						if(i!=0&&i%100==0){
							Params params=new Params();
							Map<String, Object> insertMap=new HashMap<String, Object>();
							insertMap.put("ACCESS_NUM", "ACCESS_NUM");
							params.setTables("MSG_TEMP");
							params.setInsertMap(insertMap);
							params.setBacthInsertMap(bacthInsertMap);;
							commonMapper.batchAdd(params);
							bacthInsertMap=new ArrayList<Map<String, Object>>();
						}
				}
				
				// 看是否处理好了
				if(bacthInsertMap!=null&&bacthInsertMap.size()>0){
					Params params=new Params();
					Map<String, Object> insertMap=new HashMap<String, Object>();
					insertMap.put("ACCESS_NUM", "ACCESS_NUM");
					params.setTables("MSG_TEMP");
					params.setInsertMap(insertMap);
					params.setBacthInsertMap(bacthInsertMap);;
					commonMapper.batchAdd(params);
					bacthInsertMap=new ArrayList<Map<String, Object>>();
				}
				// 求一下个数
				sb=new StringBuilder();
				sb.append(" select count(1) as a  from MSG_TEMP a");
				long result=commonMapper.findCount(sb.toString());
				if(result>5000){
					throw new CustomException("目前系统支持输入出库一次最多为5000");
				}
				
				// 判断数据类型
				xuyuContentCardMgrVo.setNumType("01");
				// 查询数据类型
				sb=new StringBuilder();
				sb.append(" select count(1) as a from  MSG_TEMP a1 ");
				sb.append(" inner join XUYU_CONTENT_CARD_MGR t1 on  a1.ACCESS_NUM=t1.ICCID ");
				long r=commonMapper.findCount(sb.toString());
				if(r>0){
					xuyuContentCardMgrVo.setNumType("02");
				}
				
			    // 处理导入表的数据
				if("01".equals(xuyuContentCardMgrVo.getNumType())){
					sb=new StringBuilder();
					sb.append(" update XUYU_CONTENT_CARD_MGR t1,MSG_TEMP a1 ");
					sb.append(" set t1.AGENCY='"+xuyuContentCardMgrVo.getAgency()+"', t1.AGENCY_NAME='"+xuyuContentCardMgrVo.getAgencyName()+"', ");
					sb.append(" t1.MANAGER_FLAG='"+SystemConstants.STRING_YES+"',t1.ESTABLISH_DATE=now() ");
					sb.append(" where a1.ACCESS_NUM=t1.ACCESS_NUM ");
					commonMapper.executeAction(sb.toString());
					
					// 插入info
					HashMap map=new HashMap();
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoDao.insertInfoSelF(map);
					// 插入明细表
					map=new HashMap();
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					map.put("unitcost", xuyuContentCardMgrVo.getUnitCost());
					xuyuContentCardInfoRecordDao.insertInfoSel(map);
				}else{
					sb=new StringBuilder();
					sb.append(" update  XUYU_CONTENT_CARD_MGR t1,MSG_TEMP a1");
					sb.append(" set t1.AGENCY='"+xuyuContentCardMgrVo.getAgency()+"', t1.AGENCY_NAME='"+xuyuContentCardMgrVo.getAgencyName()+"', ");
					sb.append(" t1.MANAGER_FLAG='"+SystemConstants.STRING_YES+"',t1.ESTABLISH_DATE=now() ");
					sb.append(" where a1.ACCESS_NUM=t1.ICCID  ");
					commonMapper.executeAction(sb.toString());
					
					// 插入info
					HashMap map=new HashMap();
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					xuyuContentCardInfoDao.insertInfoSelF(map);
					// 插入明细表
					map=new HashMap();
					map.put("numType", xuyuContentCardMgrVo.getNumType());
					map.put("unitcost", xuyuContentCardMgrVo.getUnitCost());
					xuyuContentCardInfoRecordDao.insertInfoSel(map);
				}
				// 调用批量
//				synInfoBatchService.setMobileStartFlag("1");
			}
		}else{
			// 下级代理商出库
			String accessNums=xuyuContentCardMgrVo.getAccessNums();
			String[] accessNumStr=null;
			if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
				accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
				accessNumStr=accessNums.split(SystemConstants.STRING_SENT);
			}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
				accessNumStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
			}else{
				accessNumStr=accessNums.split(SystemConstants.STRING_ENTER);
			}
		    List<Map<String, Object>> bacthInsertMap=null;
			Map<String, Object> valuesMap=null;  
			StringBuilder sb=new StringBuilder();
			sb.append(" delete from  MSG_TEMP ");
			commonMapper.executeAction(sb.toString());
			
			if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
				for (int i = 0; i < accessNumStr.length; i++) {
					if(i==0){
						bacthInsertMap=new ArrayList<Map<String, Object>>();
						valuesMap=new HashMap<String, Object>();
						valuesMap.put("values", accessNumStr[i]);
						bacthInsertMap.add(valuesMap);
					}else{
						valuesMap=new HashMap<String, Object>();
						valuesMap.put("values", accessNumStr[i]);
						bacthInsertMap.add(valuesMap);
					}
					if(i!=0&&i%100==0){
						Params params=new Params();
						Map<String, Object> insertMap=new HashMap<String, Object>();
						insertMap.put("ACCESS_NUM", "ACCESS_NUM");
						params.setTables("MSG_TEMP");
						params.setInsertMap(insertMap);
						params.setBacthInsertMap(bacthInsertMap);;
						commonMapper.batchAdd(params);
						bacthInsertMap=new ArrayList<Map<String, Object>>();
					}
				}
				
				// 看是否处理好了
				if(bacthInsertMap!=null&&bacthInsertMap.size()>0){
					Params params=new Params();
					Map<String, Object> insertMap=new HashMap<String, Object>();
					insertMap.put("ACCESS_NUM", "ACCESS_NUM");
					params.setTables("MSG_TEMP");
					params.setInsertMap(insertMap);
					params.setBacthInsertMap(bacthInsertMap);;
					commonMapper.batchAdd(params);
					bacthInsertMap=new ArrayList<Map<String, Object>>();
				}
				
				// 求一下个数
				sb=new StringBuilder();
				sb.append(" select count(1) as a  from MSG_TEMP a");
				long result=commonMapper.findCount(sb.toString());
				if(result>5000){
					throw new CustomException("目前系统支持输入出库一次最多为5000");
				}
				
				// 判断数据类型
				xuyuContentCardMgrVo.setNumType("01");
				// 查询数据类型
				sb=new StringBuilder();
				sb.append(" select count(1) as a from   MSG_TEMP a1  ");
				sb.append(" inner join XUYU_CONTENT_CARD_MGR t1 on  a1.ACCESS_NUM=t1.ICCID  ");
				long r=commonMapper.findCount(sb.toString());
				if(r>0){
					xuyuContentCardMgrVo.setNumType("02");
				}
				// 处理二次出库，只需要调整归属机构
				if("01".equals(xuyuContentCardMgrVo.getNumType())){
					sb=new StringBuilder();
					sb.append(" update  XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 ");
					sb.append(" set t1.AGENCY='"+xuyuContentCardMgrVo.getAgency()+"', t1.AGENCY_NAME='"+xuyuContentCardMgrVo.getAgencyName()+"' ");
					sb.append(" where a1.ACCESS_NUM=t1.ACCESS_NUM ");
					commonMapper.executeAction(sb.toString());
				}else{
					sb=new StringBuilder();
					sb.append(" update  XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 ");
					sb.append(" set t1.AGENCY='"+xuyuContentCardMgrVo.getAgency()+"', t1.AGENCY_NAME='"+xuyuContentCardMgrVo.getAgencyName()+"' ");
					sb.append(" where a1.ACCESS_NUM=t1.ICCID ");
					commonMapper.executeAction(sb.toString());
				}
				// 插入明细表
				HashMap map=new HashMap();
				map.put("numType", xuyuContentCardMgrVo.getNumType());
				map.put("unitcost", xuyuContentCardMgrVo.getUnitCost());
				
				xuyuContentCardInfoRecordDao.insertInfoSel(map);
			}
			
		
		}
		
	}
    
	/**
	 * 划卡
	 * @throws CustomException 
	 */
	@Override
	public synchronized void setOwner(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException {
		// 先判断是否为卡段处理
		if(xuyuContentCardMgrVo!=null){
			if(SystemConstants.STRING_YES.equals(xuyuContentCardMgrVo.getYesNo())){
				setOwnerUse(xuyuContentCardMgrVo);
			}else{
				setOwnerNoUse(xuyuContentCardMgrVo);
			}
		}
	}
    
	/**
	 * 不通过卡段划卡
	 * @param xuyuContentCardMgrVo
	 * @throws CustomException 
	 */
	private synchronized void setOwnerNoUse(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException {
		String accessNums=xuyuContentCardMgrVo.getAccessNums();
		// 计算没有沉默期和测试的日期运算
		String silentType=xuyuContentCardMgrVo.getSilentType();
		String haveTest=xuyuContentCardMgrVo.getHaveTest();
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			String[] accessNumStr=null;
			if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
				accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
				accessNumStr=accessNums.split(SystemConstants.STRING_SENT);
			}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
				accessNumStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
			}else{
				accessNumStr=accessNums.split(SystemConstants.STRING_ENTER);
			}
			List<Map<String, Object>> bacthInsertMap=null;
			Map<String, Object> valuesMap=null;  
			StringBuilder sb=new StringBuilder();
			sb.append(" delete from  MSG_TEMP ");
			commonMapper.executeAction(sb.toString());
			
			for (int i = 0; i < accessNumStr.length; i++) {
				if(i==0){
					bacthInsertMap=new ArrayList<Map<String, Object>>();
					valuesMap=new HashMap<String, Object>();
					valuesMap.put("values", accessNumStr[i]);
					bacthInsertMap.add(valuesMap);
				}else{
					valuesMap=new HashMap<String, Object>();
					valuesMap.put("values", accessNumStr[i]);
					bacthInsertMap.add(valuesMap);
				}
				if(i!=0&&i%100==0){
					Params params=new Params();
					Map<String, Object> insertMap=new HashMap<String, Object>();
					insertMap.put("ACCESS_NUM", "ACCESS_NUM");
					params.setTables("MSG_TEMP");
					params.setInsertMap(insertMap);
					params.setBacthInsertMap(bacthInsertMap);;
					commonMapper.batchAdd(params);
					bacthInsertMap=new ArrayList<Map<String, Object>>();
				}
			}
			
			// 看是否处理好了
			if(bacthInsertMap!=null&&bacthInsertMap.size()>0){
				Params params=new Params();
				Map<String, Object> insertMap=new HashMap<String, Object>();
				insertMap.put("ACCESS_NUM", "ACCESS_NUM");
				params.setTables("MSG_TEMP");
				params.setInsertMap(insertMap);
				params.setBacthInsertMap(bacthInsertMap);;
				commonMapper.batchAdd(params);
				bacthInsertMap=new ArrayList<Map<String, Object>>();
			}
			
			// 求一下个数
			sb=new StringBuilder();
			sb.append(" select count(1) as a  from MSG_TEMP a");
			long result=commonMapper.findCount(sb.toString());
			if(result>5000){
				throw new CustomException("目前系统支持输入划卡一次最多为5000");
			}
			// 判断数据类型
			xuyuContentCardMgrVo.setNumType("01");
			// 查询数据类型
			sb=new StringBuilder();
			sb.append(" select count(1) as a from MSG_TEMP a1 inner join    XUYU_CONTENT_CARD_INFO t1 ");
			sb.append(" on a1.ACCESS_NUM=t1.ICCID ");
			long r=commonMapper.findCount(sb.toString());
			if(r>0){
				xuyuContentCardMgrVo.setNumType("02");
			}
			// 首先判断所有卡是否都属于本代理商
			HashMap map=new HashMap();
		    // 修改info表
		    XuyuContentCardInfo xuyuContentCardInfo=new XuyuContentCardInfo();
			// 判定是否为群组划卡
			if(SystemConstants.STRING_YES.equals(xuyuContentCardMgrVo.getGroupYesNo())){
				xuyuContentCardInfo.setMgrOwnFlag(SystemConstants.STRING_YES);
				xuyuContentCardInfo.setOwner(xuyuContentCardMgrVo.getOwner());
				// 查询群组信息
				XuyuOwnerInfoVo xuyuOwnerInfoVo=xuyuOwnerInfoService.find(xuyuContentCardMgrVo.getOwner());
				xuyuContentCardInfo.setComboType(xuyuOwnerInfoVo.getComboType());
				xuyuContentCardInfo.setComnoName(xuyuOwnerInfoVo.getComboName());
				xuyuContentCardInfo.setPaymentType(xuyuOwnerInfoVo.getPayType());
				xuyuContentCardInfo.setTestType(xuyuOwnerInfoVo.getHaveTest());
				xuyuContentCardInfo.setWaitType(xuyuOwnerInfoVo.getSilentType());
			}else{
				xuyuContentCardInfo.setMgrOwnFlag(SystemConstants.STRING_NO);
				xuyuContentCardInfo.setOwner(SystemConstants.STRINGEMPTY);
				xuyuContentCardInfo.setComboType(xuyuContentCardMgrVo.getComboType());
				xuyuContentCardInfo.setComnoName(xuyuContentCardMgrVo.getComboName());
				xuyuContentCardInfo.setPaymentType(xuyuContentCardMgrVo.getPayType());
				xuyuContentCardInfo.setTestType(xuyuContentCardMgrVo.getHaveTest());
				xuyuContentCardInfo.setWaitType(xuyuContentCardMgrVo.getSilentType());
			}
			map=new HashMap();
			map.put("xuyuContentCardInfo", xuyuContentCardInfo);
			map.put("numType", xuyuContentCardMgrVo.getNumType());
			xuyuContentCardInfoDao.updateInfoOwnerNew(map);
			// 插入并且更新临时表
			xuyuContentCardInfoDao.truncateTable();
			xuyuContentCardInfoDao.insertInfoOwnerTempNew(map);
			// 计算激活日期
			sb=new StringBuilder();
			sb.append("  update XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 set t1.ACTIVATE_DATE=( ");
			sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') and (a1.TEST_TYPE is null or a1.TEST_TYPE='') then a1.ESTABLISH_DATE ");
			sb.append("  else null end ACTIVATE_DATE from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM ");
			sb.append("  )  where 1=1     ");
			if("01".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND a1.ACCESS_NUM=t1.ACCESS_NUM ");
				
			}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND a1.ACCESS_NUM=t1.ICCID ");
			}
			commonMapper.executeAction(sb.toString());
			// 计算沉默期到期时间--要区分联通和非联通的数据
			sb=new StringBuilder();
			sb.append("  update XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 set t1.WAIT_DATE=( ");
			sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') then a1.ESTABLISH_DATE  ");
			sb.append("  else last_day(date_sub(a1.ESTABLISH_DATE,interval  -(a1.WAIT_TYPE-1) month))");
			sb.append("  end WAIT_DATE    ");
			sb.append(" from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM )");
			sb.append(" where 1=1 and  t1.PROVIDER!='2'   ");
			if("01".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND a1.ACCESS_NUM=t1.ACCESS_NUM");
			}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND  a1.ACCESS_NUM=t1.ICCID ");
			}
			commonMapper.executeAction(sb.toString());
			
			sb=new StringBuilder();
			sb.append("  update XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 set t1.WAIT_DATE=( ");
			sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') then a1.ESTABLISH_DATE  ");
			sb.append("  else ");
			sb.append(" (case when date_format(a1.ESTABLISH_DATE,'%d')>26 then ");
			sb.append(" date_format(DATE_ADD(DATE_ADD(a1.ESTABLISH_DATE,INTERVAL (a1.WAIT_TYPE) month),interval -day(a1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
			sb.append(" else ");
			sb.append(" date_format(DATE_ADD(DATE_ADD(a1.ESTABLISH_DATE,INTERVAL (a1.WAIT_TYPE-1) month),interval -day(a1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
			sb.append(" end ");
			sb.append(" )");
			sb.append("  end WAIT_DATE    ");
			sb.append(" from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM )");
			sb.append(" where 1=1 and  t1.PROVIDER='2'   ");
			if("01".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND a1.ACCESS_NUM=t1.ACCESS_NUM ");
			}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
				sb.append("   AND a1.ACCESS_NUM=t1.ICCID ");
			}
			commonMapper.executeAction(sb.toString());
			
			// 插入明细表
			XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
			BeanUtils.copyProperties(xuyuContentCardInfo, xuyuContentCardInfoRecord);
			map=new HashMap();
			map.put("xuyuContentCardInfoRecord", xuyuContentCardInfoRecord);
			map.put("numType", xuyuContentCardMgrVo.getNumType());
			xuyuContentCardInfoRecordDao.insertOwnerNew(map);
		}
	}
    
	/**
	 * 通过卡段划卡
	 * @param xuyuContentCardMgrVo
	 * @throws CustomException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized void setOwnerUse(XuyuContentCardMgrSelfVo xuyuContentCardMgrVo) throws CustomException {
		// 首先判断所有卡是否都属于本代理商
		HashMap map=new HashMap();
	    // 修改info表
	    XuyuContentCardInfo xuyuContentCardInfo=new XuyuContentCardInfo();
		// 判定是否为群组划卡
		if(SystemConstants.STRING_YES.equals(xuyuContentCardMgrVo.getGroupYesNo())){
			xuyuContentCardInfo.setMgrOwnFlag(SystemConstants.STRING_YES);
			xuyuContentCardInfo.setOwner(xuyuContentCardMgrVo.getOwner());
			// 查询群组信息
			XuyuOwnerInfoVo xuyuOwnerInfoVo=xuyuOwnerInfoService.find(xuyuContentCardMgrVo.getOwner());
			xuyuContentCardInfo.setComboType(xuyuOwnerInfoVo.getComboType());
			xuyuContentCardInfo.setComnoName(xuyuOwnerInfoVo.getComboName());
			xuyuContentCardInfo.setPaymentType(xuyuOwnerInfoVo.getPayType());
			xuyuContentCardInfo.setTestType(xuyuOwnerInfoVo.getHaveTest());
			xuyuContentCardInfo.setWaitType(xuyuOwnerInfoVo.getSilentType());
		}else{
			xuyuContentCardInfo.setMgrOwnFlag(SystemConstants.STRING_NO);
			xuyuContentCardInfo.setOwner(SystemConstants.STRINGEMPTY);
			xuyuContentCardInfo.setComboType(xuyuContentCardMgrVo.getComboType());
			xuyuContentCardInfo.setComnoName(xuyuContentCardMgrVo.getComboName());
			xuyuContentCardInfo.setPaymentType(xuyuContentCardMgrVo.getPayType());
			xuyuContentCardInfo.setTestType(xuyuContentCardMgrVo.getHaveTest());
			xuyuContentCardInfo.setWaitType(xuyuContentCardMgrVo.getSilentType());
		}
		long result=0;
		if("01".equals(xuyuContentCardMgrVo.getNumType())){
			StringBuilder sb=new StringBuilder();
			sb.append(" select count(1) as a  from XUYU_CONTENT_CARD_INFO where 1=1 ");
			sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
			result=commonMapper.findCount(sb.toString());
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append(" select count(1) as a  from XUYU_CONTENT_CARD_INFO where 1=1  ");
			sb.append("  AND ICCID>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ICCID<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
			result=commonMapper.findCount(sb.toString());
		}
		if(result>100000){
			throw new CustomException("目前系统支持的划卡数据一次最多为100000");
		}
		map=new HashMap();
		map.put("xuyuContentCardInfo", xuyuContentCardInfo);
		map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
		map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
		map.put("numType", xuyuContentCardMgrVo.getNumType());
		xuyuContentCardInfoDao.updateInfoOwner(map);
		// 插入并且更新临时表
		xuyuContentCardInfoDao.truncateTable();
		xuyuContentCardInfoDao.insertInfoOwnerTemp(map);
//		xuyuContentCardInfoDao.updateInfoOwnerDate(map);
		// 计算激活日期
		StringBuilder sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1 set t1.ACTIVATE_DATE=( ");
		sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') and (a1.TEST_TYPE is null or a1.TEST_TYPE='') then a1.ESTABLISH_DATE ");
		sb.append("  else null end ACTIVATE_DATE from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM ");
		sb.append("  )  where 1=1     ");
		if("01".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ICCID>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ICCID<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}
		commonMapper.executeAction(sb.toString());
		// 计算沉默期到期时间--要区分联通和非联通的数据
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1 set t1.WAIT_DATE=( ");
		sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') then a1.ESTABLISH_DATE  ");
		sb.append("  else last_day(date_sub(a1.ESTABLISH_DATE,interval  -(a1.WAIT_TYPE-1) month))");
		sb.append("  end WAIT_DATE    ");
		sb.append(" from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM )");
		sb.append(" where 1=1 and  t1.PROVIDER!='2'   ");
		if("01".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ICCID>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ICCID<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}
		commonMapper.executeAction(sb.toString());
		
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1 set t1.WAIT_DATE=( ");
		sb.append("  select case when (a1.WAIT_TYPE is null or a1.WAIT_TYPE='') then a1.ESTABLISH_DATE  ");
		sb.append("  else ");
		sb.append(" (case when date_format(a1.ESTABLISH_DATE,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(a1.ESTABLISH_DATE,INTERVAL (a1.WAIT_TYPE) month),interval -day(a1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(a1.ESTABLISH_DATE,INTERVAL (a1.WAIT_TYPE-1) month),interval -day(a1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		sb.append("  end WAIT_DATE    ");
		sb.append(" from XUYU_CONTENT_CARD_INFO_TEMMP a1 where a1.ACCESS_NUM=t1.ACCESS_NUM )");
		sb.append(" where 1=1 and  t1.PROVIDER='2'   ");
		if("01".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}else if("02".equals(xuyuContentCardMgrVo.getNumType())){
			sb.append("  AND ICCID>='"+xuyuContentCardMgrVo.getAccessNumStart()+"'");
			sb.append("  AND ICCID<='"+xuyuContentCardMgrVo.getAccessNumEnd()+"'");
		}
		commonMapper.executeAction(sb.toString());
		
		// 插入明细表
		XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
		BeanUtils.copyProperties(xuyuContentCardInfo, xuyuContentCardInfoRecord);
		map=new HashMap();
		map.put("xuyuContentCardInfoRecord", xuyuContentCardInfoRecord);
		map.put("accessNumStart", xuyuContentCardMgrVo.getAccessNumStart());
		map.put("accessNumEnd", xuyuContentCardMgrVo.getAccessNumEnd());
		map.put("numType", xuyuContentCardMgrVo.getNumType());
		xuyuContentCardInfoRecordDao.insertOwner(map);
	}

}
