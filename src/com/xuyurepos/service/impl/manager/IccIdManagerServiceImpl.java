package com.xuyurepos.service.impl.manager;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.analysis.BigDataExcelOutWrite;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.DateUtil;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.dao.manager.XuyuContentCardMgrDao;
import com.xuyurepos.dao.manager.XuyuMessageLogDao;
import com.xuyurepos.entity.manager.GPRSDosageInfo;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.entity.manager.XuyuContentCardMgr;
import com.xuyurepos.entity.manager.XuyuMessageLog;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.entity.system.XuyuDown;
import com.xuyurepos.service.intergration.facade.SynInfoFacadeService;
import com.xuyurepos.service.intergration.facade.SynInfoJSFacadeService;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.service.system.SystemAnnexeService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.service.system.XuyuDownService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;
import com.xuyurepos.vo.system.SystemAnnexeVo;
import com.xuyurepos.vo.system.SystemOrgVo;
@Transactional
@Service
public class IccIdManagerServiceImpl implements IccIdManagerService{
	Logger logger=LoggerFactory.getInstance().getLogger(IccIdManagerServiceImpl.class);

	@Autowired
	private XuyuContentCardMgrDao xuyuContentCardMgrDao;
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	@Autowired
	private XuyuMessageLogDao messageLogDao;
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	@Autowired
	private SystemAnnexeService systemAnnexeService;
	
	@javax.annotation.Resource
	private SqlSessionFactory sqlSessionFactory;
	@Autowired
	private IccIdManagerExcelService iccIdManagerExcelService;
	
	@Autowired
	private XuyuDownService xuyuDownService;
	
	@Autowired
	private CommonMapper commonMapper;
	
	public Connection getCon() {  
	    Connection connection = null;  
	    SqlSession sqlSession = sqlSessionFactory.openSession();  
	    connection = sqlSession.getConnection();  
	    return connection;  
	} 
	
	@Override
	public ArrayList<GPRSDosageInfo> findGPRSInfo() {
		
		ArrayList<GPRSDosageInfo> arrayList = xuyuContentCardInfoDao.findGPRSInfo();
		
		return arrayList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findList(PageModel pageModel) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		String orgId=systemUser.getOrgId();
		// 查找机构信息
		SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
		if(systemOrgVo.getOrgLevel().equals("1")){
			// 代理商用户开始划卡
			logger.info("总部物联卡信息查询");
			XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo=(XuyuContentCardMgrSelfVo)pageModel.getQueryObj();
			String agency=systemOrgVo.getOrgId();
			logger.info("agency:"+agency);
			xuyuContentCardMgrSelfVo.setAgency(agency);
			pageModel.setRows(xuyuContentCardInfoDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(xuyuContentCardInfoDao.selectUserCountWithPage(pageModel));
		}else{
			// 代理商用户开始划卡
			logger.info("代理商物联卡信息查询");
			XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo=(XuyuContentCardMgrSelfVo)pageModel.getQueryObj();
			String agency=systemOrgVo.getOrgId();
			logger.info("agency:"+agency);
			xuyuContentCardMgrSelfVo.setAgency(agency);
			pageModel.setRows(xuyuContentCardInfoDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(xuyuContentCardInfoDao.selectUserCountWithPage(pageModel));
		}
		
	}
    
	/***
	 * 发送短信
	 */
	@Override
	public void messageSend(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException {
		String accessNums=xuyuMessageLogVo.getAccessNums();
		String message=xuyuMessageLogVo.getMessage();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			String[] accessNumStr=accessNums.split(";");
			if(accessNumStr.length>0){
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(new Date());
				XuyuMessageLog xuyuMessageLog=null;
				SynInfoFacadeService synInfoFacadeService=SynInfoFacadeService.getInstance();
				SynInfoJSFacadeService synInfoJSFacadeService=SynInfoJSFacadeService.getInstance();
				for (int i = 0; i < accessNumStr.length; i++) {
					XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNumStr[i]);
					if(contentCardInfo!=null){
						String ownerPlace=contentCardInfo.getOwnerPlace();

						// 先保存数据
						xuyuMessageLog=new XuyuMessageLog();
						xuyuMessageLog.setProvider(contentCardInfo.getProvider());
						xuyuMessageLog.setOwnerPlace(SystemConstants.STRINGEMPTY);
						xuyuMessageLog.setAccessNum(contentCardInfo.getAccessNum());
						xuyuMessageLog.setCreateTime(cal1.getTime());
						xuyuMessageLog.setCreateUser(systemUser.getUserName());
						xuyuMessageLog.setCreateUserName(systemUser.getCname());
						xuyuMessageLog.setMessage(message);
						messageLogDao.insert(xuyuMessageLog);
						// 判定运营商
						if("1".equals(contentCardInfo.getProvider())){// 移动
							// 判定是否存在二级运营商
							if(ownerPlace==null||SystemConstants.STRINGEMPTY.equals(ownerPlace)){
								// 分别判定
								if("1".equals(ownerPlace)){// 淮安
									synInfoJSFacadeService.mobileMessageSendService(contentCardInfo.getImsi(), message, ownerPlace);
								}else if("2".equals(ownerPlace)){// 盐城
									synInfoJSFacadeService.mobileMessageSendService(contentCardInfo.getImsi(), message, ownerPlace);
								}else if("4".equals(ownerPlace)){// 奉贤
									synInfoFacadeService.mobileMessageSendServiceFengXian(contentCardInfo.getImsi(), message,ownerPlace);
								}else{// 其他
									synInfoFacadeService.mobileMessageSendService(contentCardInfo.getImsi(), message);
								}
							}
						}else if("2".equals(contentCardInfo.getProvider())){//联通
							synInfoFacadeService.unicomMessageSendService(contentCardInfo.getIccid(), message);
						}else{
							// 电信直接报错
							throw new CustomException("电信卡不能发送短信");
						}
					}
				}
			}
		}
		
	}
    
	/**
	 * 用户状态更新
	 * @param xuyuMessageLogVo
	 * @throws CustomException 
	 */
	@Override
	public String userStatusQuery(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException {
		String accessNums=xuyuMessageLogVo.getAccessNums();
		String workingCondition=SystemConstants.STRINGEMPTY;
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNums);
			if(contentCardInfo!=null){
				SynInfoFacadeService synInfoFacadeService=SynInfoFacadeService.getInstance();
				SynInfoJSFacadeService synInfoJSFacadeService=SynInfoJSFacadeService.getInstance();
				String ownerPlace=contentCardInfo.getOwnerPlace();
				// 判定运营商
				if("1".equals(contentCardInfo.getProvider())){// 移动
					// 判定是否存在二级运营商
					if(ownerPlace!=null&&!SystemConstants.STRINGEMPTY.equals(ownerPlace)){
						// 分别判定
						if("1".equals(ownerPlace)){// 淮安
							workingCondition=synInfoJSFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum(), ownerPlace);
							logger.info("淮安查询状态码"+workingCondition);
							if("1".equals(workingCondition)){
								logger.info("*************淮安修改數據庫状态*********");
								xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "00");
							}else{
								xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "02");
							}
						}else if("2".equals(ownerPlace)){// 盐城
							workingCondition=synInfoJSFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum(), ownerPlace);
							logger.info("盐城查询状态码"+workingCondition);
							if("1".equals(workingCondition)){
								logger.info("*************淮安修改數據庫状态*********");
								xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "00");
							}else{
								xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "02");
							}
						}else if("3".equals(ownerPlace)){// 闵行 -- 更新流量
							workingCondition=synInfoFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum());
						}else if("4".equals(ownerPlace)){// 奉贤
							synInfoFacadeService.mobileUserStatusQueryFengXian(contentCardInfo.getAccessNum(), ownerPlace);
						}else{// 其他  走闵行
							workingCondition=synInfoFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum());
						}
					}else{// 不存在二级运营商   走闵行
						workingCondition=synInfoFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum());
					}
				}else if("2".equals(contentCardInfo.getProvider())){//联通
					workingCondition=synInfoFacadeService.unicomUserStatusQuery(contentCardInfo.getIccid());
				}else if("3".equals(contentCardInfo.getProvider())){//电信
					workingCondition=synInfoFacadeService.telecomUserStatusQuery(contentCardInfo.getAccessNum(),ownerPlace);
				}
				
				/*System.out.println("数据状态："+workingCondition);
				if(!SystemConstants.STRINGEMPTY.equals(workingCondition)){
					try {
						// 移动
						contentCardInfo.setWorkingCondition(workingCondition);
						xuyuContentCardInfoDao.update(contentCardInfo);
//						if(!"1".equals(contentCardInfo.getProvider())){// 移动
//							contentCardInfo.setWorkingCondition(workingCondition);
//							xuyuContentCardInfoDao.update(contentCardInfo);
//						}else{
//							 gprsStatusQuery(xuyuMessageLogVo);
//						}
						
					} catch (Exception e){
						e.printStackTrace();
						throw new CustomException("用户状态更新失败");
					}
				}*/
			}
		}
		if("1".equals(workingCondition)){
			return "00";
		}else{
			return "02";
		}
	}
    
	/**
	 * 流量实时查询
	 * @throws CustomException 
	 */
	@Override
	public void gprsQuery(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException {
		String accessNums=xuyuMessageLogVo.getAccessNums();
		logger.info("请求数据："+accessNums);
		String gprs=SystemConstants.STRINGEMPTY;
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNums);
			if(contentCardInfo!=null){
				gprs=getGPRS(contentCardInfo);
				gprs=gprs.replace("MB", "");
				BigDecimal chargeGprs=new BigDecimal(0.00);
				if(contentCardInfo.getChargeGprs()!=null){
					chargeGprs=contentCardInfo.getChargeGprs();
				}
				if(!SystemConstants.STRINGEMPTY.equals(gprs)){
					try {
						logger.info("查询当前用量："+gprs);
						contentCardInfo.setUseGprs(BigDecimal.valueOf(Double.valueOf(gprs)));
						// 判断是否有群组，有群组直接算月份减
						// 无群组直接减
						if(contentCardInfo.getOwner()!=null&&!SystemConstants.STRINGEMPTY.equals(contentCardInfo.getOwner())){
							BigDecimal remainGps=BigDecimal.valueOf(Long.valueOf(contentCardInfo.getComnoName())).subtract(contentCardInfo.getUseGprs());
							contentCardInfo.setRemainGps(remainGps);
						}else{
							// 首先判定是否超过一个月
							// 第一步判断是否为联通的卡
							if(contentCardInfo.getProvider().equals("2")){
								StringBuilder sb=new StringBuilder();
								SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
								String dateNow=format.format(contentCardInfo.getActivateDateRestart());
								int month=1;
								sb.append(" select case when date_format('"+dateNow+"','%d')>26 then ");
								sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+month+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
								sb.append(" else  ");
								sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+(month-1)+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
								sb.append(" END  as  DATE from dual ");
								Map<String, Object> map=commonMapper.findOneData(sb.toString());
								String result=SystemConstants.STRINGEMPTY+map.get("DATE");
								int result1=format.format(new Date()).compareTo(result);
								if(result1>0){
									// 把历史中数据都拿过来
									// 计算累计流量
									sb=new StringBuilder();
									sb.append(" select ifnull(SUM( t3.USE_GPRS ),0) as PRE_USE_GPRS");
									sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
									sb.append(" and t3.ACCESS_NUM ='"+contentCardInfo.getAccessNum()+"'");
									sb.append(" and t3.DOWNLOAD_DATE >="+dateNow);
									sb.append(" and t3.DOWNLOAD_DATE=date_format(DATE_ADD(DATE_ADD((t3.DOWNLOAD_DATE),INTERVAL 0 month),interval -day(t3.DOWNLOAD_DATE)+26 day),'%Y-%m-%d') ");
									Map<String, Object> mapPre=commonMapper.findOneData(sb.toString());
									String useGprs=SystemConstants.STRINGEMPTY+mapPre.get("PRE_USE_GPRS");
									BigDecimal preUseGprs=BigDecimal.valueOf(Double.valueOf(useGprs));
									BigDecimal remainGps=contentCardInfo.getTotalGprs().subtract(contentCardInfo.getUseGprs()).subtract(preUseGprs).add(chargeGprs);
									contentCardInfo.setRemainGps(remainGps);
								}else{
									BigDecimal remainGps=contentCardInfo.getTotalGprs().subtract(contentCardInfo.getUseGprs()).add(chargeGprs);
									contentCardInfo.setRemainGps(remainGps);
								}
							}else{
								SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
								String dateNow=format.format(contentCardInfo.getActivateDateRestart());
								int month=0;
							    StringBuilder sb=new StringBuilder();
								sb.append(" select  last_day(DATE_ADD('"+dateNow+"',INTERVAL "+month+" month)) as DATE from dual ");
								Map<String, Object> map1=commonMapper.findOneData(sb.toString());
								String result=SystemConstants.STRINGEMPTY+map1.get("DATE");
								int result1=format.format(new Date()).compareTo(result);
								if(result1>0){
									// 计算累计流量
									sb=new StringBuilder();
									sb.append(" select ifnull(SUM( t3.USE_GPRS ),0) as PRE_USE_GPRS");
									sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
									sb.append(" and t3.ACCESS_NUM ='"+contentCardInfo.getAccessNum()+"'");
									sb.append(" and t3.DOWNLOAD_DATE >="+dateNow);
									sb.append(" and t3.DOWNLOAD_DATE=LAST_DAY(t3.DOWNLOAD_DATE) ");
									Map<String, Object> mapPre=commonMapper.findOneData(sb.toString());
									String useGprs=SystemConstants.STRINGEMPTY+mapPre.get("PRE_USE_GPRS");
									BigDecimal preUseGprs=BigDecimal.valueOf(Double.valueOf(useGprs));
									BigDecimal remainGps=contentCardInfo.getTotalGprs().subtract(contentCardInfo.getUseGprs()).subtract(preUseGprs).add(chargeGprs);
									contentCardInfo.setRemainGps(remainGps);
								}else{
									BigDecimal remainGps=contentCardInfo.getTotalGprs().subtract(contentCardInfo.getUseGprs()).add(chargeGprs);
									contentCardInfo.setRemainGps(remainGps);
								}
							}
						}
						xuyuContentCardInfoDao.update(contentCardInfo);
					} catch (Exception e) {
						e.printStackTrace();
						throw new CustomException("流量实时查询失败");
					}
					
				}
			}
			
		}
	}
	
	/**
	 * 获取实际的GPRS流量
	 * @param contentCardInfo
	 * @return
	 */
	public String getGPRS(XuyuContentCardInfo contentCardInfo){
		String gprs=SystemConstants.STRINGEMPTY;
		SynInfoFacadeService synInfoFacadeService=SynInfoFacadeService.getInstance();
		SynInfoJSFacadeService synInfoJSFacadeService=SynInfoJSFacadeService.getInstance();
		String ownerPlace=contentCardInfo.getOwnerPlace();
		// 判定运营商
		if("1".equals(contentCardInfo.getProvider())){// 移动
			if(ownerPlace==null||SystemConstants.STRINGEMPTY.equals(ownerPlace)){
				gprs=synInfoFacadeService.mobileUserStatusQuery(contentCardInfo.getAccessNum());
			}else{
				// 分别判定
				if("1".equals(ownerPlace)){// 淮安
					gprs=synInfoJSFacadeService.mobileGPRSQueryService(contentCardInfo.getAccessNum(), ownerPlace);
				}else if("2".equals(ownerPlace)){// 盐城
					gprs=synInfoJSFacadeService.mobileGPRSQueryService(contentCardInfo.getAccessNum(), ownerPlace);
				}else if("4".equals(ownerPlace)){// 奉贤
					synInfoFacadeService.mobileGPRSQueryServiceFengXian(contentCardInfo.getAccessNum(), ownerPlace);
				}else{// 其他
					gprs=synInfoFacadeService.mobileGPRSQueryService(contentCardInfo.getAccessNum());
				}
			}
			
		}else if("2".equals(contentCardInfo.getProvider())){//联通
			gprs=synInfoFacadeService.unicomGPRSQueryService(contentCardInfo.getIccid());
		}else if("3".equals(contentCardInfo.getProvider())){//电信
			gprs=synInfoFacadeService.telecomGPRSQueryService(contentCardInfo.getAccessNum(),ownerPlace);
		}
//		gprs="1600MB";
		return gprs;
	}
	/**
	 * 查看卡所对应的城市编号
	 * @param 卡号
	 * @throws CustomException 
	 */
	@Override
	public XuyuContentCardInfo findCardOwnerPlace(String accessNums){
		String ownerPlace = SystemConstants.STRINGEMPTY;
		XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNums);
		return contentCardInfo;
	}	
	private String getMD5(String str) {
		String key="xuYuRepos2019";
		String signStr=str+"key="+key;//&key
		String sign=DigestUtils.md5Hex(signStr).toUpperCase();
		return sign;
	}
	private static final String huaianPLTFJ="http://192.168.1.116:8081/XuYuRepos/facade/changeCardStateAll";
	private static final String yanchengPLTFJ="http://47.101.207.177:8080/XuYuRepos/facade/changeCardStateAll";
	/**
	 * 停复机
	 * @param xuyuMessageLogVo
	 * @throws CustomException 
	 */
	@Override
	public String changeCardState(String accessNums,String bool) throws CustomException {
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNums);
			if(contentCardInfo!=null){
				SynInfoFacadeService synInfoFacadeService=SynInfoFacadeService.getInstance();
				SynInfoJSFacadeService synInfoJSFacadeService=SynInfoJSFacadeService.getInstance();
				String ownerPlace=contentCardInfo.getOwnerPlace();
				
				XuyuContentCardInfo xuyuContentCardInfo = xuyuContentCardInfoDao.find(contentCardInfo.getAccessNum());
				String remainGps = xuyuContentCardInfo.getRemainGps().toString();
				double parseDouble = Double.parseDouble(remainGps);
				
				// 判定运营商
				if("1".equals(contentCardInfo.getProvider())){// 移动
					// 判定是否存在二级运营商
					if(ownerPlace!=null||!SystemConstants.STRINGEMPTY.equals(ownerPlace)){
						// 分别判定
						if("1".equals(ownerPlace)){// 淮安
							if("false".equals(bool)){
								
								if(parseDouble>=30){//余额大于30，进行复机
									String mobileChangeCardState = synInfoJSFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(), "2", ownerPlace);
									if(SystemConstants.STATE_CG.equals(mobileChangeCardState)){
										xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "00");
									}
									return mobileChangeCardState;
								}else{
									return SystemConstants.STATE_YEBZ;
								}
								
							}else{
								
								String mobileChangeCardState  = synInfoJSFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(), "1", ownerPlace);
								if(SystemConstants.STATE_CG.equals(mobileChangeCardState)){
									xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "02");
								}
								return mobileChangeCardState;
								
							}
						}else if("2".equals(ownerPlace)){// 盐城
							if("false".equals(bool)){//是否停机
								
								if(parseDouble>=30){//余额大于30，进行复机
									String mobileChangeCardState = synInfoJSFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(), "2", ownerPlace);
									if(SystemConstants.STATE_CG.equals(mobileChangeCardState)){
										xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "00");
									}
									return mobileChangeCardState;
								}else{
									return SystemConstants.STATE_YEBZ;
								}
								
							}else{
								
								String mobileChangeCardState  = synInfoJSFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(), "1", ownerPlace);
								if(SystemConstants.STATE_CG.equals(mobileChangeCardState)){
									xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "02");
								}
								return mobileChangeCardState;
								
							}
						}else if("3".equals(ownerPlace)){// 闵行
							if("false".equals(bool)){
								boolean mobileChangeCardState = synInfoFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(),"1");
								if(mobileChangeCardState){
									xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "00");
									return SystemConstants.STATE_CG;
								}else{
									return SystemConstants.STATE_SB;
								}
							}else{
								boolean mobileChangeCardState = synInfoFacadeService.mobileChangeCardState(contentCardInfo.getAccessNum(),"0");
								if(mobileChangeCardState){
									xuyuContentCardInfoDao.updateCardState(contentCardInfo.getAccessNum(), "02");
									return SystemConstants.STATE_CG;
								}else{
									return SystemConstants.STATE_SB;
								}
							}
						}else if("4".equals(ownerPlace)){// 奉贤
							if("false".equals(bool)){
								synInfoFacadeService.mobileChangeCardStateFengXian(contentCardInfo.getAccessNum(), "1", ownerPlace);
							}else{
								synInfoFacadeService.mobileChangeCardStateFengXian(contentCardInfo.getAccessNum(), "0", ownerPlace);
							}
						}
					}
				}else if("2".equals(contentCardInfo.getProvider())){//联通
					if("false".equals(bool)){
						synInfoFacadeService.unicomChangeCardState(contentCardInfo.getIccid(),"1");
					}else{
						synInfoFacadeService.unicomChangeCardState(contentCardInfo.getIccid(),"0");
					}
				}else if("3".equals(contentCardInfo.getProvider())){//电信
					if("false".equals(bool)){
						synInfoFacadeService.telecomChangeCardState(contentCardInfo.getAccessNum(),"1",ownerPlace);
					}else{
						synInfoFacadeService.telecomChangeCardState(contentCardInfo.getAccessNum(),"0",ownerPlace);
					}
				}
			}
			
		}
		return SystemConstants.STATE_CG;
	}
    
	/**
	 * 设置备注
	 * @throws CustomException 
	 */
	@Override
	public synchronized void setMark(HttpServletRequest request) throws CustomException {
		String accessNums=request.getParameter("accessNums");
		String markFirst=request.getParameter("markFirst");
		if(!StringUtil.isEmpty(accessNums)){
			String[] idsStr=null;
			if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
				accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
				idsStr=accessNums.split(SystemConstants.STRING_SENT);
			}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
				idsStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
			}else{
				idsStr=accessNums.split(SystemConstants.STRING_ENTER);
			}
			// 首先判断是否为总部
			Boolean check=true;
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if("1000".equals(systemUser.getOrgCode())){
				check=false;
			}
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(idsStr[i]);
					if(contentCardInfo!=null){
						if(check){
							if(!contentCardInfo.getAgency().equals(systemUser.getOrgId())){
								throw new CustomException("只能备注当前客户名下的卡："+idsStr[i]+"不属于当前客户");
							}
						}
						contentCardInfo.setMarkFirst(markFirst);
						xuyuContentCardInfoDao.update(contentCardInfo);
					}else{
						throw new CustomException("接入号输入错误请检查："+idsStr[i]+"在系统中不存在");
					}
				}
			}
		}
	}

	@Override
	public Map<String, Object> exportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo) {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
	        String downLoadSize=ProsUtil.getProperty("downLoadSize");
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t2.ACCESS_NUM,t2.ICCID,t2.IMSI, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t2.PROVIDER) as PROVIDER,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='PAY_TYPE'  and a1.F_CODE=t2.PAYMENT_TYPE) as PAYMENT_TYPE,	");
	        sb.append(" t2.AGENCY_NAME, ");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  end WORKING_CONDITION, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t2.COMBO_TYPE) as COMBO_TYPE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_NAME'  and a1.F_CODE=t2.COMNO_NAME) as COMNO_NAME,");
	        sb.append(" 	t2.USE_GPRS,t2.COMNO_NAME AS TOTAL_GPRS,t2.REMAIN_GPS,t2.MESSAGE_COUNT,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='SILENT_TYPE'  and a1.F_CODE=t2.WAIT_TYPE) as WAIT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='HAVE_TEST'  and a1.F_CODE=t2.TEST_TYPE) as TEST_TYPE,	");
	        sb.append(" t2.DEADLINE_DATE,");
	        sb.append(" t2.ESTABLISH_DATE,");
	        sb.append("	t2.ACTIVATE_DATE,");
	        sb.append(" t2.WAIT_DATE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='ONLINE_STATU'  and a1.F_CODE=t2.BILLING_STATUS) as BILLING_STATUS,");
	        sb.append("  t3.OWNER_NAME,t2.MARK_FIRST from (select * from XUYU_CONTENT_CARD_INFO WHERE 1=1 ");
			String orgId=systemUser.getOrgCode();
	        sb.append(" and EXISTS ( SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+orgId+"%' and t1.ID=AGENCY ) ");
	        if(xuyuContentCardMgrSelfVo!=null){
	        	if(xuyuContentCardMgrSelfVo.getKeywords()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getKeywords())){
	 	        	sb.append(" AND ( ACCESS_NUM like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or ICCID like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or IMSI like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%'  )");
	 	         }
	        	 if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
	        		 SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
	        		 sb.append(" AND EXISTS (SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+systemOrgVo.getOrgId()+"%' and t1.ID=AGENCY) ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getPaymentType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getPaymentType())){
	        		 sb.append(" AND PAYMENT_TYPE='"+xuyuContentCardMgrSelfVo.getPaymentType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getProvider()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getProvider())){
	        		 sb.append(" AND PROVIDER='"+xuyuContentCardMgrSelfVo.getProvider()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwnerPlace()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwnerPlace())){
	        		 sb.append(" AND OWNER_PLACE='"+xuyuContentCardMgrSelfVo.getOwnerPlace()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboType())){
	        		 sb.append(" AND COMBO_TYPE='"+xuyuContentCardMgrSelfVo.getComboType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboName()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboName())){
	        		 sb.append("  AND COMNO_NAME='"+xuyuContentCardMgrSelfVo.getComboName()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRealComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRealComboType())){
	        		 sb.append("  AND REAL_COMBOTYPE='"+xuyuContentCardMgrSelfVo.getRealComboType()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getWorkingCondition()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getWorkingCondition())){
	        		 sb.append("  AND WORKING_CONDITION='"+xuyuContentCardMgrSelfVo.getWorkingCondition()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getBillingStatus()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getBillingStatus())){
	        		 sb.append("  AND BILLING_STATUS='"+xuyuContentCardMgrSelfVo.getBillingStatus()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateStart())){
	        		 sb.append("  AND ESTABLISH_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateEnd())){
	        		 sb.append("  AND ESTABLISH_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateStart())){
	        		 sb.append("  AND ACTIVATE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateEnd())){
	        		 sb.append("  AND ACTIVATE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateStart())){
	        		 sb.append("  AND DEADLINE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateEnd())){
	        		 sb.append("  AND DEADLINE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumStart())){
	        		 sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrSelfVo.getAccessNumStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumEnd())){
	        		 sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrSelfVo.getAccessNumEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidStart())){
	        		 sb.append("  AND ICCID>='"+xuyuContentCardMgrSelfVo.getIccidStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidEnd())){
	        		 sb.append("  AND ICCID<='"+xuyuContentCardMgrSelfVo.getIccidEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiStart())){
	        		 sb.append("  AND IMSI>='"+xuyuContentCardMgrSelfVo.getImsiStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiEnd())){
	        		 sb.append("  AND IMSI<='"+xuyuContentCardMgrSelfVo.getImsiEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getMark()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getMark())){
	        		 sb.append("  AND MARK_FIRST like '"+xuyuContentCardMgrSelfVo.getMark()+"%'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwner())){
	        		 sb.append("  AND OWNER= '"+xuyuContentCardMgrSelfVo.getOwner()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRemainGpsType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGpsType())){
	        		 if(xuyuContentCardMgrSelfVo.getRemainGps()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGps())){
	        			 sb.append("  AND REMAIN_GPS  "+xuyuContentCardMgrSelfVo.getRemainGpsType()+"  cast('"+xuyuContentCardMgrSelfVo.getRemainGps()+"' as decimal(16,4)) ");
	        		 }
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIsowner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        		 if("y".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND !(ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        		 if("n".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND (ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        	 }
	        	 
	        }
	       
	        sb.append("  ) t2  left JOIN XUYU_OWNER_INFO t3 ON t2.`OWNER` = t3.ID   ");
	        sb.append(" order by t2.ACCESS_NUM,t2.IMSI,t2.ICCID limit  "+downLoadSize);
	        logger.info("下载的sql:"+sb.toString());
	        map=createFile(sb.toString(),false);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
    private Map<String ,Object> createFile(String sql,Boolean isResult){
    	Map<String ,Object> map=new HashMap<String,Object>();
    	try {
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	    String nowdate = DateUtil.convert(new Date(), "yyyy_MM_dd_HH_mm_ss");
    		String nowdate1 = DateUtil.convert(new Date(), "yyyyMMdd");
    		
    		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
    		String userId=systemUser.getUserName();
    		String filename="export_" + userId +"_"+ nowdate +".xlsx";
    		String sheetName="Sheet";
    		String filepath="";
    		String path="";
    		// 获取生成文件的路径
            StringBuilder builder = new StringBuilder();
            builder.append(ProsUtil.getProperty("exportPath")+File.separator+nowdate1);
            // 查看目录是否存在
            File file = new File(builder.toString());
    		if (!file.exists()) {
    			file.mkdir();
    		}
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            path=builder.toString();
            filepath = path+filename;
        	if(isResult){
        		String[] colmnsList = {
            			"ID(不可更改)",
            			"接入号",
            			"iccid",
            			"imsi",
            			"支付类型",
            			"运营商",
            			"二级运营商",
            			"客户名称",
            			"卡状态#",
            			"套餐类型",
            			"套餐",
            			"当月用量(MB)#",
            			"套餐总量(MB)#",
            			"余额",
            			"已发短信",
            			
            			"沉默期类型#",
            			"测试期类型#",
            			"到期日期#",
            			"开户日期# ",
            			"激活日期# ",
            			"沉默期到期时间#",
            			"沉默期类型(实际)",
            			"测试期类型(实际)",
            			"卡种#",
            			"规格#",
            			"开户日期 (实际)",
            			"激活日期 (实际)",
            			"沉默期到期时间(实际)",
            			"在线状态",
            			"实际套餐#",
            			"成本单价#",
            			"当月费用",
            			"理想化成本",
            			"营收",
            			"上月消费额度",
            			"剩余额度",
            			"实际利润",
            			"理想化利润",
            			"备注#",
            			"错误信息"
    		  	};
    			// 字段映射
    			Map<String, String> fieldLabel=new HashMap<String, String>();
    			fieldLabel.put("A", "ID(不可更改)");
    			fieldLabel.put("B", "接入号");
    			fieldLabel.put("C", "ICCID");
    			fieldLabel.put("D", "IMSI");
    			fieldLabel.put("E", "支付类型");
    			fieldLabel.put("F", "运营商");
    			fieldLabel.put("G", "二级运营商");
    			fieldLabel.put("H", "客户名称");
    			fieldLabel.put("I", "卡状态");
    			fieldLabel.put("J", "套餐类型");
    			fieldLabel.put("K", "套餐");
    			fieldLabel.put("L", "当月用量");
    			fieldLabel.put("M", "套餐总量(MB)");
    			fieldLabel.put("N", "余额");
    			fieldLabel.put("O", "已发短信");
    			
    			fieldLabel.put("P", "沉默期类型");
    			fieldLabel.put("Q", "测试期类型");
    			fieldLabel.put("R", "到期日期");
    			fieldLabel.put("S", "开户日期");
    			fieldLabel.put("T", "激活日期");
    			fieldLabel.put("U", "沉默期到期时间");
    			fieldLabel.put("V", "沉默期类型(实际)");
    			fieldLabel.put("W", "测试期类型(实际)");
    			fieldLabel.put("X", "卡种");
    			fieldLabel.put("Y", "规格");
    			fieldLabel.put("Z", "开户日期 (实际)");
    			fieldLabel.put("AA", "激活日期 (实际)");
    			fieldLabel.put("AB", "沉默期到期时间(实际)");
    			fieldLabel.put("AC", "在线状态");
    			fieldLabel.put("AD", "实际套餐");
    			fieldLabel.put("AE", "成本单价");
    			fieldLabel.put("AF", "当月费用");
    			fieldLabel.put("AG", "理想化成本");
    			fieldLabel.put("AH", "营收");
    			fieldLabel.put("AI", "上月消费额度");
    			fieldLabel.put("AJ", "剩余额度");
    			fieldLabel.put("AK", "实际利润");
    			fieldLabel.put("AL", "理想化利润");
    			fieldLabel.put("AM", "备注");
    			fieldLabel.put("RESULT", "错误信息");
    			
    			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
    			bdeo.WriteExcel(false,getCon(),sql);

    			map.put("filename", filename);
    			map.put("annexeName", "卡管理导出数据.xlsx");
    			map.put("filepath", path);
        	}else{
        		// 字段映射
        		Map<String, String> fieldLabel=new HashMap<String, String>();
        		String[] colmnsList={
         			"接入号",
         			"iccid",
         			"imsi",
         			"运营商",
         			"支付类型",
         			"客户名称",
         			"卡状态",
         			"套餐类型",
         			"套餐",
         			"当月用量(MB)",
         			"套餐总量",
         			"余额 ",
         			"已发短信",
         			"沉默期类型",
         			"测试期类型",
         			"到期日期",
         			"开户日期 ",
         			"激活日期 ",
         			"沉默期到期时间",
         			"在线状态",
         			"群组",
         			"备注"
     	  	   };
        		
        		fieldLabel.put("ACCESS_NUM", "接入号");
        		fieldLabel.put("ICCID", "ICCID");
        		fieldLabel.put("IMSI", "IMSI");
        		fieldLabel.put("PROVIDER", "运营商");
        		fieldLabel.put("PAYMENT_TYPE", "支付类型");
        		fieldLabel.put("AGENCY_NAME", "客户名称");
        		fieldLabel.put("WORKING_CONDITION", "卡状态");
        		fieldLabel.put("COMBO_TYPE", "套餐类型");
        		fieldLabel.put("COMNO_NAME", "套餐");
        		fieldLabel.put("USE_GPRS", "当月用量");
        		fieldLabel.put("TOTAL_GPRS", "套餐总量");
        		fieldLabel.put("REMAIN_GPS", "余额");
        		fieldLabel.put("MESSAGE_COUNT", "已发短信");
        		fieldLabel.put("WAIT_TYPE", "沉默期类型");
        		fieldLabel.put("TEST_TYPE", "测试期类型");
        		fieldLabel.put("DEADLINE_DATE", "到期日期 ");
        		fieldLabel.put("ESTABLISH_DATE", "开户日期 ");
        		fieldLabel.put("ACTIVATE_DATE", "激活日期 ");
        		fieldLabel.put("WAIT_DATE", "沉默期到期时间");
        		fieldLabel.put("BILLING_STATUS", "在线状态");
        		fieldLabel.put("OWNER_NAME", "群组");
        		fieldLabel.put("MARK_FIRST", "备注");
        		
        		BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
        		bdeo.WriteExcel(false,getCon(),sql);
        		
        		map.put("filename", filename);
        		map.put("annexeName", "物联卡信息导出.xlsx");
        		map.put("filepath", path);
        	}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
    }
    
	/**
	 * 删除数据
	 */
	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					String  id=idsStr[i];
					XuyuContentCardMgr xuyuContentCardMgr=xuyuContentCardMgrDao.find(id);
					if(xuyuContentCardMgr!=null){
						xuyuContentCardMgrDao.del(id);
					}
					XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(id);
					if(xuyuContentCardInfo!=null){
						xuyuContentCardInfoDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 导入更新数据
	 * @throws CustomException 
	 */
	@Override
	public synchronized Map<String ,Object> updateByFile() throws CustomException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			String annexeId=request.getParameter("annexeId");
			SystemAnnexeVo systemAnnexeVo=systemAnnexeService.find(annexeId);
			iccIdManagerExcelService.anaLysisData(systemAnnexeVo.getUploadPath());
			// 删除错误的数据
			//第一步删除无效数据
			StringBuilder sb=new StringBuilder();
			sb.append(" delete from UPDATE_TEMP  where B='接入号' ");
			commonMapper.executeAction(sb.toString());
			// 获取是否有异常
			sb=new StringBuilder();
			sb.append(" select count(1) as a from UPDATE_TEMP a where 1=1 and RESULT is not null ");
			long j=commonMapper.findCount(sb.toString());
			if(j==0){
				// 开始更新数据
				sb=new StringBuilder();
				sb.append(" select * from UPDATE_TEMP a ");
				List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
				if(list.size()>0){
					logger.info("开始更新数据");
					// 更新主表
					sb=new StringBuilder();
					sb.append(" update UPDATE_TEMP t2 set  ");
					sb.append(" t2.X=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='CARD_TYPE' and t.F_VALUE=t2.X ), ");
					sb.append(" t2.Y=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='STANDARD' and t.F_VALUE=t2.Y ), ");
					sb.append(" t2.AD=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='OPERATOR_COMBO_TYPE' and t.F_VALUE=t2.AD ) ");
					commonMapper.executeAction(sb.toString());
					
					sb=new StringBuilder();
					sb.append(" update UPDATE_TEMP t2 set   ");
					sb.append(" t2.I=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='MOBILE_CARD_STATU' and t.F_VALUE=t2.I )");
					sb.append(" where  t2.F='移动' ");
					commonMapper.executeAction(sb.toString());
					
					sb=new StringBuilder();
					sb.append(" update UPDATE_TEMP t2 set  ");
					sb.append(" t2.I=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='UNICOM_CARD_STATU' and t.F_VALUE=t2.I )");
					sb.append(" where t2.F='联通' ");
					commonMapper.executeAction(sb.toString());
					
					sb=new StringBuilder();
					sb.append(" update UPDATE_TEMP t2 set  ");
					sb.append(" t2.I=(select t.F_CODE from SYSTEM_LOOKUP_ITEM t where t.F_LOOKUP_ID='TELECOM_CARD_STATU' and t.F_VALUE=t2.I ) ");
					sb.append(" where t2.F='电信' ");
					commonMapper.executeAction(sb.toString());
					
					sb=new StringBuilder();
					sb.append(" update  XUYU_CONTENT_CARD_MGR t1,XUYU_CONTENT_CARD_INFO t2 ,UPDATE_TEMP t set ");
					sb.append(" t1.ACCESS_NUM=t.B, ");
					sb.append(" t1.ICCID=t.C, ");
					sb.append(" t1.IMSI=t.D, ");

					sb.append(" t1.DEADLINE_DATE=STR_TO_DATE(if(t.R='', null , left(t.R,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.ESTABLISH_DATE=STR_TO_DATE(if(t.S='', null , left(t.S,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.ACTIVATE_DATE= STR_TO_DATE(if(t.T='', null , left(t.T,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.WAIT_DATE=STR_TO_DATE(if(t.U='', null , left(t.U,10) ),'%Y-%m-%d'), ");

					sb.append(" t1.MARK=t.AM, ");

					sb.append(" t1.WORKING_CONDITION=t.I, ");
					sb.append(" t1.CARD_TYPE=t.X, ");
					sb.append(" t1.STANDARD=t.Y, ");
					sb.append(" t1.REAL_COMBOTYPE=t.AD, ");

				    sb.append(" t1.UNIT_COST=IF ( t.AE = '',NULL,t.AE) , ");

				    sb.append(" t1.USE_GPRS=if(t.L='', null , t.L), ");
				    sb.append(" t1.TOTAL_GPRS=if(t.M='', null , t.M), ");
				    sb.append(" t1.REMAIN_GPS=if(t.M='', null , t.M)-if(t.L='', 0 , t.L)  ");
				    sb.append(" where t2.ACCESS_NUM=t1.ACCESS_NUM ");
				    sb.append(" and t.A = t2.ID ");
					commonMapper.executeAction(sb.toString());
					
					sb=new StringBuilder();
					sb.append(" update  XUYU_CONTENT_CARD_INFO t1,UPDATE_TEMP t set ");
					sb.append(" t1.ACCESS_NUM=t.B, ");
					sb.append(" t1.ICCID=t.C, ");
					sb.append(" t1.IMSI=t.D, ");

					sb.append(" t1.DEADLINE_DATE=STR_TO_DATE(if(t.R='', null , left(t.R,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.ESTABLISH_DATE=STR_TO_DATE(if(t.S='', null , left(t.S,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.ACTIVATE_DATE= STR_TO_DATE(if(t.T='', null , left(t.T,10) ),'%Y-%m-%d'), ");
					sb.append(" t1.WAIT_DATE=STR_TO_DATE(if(t.U='', null , left(t.U,10) ),'%Y-%m-%d'), ");

					sb.append(" t1.MARK=t.AM, ");

					sb.append(" t1.WORKING_CONDITION=t.I, ");
					sb.append(" t1.CARD_TYPE=t.X, ");
					sb.append(" t1.STANDARD=t.Y, ");
					sb.append(" t1.REAL_COMBOTYPE=t.AD, ");

				    sb.append(" t1.UNIT_COST=IF ( t.AE = '',NULL,t.AE) , ");

				    sb.append(" t1.USE_GPRS=if(t.L='', null , t.L), ");
				    sb.append(" t1.TOTAL_GPRS=if(t.M='', null , t.M), ");
				    sb.append(" t1.REMAIN_GPS=if(t.M='', null , t.M)-if(t.L='', 0 , t.L)  ");
				    sb.append(" where  t.A=t1.id ");
					commonMapper.executeAction(sb.toString());
					
					
//					ConcurrentHashMap<String, String> mapCardType=new ConcurrentHashMap<String, String>();
//					ConcurrentHashMap<String, String> mapStandard=new ConcurrentHashMap<String, String>();
//					ConcurrentHashMap<String, String> mapMobile=new ConcurrentHashMap<String, String>();
//					ConcurrentHashMap<String, String> mapUnicom=new ConcurrentHashMap<String, String>();
//					ConcurrentHashMap<String, String> mapTelecom=new ConcurrentHashMap<String, String>();
//					ConcurrentHashMap<String, String> mapRealCombotype=new ConcurrentHashMap<String, String>();
//					
//					mapCardType=LookupManager.getInstance().getKeys("CARD_TYPE");
//					mapStandard=LookupManager.getInstance().getKeys("STANDARD");
//					mapMobile=LookupManager.getInstance().getKeys("MOBILE_CARD_STATU");
//					mapUnicom=LookupManager.getInstance().getKeys("UNICOM_CARD_STATU");
//					mapTelecom=LookupManager.getInstance().getKeys("TELECOM_CARD_STATU");
//					mapRealCombotype=LookupManager.getInstance().getKeys("OPERATOR_COMBO_TYPE");
//					
//					SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
//					String accessNum=SystemConstants.STRINGEMPTY;
//					String id=SystemConstants.STRINGEMPTY;
//					for (Map<String, Object> map2 : list) {
//						id=SystemConstants.STRINGEMPTY+map2.get("A");
//						XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.findById(id);
//						if(xuyuContentCardInfo!=null){
//							// 然后更新主表
//							xuyuContentCardInfo.setAccessNum(SystemConstants.STRINGEMPTY+map2.get("B"));
//							xuyuContentCardInfo.setIccid(SystemConstants.STRINGEMPTY+map2.get("C"));
//							xuyuContentCardInfo.setImsi(SystemConstants.STRINGEMPTY+map2.get("D"));
//							
//							if(map2.get("R")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("R"))){
//								xuyuContentCardInfo.setDeadlineDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("R")));
//							}
//							if(map2.get("S")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("S"))){
//								xuyuContentCardInfo.setEstablishDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("S")));
//							}
//							if(map2.get("T")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("T"))){
//								xuyuContentCardInfo.setActivateDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("T")));
//							}
//							if(map2.get("U")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("U"))){
//								xuyuContentCardInfo.setWaitDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("U")));
//							}
//							if(map2.get("AM")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AM"))){
//								xuyuContentCardInfo.setMark(SystemConstants.STRINGEMPTY+map2.get("AM"));
//							}
//							if(map2.get("I")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("I"))){
//								// 卡状态更新
//								if("1".equals(xuyuContentCardInfo.getProvider())){
//									xuyuContentCardInfo.setWorkingCondition(mapMobile.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//								}else if("2".equals(xuyuContentCardInfo.getProvider())){
//									xuyuContentCardInfo.setWorkingCondition(mapUnicom.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//								}else if("3".equals(xuyuContentCardInfo.getProvider())){
//									xuyuContentCardInfo.setWorkingCondition(mapTelecom.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//								}
//								
//							}
//							if(map2.get("X")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("X"))){
//								// 卡种
//								xuyuContentCardInfo.setCardType(mapCardType.get(SystemConstants.STRINGEMPTY+map2.get("X")));
//							}
//							if(map2.get("Y")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("Y"))){
//								// 规格
//								xuyuContentCardInfo.setStandard(mapStandard.get(SystemConstants.STRINGEMPTY+map2.get("Y")));
//							}
//							if(map2.get("AD")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AD"))){
//								xuyuContentCardInfo.setRealCombotype(mapRealCombotype.get(SystemConstants.STRINGEMPTY+map2.get("AD")));
//							}
//							if(map2.get("AE")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AE"))){
//								BigDecimal unitCost=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map2.get("AE")));
//								xuyuContentCardInfo.setUnitCost(unitCost);
//							}
//							BigDecimal useGprs=new BigDecimal(0.00);
//							BigDecimal totalGprs=new BigDecimal(0.00);
//							if(map2.get("L")!=null){
//								 useGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map2.get("L")));
//							}
//							
//							xuyuContentCardInfo.setUseGprs(useGprs);
//							if(map2.get("M")!=null){
//								totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map2.get("M")));
//							}
//							xuyuContentCardInfo.setTotalGprs(totalGprs);
//							// 判断是否有群组，有群组直接算月份减
//							// 无群组直接减
//							if(xuyuContentCardInfo.getOwner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getOwner())){
//								BigDecimal remainGps=BigDecimal.valueOf(Long.valueOf(xuyuContentCardInfo.getComnoName())).subtract(xuyuContentCardInfo.getUseGprs());
//								xuyuContentCardInfo.setRemainGps(remainGps);
//							}else{
//								// 首先判定是否超过一个月
//								// 第一步判断是否为联通的卡
//								if(xuyuContentCardInfo.getProvider().equals("2")){
//									sb=new StringBuilder();
//									if(xuyuContentCardInfo.getActivateDateRestart()!=null){
//										String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
//										
//										int month=1;
//										sb.append(" select case when date_format('"+dateNow+"','%d')>26 then ");
//										sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+month+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
//										sb.append(" else  ");
//										sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+(month-1)+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
//										sb.append(" END  as  DATE from dual ");
//										Map<String, Object> map1=commonMapper.findOneData(sb.toString());
//										String result=SystemConstants.STRINGEMPTY+map1.get("DATE");
//										int result1=format.format(new Date()).compareTo(result);
//										if(result1>0){
//											// 把历史中数据都拿过来
//											// 计算累计流量
//											sb=new StringBuilder();
//											sb.append(" select SUM(t3.USE_GPRS) as PRE_USE_GPRS");
//											sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
//											sb.append(" and t3.ACCESS_NUM ="+xuyuContentCardInfo.getAccessNum());
//											sb.append(" and t3.DOWNLOAD_DATE >="+dateNow);
//											sb.append(" and t3.DOWNLOAD_DATE=date_format(DATE_ADD(DATE_ADD((t3.DOWNLOAD_DATE),INTERVAL 0 month),interval -day(t3.DOWNLOAD_DATE)+26 day),'%Y-%m-%d') ");
//											Map<String, Object> mapPre=commonMapper.findOneData(sb.toString());
//											if(mapPre!=null){
//												String useGprsPre=SystemConstants.STRINGEMPTY+mapPre.get("PRE_USE_GPRS");
//												BigDecimal preUseGprs=BigDecimal.valueOf(Double.valueOf(useGprsPre));
//												BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs()).subtract(preUseGprs);
//												xuyuContentCardInfo.setRemainGps(remainGps);
//											}else{
//												BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//												xuyuContentCardInfo.setRemainGps(remainGps);
//											}
//										}else{
//											BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//											xuyuContentCardInfo.setRemainGps(remainGps);
//										}
//									}else{
//										BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//										xuyuContentCardInfo.setRemainGps(remainGps);
//									}
//									
//								}else{
//									if(xuyuContentCardInfo.getActivateDateRestart()!=null){
//										String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
//										
//										int month=0;
//									    sb=new StringBuilder();
//										sb.append(" select  last_day(DATE_ADD('"+dateNow+"',INTERVAL "+month+" month)) as DATE from dual ");
//										Map<String, Object> map1=commonMapper.findOneData(sb.toString());
//										String result=SystemConstants.STRINGEMPTY+map1.get("DATE");
//										int result1=format.format(new Date()).compareTo(result);
//										if(result1>0){
//											// 计算累计流量
//											sb=new StringBuilder();
//											sb.append(" select SUM(t3.USE_GPRS) as PRE_USE_GPRS");
//											sb.append(" from XUYU_CONTENT_CARD_INFO_IMPORT t3 where 1=1 ");
//											sb.append(" and t3.ACCESS_NUM ="+xuyuContentCardInfo.getAccessNum());
//											sb.append(" and t3.DOWNLOAD_DATE >="+dateNow);
//											sb.append(" and t3.DOWNLOAD_DATE=LAST_DAY(t3.DOWNLOAD_DATE) ");
//											Map<String, Object> mapPre=commonMapper.findOneData(sb.toString());
//											if(mapPre!=null){
//												String useGprsPre=SystemConstants.STRINGEMPTY+mapPre.get("PRE_USE_GPRS");
//												BigDecimal preUseGprs=BigDecimal.valueOf(Double.valueOf(useGprsPre));
//												BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs()).subtract(preUseGprs);
//												xuyuContentCardInfo.setRemainGps(remainGps);
//											}else{
//												BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//												xuyuContentCardInfo.setRemainGps(remainGps);
//											}
//										}else{
//											BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//											xuyuContentCardInfo.setRemainGps(remainGps);
//										}
//									}else{
//										BigDecimal remainGps=xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getUseGprs());
//										xuyuContentCardInfo.setRemainGps(remainGps);
//									}
//									
//								}
//							}
//							xuyuContentCardInfoDao.update(xuyuContentCardInfo);
//							// 首先更新附表
//							accessNum=xuyuContentCardInfo.getAccessNum();
//							XuyuContentCardMgr xuyuContentCardMgr=xuyuContentCardMgrDao.find(accessNum);
//							if(xuyuContentCardMgr!=null){
//								xuyuContentCardMgr.setAccessNum(SystemConstants.STRINGEMPTY+map2.get("B"));
//								xuyuContentCardMgr.setIccid(SystemConstants.STRINGEMPTY+map2.get("C"));
//								xuyuContentCardMgr.setImsi(SystemConstants.STRINGEMPTY+map2.get("D"));
//								
//								if(map2.get("R")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("R"))){
//									xuyuContentCardMgr.setDeadlineDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("R")));
//								}
//								if(map2.get("S")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("S"))){
//									xuyuContentCardMgr.setEstablishDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("S")));
//								}
//								if(map2.get("T")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("T"))){
//									xuyuContentCardMgr.setActivateDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("T")));
//								}
//								if(map2.get("U")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("U"))){
//									xuyuContentCardMgr.setWaitDate(format.parse(SystemConstants.STRINGEMPTY+map2.get("U")));
//								}
//								if(map2.get("AM")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AM"))){
//									xuyuContentCardMgr.setMark(SystemConstants.STRINGEMPTY+map2.get("AM"));
//								}
//								if(map2.get("I")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("I"))){
//									// 卡状态更新
//									if("1".equals(xuyuContentCardMgr.getProvider())){
//										xuyuContentCardMgr.setWorkingCondition(mapMobile.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//									}else if("2".equals(xuyuContentCardMgr.getProvider())){
//										xuyuContentCardMgr.setWorkingCondition(mapUnicom.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//									}else if("3".equals(xuyuContentCardMgr.getProvider())){
//										xuyuContentCardMgr.setWorkingCondition(mapTelecom.get(SystemConstants.STRINGEMPTY+map2.get("I")));
//									}
//									
//								}
//								if(map2.get("X")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("X"))){
//									// 卡种
//									xuyuContentCardMgr.setCardType(mapCardType.get(SystemConstants.STRINGEMPTY+map2.get("X")));
//								}
//								if(map2.get("Y")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("Y"))){
//									// 规格
//									xuyuContentCardMgr.setStandard(mapStandard.get(SystemConstants.STRINGEMPTY+map2.get("Y")));
//								}
//								if(map2.get("AD")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AD"))){
//									xuyuContentCardMgr.setRealCombotype(mapRealCombotype.get(SystemConstants.STRINGEMPTY+map2.get("AD")));
//								}
//								if(map2.get("AE")!=null&&!SystemConstants.STRINGEMPTY.equals(map2.get("AE"))){
//									BigDecimal unitCost=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map2.get("AE")));
//									xuyuContentCardMgr.setUnitCost(unitCost);
//								}
//								xuyuContentCardMgr.setUseGprs(useGprs);
//								xuyuContentCardMgr.setTotalGprs(totalGprs);
//								xuyuContentCardMgr.setRemainGps(xuyuContentCardInfo.getRemainGps());
//								xuyuContentCardMgrDao.update(xuyuContentCardMgr);
//							}
//						}
//					}
				}
				map.put("sucess", true);
			}else{
				// 生成附件
				sb=new StringBuilder();
				sb.append(" select A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,RESULT");
				sb.append(" from UPDATE_TEMP t1 ");
				logger.info("下载的sql:"+sb.toString());
				map=createFile(sb.toString(),true);
				map.put("sucess", false);
			}
		}catch(DuplicateKeyException e){
			e.printStackTrace();
			throw new CustomException("接入号输入错误,接入号已经存在或者重复");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("导入失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> syncExportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,Map<String, Object> mapSet) {

		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t2.ACCESS_NUM,t2.ICCID,t2.IMSI, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t2.PROVIDER) as PROVIDER,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='PAY_TYPE'  and a1.F_CODE=t2.PAYMENT_TYPE) as PAYMENT_TYPE,	");
	        sb.append(" t2.AGENCY_NAME, ");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  end WORKING_CONDITION, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t2.COMBO_TYPE) as COMBO_TYPE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_NAME'  and a1.F_CODE=t2.COMNO_NAME) as COMNO_NAME,");
	        sb.append(" 	t2.USE_GPRS,t2.COMNO_NAME AS TOTAL_GPRS,t2.REMAIN_GPS,t2.MESSAGE_COUNT,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='SILENT_TYPE'  and a1.F_CODE=t2.WAIT_TYPE) as WAIT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='HAVE_TEST'  and a1.F_CODE=t2.TEST_TYPE) as TEST_TYPE,	");
	        sb.append(" t2.DEADLINE_DATE,");
	        sb.append(" t2.ESTABLISH_DATE,");
	        sb.append("	t2.ACTIVATE_DATE,");
	        sb.append(" t2.WAIT_DATE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='ONLINE_STATU'  and a1.F_CODE=t2.BILLING_STATUS) as BILLING_STATUS,");
	        sb.append("  t3.OWNER_NAME,t2.MARK_FIRST from (select * from XUYU_CONTENT_CARD_INFO WHERE 1=1 ");
			String orgId=systemUser.getOrgCode();
	        sb.append(" and EXISTS ( SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+orgId+"%' and t1.ID=AGENCY ) ");
	        if(xuyuContentCardMgrSelfVo!=null){
	        	if(xuyuContentCardMgrSelfVo.getKeywords()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getKeywords())){
	 	        	sb.append(" AND ( ACCESS_NUM like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or ICCID like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or IMSI like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%'  )");
	 	         }
	        	 if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
	        		 SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
	        		 sb.append(" AND EXISTS (SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+systemOrgVo.getOrgId()+"%' and t1.ID=AGENCY) ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getPaymentType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getPaymentType())){
	        		 sb.append(" AND PAYMENT_TYPE='"+xuyuContentCardMgrSelfVo.getPaymentType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getProvider()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getProvider())){
	        		 sb.append(" AND PROVIDER='"+xuyuContentCardMgrSelfVo.getProvider()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwnerPlace()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwnerPlace())){
	        		 sb.append(" AND OWNER_PLACE='"+xuyuContentCardMgrSelfVo.getOwnerPlace()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboType())){
	        		 sb.append(" AND COMBO_TYPE='"+xuyuContentCardMgrSelfVo.getComboType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboName()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboName())){
	        		 sb.append("  AND COMNO_NAME='"+xuyuContentCardMgrSelfVo.getComboName()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRealComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRealComboType())){
	        		 sb.append("  AND REAL_COMBOTYPE='"+xuyuContentCardMgrSelfVo.getRealComboType()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getWorkingCondition()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getWorkingCondition())){
	        		 sb.append("  AND WORKING_CONDITION='"+xuyuContentCardMgrSelfVo.getWorkingCondition()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getBillingStatus()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getBillingStatus())){
	        		 sb.append("  AND BILLING_STATUS='"+xuyuContentCardMgrSelfVo.getBillingStatus()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateStart())){
	        		 sb.append("  AND ESTABLISH_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateEnd())){
	        		 sb.append("  AND ESTABLISH_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateStart())){
	        		 sb.append("  AND ACTIVATE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateEnd())){
	        		 sb.append("  AND ACTIVATE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateStart())){
	        		 sb.append("  AND DEADLINE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateEnd())){
	        		 sb.append("  AND DEADLINE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumStart())){
	        		 sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrSelfVo.getAccessNumStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumEnd())){
	        		 sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrSelfVo.getAccessNumEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidStart())){
	        		 sb.append("  AND ICCID>='"+xuyuContentCardMgrSelfVo.getIccidStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidEnd())){
	        		 sb.append("  AND ICCID<='"+xuyuContentCardMgrSelfVo.getIccidEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiStart())){
	        		 sb.append("  AND IMSI>='"+xuyuContentCardMgrSelfVo.getImsiStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiEnd())){
	        		 sb.append("  AND IMSI<='"+xuyuContentCardMgrSelfVo.getImsiEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getMarkFirst()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getMarkFirst())){
	        		 sb.append("  AND MARK_FIRST like '"+xuyuContentCardMgrSelfVo.getMarkFirst()+"%'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwner())){
	        		 sb.append("  AND OWNER= '"+xuyuContentCardMgrSelfVo.getOwner()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRemainGpsType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGpsType())){
	        		 if(xuyuContentCardMgrSelfVo.getRemainGps()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGps())){
	        			 sb.append("  AND REMAIN_GPS  "+xuyuContentCardMgrSelfVo.getRemainGpsType()+"  cast('"+xuyuContentCardMgrSelfVo.getRemainGps()+"' as decimal(16,4)) ");
	        		 }
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIsowner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        		 if("y".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND !(ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        		 if("n".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND (ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        	 }
	        	 
	        }
	       
	        sb.append("  ) t2  left JOIN XUYU_OWNER_INFO t3 ON t2.`OWNER` = t3.ID   ");
	        sb.append(" order by t2.ACCESS_NUM,t2.IMSI,t2.ICCID ");
	        logger.info("下载的sql:"+sb.toString());

    		String sheetName="Sheet";
			String path=SystemConstants.STRINGEMPTY+mapSet.get("filepath");
			String filename=SystemConstants.STRINGEMPTY+mapSet.get("filename");
	        String filepath = path+filename;

	        String[] colmnsList={
         			"接入号",
         			"iccid",
         			"imsi",
         			"运营商",
         			"支付类型",
         			"客户名称",
         			"卡状态",
         			"套餐类型",
         			"套餐",
         			"当月用量(MB)",
         			"套餐总量",
         			"余额 ",
         			"已发短信",
         			"沉默期类型",
         			"测试期类型",
         			"到期日期",
         			"开户日期 ",
         			"激活日期 ",
         			"沉默期到期时间",
         			"在线状态",
         			"群组",
         			"备注"
     	  	   };
	        Map<String, String> fieldLabel=new HashMap<String, String>();
	        
    		fieldLabel.put("ACCESS_NUM", "接入号");
    		fieldLabel.put("ICCID", "ICCID");
    		fieldLabel.put("IMSI", "IMSI");
    		fieldLabel.put("PROVIDER", "运营商");
    		fieldLabel.put("PAYMENT_TYPE", "支付类型");
    		fieldLabel.put("AGENCY_NAME", "客户名称");
    		fieldLabel.put("WORKING_CONDITION", "卡状态");
    		fieldLabel.put("COMBO_TYPE", "套餐类型");
    		fieldLabel.put("COMNO_NAME", "套餐");
    		fieldLabel.put("USE_GPRS", "当月用量");
    		fieldLabel.put("TOTAL_GPRS", "套餐总量");
    		fieldLabel.put("REMAIN_GPS", "余额");
    		fieldLabel.put("MESSAGE_COUNT", "已发短信");
    		fieldLabel.put("WAIT_TYPE", "沉默期类型");
    		fieldLabel.put("TEST_TYPE", "测试期类型");
    		fieldLabel.put("DEADLINE_DATE", "到期日期 ");
    		fieldLabel.put("ESTABLISH_DATE", "开户日期 ");
    		fieldLabel.put("ACTIVATE_DATE", "激活日期 ");
    		fieldLabel.put("WAIT_DATE", "沉默期到期时间");
    		fieldLabel.put("BILLING_STATUS", "在线状态");
    		fieldLabel.put("OWNER_NAME", "群组");
    		fieldLabel.put("MARK_FIRST", "备注");
			
			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
			bdeo.WriteExcel(false,getCon(),sb.toString());
			
			// 结束以后把异步任务表改回来
			String id=SystemConstants.STRINGEMPTY+mapSet.get("id");
			XuyuDown down=xuyuDownService.find(id);
			down.setStatu(SystemConstants.STRING_YES);
			xuyuDownService.saveInfo(down);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapSet;
	}
    
	/**
	 * 在线状态查询
	 */
	@Override
	public void gprsStatusQuery(XuyuMessageLogVo xuyuMessageLogVo) throws CustomException {

		String accessNums=xuyuMessageLogVo.getAccessNums();
		String billingStatus=SystemConstants.STRINGEMPTY;
		if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
			XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNums);
			if(contentCardInfo!=null){
				SynInfoFacadeService synInfoFacadeService=SynInfoFacadeService.getInstance();
				SynInfoJSFacadeService synInfoJSFacadeService=SynInfoJSFacadeService.getInstance();
				String ownerPlace=contentCardInfo.getOwnerPlace();
				// 判定是否存在二级运营商
				if(ownerPlace==null||SystemConstants.STRINGEMPTY.equals(ownerPlace)){
					// 判定运营商
					if("1".equals(contentCardInfo.getProvider())){// 移动
					}else if("2".equals(contentCardInfo.getProvider())){//联通
					}else if("3".equals(contentCardInfo.getProvider())){//电信
					}
				}else{
					// 分别判定
					if("1".equals(ownerPlace)){// 淮安
					}else if("2".equals(ownerPlace)){// 盐城
					}else{// 其他
						billingStatus=synInfoFacadeService.mobileGPRSStatusQuery(contentCardInfo.getAccessNum());
						if(billingStatus.equals("00")){
							billingStatus="02";
						}
					}
				}
				if(!SystemConstants.STRINGEMPTY.equals(billingStatus)){
					try {
						contentCardInfo.setBillingStatus(billingStatus);
						xuyuContentCardInfoDao.update(contentCardInfo);
					} catch (Exception e){
						e.printStackTrace();
						throw new CustomException("用户在线状态更新失败");
					}
				}
			}
			
		}
	
	}

}
