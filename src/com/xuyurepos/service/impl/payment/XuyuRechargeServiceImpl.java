package com.xuyurepos.service.impl.payment;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.analysis.BigDataExcelOutWrite;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.DateUtil;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.dao.payment.XuyuRechargeDao;
import com.xuyurepos.entity.comm.Params;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.entity.payment.XuyuRecharge;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.service.payment.XuyuRechargeService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;
import com.xuyurepos.vo.payment.XuyuRechargeResultVo;
import com.xuyurepos.vo.payment.XuyuRechargeVo;
import com.xuyurepos.vo.system.SystemOrgVo;
/**
 * 充值处理实现类
 * @author yangfei
 */
@Service
@Transactional
public class XuyuRechargeServiceImpl implements XuyuRechargeService{
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuRechargeServiceImpl.class);
	
	@Autowired
	private  XuyuRechargeDao xuyuRechargeDao;
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private IccIdManagerService iccIdManagerService; 
	
	
	public Connection getCon() {  
	    Connection connection = null;  
	    SqlSession sqlSession = sqlSessionFactory.openSession();  
	    connection = sqlSession.getConnection();  
	    return connection;  
	} 
    
	/**
	 * 保存方法
	 * @throws CustomException 
	 */
	@Override
	public void saveInfo(XuyuRechargeVo xuyuRechargeVo) throws Exception {
		logger.info(" saveInfo(XuyuRechargeVo xuyuRechargeVo) XuyuRechargeServiceImpl start");
		logger.info("xuyuRechargeVo:"+xuyuRechargeVo.toString());
		if(xuyuRechargeVo.getId()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getId())){
		}else{
			add(xuyuRechargeVo);
		}
		logger.info(" saveInfo(XuyuRechargeVo xuyuRechargeVo) XuyuRechargeServiceImpl end");
	}
	
	/**
	 * 	充值后实际充值操作
	 */
	public void payNotify(String out_trade_no){
		logger.info("充值成功后回调："+out_trade_no);
        try {
    		XuyuRecharge xuyuRecharge=xuyuRechargeDao.find(out_trade_no);
    		if(xuyuRecharge!=null){
    			if(SystemConstants.STRING_YES.equals(xuyuRecharge.getPayCommission())){
    				throw new CustomException("重复处理");
    			}
    			xuyuRecharge.setPayCommission(SystemConstants.STRING_YES);
    			xuyuRechargeDao.update(xuyuRecharge);
    		}else{
    			logger.error("订单号错误："+out_trade_no);
    			throw new CustomException("订单号错误");
    		}
    		// 首先判断是否为预充值
    		if(SystemConstants.STRING_YES.equals(xuyuRecharge.getYc())){
    			// 预充值啥也不需要做
    		}else{
    			XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(xuyuRecharge.getAccessNum());
        		BigDecimal totalGprs=xuyuRecharge.getTotalGprs();
        		Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				// 判断是否为加油包
				if(SystemConstants.STRING_YES.equals(xuyuRecharge.getComeon())){
					// 加油包啥也不做直接加总量
					xuyuContentCardInfo.setTotalGprs(xuyuContentCardInfo.getTotalGprs().add(totalGprs));
					xuyuContentCardInfo.setRemainGps(xuyuContentCardInfo.getRemainGps().add(totalGprs));
				}else{
					// 设置套餐总量
	        		// 预充值只需要直接清空当前流量
	        		// 所以直接设置总额就行
	        		// 直接设置套餐的总量
					xuyuContentCardInfo.setComboType(xuyuRecharge.getComboType());
					xuyuContentCardInfo.setComnoName(xuyuRecharge.getComnoName());
					// 首先判断剩余流量是否大于等于0
					if(xuyuContentCardInfo.getRemainGps()!=null){
						int cam=xuyuContentCardInfo.getRemainGps().compareTo(BigDecimal.ZERO); 
						if(cam==-1){ 
							// 那计算总量就是目前的总量减去之前欠费的
							totalGprs=totalGprs.add(xuyuContentCardInfo.getRemainGps());
						}
					}
					// 首先同步下流量
					// 首先同步下流量
					String gprs=SystemConstants.STRINGEMPTY;
					try {
						XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(xuyuContentCardInfo.getAccessNum());
						gprs=iccIdManagerService.getGPRS(contentCardInfo);
						gprs=gprs.replace("MB", "");
					} catch (Exception e) {
						logger.error("同步流量发生错误："+e.getMessage());
					}
					if(SystemConstants.STRINGEMPTY.equals(gprs)){
						if(xuyuContentCardInfo.getUseGprs()!=null){
							gprs=""+xuyuContentCardInfo.getUseGprs();
						}else{
							gprs="0.00";
						}
					}
					xuyuContentCardInfo.setChargeGprs(BigDecimal.valueOf(Double.valueOf(gprs)));
					xuyuContentCardInfo.setTotalGprs(totalGprs);
					xuyuContentCardInfo.setRemainGps(totalGprs);
					xuyuContentCardInfo.setActivateDateRestart(calendar.getTime());
					
					String testType=xuyuContentCardInfo.getTestType();
					String waitType=xuyuContentCardInfo.getWaitType();
					// 没有测试期和成默契直接计算
					if((SystemConstants.STRINGEMPTY.equals(testType)
							   &&SystemConstants.STRINGEMPTY.equals(waitType))||(xuyuContentCardInfo.getDeadlineDate()!=null)){
						try {
							SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
							// 通过激活日期计算到期日
						    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
						    int month=Integer.valueOf(xuyuContentCardInfo.getComboType());
						    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
						    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
						}catch (Exception e) {
							e.printStackTrace();
							throw new CustomException("满期日计算失败");
						}
					}else{
						// 如果沉默期和测试期存在的情况
						// 则需要判断是否激活
						if(xuyuContentCardInfo.getActivateDate()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getActivateDate())){
							try {
								SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
								// 通过激活日期计算到期日
							    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
							    int month=Integer.valueOf(xuyuContentCardInfo.getComboType());
							    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
							    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
							}catch (Exception e) {
								e.printStackTrace();
								throw new CustomException("满期日计算失败");
							}
						}
					}
				}
        		
        		xuyuContentCardInfoDao.update(xuyuContentCardInfo);
    		}
		} catch (Exception e) {
			logger.error("回调失败："+e.getMessage());
			e.printStackTrace();
		}
		
	}
    
	/**
	 * 添加数据
	 * @param xuyuRechargeVo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized void add(XuyuRechargeVo xuyuRechargeVo) throws Exception {
		try {
			XuyuRecharge xuyuRecharge=null;
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			String orgId=systemUser.getOrgId();
			// 查找机构信息
			SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
			// 判断是否为卡段充值
			if(SystemConstants.STRING_YES.equals(xuyuRechargeVo.getYesNo())){
				if(systemOrgVo.getOrgLevel().equals("1")){
					int result=0;
					result=xuyuContentCardInfoDao.findCount(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getNumType());
					logger.info("result:"+result);
					if(result>100000){
						throw new CustomException("一次充值最多100000张卡");
					}else{
						// 首先判断数据有没有设置套餐
						result=xuyuContentCardInfoDao.findComboType(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getNumType());
						if(result>0){
							throw new CustomException("还未设置套餐，请先设置套餐");
						}
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(new Date());
						xuyuRecharge=new XuyuRecharge();
						xuyuRechargeVo.setIsPay(SystemConstants.STRING_YES);
						xuyuRechargeVo.setCreateUser(systemUser.getUserName());
						xuyuRechargeVo.setCreateOrg(systemUser.getOrgId());
						HashMap map=new HashMap();
						map.put("record", xuyuRechargeVo);
						xuyuRechargeDao.insertAccs(map);
						// 更新套餐总量
						xuyuContentCardInfoDao.updateTotalGprsAdd(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						xuyuContentCardInfoDao.updateTotalGprs(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						// 插入并且更新临时表
						map=new HashMap();
						map.put("accessNumStart", xuyuRechargeVo.getAccessNumStart());
						map.put("accessNumEnd", xuyuRechargeVo.getAccessNumEnd());
						xuyuContentCardInfoDao.truncateTable();
						xuyuContentCardInfoDao.insertInfoOwnerTemp(map);
						// 更新满期日
						xuyuContentCardInfoDao.updateDeadlineDateAdd(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						xuyuContentCardInfoDao.updateDeadlineDate(xuyuRechargeVo.getAccessNumStart(), xuyuRechargeVo.getAccessNumEnd(),xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
					}
				}else{
					throw new CustomException("超出当前用户权限，只有总部用户才能充值");
				}
			}else{
				if(systemOrgVo.getOrgLevel().equals("1")){
					String accessNums=xuyuRechargeVo.getAccessNums();
					
					if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(new Date());
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
//							XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(accessNumStr[i]);
//							xuyuRecharge=new XuyuRecharge();
//							xuyuRecharge.setId(createId()+i);
//							// 设置后台充值
//							BigDecimal money=xuyuContentCardInfo.getUnitCost().multiply(BigDecimal.valueOf(Long.valueOf(xuyuRechargeVo.getChargeCost())));
//							if(xuyuContentCardInfo.getComboType()==null||SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getComboType())){
//								throw new CustomException("还未设置套餐，请先设置套餐");
//							}
//							BigDecimal totalGprs=BigDecimal.valueOf(Long.valueOf(xuyuContentCardInfo.getComboType())).multiply(BigDecimal.valueOf(Long.valueOf(xuyuContentCardInfo.getComnoName()))).multiply(BigDecimal.valueOf(Long.valueOf(xuyuRechargeVo.getChargeCost())));
//							xuyuRecharge.setAccessNum(xuyuContentCardInfo.getAccessNum());
//							xuyuRecharge.setComboType(xuyuContentCardInfo.getComboType());
//							xuyuRecharge.setComnoName(xuyuContentCardInfo.getComnoName());
//							xuyuRecharge.setTotalGprs(totalGprs);
//							xuyuRecharge.setPrice(xuyuContentCardInfo.getUnitCost());
//							xuyuRecharge.setMoney(money);
//							xuyuRecharge.setIsPay(SystemConstants.STRING_YES);
//							xuyuRecharge.setCreateUser(systemUser.getUserName());
//							xuyuRecharge.setCreateOrg(systemUser.getOrgId());
//							xuyuRecharge.setCreateDate(calendar.getTime());
//							
//							xuyuRecharge.setIccid(xuyuContentCardInfo.getIccid());
//							xuyuRecharge.setProvider(xuyuContentCardInfo.getProvider());
//							xuyuRecharge.setCustomer(xuyuContentCardInfo.getAgency());
//							xuyuRecharge.setComeon(SystemConstants.STRING_NO);
//							xuyuRecharge.setYc(SystemConstants.STRING_NO);
//							xuyuRechargeDao.insert(xuyuRecharge);
//							// 设置套餐总量
//							// 如果套餐总量为空则直接设置
//							if(xuyuContentCardInfo.getTotalGprs()==null||SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getTotalGprs())){
//								xuyuContentCardInfo.setTotalGprs(totalGprs);
//							}else{
//								// 如果已经存在套餐则需要累加
//								BigDecimal totalGprsNew=xuyuContentCardInfo.getTotalGprs().add(totalGprs);
//								xuyuContentCardInfo.setTotalGprs(totalGprsNew);
//							}
//							String testType=xuyuContentCardInfo.getTestType();
//							String waitType=xuyuContentCardInfo.getWaitType();
//							// 没有测试期和沉默期直接计算
//							if(SystemConstants.STRINGEMPTY.equals(testType)
//									   &&SystemConstants.STRINGEMPTY.equals(waitType)){
//								try {
//									SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//									// 有沉默期则计算沉默期到期时间
//									if(xuyuContentCardInfo.getDeadlineDate()==null){
//										// 通过激活日期计算到期日
//									    String dateNow=format.format(xuyuContentCardInfo.getActivateDate());
//									    int month=Integer.valueOf(xuyuRechargeVo.getChargeCost())*Integer.valueOf(xuyuContentCardInfo.getComboType());
//									    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
//									    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
//									}else{
//										// 到期日直接增加
//									    String dateNow=format.format(xuyuContentCardInfo.getDeadlineDate());
//									    int month=Integer.valueOf(xuyuRechargeVo.getChargeCost())*Integer.valueOf(xuyuContentCardInfo.getComboType());
//									    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
//									    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
//									}
//								}catch (Exception e) {
//									e.printStackTrace();
//									throw new CustomException("满期日计算失败");
//								}
//							}else{
//								// 如果沉默期和测试期存在的情况
//								// 则判断是否激活
//								if(xuyuContentCardInfo.getActivateDate()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getActivateDate())){
//									try {
//										SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//										// 有沉默期则计算沉默期到期时间
//										if(xuyuContentCardInfo.getDeadlineDate()==null){
//											// 通过激活日期计算到期日
//										    String dateNow=format.format(xuyuContentCardInfo.getActivateDate());
//										    int month=Integer.valueOf(xuyuRechargeVo.getChargeCost())*Integer.valueOf(xuyuContentCardInfo.getComboType());
//										    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
//										    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
//										}else{
//											// 到期日直接增加
//										    String dateNow=format.format(xuyuContentCardInfo.getDeadlineDate());
//										    int month=Integer.valueOf(xuyuRechargeVo.getChargeCost())*Integer.valueOf(xuyuContentCardInfo.getComboType());
//										    String waitDate=getDeadlineDate(dateNow, month, xuyuContentCardInfo.getProvider());
//										    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
//										}
//									}catch (Exception e) {
//										e.printStackTrace();
//										throw new CustomException("满期日计算失败");
//									}
//								}
//							}
//							xuyuContentCardInfoDao.update(xuyuContentCardInfo);
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
							throw new CustomException("目前系统支持输入充值一次最多为5000");
						}
						
						// 判断数据类型
						xuyuRechargeVo.setNumType("01");
						// 查询数据类型
						sb=new StringBuilder();
						sb.append(" select count(1) as a from  MSG_TEMP a1  inner join   XUYU_CONTENT_CARD_MGR t1 ");
						sb.append(" on a1.ACCESS_NUM=t1.ICCID  ");
						
						long r=commonMapper.findCount(sb.toString());
						if(r>0){
							xuyuRechargeVo.setNumType("02");
						}
						
						calendar = Calendar.getInstance();
						calendar.setTime(new Date());
						xuyuRecharge=new XuyuRecharge();
						xuyuRechargeVo.setIsPay(SystemConstants.STRING_YES);
						xuyuRechargeVo.setCreateUser(systemUser.getUserName());
						xuyuRechargeVo.setCreateOrg(systemUser.getOrgId());
						HashMap map=new HashMap();
						map.put("record", xuyuRechargeVo);
						xuyuRechargeDao.insertAccsNew(map);
						// 更新套餐总量
						xuyuContentCardInfoDao.updateTotalGprsAddNew(xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						xuyuContentCardInfoDao.updateTotalGprsNew(xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						// 插入并且更新临时表
						map=new HashMap();
						map.put("numType", xuyuRechargeVo.getNumType());
						xuyuContentCardInfoDao.truncateTable();
						xuyuContentCardInfoDao.insertInfoOwnerTempNew(map);
						// 更新满期日
						xuyuContentCardInfoDao.updateDeadlineDateAddNew(xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
						xuyuContentCardInfoDao.updateDeadlineDateNew(xuyuRechargeVo.getChargeCost(),xuyuRechargeVo.getNumType());
					}else{
						throw new CustomException("非卡段充值，请选择需要充值的数据");
					}
				}else{
					throw new CustomException("超出当前用户权限，只有总部用户才能充值");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    
	/**
	 * 充值记录查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		try {
			pageModel.setRows(xuyuRechargeDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(xuyuRechargeDao.selectUserCountWithPage(pageModel));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 生成随机数订单
	 * @return
	 */
	private String createId(){
		String str="0123456789";
    	StringBuilder sb=new StringBuilder(4);
    	for(int i=0;i<3;i++)
    	{
    	char ch=str.charAt(new Random().nextInt(str.length()));
    	sb.append(ch);
    	}
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmsssss");
        String out_trade_no = sdf.format(new Date())+sb.toString();
        return out_trade_no;
	}
    
	/**
	 * 创建本地订单表
	 */
	@Override
	public synchronized XuyuRechargeResultVo createOrder(XuyuRechargeVo xuyuRechargeVo) {
		XuyuRecharge xuyuRecharge=new XuyuRecharge();
		xuyuRecharge.setId(createId());
		xuyuRecharge.setAccessNum(xuyuRechargeVo.getAccessNum());
		xuyuRecharge.setComboType(xuyuRechargeVo.getComboType());
		xuyuRecharge.setComnoName(xuyuRechargeVo.getComnoName());
		xuyuRecharge.setTotalGprs(xuyuRechargeVo.getTotalGprs());
		xuyuRecharge.setPrice(xuyuRechargeVo.getPrice());
		xuyuRecharge.setMoney(xuyuRechargeVo.getMoney());
		xuyuRecharge.setIsPay(SystemConstants.STRING_NO);
		xuyuRecharge.setPayCommission(SystemConstants.STRING_NO);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		xuyuRecharge.setCreateDate(calendar.getTime());
		xuyuRecharge.setCustomer(xuyuRechargeVo.getCustomer());
		xuyuRecharge.setYc(xuyuRechargeVo.getYc());
		xuyuRecharge.setIsDeal(SystemConstants.STRING_NO);
		xuyuRecharge.setComeon(xuyuRechargeVo.getComeon());
		xuyuRecharge.setIccid(xuyuRechargeVo.getIccid());
		xuyuRecharge.setProvider(xuyuRechargeVo.getProvider());
		xuyuRechargeDao.createOrder(xuyuRecharge);
		
		XuyuRechargeResultVo xuyuRechargeResultVo=new XuyuRechargeResultVo();
		BeanUtils.copyProperties(xuyuRecharge,xuyuRechargeResultVo);
		return xuyuRechargeResultVo;
	}
    
	/**
	 * 三方支付充值记录
	 * @param pageModel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void customerFindList(PageModel pageModel) {
		try {
			pageModel.setRows(xuyuRechargeDao.selectCustomerListWithPage(pageModel));
		    pageModel.setTotal(xuyuRechargeDao.selectCustomerCountWithPage(pageModel));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 代理商创建充值订单表
	 * @throws CustomException 
	 */
	@Override
	public synchronized XuyuRechargeResultVo createOrder() throws CustomException {
		XuyuRechargeResultVo xuyuRechargeResultVo=null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		try {
			String accessNums=request.getParameter("accessNums");
			String payId=request.getParameter("payId");
			String yc=request.getParameter("yc");
			// 查询当前代理商的充值
			if(payId==null||SystemConstants.STRINGEMPTY.equals(payId)){
				throw new CustomException("参数异常，请联系系统管理员");
			}
			
			StringBuilder sb=new StringBuilder();
			sb.append("  select * from XUYU_PACKAGES t1 where t1.ID='"+payId+"' ");
			Map<String, Object> map=commonMapper.findOneData(sb.toString());
			
			BigDecimal totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("GPRS")));
			BigDecimal price=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("ACTUAL_PRICE")));
			BigDecimal money=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("SELF_PRICE")));
			
			logger.info("金额:"+money);
			XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(accessNums);
			// 判断是否为流量池的卡，如果是不能充值
			if(!(xuyuContentCardInfo.getOwner()==null||SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getOwner()))){
				throw new CustomException("目前流量池的卡不支持用户自主充值");
			}
			if(xuyuContentCardInfo!=null){
				XuyuRechargeVo xuyuRechargeVo=new XuyuRechargeVo();
				xuyuRechargeVo.setAccessNum(xuyuContentCardInfo.getAccessNum());
				xuyuRechargeVo.setComboType(SystemConstants.STRINGEMPTY+map.get("AVAILABLE_TYPE"));
				xuyuRechargeVo.setComnoName(SystemConstants.STRINGEMPTY+map.get("COMNO_NAME"));
				xuyuRechargeVo.setMoney(money);
				xuyuRechargeVo.setPrice(price);
				xuyuRechargeVo.setTotalGprs(totalGprs);
				xuyuRechargeVo.setYc(yc);
				
				// 不管谁充值都算当前人的
				xuyuRechargeVo.setIccid(xuyuContentCardInfo.getIccid());
				xuyuRechargeVo.setProvider(xuyuContentCardInfo.getProvider());
				xuyuRechargeVo.setCustomer(xuyuContentCardInfo.getAgency());
				xuyuRechargeVo.setComeon(SystemConstants.STRINGEMPTY+map.get("COMEON"));
				xuyuRechargeResultVo=createOrder(xuyuRechargeVo);
			}
		}catch(CustomException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xuyuRechargeResultVo;
	}
    
	/**
	 * 检查卡类型
	 * @throws CustomException 
	 */
	@Override
	public synchronized Map<String,Object> checkType(XuyuRechargeVo xuyuRechargeVo) throws CustomException {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			// 判断是否为卡段充值
			if(SystemConstants.STRING_YES.equals(xuyuRechargeVo.getYesNo())){
				// 判断当前卡是否为流量池的卡
				StringBuilder sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO where 1=1 ");
				sb.append("  AND ACCESS_NUM >='"+xuyuRechargeVo.getAccessNumStart()+"'    AND ACCESS_NUM <='"+xuyuRechargeVo.getAccessNumEnd()+"'");
				long i=commonMapper.findCount(sb.toString());
				
				sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO where 1=1 ");
				sb.append("  AND ACCESS_NUM >='"+xuyuRechargeVo.getAccessNumStart()+"'    AND ACCESS_NUM <='"+xuyuRechargeVo.getAccessNumEnd()+"'");
				sb.append("  and `OWNER` is not null and LENGTH(trim(`OWNER`))>=1 ");
				long j=commonMapper.findCount(sb.toString());
				
				sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO where 1=1 ");
				sb.append("  AND ACCESS_NUM >='"+xuyuRechargeVo.getAccessNumStart()+"'    AND ACCESS_NUM <='"+xuyuRechargeVo.getAccessNumEnd()+"'");
				sb.append("  and (`OWNER` is  null or LENGTH(trim(`OWNER`))<1) ");
				long k=commonMapper.findCount(sb.toString());
				if(i==j){
					map.put("type", "01");
					map.put("sucess", true);
				}else if(i==k){
					map.put("type", "02");
					map.put("sucess", true);
				}else{
					throw new CustomException("卡中既有流量池的卡，又有单卡的不能一起充值");
				}
			}else{
				// 同样的处理方式
				StringBuilder sb=new StringBuilder();
				sb.append(" truncate table CHARGE_TEMP ");
				commonMapper.executeAction(sb.toString());
				// 判断是否为页面的数据
				if(xuyuRechargeVo.getAccessNums().indexOf(";")!=-1){
					String accessNums=xuyuRechargeVo.getAccessNums().replace("\r\n", "");
					String[] accessNumStr=accessNums.split(";");
				    List<Map<String, Object>> bacthInsertMap=null;
				    Map<String, Object> valuesMap=null;
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
							params.setTables("CHARGE_TEMP");
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
						params.setTables("CHARGE_TEMP");
						params.setInsertMap(insertMap);
						params.setBacthInsertMap(bacthInsertMap);;
						commonMapper.batchAdd(params);
						bacthInsertMap=new ArrayList<Map<String, Object>>();
					}
				}else{
					String accessNums=xuyuRechargeVo.getAccessNums();
					String[] accessNumStr=accessNums.split("\r\n");
					List<Map<String, Object>> bacthInsertMap=null;
				    Map<String, Object> valuesMap=null;
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
							params.setTables("CHARGE_TEMP");
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
						params.setTables("CHARGE_TEMP");
						params.setInsertMap(insertMap);
						params.setBacthInsertMap(bacthInsertMap);;
						commonMapper.batchAdd(params);
						bacthInsertMap=new ArrayList<Map<String, Object>>();
					}
				}
				
				// 判断当前卡是否为流量池的卡
				sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO t1 where 1=1 ");
				sb.append("  and EXISTS (select * from  CHARGE_TEMP t2 where t2.ACCESS_NUM=t1.ACCESS_NUM) ");
				long i=commonMapper.findCount(sb.toString());
				
				sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO t1 where 1=1 ");
				sb.append("  and EXISTS (select * from  CHARGE_TEMP t2 where t2.ACCESS_NUM=t1.ACCESS_NUM) ");
				sb.append("  and `OWNER` is not null and LENGTH(trim(`OWNER`))>=1 ");
				long j=commonMapper.findCount(sb.toString());
				
				sb=new StringBuilder();
				sb.append("  select count(1) as a from XUYU_CONTENT_CARD_INFO t1 where 1=1 ");
				sb.append("  and EXISTS (select * from  CHARGE_TEMP t2 where t2.ACCESS_NUM=t1.ACCESS_NUM) ");
				sb.append("  and (`OWNER` is  null or LENGTH(trim(`OWNER`))<1) ");
				long k=commonMapper.findCount(sb.toString());
				if(i==j){
					map.put("type", "01");
					map.put("sucess", true);
				}else if(i==k){
					map.put("type", "02");
					map.put("sucess", true);
				}else{
					throw new CustomException("卡中既有流量池的卡，又有单卡的不能一起充值");
				}
			}
		} catch (DuplicateKeyException e) {
			throw new CustomException("有重复接入号无法充值");
		}catch (Exception e) {
			throw e;
		}
		return map;
	}
    
	/**
	 * 后台充值方法
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Override
	public synchronized void backOrder(XuyuRechargeVo xuyuRechargeVo) throws  Exception {
		// 判断是否为卡段充值
		if(SystemConstants.STRING_YES.equals(xuyuRechargeVo.getYesNo())){
			throw new CustomException("单卡不支持卡段充值");
		}else{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			String orgId=systemUser.getOrgId();
			String b=request.getParameter("b");
			XuyuRechargeVo xuyuRechargeVo1=JSONObject.parseObject(b,XuyuRechargeVo.class);
			// 查找机构信息
			SystemOrgVo systemOrgVo=systemOrgService.find(orgId);
			if(systemOrgVo.getOrgLevel().equals("1")){
				String accessNums=xuyuRechargeVo.getAccessNums();
				String[] accessNumStr=null;
				if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
					accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
					accessNumStr=accessNums.split(SystemConstants.STRING_SENT);
				}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
					accessNumStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
				}else{
					accessNumStr=accessNums.split(SystemConstants.STRING_ENTER);
				}
				if(accessNums!=null&&!SystemConstants.STRINGEMPTY.equals(accessNums)){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					
					StringBuilder sb=new StringBuilder();
					sb.append("  select * from XUYU_PACKAGES t1 where t1.ID='"+xuyuRechargeVo1.getPayId()+"' ");
					Map<String, Object> map=commonMapper.findOneData(sb.toString());
					String opera=SystemConstants.STRINGEMPTY+map.get("OPERA");
					
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					
					for (int i = 0; i < accessNumStr.length; i++) {
						XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(accessNumStr[i]);
						XuyuRecharge xuyuRecharge=new XuyuRecharge();
						xuyuRecharge.setId(createId()+i);
						// 设置后台充值
						if(xuyuContentCardInfo.getComboType()==null||SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getComboType())){
							throw new CustomException("还未设置套餐，请先设置套餐");
						}
						if(!opera.equals(xuyuContentCardInfo.getProvider())){
							throw new CustomException("套餐的运营商和卡不一致");
						}
						BigDecimal totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("GPRS")));
						BigDecimal price=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("ACTUAL_PRICE")));
						BigDecimal money=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("SELF_PRICE")));
						xuyuRecharge.setAccessNum(xuyuContentCardInfo.getAccessNum());
						xuyuRecharge.setComboType(SystemConstants.STRINGEMPTY+map.get("AVAILABLE_TYPE"));
						xuyuRecharge.setComnoName(SystemConstants.STRINGEMPTY+map.get("COMNO_NAME"));
						xuyuRecharge.setTotalGprs(totalGprs);
						xuyuRecharge.setPrice(price);
						xuyuRecharge.setMoney(money);
						
						xuyuRecharge.setIsPay(SystemConstants.STRING_YES);
						xuyuRecharge.setCreateUser(systemUser.getUserName());
						xuyuRecharge.setCreateOrg(systemUser.getOrgId());
						xuyuRecharge.setCreateDate(calendar.getTime());
						xuyuRecharge.setYc(xuyuRechargeVo1.getYc());
						
						xuyuRecharge.setIccid(xuyuContentCardInfo.getIccid());
						xuyuRecharge.setProvider(xuyuContentCardInfo.getProvider());
						xuyuRecharge.setCustomer(xuyuContentCardInfo.getAgency());
						xuyuRecharge.setComeon(SystemConstants.STRINGEMPTY+map.get("COMEON"));
						// 首先判断是否为预充值
						if(SystemConstants.STRING_YES.equals(xuyuRechargeVo1.getYc())){
							// 啥也不用做
							xuyuRecharge.setIsDeal(SystemConstants.STRING_NO);
						}else{
							// 判断是否为加油包
							if(SystemConstants.STRING_YES.equals(xuyuRecharge.getComeon())){
								// 加油包啥也不做直接加总量
								if(xuyuContentCardInfo.getTotalGprs()==null){
									throw new CustomException("当前卡还未充值,不能直接充加油包");
								}else{
									xuyuContentCardInfo.setTotalGprs(xuyuContentCardInfo.getTotalGprs().add(totalGprs));
									xuyuContentCardInfo.setRemainGps(xuyuContentCardInfo.getRemainGps().add(totalGprs));
								}
							}else{
								// 直接设置套餐的总量
								xuyuContentCardInfo.setComboType(SystemConstants.STRINGEMPTY+map.get("AVAILABLE_TYPE"));
								xuyuContentCardInfo.setComnoName(SystemConstants.STRINGEMPTY+map.get("GPRS"));
								// 首先判断剩余流量是否大于等于0
								if(xuyuContentCardInfo.getRemainGps()!=null){
									int cam=xuyuContentCardInfo.getRemainGps().compareTo(BigDecimal.ZERO); 
									if(cam==-1){ 
										// 那计算总量就是目前的总量减去之前欠费的
										totalGprs=totalGprs.add(xuyuContentCardInfo.getRemainGps());
									}
								}
								// 保留下当前使用的流量
								// 首先同步下流量
								String gprs=SystemConstants.STRINGEMPTY;
								try {
									XuyuMessageLogVo xuyuMessageLogVo=new XuyuMessageLogVo();
									xuyuMessageLogVo.setAccessNums(xuyuContentCardInfo.getAccessNum());
									XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(xuyuContentCardInfo.getAccessNum());
									gprs=iccIdManagerService.getGPRS(contentCardInfo);
									gprs=gprs.replace("MB", "");
								} catch (Exception e) {
									logger.error("同步流量发生错误："+e.getMessage());
								}
								if(SystemConstants.STRINGEMPTY.equals(gprs)){
									if(xuyuContentCardInfo.getUseGprs()!=null){
										gprs=""+xuyuContentCardInfo.getUseGprs();
									}else{
										gprs="0.00";
									}
								}
								xuyuContentCardInfo.setChargeGprs(BigDecimal.valueOf(Double.valueOf(gprs)));
								xuyuContentCardInfo.setTotalGprs(totalGprs);
								xuyuContentCardInfo.setRemainGps(totalGprs);
								xuyuContentCardInfo.setActivateDateRestart(calendar.getTime());
								
								String testType=xuyuContentCardInfo.getTestType();
								String waitType=xuyuContentCardInfo.getWaitType();
								// 没有测试期和成默契直接计算
								if((SystemConstants.STRINGEMPTY.equals(testType)
										   &&SystemConstants.STRINGEMPTY.equals(waitType))||(xuyuContentCardInfo.getDeadlineDate()!=null)){
									try {
										// 通过激活日期计算沉到期时间
									    String dateNow=format.format(xuyuContentCardInfo.getActivateDateRestart());
									    String waitDate=getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
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
										    String waitDate=getDeadlineDate(dateNow, Integer.valueOf(xuyuContentCardInfo.getComboType()), xuyuContentCardInfo.getProvider());
										    xuyuContentCardInfo.setDeadlineDate(new Timestamp(format.parse(waitDate).getTime()));
										}catch (Exception e) {
											e.printStackTrace();
											throw new CustomException("满期日计算失败");
										}
									}
								}
							}
						}
						xuyuRechargeDao.insert(xuyuRecharge);
						xuyuContentCardInfoDao.update(xuyuContentCardInfo);
					}
				}else{
					throw new CustomException("非卡段充值，请选择需要充值的数据");
				}
			}else{
				throw new CustomException("超出当前用户权限，只有总部用户才能充值");
			}
		}
	}
	
	/**
	 * 根据不同的运营商计算到期日
	 * @param dateNow
	 * @param month1
	 * @param provider
	 * @return
	 */
	public synchronized String getDeadlineDate(String dateNow,Integer month1,String provider){
		// 通过激活日期计算沉到期时间
		// 有沉默期则计算沉默期到期时间
		if("2".equals(provider)){
			// 联通逻辑
		    int month=month1;
		    StringBuilder sb=new StringBuilder();
			sb.append(" select case when date_format('"+dateNow+"','%d')>26 then ");
			sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+month+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
			sb.append(" else  ");
			sb.append(" date_format(DATE_ADD(DATE_ADD(('"+dateNow+"'),INTERVAL "+(month-1)+" month),interval -day('"+dateNow+"')+26 day),'%Y-%m-%d') ");
			sb.append(" END  as  DATE from dual ");
			Map<String, Object> map1=commonMapper.findOneData(sb.toString());
			String waitDate=SystemConstants.STRINGEMPTY+map1.get("DATE");
			return waitDate;
		}else{
		    int month=month1-1;
		    StringBuilder sb=new StringBuilder();
			sb.append(" select  last_day(DATE_ADD('"+dateNow+"',INTERVAL "+month+" month)) as DATE from dual ");
			Map<String, Object> map1=commonMapper.findOneData(sb.toString());
			String waitDate=SystemConstants.STRINGEMPTY+map1.get("DATE");
			return waitDate;
		}
	}
    
	/**
	 * 客户充值记录导出
	 */
	@Override
	public  Map<String, Object> exporCtustomerData(XuyuRechargeVo xuyuRechargeVo) {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
	        String downLoadSize=ProsUtil.getProperty("downLoadSize");
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t3.ORG_NAME,t1.ACCESS_NUM,t1.ICCID,t1.TOTAL_GPRS, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='AVAILABLE_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  as COMBO_TYPE,");
	        sb.append(" t1.PRICE,t1.MONEY,t1.CREATE_DATE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TRADE_TYPE'  and a1.F_CODE=t2.TRADE_TYPE)  as TRADE_TYPE, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='ORDER_STATUS'  and a1.F_CODE=t2.ORDER_STATUS)  as ORDER_STATUS, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='YES_NO'  and a1.F_CODE=t1.COME_ON)  as COME_ON,  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='YES_NO'  and a1.F_CODE=t1.YC)  as YC ");
	        sb.append(" FROM ");
	        sb.append(" ( SELECT * FROM XUYU_RECHARGE a WHERE IS_PAY = 'n'");
	        if(xuyuRechargeVo!=null){
	        	 if(xuyuRechargeVo.getAccessNum()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNum())){
	        		 sb.append(" AND ACCESS_NUM='"+xuyuRechargeVo.getAccessNum()+"' ");
	 	         } 
	        	 if(xuyuRechargeVo.getCustomer()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCustomer())){
	 	        	sb.append("  and EXISTS (SELECT * FROM SYSTEM_AUTH_ORG b where b.ORG_ID like '"+xuyuRechargeVo.getCustomer()+"%' and b.ID=a.CUSTOMER) ");
	 	         }
	        	 if(xuyuRechargeVo.getAvailableType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAvailableType())){
	 	        	sb.append(" AND COMBO_TYPE='"+xuyuRechargeVo.getAvailableType()+"' ");
	 	         } 
	        	 if(xuyuRechargeVo.getCreateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCreateDateStart())){
		 	        sb.append(" AND CREATE_DATE>='"+xuyuRechargeVo.getCreateDateStart()+"' ");
		 	     }
	        	 if(xuyuRechargeVo.getCreateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCreateDateEnd())){
		 	        sb.append(" AND CREATE_DATE<='"+xuyuRechargeVo.getCreateDateEnd()+"' ");
		 	     }
	        	 if(xuyuRechargeVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNumStart())){
			 	    sb.append(" AND ACCESS_NUM>='"+xuyuRechargeVo.getAccessNumStart()+"' ");
			 	 }
	        	 if(xuyuRechargeVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNumEnd())){
			 	    sb.append(" AND ACCESS_NUM<='"+xuyuRechargeVo.getAccessNumEnd()+"' ");
			 	 }
	        	 if(xuyuRechargeVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getIccidStart())){
				 	 sb.append(" AND ICCID>='"+xuyuRechargeVo.getIccidStart()+"' ");
				 }
		         if(xuyuRechargeVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getIccidEnd())){
				 	 sb.append(" AND ICCID<='"+xuyuRechargeVo.getIccidEnd()+"' ");
				 }
	        	 if(xuyuRechargeVo.getComeon()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getComeon())){
				 	sb.append(" AND COME_ON='"+xuyuRechargeVo.getComeon()+"' ");
				 }
	        	 if(xuyuRechargeVo.getYc()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getYc())){
				 	sb.append(" AND YC='"+xuyuRechargeVo.getYc()+"' ");
				 }
	        }
	        sb.append("  LIMIT  "+downLoadSize);
	        sb.append(" ) t1  ");
	        if(xuyuRechargeVo!=null){
	        	 if(xuyuRechargeVo.getOrderStatus()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getOrderStatus())){
	        		 sb.append(" INNER join XUYU_PAYMENT t2 on t1.id=t2.TRADE_NO ");
	        		 sb.append(" AND t2.ORDER_STATUS='"+xuyuRechargeVo.getOrderStatus()+"' ");
	        	 }else if(xuyuRechargeVo.getTradeType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getTradeType())){
	        		 if(xuyuRechargeVo.getOrderStatus()==null||SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getOrderStatus())){
	        			 sb.append(" INNER join XUYU_PAYMENT t2 on t1.id=t2.TRADE_NO ");
	        		 }
	        		 sb.append(" AND t2.TRADE_TYPE='"+xuyuRechargeVo.getTradeType()+"' ");
	        	 }else{
	        		 sb.append("  LEFT JOIN XUYU_PAYMENT t2 ON t1.id = t2.TRADE_NO ");
	        	 }
	        }
	        sb.append("  LEFT JOIN SYSTEM_AUTH_ORG t3 on t1.CUSTOMER=t3.id ");
	        sb.append("  ORDER BY t1.CREATE_DATE DESC  ");
	        logger.info("下载的sql:"+sb.toString());
	        map=createFile(sb.toString(),false);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	private  Map<String ,Object> createFile(String sql,Boolean isResult){
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
	        	

        		String[] colmnsList = {
            			"客户",
            			"接入号",
            			"ICCID",
            			"充值流量(MB)",
            			"有效期",
            			"原价",
            			"售价",
            			"充值时间",
            			"充值方式",
            			"充值状态",
            			"加油包",
            			"预充值"

    		  	};
    			// 字段映射
    			Map<String, String> fieldLabel=new HashMap<String, String>();
    			fieldLabel.put("ORG_NAME", "客户");
    			fieldLabel.put("ACCESS_NUM", "接入号");
    			fieldLabel.put("ICCID", "ICCID");
    			fieldLabel.put("TOTAL_GPRS", "充值流量(MB)");
    			fieldLabel.put("COMBO_TYPE", "有效期");
    			fieldLabel.put("PRICE", "原价");
    			fieldLabel.put("MONEY", "售价");
    			fieldLabel.put("CREATE_DATE", "充值时间");
    			fieldLabel.put("TRADE_TYPE", "充值方式");
    			fieldLabel.put("ORDER_STATUS", "充值状态");
    			fieldLabel.put("COME_ON", "加油包");
    			fieldLabel.put("YC", "预充值");
    			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
    			bdeo.WriteExcel(false,getCon(),sql);

    			map.put("filename", filename);
    			map.put("annexeName", "客户充值记录导出.xlsx");
    			map.put("filepath", path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
	}
    
	/**
	 * 后台充值下载
	 * @param xuyuRechargeVo
	 * @return
	 */
	@Override
	public  Map<String, Object> exporData(XuyuRechargeVo xuyuRechargeVo) {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
	        String downLoadSize=ProsUtil.getProperty("downLoadSize");
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t3.ORG_NAME,t1.ACCESS_NUM,t1.ICCID,t1.TOTAL_GPRS, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='AVAILABLE_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  as COMBO_TYPE,");
	        sb.append(" t1.CREATE_DATE,t1.CREATE_USER,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='YES_NO'  and a1.F_CODE=t1.COME_ON)  as COME_ON,  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='YES_NO'  and a1.F_CODE=t1.YC)  as YC ");
	        sb.append(" FROM ");
	        sb.append(" XUYU_RECHARGE t1 left join SYSTEM_AUTH_ORG t3 on t1.CUSTOMER=t3.id ");
	        sb.append(" WHERE IS_PAY = 'y' ");
	        if(xuyuRechargeVo!=null){
	        	 if(xuyuRechargeVo.getAccessNum()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNum())){
	        		 sb.append(" AND ACCESS_NUM='"+xuyuRechargeVo.getAccessNum()+"' ");
	 	         } 
	        	 if(xuyuRechargeVo.getCustomer()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCustomer())){
	 	        	sb.append("  and EXISTS (SELECT * FROM SYSTEM_AUTH_ORG b where b.ORG_ID like '"+xuyuRechargeVo.getCustomer()+"%' and b.ID=t1.CUSTOMER) ");
	 	         } 
	        	 if(xuyuRechargeVo.getAvailableType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAvailableType())){
	 	        	sb.append(" AND COMBO_TYPE='"+xuyuRechargeVo.getAvailableType()+"' ");
	 	         } 
	        	 if(xuyuRechargeVo.getCreateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCreateDateStart())){
		 	        sb.append(" AND CREATE_DATE>='"+xuyuRechargeVo.getCreateDateStart()+"' ");
		 	     } 
	        	 if(xuyuRechargeVo.getCreateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCreateDateEnd())){
		 	        sb.append(" AND CREATE_DATE>='"+xuyuRechargeVo.getCreateDateEnd()+"' ");
		 	     } 
	        	 if(xuyuRechargeVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNumStart())){
			 	    sb.append(" AND ACCESS_NUM>='"+xuyuRechargeVo.getAccessNumStart()+"' ");
			 	 } 
	        	 if(xuyuRechargeVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getAccessNumEnd())){
			 	    sb.append(" AND ACCESS_NUM<='"+xuyuRechargeVo.getAccessNumEnd()+"' ");
			 	 } 
	        	 if(xuyuRechargeVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getIccidStart())){
				 	 sb.append(" AND ICCID>='"+xuyuRechargeVo.getIccidStart()+"' ");
				 }
		         if(xuyuRechargeVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getIccidEnd())){
				 	 sb.append(" AND ICCID<='"+xuyuRechargeVo.getIccidEnd()+"' ");
				 }
	        	 if(xuyuRechargeVo.getComeon()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getComeon())){
					sb.append(" AND COME_ON='"+xuyuRechargeVo.getComeon()+"' ");
			     } 
	        	 if(xuyuRechargeVo.getYc()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getYc())){
					sb.append(" AND YC='"+xuyuRechargeVo.getYc()+"' ");
				 } 
	        	 if(xuyuRechargeVo.getCreateUser()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuRechargeVo.getCreateUser())){
					sb.append(" AND CREATE_USER like '%"+xuyuRechargeVo.getCreateUser()+"%' ");
				 }
	        }
	        sb.append("  ORDER BY t1.CREATE_DATE DESC  ");
	        sb.append("  LIMIT "+downLoadSize);
	        logger.info("下载的sql:"+sb.toString());
	        map=createFile(sb.toString());
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
    
	/**
	 * 充值记录导出
	 * @param sql
	 * @return
	 */
	private  Map<String, Object> createFile(String sql) {
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
        	

    		String[] colmnsList = {
        			"客户",
        			"接入号",
        			"ICCID",
        			"充值流量(MB)",
        			"有效期",
        			"充值时间",
        			"操作人账号",
        			"加油包",
        			"预充值"

		  	};
			// 字段映射
			Map<String, String> fieldLabel=new HashMap<String, String>();
			fieldLabel.put("ORG_NAME", "客户");
			fieldLabel.put("ACCESS_NUM", "接入号");
			fieldLabel.put("ICCID", "ICCID");
			fieldLabel.put("TOTAL_GPRS", "充值流量(MB)");
			fieldLabel.put("COMBO_TYPE", "有效期");
			fieldLabel.put("CREATE_DATE", "充值时间");
			fieldLabel.put("CREATE_USER", "操作人账号");
			fieldLabel.put("COME_ON", "加油包");
			fieldLabel.put("YC", "预充值");
			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
			bdeo.WriteExcel(false,getCon(),sql);

			map.put("filename", filename);
			map.put("annexeName", "后台充值记录导出.xlsx");
			map.put("filepath", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
