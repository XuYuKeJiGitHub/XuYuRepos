package com.xuyurepos.service.impl.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.QuartzImportDao;
import com.xuyurepos.dao.manager.XuyuContentCardMgrDao;
import com.xuyurepos.entity.system.SystemAnnexe;
import com.xuyurepos.service.manager.ImportTempService;
import com.xuyurepos.service.system.SystemAnnexeService;
/**
 * 临时表处理
 * @author yangfei
 *
 */
@Transactional
@Service
public class ImportTempServiceImpl implements ImportTempService{
	Logger logger=LoggerFactory.getInstance().getLogger(ImportTempServiceImpl.class);

	@Autowired
	private ImportTempExcelParse importTempExcelParse;
	@Autowired
	private ImportTempExcelMoreParse importTempExcelMoreParse;
	@Autowired
	private SystemAnnexeService systemAnnexeService;
	@Autowired
	private QuartzImportDao quartzImportDao;
	@Autowired
	private XuyuContentCardMgrDao xuyuContentCardMgrDao;
	@Autowired
	private CommonMapper commonMapper;
	
	/*
	 * 首次数据导入库
	 * 注意异常不能抛出，否则会导致任务异常
	 * (non-Javadoc)
	 * @see com.xuyurepos.service.manager.ImportTempService#anaLysisData(java.lang.String)
	 */
	@Override
	public void anaLysisData(String batchNo) {
		if(batchNo==null||"".equals(batchNo)){
			logger.error("解析异常：没有正确传入参数");
		}else{
			try {
				SystemAnnexe annexe=new SystemAnnexe();
				annexe.setUploadBatchno(batchNo);
				List<SystemAnnexe> list=systemAnnexeService.getList(annexe);
				if(list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						annexe=list.get(i);
						logger.info("解析数据目录");
						importTempExcelParse.anaLysisData(annexe.getUploadPath());
						// 解析完成后操作
						// 第一步确认数据是移动联通还是电信
						// 移动1联通2电信3
						if("1".equals(annexe.getRelationInfo())){
							insertYd(annexe, batchNo);
						}else if("2".equals(annexe.getRelationInfo())){
							insertLt(annexe, batchNo);
						}else if("3".equals(annexe.getRelationInfo())){
							insertDx(annexe, batchNo);
						}
					}
				}
			} catch (Exception e) {
				logger.error("导入异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 移动首次导入
	 * @param annexe
	 * @param batchNo
	 */
	private void insertYd(SystemAnnexe annexe,String batchNo){
		// 第一步删除无效数据
		quartzImportDao.delYd();
		// 删除重复数据
		StringBuilder sb=new StringBuilder();
		sb.append(" DELETE FROM QUARTZ_IMPORT  where id in  ");
		sb.append(" (select t.ID from  ");
		sb.append(" (select t1.ID from QUARTZ_IMPORT  t1  ");
		sb.append(" WHERE A IN ( ");
		sb.append(" SELECT A FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" AND ID NOT IN (");
		sb.append(" SELECT min(ID) FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" ) t ) ");
		logger.info("下载的sql:"+sb.toString());
		commonMapper.executeAction(sb.toString());
		
		// 第二部分数据校验
		int j = quartzImportDao.getCountYd();
		if (j == 0) {
			// 通过逻辑计算套餐总量
			// 目标表没数据，直接插入表数据

			logger.info("打印annexe.getUnitCost():"+annexe.getUnitCost());
			xuyuContentCardMgrDao.insertYd("1", annexe.getRelationMod(), batchNo, annexe.getComboType(),
					annexe.getWaitType(), annexe.getTestType(), annexe.getCardType(), annexe.getStandard(),
					annexe.getUnitCost());
		} else {
			// 插入日志表
			logger.info("没有插入的数据");
		}
	}
	
	/**
	 * 联通首次导入
	 * @param annexe
	 * @param batchNo
	 */
	private void insertLt(SystemAnnexe annexe,String batchNo){
		// 联通导入
		// 第一步删除无效数据
		quartzImportDao.delLt();
		// 删除重复数据
		StringBuilder sb=new StringBuilder();
		sb.append(" DELETE FROM QUARTZ_IMPORT  where id in  ");
		sb.append(" (select t.ID from  ");
		sb.append(" (select t1.ID from QUARTZ_IMPORT  t1  ");
		sb.append(" WHERE A IN ( ");
		sb.append(" SELECT A FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" AND ID NOT IN (");
		sb.append(" SELECT min(ID) FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" ) t ) ");
		logger.info("下载的sql:"+sb.toString());
		commonMapper.executeAction(sb.toString());
		// 第二部分数据校验
		int j = quartzImportDao.getCountLt();
		if (j == 0) {
			// 目标表没数据，直接插入表数据
			xuyuContentCardMgrDao.insertLt("2",annexe.getRelationMod(), batchNo, annexe.getComboType(), annexe.getWaitType(),
					annexe.getTestType(), annexe.getCardType(), annexe.getStandard(), annexe.getUnitCost());
		} else {
			// 插入日志表
			logger.info("没有插入的数据");
		}
	}
	
	/**
	 * 电信首次导入
	 * @param annexe
	 * @param batchNo
	 */
	private void insertDx(SystemAnnexe annexe,String batchNo){
		// 电信导入
		// 第一步删除无效数据
		quartzImportDao.delDx();
		// 删除重复数据
		StringBuilder sb=new StringBuilder();
		sb.append(" DELETE FROM QUARTZ_IMPORT  where id in  ");
		sb.append(" (select t.ID from  ");
		sb.append(" (select t1.ID from QUARTZ_IMPORT  t1  ");
		sb.append(" WHERE A IN ( ");
		sb.append(" SELECT A FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" AND ID NOT IN (");
		sb.append(" SELECT min(ID) FROM QUARTZ_IMPORT GROUP BY A HAVING count(A) > 1");
		sb.append(" )");
		sb.append(" ) t ) ");
		commonMapper.executeAction(sb.toString());
		// 第二部分数据校验
		int j = quartzImportDao.getCountDx();
		if (j == 0) {
			// 目标表没数据，直接插入表数据
			xuyuContentCardMgrDao.insertDx("3",annexe.getRelationMod(), batchNo, annexe.getComboType(), annexe.getWaitType(),
					annexe.getTestType(), annexe.getCardType(), annexe.getStandard(), annexe.getUnitCost());
		} else {
			// 插入日志表
			System.out.println("没有插入的数据");
		}
	}
    
	/*
	 * 数据更新逻辑
	 * (non-Javadoc)
	 * @see com.xuyurepos.service.manager.ImportTempService#anaLysisUpdateData(java.lang.String)
	 */
	@Override
	public void anaLysisUpdateData(String batchNo) {
		if(batchNo==null||"".equals(batchNo)){
			logger.error("更新解析异常：没有正确传入参数");
		}else{
			try {
				SystemAnnexe annexe=new SystemAnnexe();
				annexe.setUploadBatchno(batchNo);
				List<SystemAnnexe> list=systemAnnexeService.getList(annexe);
				if(list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						annexe=list.get(i);
						logger.info("更新解析数据目录");
						importTempExcelMoreParse.anaLysisData(annexe.getUploadPath());
						// 解析完成后操作
						// 第一步确认数据是移动联通还是电信
						// 移动1联通2电信3
						if("1".equals(annexe.getRelationInfo())){
							updateYd(batchNo);
						}else if("2".equals(annexe.getRelationInfo())){
							updateLt(batchNo);
						}else if("3".equals(annexe.getRelationInfo())){
							updateDx(batchNo);
						}else {
							cardUpdateState(batchNo);
						}
					}
				}
			} catch (Exception e) {
				logger.error("更新数据异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	/**
	 * 移动卡状态手动更新
	 * @param batchNo
	 */
	private void cardUpdateState(String batchNo){
		//删除无效数据
		StringBuilder sb=new StringBuilder();
		sb.append(" delete from QUARTZ_UPDATE  where A='物联卡号' AND B='ICCID' AND C='IMSI' ");
		commonMapper.executeAction(sb.toString());
		// 更新状态
//		sb=new StringBuilder();
//		sb.append(" update QUARTZ_UPDATE set E = '00' where E = '开启' ");
//		commonMapper.executeAction(sb.toString());
//		sb=new StringBuilder();
//		sb.append(" update QUARTZ_UPDATE set E = '02' where E = '关闭' ");
//		commonMapper.executeAction(sb.toString());
		//修改总量
		sb=new StringBuilder();
		sb.append("UPDATE XUYU_CONTENT_CARD_INFO t1,QUARTZ_UPDATE t2 SET t1.TOTAL_GPRS = t2.E,t1.DEADLINE_DATE = DATE_FORMAT(IFNULL(t2.F,'%Y/%m/%d'),'%Y/%m/%d') WHERE t1.ICCID = t2.B");
		commonMapper.executeAction(sb.toString());

	}


	/**
	 * 移动数据更新
	 * @param batchNo
	 */
	private void updateYd(String batchNo){
		//第一步删除无效数据
		StringBuilder sb=new StringBuilder();
		sb.append(" delete from QUARTZ_UPDATE  where B='卡号备注' AND C='IMSI' AND D='ICCID' ");
		commonMapper.executeAction(sb.toString());
		// 值类型转换
		sb=new StringBuilder();
		sb.append(" update  QUARTZ_UPDATE t1");
		sb.append(" set t1.G=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='MOBILE_CARD_STATU' and t2.F_VALUE=t1.G),");
		sb.append(" t1.H=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='ONLINE_STATU' and t2.F_VALUE=t1.H) ");
		commonMapper.executeAction(sb.toString());
		
		//第二步校验数据是否已经更新过
	    sb=new StringBuilder();
		sb.append(" select count(1) as a from  QUARTZ_UPDATE  b inner  join  ");
		sb.append(" XUYU_CONTENT_CARD_INFO_IMPORT  a on  b.A=a.ACCESS_NUM ");
		sb.append(" and DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d')   ");
		long j=commonMapper.findCount(sb.toString());
		if(j==0){
			sb=new StringBuilder();
			sb.append("  insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID, ");
			sb.append("  IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append("  AGENCY, CARD_KIND, BILLING_STATUS, ");
			sb.append("  COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append("  REMAIN_GPS, USE_GPRS, MESSAGE_COUNT, ");
			sb.append("  ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append("  WAIT_TYPE, WAIT_DATE, WORKING_CONDITION, ");
			sb.append("  OWNER, PAYMENT_TYPE, REAL_ESTABLISH, ");
			sb.append("  REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE, ");
			sb.append("  REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append("  AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append("  REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append("  MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append("  MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append("  IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append("  AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append("  DOWNLOAD_DATE, IS_DEAL)");
			
			sb.append(" select _nextval('cardImport'),");
			sb.append(" ACCESS_NUM, ICCID, ");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, t2.H as BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, t2.I as USE_GPRS,t2.j as MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE,t2.G WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, ");
			sb.append(" STR_TO_DATE(if(t2.K='', null , t2.K),'%Y-%m-%d') as REAL_ESTABLISH, ");
			sb.append(" STR_TO_DATE(if(t2.L='', null , t2.L),'%Y-%m-%d') as REAL_ACTIVATE,");
			sb.append(" REAL_DEADLINE, ");
			sb.append("	REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" curdate() as DOWNLOAD_DATE, ");
			sb.append("	'y' as IS_DEAL  from XUYU_CONTENT_CARD_INFO t1 inner join QUARTZ_UPDATE   t2 on");
			sb.append("	t1.ACCESS_NUM=t2.A ");
			logger.info("下载的sql:"+sb.toString());
			commonMapper.executeAction(sb.toString());
		}else{
			// 数据已经插入的，针对已经存在的做更新操作，
			// 不存在的做插入操作
			// 首先更新批次号码
			sb=new StringBuilder();
			sb.append("  update XUYU_CONTENT_CARD_INFO_IMPORT a,QUARTZ_UPDATE  b set a.IMPORT_BATCHNO='"+batchNo+"' where 1=1 and ");
			sb.append("  b.A=a.ACCESS_NUM ");
			sb.append("  and  DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
						
			sb=new StringBuilder();
			sb.append("	update XUYU_CONTENT_CARD_INFO_IMPORT t3,QUARTZ_UPDATE t4  ");
			sb.append("	 set ");
			sb.append("	t3.WORKING_CONDITION=t4.G ,");
			sb.append("	t3.BILLING_STATUS=t4.H,");
			sb.append("	t3.USE_GPRS=t4.I,");
			sb.append("	t3.MESSAGE_COUNT=t4.J,");
			sb.append("	t3.REAL_ESTABLISH=STR_TO_DATE(if(t4.K='',null,t4.K),'%Y-%m-%d'),");
			sb.append("	t3.REAL_ACTIVATE=STR_TO_DATE(if(t4.L='' ,null,t4.L),'%Y-%m-%d'),");
			sb.append("	t3.IMPORT_BATCHNO="+batchNo+"");
			sb.append("	where ");
			sb.append("	t4.A=t3.ACCESS_NUM ");
			sb.append("	and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
			
			sb=new StringBuilder();
			sb.append(" insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID,  ");
			sb.append("  IMSI, PROVIDER, OWNER_PLACE,  ");
			sb.append("  AGENCY, CARD_KIND, BILLING_STATUS,  ");
			sb.append("  COMBO_TYPE, COMNO_NAME, TOTAL_GPRS,  ");
			sb.append("  REMAIN_GPS, USE_GPRS, MESSAGE_COUNT,  ");
			sb.append("  ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE,  ");
			sb.append("  WAIT_TYPE, WAIT_DATE, WORKING_CONDITION,  ");
			sb.append("  OWNER, PAYMENT_TYPE, REAL_ESTABLISH,  ");
			sb.append("  REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE,  ");
			sb.append("  REAL_COMBONAME, UNIT_COST, MONTH_FEE,  ");
			sb.append("  AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE,  ");
			sb.append("  REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT,  ");
			sb.append("  MARK, PARENT_AGENTCY, MARK_FIRST,  ");
			sb.append("  MARK_PROFIT, CREATE_USER, UPDATE_USER,  ");
			sb.append("  IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG,  ");
			sb.append("  AGENCY_NAME, CREATE_DATE, UPDATE_DATE,  ");
			sb.append("  DOWNLOAD_DATE, IS_DEAL) ");
			sb.append("	 select _nextval('cardImport'),");
			sb.append("	 ACCESS_NUM, ICCID, ");
			sb.append("	 IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append("  AGENCY, CARD_KIND, t2.H as BILLING_STATUS, ");
			sb.append("  COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append("  REMAIN_GPS, t2.I as USE_GPRS,t2.j as MESSAGE_COUNT, ");
			sb.append("  ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append("  WAIT_TYPE, WAIT_DATE,t2.G WORKING_CONDITION, ");
			sb.append("  OWNER, PAYMENT_TYPE, ");
			sb.append("  STR_TO_DATE(if(t2.K='', null , t2.K),'%Y-%m-%d') as REAL_ESTABLISH, ");
			sb.append("  STR_TO_DATE(if(t2.L='', null , t2.L),'%Y-%m-%d') as REAL_ACTIVATE,");
			sb.append("  REAL_DEADLINE, ");
			sb.append("  REAL_COMBOTYPE, ");
			sb.append("  REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append("  AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append("  REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append("  MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append("  MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append("  "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append("  AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append("  curdate() as DOWNLOAD_DATE, ");
			sb.append("  'y' as IS_DEAL  from ");
			sb.append("  (select a1.* from XUYU_CONTENT_CARD_INFO a1 inner join QUARTZ_UPDATE   t2 on  a1.ACCESS_NUM=t2.A ");
			sb.append("   and not EXISTS ( select * from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1   ");
			sb.append("	 and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			sb.append("  and  t3.ACCESS_NUM=a1.ACCESS_NUM  )) t1 ");
			sb.append(" INNER JOIN QUARTZ_UPDATE t2 ON t1.ACCESS_NUM = t2.A ");
			logger.info("下载的sql:"+sb.toString());
			commonMapper.executeAction(sb.toString());
		}
		updateGprs(batchNo);
		
	}
	
	/**
	 * 更新流量和激活卡号计算到期时间等
	 * @param batchNo
	 */
    private void updateGprs(String batchNo){
    	// 区别操作
		// 流量池逻辑  start
		// 第一步直接更新状态和短信以及流量
    	StringBuilder sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 set ");
		sb.append("  t1.USE_GPRS=t2.USE_GPRS, ");
		sb.append("  t1.MESSAGE_COUNT=t2.MESSAGE_COUNT, ");
		sb.append("  t1.BILLING_STATUS=t2.BILLING_STATUS, ");
		sb.append("  t1.WORKING_CONDITION=t2.WORKING_CONDITION, ");
		sb.append("  t1.REAL_ESTABLISH=t2.REAL_ESTABLISH, ");
		sb.append("  t1.REAL_ACTIVATE=t2.REAL_ACTIVATE ");
		sb.append("  where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"  ");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1");
		sb.append("  AND t1.PROVIDER!='2' ");
		logger.info("下载的sql:"+sb.toString());
		commonMapper.executeAction(sb.toString());
		
		
		// 计算激活时间没有测试期的
		// 没有测试期，产生流量就直接激活
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2");
		sb.append("	 set t1.ACTIVATE_DATE=CURDATE() ");
		sb.append("	 WHERE t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	 and t1.ACTIVATE_DATE is null and t1.TEST_TYPE is null and t1.USE_GPRS!=0.0000");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间有测试期的
		// 首选判断当前时间是否小于等于测试期到期时间
		// 如果小于等于则，不激活
		// 如果不是，则直接激活
		sb=new StringBuilder();
		sb.append("  update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	 set t1.ACTIVATE_DATE=if(CURDATE()<= last_day(date_sub(t1.ESTABLISH_DATE,interval  -(t1.TEST_TYPE-1) month)),null,CURDATE() ) ");
		sb.append("	 WHERE  t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	 and t1.ACTIVATE_DATE is null  and t1.USE_GPRS!=0.0000  and t1.TEST_TYPE is not null ");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算到期时间
		// 流量池的到期时间
		// 直接用总流量除以套餐类型得到充值月份
		// 然后直接计算
		// 那就直接激活时间加月份
		sb=new StringBuilder();
		sb.append(" update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	set t1.DEADLINE_DATE=last_day(date_sub(t1.ACTIVATE_DATE,interval -(t1.TOTAL_GPRS/t1.COMNO_NAME-1) month)) ");
		sb.append("	WHERE t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"  ");
		sb.append("	and t1.ACTIVATE_DATE is not null and t1.DEADLINE_DATE is null ");
		sb.append(" and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算剩余流量
		// 流量池直接减掉就行
		// 但是必须是激活的卡
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.COMNO_NAME is null, 0 , (t1.COMNO_NAME-t1.USE_GPRS))");
		sb.append(" where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+" ");
		sb.append(" and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1 ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());

		// 流量池计算结束  end 
		
		// 计算单卡的逻辑  start
		// 第一步直接更新状态和短信以及流量
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 set ");
		sb.append("  t1.USE_GPRS=t2.USE_GPRS, ");
		sb.append("  t1.MESSAGE_COUNT=t2.MESSAGE_COUNT, ");
		sb.append("  t1.BILLING_STATUS=t2.BILLING_STATUS, ");
		sb.append("  t1.WORKING_CONDITION=t2.WORKING_CONDITION, ");
		sb.append("  t1.REAL_ESTABLISH=t2.REAL_ESTABLISH, ");
		sb.append("  t1.REAL_ACTIVATE=t2.REAL_ACTIVATE ");
		sb.append("  where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+" ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间没有测试期的
		// 没有测试期，产生流量就直接激活
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2");
		sb.append("	 set t1.ACTIVATE_DATE=CURDATE() ");
		sb.append("	 WHERE t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	 and t1.ACTIVATE_DATE is null and t1.TEST_TYPE is null and t1.USE_GPRS!=0.0000");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间有测试期的
		// 首选判断当前时间是否小于等于测试期到期时间
		// 如果小于等于则，不激活
		// 如果不是，则直接激活
		sb=new StringBuilder();
		sb.append("  update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	 set t1.ACTIVATE_DATE=if(CURDATE()<= last_day(date_sub(t1.ESTABLISH_DATE,interval  -(t1.TEST_TYPE-1) month)),null,CURDATE() ) ");
		sb.append("	 WHERE t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	 and t1.ACTIVATE_DATE is null  and t1.USE_GPRS!=0.0000  and t1.TEST_TYPE is not null ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算到期时间
		// 单卡到期时间
		// 直接用激活时间加套餐类型就可以了
		sb=new StringBuilder();
		sb.append(" update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	set t1.DEADLINE_DATE=last_day(date_sub(t1.ACTIVATE_DATE_RESTART,interval -(t1.COMBO_TYPE-1) month)) ");
		sb.append("	WHERE t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	and t1.ACTIVATE_DATE is not null and t1.DEADLINE_DATE is null ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算剩余流量
		// 但是必须是激活的卡
		// 区分当前流量是否为第一个月
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.TOTAL_GPRS is null, 0 , (t1.TOTAL_GPRS-t1.USE_GPRS+IF(t1.CHARGE_GPRS is null,0,t1.CHARGE_GPRS) ))");
		sb.append(" where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append(" and curdate()<=last_day(t1.ACTIVATE_DATE_RESTART) ");
		sb.append("  AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 超过一个月的要计算累计的流量使用情况然后做减法
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.TOTAL_GPRS is null, 0 , (t1.TOTAL_GPRS-t1.USE_GPRS+IF(t1.CHARGE_GPRS is null,0,t1.CHARGE_GPRS) -");
		sb.append(" (select IF(SUM(t3.USE_GPRS) is null,0,SUM(t3.USE_GPRS))");
		sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
		sb.append(" and t3.ACCESS_NUM = t1.ACCESS_NUM  ");
		sb.append(" and t3.DOWNLOAD_DATE >= t1.ACTIVATE_DATE_RESTART ");
		sb.append(" and t3.DOWNLOAD_DATE=LAST_DAY(t3.DOWNLOAD_DATE) ");
		sb.append(" )");
		sb.append(" )) ");
		sb.append(" where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append(" and curdate()>last_day(t1.ACTIVATE_DATE_RESTART) ");
		sb.append(" AND t1.PROVIDER!='2' ");
		commonMapper.executeAction(sb.toString());
		// 单卡计算逻辑end
    }
    
	/**
	 * 更新流量和激活卡号计算到期时间等
	 * @param batchNo
	 */
    private void updateGprsLt(String batchNo){
    	// 区别操作
		// 流量池逻辑  start
		// 第一步直接更新状态和短信以及流量
    	StringBuilder sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 set ");
		sb.append("  t1.USE_GPRS=t2.USE_GPRS, ");
		sb.append("  t1.MESSAGE_COUNT=t2.MESSAGE_COUNT, ");
		sb.append("  t1.BILLING_STATUS=t2.BILLING_STATUS, ");
		sb.append("  t1.WORKING_CONDITION=t2.WORKING_CONDITION, ");
		sb.append("  t1.REAL_ESTABLISH=t2.REAL_ESTABLISH, ");
		sb.append("  t1.REAL_ACTIVATE=t2.REAL_ACTIVATE ");
		sb.append("  where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		
		// 计算激活时间没有测试期的
		// 没有测试期，产生流量就直接激活
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	 set t1.ACTIVATE_DATE=CURDATE() ");
		sb.append("	 WHERE");
		sb.append("	 t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"  ");
		sb.append("	 and t1.ACTIVATE_DATE is null and t1.TEST_TYPE is null and t1.USE_GPRS!=0.0000");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间有测试期的
		// 首选判断当前时间是否小于等于测试期到期时间
		// 如果小于等于则，不激活
		// 如果不是，则直接激活
		sb=new StringBuilder();
		sb.append("  update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	 set t1.ACTIVATE_DATE=if(CURDATE()<=");
		
		sb.append(" (case when date_format(t1.ESTABLISH_DATE,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ESTABLISH_DATE,INTERVAL (t1.WAIT_TYPE) month),interval -day(t1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ESTABLISH_DATE,INTERVAL (t1.WAIT_TYPE-1) month),interval -day(t1.ESTABLISH_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		
		sb.append(" ,null,CURDATE() ) ");
		sb.append("	 WHERE ");
		sb.append("	 t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+" ");
		sb.append("	 and t1.ACTIVATE_DATE is null  and t1.USE_GPRS!=0.0000  and t1.TEST_TYPE is not null ");
		sb.append("  and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算到期时间
		// 流量池的到期时间
		// 直接用总流量除以套餐类型得到充值月份
		// 然后直接计算
		// 那就直接激活时间加月份
		sb=new StringBuilder();
		sb.append(" update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	set t1.DEADLINE_DATE=");
		
		sb.append(" (case when date_format(t1.ACTIVATE_DATE,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE,INTERVAL (t1.TOTAL_GPRS/t1.COMNO_NAME) month),interval -day(t1.ACTIVATE_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE,INTERVAL (t1.TOTAL_GPRS/t1.COMNO_NAME-1) month),interval -day(t1.ACTIVATE_DATE)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		sb.append("	WHERE ");
		sb.append("	t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"  ");
		sb.append("	and t1.ACTIVATE_DATE is not null and t1.DEADLINE_DATE is null ");
		sb.append(" and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1  ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算剩余流量
		// 流量池直接减掉就行
		// 但是必须是激活的卡
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.COMNO_NAME is null, 0 , (t1.COMNO_NAME-t1.USE_GPRS))");
		sb.append(" where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append(" and t1.`OWNER` is not null and LENGTH(trim(t1.`OWNER`))>=1 ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());

		// 流量池计算结束  end 
		
		// 计算单卡的逻辑  start
		// 第一步直接更新状态和短信以及流量
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set ");
		sb.append("  t1.USE_GPRS=t2.USE_GPRS, ");
		sb.append("  t1.MESSAGE_COUNT=t2.MESSAGE_COUNT, ");
		sb.append("  t1.BILLING_STATUS=t2.BILLING_STATUS, ");
		sb.append("  t1.WORKING_CONDITION=t2.WORKING_CONDITION, ");
		sb.append("  t1.REAL_ESTABLISH=t2.REAL_ESTABLISH, ");
		sb.append("  t1.REAL_ACTIVATE=t2.REAL_ACTIVATE ");
		sb.append("  where t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+" ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间没有测试期的
		// 没有测试期，产生流量就直接激活
		sb=new StringBuilder();
		sb.append("  update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2");
		sb.append("	 set t1.ACTIVATE_DATE=CURDATE() ");
		sb.append("	 WHERE");
		sb.append("	 t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"  ");
		sb.append("	 and t1.ACTIVATE_DATE is null and t1.TEST_TYPE is null and t1.USE_GPRS!=0.0000");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算激活时间有测试期的
		// 首选判断当前时间是否小于等于测试期到期时间
		// 如果小于等于则，不激活
		// 如果不是，则直接激活
		sb=new StringBuilder();
		sb.append("  update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	 set t1.ACTIVATE_DATE=if(CURDATE()<= last_day(date_sub(t1.ESTABLISH_DATE,interval  -(t1.TEST_TYPE-1) month)),null,CURDATE() ) ");
		sb.append("	 WHERE ");
		sb.append("	 t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"   ");
		sb.append("	 and t1.ACTIVATE_DATE is null  and t1.USE_GPRS!=0.0000  and t1.TEST_TYPE is not null ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算到期时间
		// 单卡到期时间
		// 直接用激活时间加套餐类型就可以了
		sb=new StringBuilder();
		sb.append(" update  XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2 ");
		sb.append("	set t1.DEADLINE_DATE=");
		
		sb.append(" (case when date_format(t1.ACTIVATE_DATE_RESTART,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL (t1.COMBO_TYPE) month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL (t1.COMBO_TYPE-1) month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		
		sb.append("	WHERE ");
		sb.append("	t2.ACCESS_NUM = t1.ACCESS_NUM AND t2.IMPORT_BATCHNO ="+batchNo+"    ");
		sb.append("	and t1.ACTIVATE_DATE is not null and t1.DEADLINE_DATE is null ");
		sb.append("  and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 计算剩余流量
		// 但是必须是激活的卡
		// 区分当前流量是否为第一个月
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.TOTAL_GPRS is null, 0 , (t1.TOTAL_GPRS-t1.USE_GPRS))");
		sb.append(" where ");
		sb.append(" t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append(" and curdate()<= ");
		
		sb.append(" (case when date_format(t1.ACTIVATE_DATE_RESTART,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL 1 month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL 0 month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		
		// 超过一个月的要计算累计的流量使用情况然后做减法
		sb=new StringBuilder();
		sb.append(" update XUYU_CONTENT_CARD_INFO t1,XUYU_CONTENT_CARD_INFO_IMPORT t2  set");
		sb.append(" t1.REMAIN_GPS=if(t1.TOTAL_GPRS is null, 0 , (t1.TOTAL_GPRS-t1.USE_GPRS-");
		sb.append(" (select IF(SUM(t3.USE_GPRS) is null,0,SUM(t3.USE_GPRS))");
		sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
		sb.append(" and t3.ACCESS_NUM = t1.ACCESS_NUM  ");
		sb.append(" and t3.DOWNLOAD_DATE >= t1.ACTIVATE_DATE_RESTART ");
		sb.append(" and t3.DOWNLOAD_DATE=date_format(DATE_ADD(DATE_ADD((t3.DOWNLOAD_DATE),INTERVAL 0 month),interval -day(t3.DOWNLOAD_DATE)+26 day),'%Y-%m-%d')  ");
		sb.append(" )");
		sb.append(" )) ");
		sb.append(" where ");
		sb.append(" t2.ACCESS_NUM=t1.ACCESS_NUM and t2.IMPORT_BATCHNO="+batchNo+"");
		sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
		sb.append(" and t1.ACTIVATE_DATE is not null ");
		sb.append(" and curdate()> ");
		
		sb.append(" (case when date_format(t1.ACTIVATE_DATE_RESTART,'%d')>26 then ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL 1 month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" else ");
		sb.append(" date_format(DATE_ADD(DATE_ADD(t1.ACTIVATE_DATE_RESTART,INTERVAL 0 month),interval -day(t1.ACTIVATE_DATE_RESTART)+26 day),'%Y-%m-%d') ");
		sb.append(" end ");
		sb.append(" )");
		sb.append("  AND t1.PROVIDER='2' ");
		commonMapper.executeAction(sb.toString());
		// 单卡计算逻辑end
    }
	
	/**
	 * 联通数据更新
	 * @param batchNo
	 */
	private void updateLt(String batchNo){
		//第一步删除无效数据
		StringBuilder sb=new StringBuilder();
		sb.append("  delete from QUARTZ_UPDATE  where A='ICCID' ");
		commonMapper.executeAction(sb.toString());
		
		//值类型转换
		sb=new StringBuilder();
		sb.append(" update  QUARTZ_UPDATE t1 ");
		sb.append(" set t1.H=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='UNICOM_CARD_STATU' and t2.F_VALUE=t1.H),");
		sb.append(" t1.C=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='ONLINE_STATU' and t2.F_VALUE=t1.C) ");
		commonMapper.executeAction(sb.toString());
		// 第二步校验数据是否已经更新过
		sb=new StringBuilder();
		sb.append(" select count(1) as a from XUYU_CONTENT_CARD_INFO_IMPORT a inner join QUARTZ_UPDATE  b  on ");
		sb.append(" b.A=a.ACCESS_NUM ");
		sb.append(" and DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d')   ");
		long j=commonMapper.findCount(sb.toString());
		if (j == 0) {
			sb=new StringBuilder();
			sb.append("  insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID, ");
			sb.append("  IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append("  AGENCY, CARD_KIND, BILLING_STATUS, ");
			sb.append("  COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append("  REMAIN_GPS, USE_GPRS, MESSAGE_COUNT, ");
			sb.append("  ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append("  WAIT_TYPE, WAIT_DATE, WORKING_CONDITION, ");
			sb.append("  OWNER, PAYMENT_TYPE, REAL_ESTABLISH, ");
			sb.append("  REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE, ");
			sb.append("  REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append("  AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append("  REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append("  MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append("  MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append("  IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append("  AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append("  DOWNLOAD_DATE, IS_DEAL)");
			
			sb.append(" select _nextval('cardImport'),");
			sb.append(" ACCESS_NUM, ICCID, ");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, t2.C as BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, t2.B as USE_GPRS, MESSAGE_COUNT,  ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE,t2.H WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, ");
			sb.append(" STR_TO_DATE(if(t2.I='', null , t2.I),'%Y/%m/%d') as REAL_ESTABLISH,");
			sb.append(" STR_TO_DATE(if(t2.I='', null , t2.I),'%Y/%m/%d') as REAL_ACTIVATE,");
			sb.append(" REAL_DEADLINE, ");
			sb.append("	REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" curdate() as DOWNLOAD_DATE, ");
			sb.append("	'y' as IS_DEAL  from XUYU_CONTENT_CARD_INFO t1 inner join QUARTZ_UPDATE   t2 on");
			sb.append("	t1.ACCESS_NUM=t2.A ");
			commonMapper.executeAction(sb.toString());
		}else{

			// 数据已经插入的，针对已经存在的做更新操作，
			// 不存在的做插入操作
			// 首先更新批次号码
			sb=new StringBuilder();
			sb.append("  update XUYU_CONTENT_CARD_INFO_IMPORT a,QUARTZ_UPDATE  b set a.IMPORT_BATCHNO='"+batchNo+"' where 1=1 and ");
			sb.append("  b.A=a.ACCESS_NUM  ");
			sb.append("  and DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
			
			sb=new StringBuilder();
			sb.append("	update XUYU_CONTENT_CARD_INFO_IMPORT t3, QUARTZ_UPDATE t4 ");
			sb.append("	 set ");
			sb.append("	t3.WORKING_CONDITION=t4.I,");
			sb.append("	t3.BILLING_STATUS=t4.C,");
			sb.append("	t3.USE_GPRS=t4.B,");
			sb.append("	t3.REAL_ESTABLISH= STR_TO_DATE(if(t4.I='', null , t4.I),'%Y/%m/%d'),");
			sb.append("	t3.REAL_ACTIVATE=STR_TO_DATE(if(t4.I='', null , t4.I),'%Y/%m/%d'),");
			sb.append("	t3.IMPORT_BATCHNO="+batchNo+"");
			sb.append("	where");
			sb.append("	t4.A=t3.ACCESS_NUM ");
			sb.append("	and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
			
			sb = new StringBuilder();
			sb.append(" insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID,");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, USE_GPRS, MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE, WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, REAL_ESTABLISH, ");
			sb.append(" REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" DOWNLOAD_DATE, IS_DEAL)");
			sb.append(" select _nextval('cardImport'),");
			sb.append(" ACCESS_NUM, ICCID, ");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, t2.C as BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, t2.B as USE_GPRS, MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE,t2.H WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, ");
			sb.append(" STR_TO_DATE(if(t2.I='', null , t2.I),'%Y/%m/%d') as REAL_ESTABLISH, ");
			sb.append(" STR_TO_DATE(if(t2.I='', null , t2.I),'%Y/%m/%d') as REAL_ACTIVATE,");
			sb.append(" REAL_DEADLINE, ");
			sb.append(" REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" curdate() as DOWNLOAD_DATE, ");
			sb.append("	'y' as IS_DEAL  from ");
			
			
			sb.append("  (select a1.* from XUYU_CONTENT_CARD_INFO a1 inner join QUARTZ_UPDATE   t2 on  a1.ACCESS_NUM=t2.A ");
			sb.append("   and not EXISTS ( select * from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1   ");
			sb.append("	 and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			sb.append("  and  t3.ACCESS_NUM=a1.ACCESS_NUM  )) t1 ");
			sb.append(" INNER JOIN QUARTZ_UPDATE t2 ON t1.ACCESS_NUM = t2.A ");
			commonMapper.executeAction(sb.toString());
		}
		updateGprsLt(batchNo);
	}
	/**
	 * 电信更新
	 * @param batchNo
	 */
	private void updateDx(String batchNo){
		//第一步删除无效数据
		StringBuilder sb=new StringBuilder();
		sb.append("  delete from QUARTZ_UPDATE  where A='ICCID' and B='接入号码' ");
		commonMapper.executeAction(sb.toString());
		
		//值类型转换
		sb=new StringBuilder();
		sb.append(" update  QUARTZ_UPDATE t1 ");
		sb.append(" set t1.C=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='TELECOM_CARD_STATU' and t2.F_VALUE=t1.C), ");
		sb.append(" t1.H=(select t2.F_CODE from SYSTEM_LOOKUP_ITEM t2 where t2.F_LOOKUP_ID='ONLINE_STATU' and t2.F_VALUE=t1.H) ");
		commonMapper.executeAction(sb.toString());
		// 第二步校验数据是否已经更新过
		sb=new StringBuilder();
		sb.append(" select count(1) as a from XUYU_CONTENT_CARD_INFO_IMPORT a inner join QUARTZ_UPDATE  b on  ");
		sb.append(" b.B=a.ACCESS_NUM ");
		sb.append(" and DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d')  ");
		long j=commonMapper.findCount(sb.toString());
		if (j == 0) {
			sb=new StringBuilder();
			sb.append("  insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID, ");
			sb.append("  IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append("  AGENCY, CARD_KIND, BILLING_STATUS, ");
			sb.append("  COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append("  REMAIN_GPS, USE_GPRS, MESSAGE_COUNT, ");
			sb.append("  ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append("  WAIT_TYPE, WAIT_DATE, WORKING_CONDITION, ");
			sb.append("  OWNER, PAYMENT_TYPE, REAL_ESTABLISH, ");
			sb.append("  REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE, ");
			sb.append("  REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append("  AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append("  REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append("  MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append("  MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append("  IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append("  AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append("  DOWNLOAD_DATE, IS_DEAL)");
			
			sb.append(" select _nextval('cardImport'),");
			sb.append(" ACCESS_NUM, ICCID, ");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, t2.H as BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, CASE WHEN t2.J='' THEN '0' ELSE t2.J END AS  USE_GPRS, MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE,t2.C WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, ");
			sb.append(" STR_TO_DATE(if(t2.O='', null , t2.O),'%Y-%m-%d') as REAL_ESTABLISH, ");
			sb.append(" STR_TO_DATE(if(t2.P='', null , t2.P),'%Y-%m-%d') as REAL_ACTIVATE,");
			sb.append(" REAL_DEADLINE, ");
			sb.append("	REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" curdate() as DOWNLOAD_DATE, ");
			sb.append(" 'y' as IS_DEAL  from XUYU_CONTENT_CARD_INFO t1 inner join QUARTZ_UPDATE   t2 on");
			sb.append("	t1.ACCESS_NUM=t2.B");
			commonMapper.executeAction(sb.toString());
		}else{
			// 数据已经插入的，针对已经存在的做更新操作，
			// 不存在的做插入操作
			// 首先更新批次号码
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO_IMPORT a,QUARTZ_UPDATE  b set a.IMPORT_BATCHNO='"+batchNo+"' where 1=1 and ");
			sb.append(" b.B=a.ACCESS_NUM  ");
			sb.append(" and  DATE_FORMAT(a.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
			
			sb=new StringBuilder();
			sb.append("	update XUYU_CONTENT_CARD_INFO_IMPORT t3,QUARTZ_UPDATE t4 ");
			sb.append("	 set ");
			sb.append("	t3.WORKING_CONDITION=(select a1.C from QUARTZ_UPDATE a1 where a1.B=t3.ACCESS_NUM),");
			sb.append("	t3.BILLING_STATUS=(select a1.H from QUARTZ_UPDATE a1 where a1.B=t3.ACCESS_NUM),");
			sb.append("	t3.USE_GPRS=(select CASE WHEN a1.J='' THEN '0' ELSE a1.J END AS USE_GPRS from QUARTZ_UPDATE a1 where a1.B=t3.ACCESS_NUM),");
			sb.append("	t3.REAL_ESTABLISH=(select STR_TO_DATE(if(a1.O='', null , a1.O),'%Y-%m-%d') from QUARTZ_UPDATE a1 where a1.B=t3.ACCESS_NUM),");
			sb.append("	t3.REAL_ACTIVATE=(select STR_TO_DATE(if(a1.P='', null , a1.P),'%Y-%m-%d') from QUARTZ_UPDATE a1 where a1.B=t3.ACCESS_NUM),");
			sb.append("	t3.IMPORT_BATCHNO="+batchNo+"");
			sb.append("	where");
			sb.append("	t4.B=t3.ACCESS_NUM ");
			sb.append("	and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			commonMapper.executeAction(sb.toString());
			
			sb = new StringBuilder();
			sb.append(" insert into XUYU_CONTENT_CARD_INFO_IMPORT (ID, ACCESS_NUM, ICCID,");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, USE_GPRS, MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE, WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, REAL_ESTABLISH, ");
			sb.append(" REAL_ACTIVATE, REAL_DEADLINE, REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" DOWNLOAD_DATE, IS_DEAL)");
			
			sb.append(" select _nextval('cardImport'),");
			sb.append(" ACCESS_NUM, ICCID, ");
			sb.append(" IMSI, PROVIDER, OWNER_PLACE, ");
			sb.append(" AGENCY, CARD_KIND, t2.H as BILLING_STATUS, ");
			sb.append(" COMBO_TYPE, COMNO_NAME, TOTAL_GPRS, ");
			sb.append(" REMAIN_GPS, CASE WHEN t2.J='' THEN '0' ELSE t2.J END AS  USE_GPRS,MESSAGE_COUNT, ");
			sb.append(" ESTABLISH_DATE, ACTIVATE_DATE, DEADLINE_DATE, ");
			sb.append(" WAIT_TYPE, WAIT_DATE,t2.C WORKING_CONDITION, ");
			sb.append(" OWNER, PAYMENT_TYPE, ");
			sb.append(" STR_TO_DATE(if(t2.O='', null , t2.O),'%Y-%m-%d') as REAL_ESTABLISH, ");
			sb.append(" STR_TO_DATE(if(t2.P='', null , t2.P),'%Y-%m-%d') as REAL_ACTIVATE,");
			sb.append(" REAL_DEADLINE, ");
			sb.append("	REAL_COMBOTYPE, ");
			sb.append(" REAL_COMBONAME, UNIT_COST, MONTH_FEE, ");
			sb.append(" AGENCY_FEE, REVENUE_FEE, LASTMONTH_FEE, ");
			sb.append(" REMAIN_FEE, REAL_PROFIT, AGENCY_PROFIT, ");
			sb.append(" MARK, PARENT_AGENTCY, MARK_FIRST, ");
			sb.append(" MARK_PROFIT, CREATE_USER, UPDATE_USER, ");
			sb.append(" "+batchNo+" as IMPORT_BATCHNO, EXPORT_BATCHNO, MGR_OWN_FLAG, ");
			sb.append(" AGENCY_NAME, CREATE_DATE, UPDATE_DATE, ");
			sb.append(" curdate() as DOWNLOAD_DATE, ");
			sb.append("	'y' as IS_DEAL  from ");
			
			sb.append("  (select a1.* from XUYU_CONTENT_CARD_INFO a1 inner join QUARTZ_UPDATE   t2 on  a1.ACCESS_NUM=t2.B ");
			sb.append("   and not EXISTS ( select * from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1   ");
			sb.append("	 and  DATE_FORMAT(t3.DOWNLOAD_DATE,'%Y-%m-%d')= DATE_FORMAT(curdate(),'%Y-%m-%d') ");
			sb.append("  and  t3.ACCESS_NUM=a1.ACCESS_NUM  )) t1 ");
			sb.append(" INNER JOIN QUARTZ_UPDATE t2 ON t1.ACCESS_NUM = t2.A ");
			commonMapper.executeAction(sb.toString());
		}
		updateGprs(batchNo);
	}

}
