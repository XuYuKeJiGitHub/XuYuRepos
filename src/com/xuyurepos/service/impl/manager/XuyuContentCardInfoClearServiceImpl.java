package com.xuyurepos.service.impl.manager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.service.manager.XuyuContentCardInfoClearService;
import com.xuyurepos.service.payment.XuyuRechargeService;
/**
 * 批量到期清空当前数据以及处理预充值
 * @author yangfei
 */
@Service
@Transactional
public class XuyuContentCardInfoClearServiceImpl implements XuyuContentCardInfoClearService{
	
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuContentCardInfoClearServiceImpl.class);
	
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	@Autowired
	private XuyuRechargeService xuyuRechargeService;
	
	/**
	 * 移动和电信的批量
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void clear() {
		try {
			// 
			// 第一步处理到期的数据
			// 直接将数据清空
			StringBuilder sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.TOTAL_GPRS=0.00,t1.REMAIN_GPS=0.00,t1.USE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m-%d')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER!='2' ");
			sb.append(" and t1.REMAIN_GPS>=0 ");
			commonMapper.executeAction(sb.toString());
			
			// 修改总量为剩余流量
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.TOTAL_GPRS=t1.REMAIN_GPS,t1.USE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m-%d')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER!='2' ");
			sb.append(" and t1.REMAIN_GPS<0 ");
			commonMapper.executeAction(sb.toString());
			
			// 修改上月月中充值用户
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.CHARGE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.ACTIVATE_DATE_RESTART,'%Y-%m') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER!='2' ");
			sb.append(" and t1.COMBO_TYPE='1' ");
			commonMapper.executeAction(sb.toString());
			
			// 修改非一个月的用户
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.CHARGE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.ACTIVATE_DATE_RESTART,'%Y-%m') =DATE_FORMAT(DATE_SUB(DATE_SUB(curdate(),INTERVAL 1 DAY),INTERVAL(COMBO_TYPE-1) MONTH),'%Y-%m') ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER !='2' ");
			sb.append(" and t1.COMBO_TYPE !='1' ");
			commonMapper.executeAction(sb.toString());
			
			// 预充值的数据开始处理
			sb=new StringBuilder();
			sb.append(" select * from XUYU_RECHARGE t1 where t1.YC='y' and t1.IS_DEAL='n' and t1.PROVIDER!='2' ");
			List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
			if(list.size()>0){
				Map map=null;
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < list.size(); i++) {
					map=list.get(i);
					XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(SystemConstants.STRINGEMPTY+map.get("ACCESS_NUM"));
					// 直接设置套餐的总量
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					BigDecimal totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("TOTAL_GPRS")));
					xuyuContentCardInfo.setComboType(SystemConstants.STRINGEMPTY+map.get("COMBO_TYPE"));
					xuyuContentCardInfo.setComnoName(SystemConstants.STRINGEMPTY+map.get("COMNO_NAME"));
					// 首先判断剩余流量是否大于等于0
					if(xuyuContentCardInfo.getRemainGps()!=null){
						int cam=xuyuContentCardInfo.getRemainGps().compareTo(BigDecimal.ZERO); 
						if(cam==-1){ 
							// 那计算总量就是目前的总量减去之前欠费的
							totalGprs=totalGprs.add(xuyuContentCardInfo.getRemainGps());
						}
					}
					xuyuContentCardInfo.setTotalGprs(totalGprs);
					xuyuContentCardInfo.setUseGprs(BigDecimal.valueOf(0.00));
					xuyuContentCardInfo.setRemainGps(totalGprs);
					xuyuContentCardInfo.setActivateDateRestart(calendar.getTime());
					
					String testType=xuyuContentCardInfo.getTestType();
					String waitType=xuyuContentCardInfo.getWaitType();
					// 没有测试期和成默契直接计算
					if(SystemConstants.STRINGEMPTY.equals(testType)
							   &&SystemConstants.STRINGEMPTY.equals(waitType)){
						try {
							// 通过激活日期计算沉到期时间
						    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
						    String waitDate=xuyuRechargeService.getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
						    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
						}catch (Exception e) {
							e.printStackTrace();
							throw new CustomException("满期日计算失败");
						}
					}else{
						// 如果沉默期和测试期存在的情况
						// 得需要判断是否激活
						if(xuyuContentCardInfo.getActivateDate()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getActivateDate())){
							try {
								// 通过激活日期计算沉到期时间
							    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
							    String waitDate=xuyuRechargeService.getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
							    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
							}catch (Exception e) {
								e.printStackTrace();
								throw new CustomException("满期日计算失败");
							}
						}
					}
					// 更新时间和是否处理
					sb=new StringBuilder();
					sb.append(" update XUYU_RECHARGE set IS_DEAL='y',UPDATE_DATE=SYSDATE() where");
					sb.append("  ID="+map.get("ID"));
					commonMapper.executeAction(sb.toString());
					xuyuContentCardInfoDao.update(xuyuContentCardInfo);
				}
			}
		} catch (Exception e) {
			logger.info("月末跑批失败:"+e.getMessage());
			e.printStackTrace();
		}
		
		
	}
    
	/**
	 * 联通的批量
	 */
	@Override
	public void clearUnion() {
		try {
			// 第一步处理到期的数据
			// 直接将数据清空
			StringBuilder sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.TOTAL_GPRS=0.00,t1.REMAIN_GPS=0.00,t1.USE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m-%d')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER='2' ");
			sb.append(" and t1.REMAIN_GPS>=0 ");
			commonMapper.executeAction(sb.toString());
			// 直接修改总量为剩余流量
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.TOTAL_GPRS=t1.REMAIN_GPS,t1.USE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m-%d')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER='2' ");
			sb.append(" and t1.REMAIN_GPS<0 ");
			commonMapper.executeAction(sb.toString());
			
			// 修改上月月中充值用户
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.CHARGE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.ACTIVATE_DATE_RESTART,'%Y-%m') =DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y-%m')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER='2' ");
			sb.append(" and t1.COMBO_TYPE='1' ");
			commonMapper.executeAction(sb.toString());
			
			// 修改非一个月的充值用户
			sb=new StringBuilder();
			sb.append(" update XUYU_CONTENT_CARD_INFO t1 set t1.CHARGE_GPRS=0.00 where 1=1");
			sb.append(" and DATE_FORMAT(t1.ACTIVATE_DATE_RESTART,'%Y-%m') =DATE_FORMAT(DATE_SUB(DATE_SUB(curdate(),INTERVAL 1 DAY),INTERVAL(COMBO_TYPE-1) MONTH),'%Y-%m')  ");
			sb.append(" and (t1.`OWNER` is  null or LENGTH(trim(t1.`OWNER`))<1) ");
			sb.append(" and t1.PROVIDER='2' ");
			sb.append(" and t1.COMBO_TYPE!='1' ");
			commonMapper.executeAction(sb.toString());
			
			// 预充值的数据开始处理
			sb=new StringBuilder();
			sb.append(" select * from XUYU_RECHARGE t1 where t1.YC='y' and t1.IS_DEAL='n' and t1.PROVIDER='2' ");
			List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
			if(list.size()>0){
				Map map=null;
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < list.size(); i++) {
					map=list.get(i);
					XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(SystemConstants.STRINGEMPTY+map.get("ACCESS_NUM"));
					// 直接设置套餐的总量
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					BigDecimal totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("TOTAL_GPRS")));
					xuyuContentCardInfo.setComboType(SystemConstants.STRINGEMPTY+map.get("COMBO_TYPE"));
					xuyuContentCardInfo.setComnoName(SystemConstants.STRINGEMPTY+map.get("COMNO_NAME"));
					xuyuContentCardInfo.setTotalGprs(totalGprs);
					xuyuContentCardInfo.setUseGprs(BigDecimal.valueOf(0.00));
					xuyuContentCardInfo.setRemainGps(BigDecimal.valueOf(0.00));
					xuyuContentCardInfo.setActivateDateRestart(calendar.getTime());
					
					String testType=xuyuContentCardInfo.getTestType();
					String waitType=xuyuContentCardInfo.getWaitType();
					// 没有测试期和成默契直接计算
					if(SystemConstants.STRINGEMPTY.equals(testType)
							   &&SystemConstants.STRINGEMPTY.equals(waitType)){
						try {
							// 通过激活日期计算沉到期时间
						    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
						    String waitDate=xuyuRechargeService.getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
						    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
						}catch (Exception e) {
							e.printStackTrace();
							throw new CustomException("满期日计算失败");
						}
					}else{
						// 如果沉默期和测试期存在的情况
						// 得需要判断是否激活
						if(xuyuContentCardInfo.getActivateDate()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getActivateDate())){
							try {
								// 通过激活日期计算沉到期时间
							    String dateNow=format.format(xuyuContentCardInfo.getActivateDate());
							    String waitDate=xuyuRechargeService.getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
							    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
							}catch (Exception e) {
								e.printStackTrace();
								throw new CustomException("满期日计算失败");
							}
						}
					}
					// 更新时间和是否处理
					sb=new StringBuilder();
					sb.append(" update XUYU_RECHARGE set IS_DEAL='y',UPDATE_DATE=SYSDATE() where");
					sb.append("  ID="+map.get("ID"));
					commonMapper.executeAction(sb.toString());
					xuyuContentCardInfoDao.update(xuyuContentCardInfo);
				}
			}
		} catch (Exception e) {
			logger.info("联通月末跑批失败:"+e.getMessage());
			e.printStackTrace();
		}
	}

}
