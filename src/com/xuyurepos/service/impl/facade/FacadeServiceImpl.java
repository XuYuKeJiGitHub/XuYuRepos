package com.xuyurepos.service.impl.facade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.dao.payment.XuyuRechargeDao;
import com.xuyurepos.entity.comm.Params;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.entity.payment.XuyuRecharge;
import com.xuyurepos.service.facade.FacadeService;
import com.xuyurepos.service.manager.IccIdManagerService;
import com.xuyurepos.service.payment.XuyuRechargeService;
import com.xuyurepos.util.HttpClientUtil;
import com.xuyurepos.vo.manager.XuyuMessageLogVo;
/**
 * 外部查询接口实现类
 * @author yangfei
 */
@Service
public class FacadeServiceImpl implements FacadeService{
	Logger logger=LoggerFactory.getInstance().getLogger(FacadeServiceImpl.class);
	
	@Autowired
	private XuyuRechargeService xuyuRechargeService;
	
	@Autowired
	private  XuyuRechargeDao xuyuRechargeDao;
	
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	@Autowired
	private IccIdManagerService iccIdManagerService; 
	
	@Autowired
	private CommonMapper commonMapper;

	@Override
	public Map<String, Object> singleGprsInfoQuery(Map paramMap) throws CustomException {
		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNum=(String) paramMap.get("accessNum");
		String queryDate=(String) paramMap.get("queryDate");
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			sb=new StringBuilder();
			sb.append(" select ");
			sb.append(" ACCESS_NUM  accessNum, ");
			sb.append(" ICCID  iccid, ");
			sb.append(" case when t1.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append("  when t1.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append(" when t1.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append("  end workingCondition, ");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.PROVIDER) as PROVIDER, ");
			sb.append(" '' as   arrea,");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  comboType,");
			sb.append(" t1.COMNO_NAME  comboValue, ");
			sb.append(" IFNULL(floor(TOTAL_GPRS),0)  totalGprs,");
			sb.append(" IFNULL(floor(REMAIN_GPS),0)  remainGprs,");
			sb.append(" IFNULL(floor(USE_GPRS),0)    useGprs,");
			sb.append(" DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') deadLineDate");
			sb.append(" from XUYU_CONTENT_CARD_INFO t1 where t1.ACCESS_NUM='"+accessNum+"'  ");
			sb.append(" ");
			map=commonMapper.findOneData(sb.toString());
			
			// 判断是否有叠加包
			
			return map;
		}
		
	}
    
	/**
	 * 批量数据查询接口
	 * @throws CustomException 
	 */
	@Override
	public List<Map<String, Object>> batchGprsInfoQuery(Map paramMap) throws CustomException {
		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNumList=(String) paramMap.get("accessNumList");
		String queryDate=(String) paramMap.get("queryDate");
		
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			// 首先把参数插入临时表
		    List<Map<String, Object>> bacthInsertMap=null;
			Map<String, Object> valuesMap=null;  
			sb=new StringBuilder();
			sb.append(" delete from  FADE_TEMP ");
			commonMapper.executeAction(sb.toString());
			String[]  idsStr=accessNumList.split(SystemConstants.STRING_SENT);
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
					params.setTables("FADE_TEMP");
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
				params.setTables("FADE_TEMP");
				params.setInsertMap(insertMap);
				params.setBacthInsertMap(bacthInsertMap);;
				commonMapper.batchAdd(params);
				bacthInsertMap=new ArrayList<Map<String, Object>>();
			}
			// 求一下个数
			sb=new StringBuilder();
			sb.append(" select count(1) as a  from FADE_TEMP a");
			long result=commonMapper.findCount(sb.toString());
			if(result>1000){
				throw new CustomException("目前系统支持批量查询最多为1000");
			}else{
				// 删除非当前代理商的账号
				
				sb=new StringBuilder();
				sb.append(" select ");
				sb.append(" t1.ACCESS_NUM  accessNum, ");
				sb.append(" ICCID  iccid, ");
				sb.append(" case when t1.PROVIDER='1' then  ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append("  when t1.PROVIDER='2' then ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append(" when t1.PROVIDER='3' then ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append("  end workingCondition, ");
				sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.PROVIDER) as PROVIDER, ");
				sb.append(" ''  AS   arrea,");
				sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  comboType,");
				sb.append(" t1.COMNO_NAME  comboValue, ");
				sb.append(" IFNULL(floor(t1.TOTAL_GPRS),0)  totalGprs,");
				sb.append(" IFNULL(floor(t1.REMAIN_GPS),0)  remainGprs,");
				sb.append(" IFNULL(floor(t1.USE_GPRS),0)    useGprs,");
				sb.append(" DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') deadLineDate");
				sb.append(" from FADE_TEMP t2  left join XUYU_CONTENT_CARD_INFO t1  on t2.ACCESS_NUM=t1.ACCESS_NUM ");
				List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
				return list;
			}
		}
	}
    
	/**
	 * 充值套餐查询
	 * @param paramMap
	 * @return
	 * @throws CustomException 
	 */
	@Override
	public List<Map<String, Object>> rechargeQuery(Map paramMap) throws CustomException {
		String agencyCode=(String) paramMap.get("agencyCode");
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			sb=new StringBuilder();
			sb.append(" select ");
			sb.append(" t1.ID,");
			sb.append(" t1.GPRS,");
			sb.append(" TRUNCATE(t1.SELF_PRICE,2) as selfPrice, ");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.OPERA) as provide, ");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='AVAILABLE_TYPE'  and a1.F_CODE=t1.AVAILABLE_TYPE)  comboType,");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='YES_NO'  and a1.F_CODE=t1.COMEON)  comeon");
			sb.append(" from XUYU_PACKAGES");
			sb.append(" t1 where  EXISTS");
			sb.append(" (select * from SYSTEM_AUTH_USER t2 where t2.USER_NAME='"+agencyCode+"' and t2.ORG_ID=t1.AGENCY_ID) ");
			sb.append(" and t1.STATE='01' ");
			sb.append(" order by t1.OPERA,t1.ORDER_ ");
			List<Map<String, Object>> map1=commonMapper.findManyData(sb.toString());
			
			// 判断是否有叠加包
			
			return map1;
		}
	}

	private String getMD5(String str) {
		String key="xuYuRepos2019";
		String signStr=str+"key="+key;//&key
		String sign=DigestUtils.md5Hex(signStr).toUpperCase();
		return sign;
	}
	private static final String huaianPLTFJ="http://47.102.220.16:8080/XuYuRepos/facade/changeCardStateAll";
	private static final String yanchengPLTFJ="http://47.101.207.177:8080/XuYuRepos/facade/changeCardStateAll";
	
	@Override
	public Map<String, Object> pay(Map paramMap) throws Exception {

		String agencyCode=(String) paramMap.get("agencyCode");
		String accessNum=(String) paramMap.get("accessNum");
		String payId=(String) paramMap.get("payId");
		String yc=(String) paramMap.get("yc");
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			// 数据查询下是否为接入号
			sb=new StringBuilder();
			sb.append(" select * from XUYU_CONTENT_CARD_INFO t1 where t1.ACCESS_NUM='"+accessNum+"' ");
			Map<String, Object> map1=commonMapper.findOneData(sb.toString());
			if(map1==null){
				sb=new StringBuilder();
				sb.append(" select * from XUYU_CONTENT_CARD_INFO t1 where t1.ICCID='"+accessNum+"' ");
				Map<String, Object> map2=commonMapper.findOneData(sb.toString());
				if(map2!=null){
					accessNum=(String) map2.get("ACCESS_NUM");
				}
			}
			System.out.println("充值接入号："+accessNum);
			backOrder(payId, accessNum, yc,agencyCode);
			
			Map<String, Object> resultMap=new HashMap();
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", "充值成功");
			//充值成功去数据库查询--判断是不是盐城或淮安的卡
			XuyuContentCardInfo contentCardInfo=xuyuContentCardInfoDao.find(accessNum);
			String ownerPlace = contentCardInfo.getOwnerPlace();
			String provider = contentCardInfo.getProvider();
			
			
			StringBuffer sbf = new StringBuffer();
			sbf.append("agencyCode=xuyu");
			sbf.append("&accessNums="+accessNum);
			String bool = SystemConstants.STATE_FJ;
			sbf.append("&bool="+bool+"&");
			String sign = getMD5(sbf.toString());
			String changeCardState = "";
			if ("1".equals(provider) && "1".equals(ownerPlace))
				try {
					{//淮安
						String url = huaianPLTFJ;
						HashMap<String, Object> params = new HashMap<>();
						params.put("agencyCode", "xuyu");
						params.put("accessNums", accessNum);
						params.put("bool", bool);
						params.put("sign", sign);
						String post = HttpClientUtil.doPost(url, params);
						JSONObject parseObject = JSONObject.parseObject(post);
						changeCardState = (String) parseObject.get("requestCode"); 
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if("1".equals(provider) && "2".equals(ownerPlace))
				try {
					{//盐城
						String url = yanchengPLTFJ;
						HashMap<String, Object> params = new HashMap<>();
						params.put("agencyCode", "xuyu");
						params.put("accessNums", accessNum);
						params.put("bool", bool);
						params.put("sign", sign);
						String post = HttpClientUtil.doPost(url, params);
						JSONObject parseObject = JSONObject.parseObject(post);
						changeCardState = (String) parseObject.get("requestCode");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else{
				/*bool = "false";
				changeCardState = iccIdManagerService.changeCardState(accessNum,bool);*/
				changeCardState = SystemConstants.STATE_ZBZC;
			}
			if(SystemConstants.STATE_CG.equals(changeCardState)){
				resultMap.put("requestCode", "0");
				resultMap.put("requestMsg", "复机操作成功");
			}else if(SystemConstants.STATE_SB.equals(changeCardState)){
				resultMap.put("requestCode", "1");
				resultMap.put("requestMsg", "复机操作失败");
			}else if(SystemConstants.STATE_PF.equals(changeCardState)){
				resultMap.put("requestCode", "-1");
				resultMap.put("requestMsg", "十分钟内做用户状态修改业务次数不得大于三次");
			}else if(SystemConstants.STATE_YEBZ.equals(changeCardState)){
				resultMap.put("requestCode", "3");
				resultMap.put("requestMsg", "余额不足，不能复机");
			}else if(SystemConstants.STATE_ZBZC.equals(changeCardState)){
				resultMap.put("requestCode", "-2");
				resultMap.put("requestMsg", "该地区卡号暂不支持停复机操作");
			}else{
				resultMap.put("requestCode", "2");
				resultMap.put("requestMsg", "系统或请求异常");
			}
		    return resultMap;
		}
	}
	
	/**
	 * 充值接口
	 * @param payId
	 * @param accessNum
	 * @param yc
	 * @throws Exception
	 */
	private void backOrder(String payId,String accessNum,String yc,String createUser) throws  CustomException {
		if(accessNum!=null&&!SystemConstants.STRINGEMPTY.equals(accessNum)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			
			StringBuilder sb=new StringBuilder();
			sb.append("  select * from XUYU_PACKAGES t1 where t1.ID='"+payId+"' ");
			Map<String, Object> map=commonMapper.findOneData(sb.toString());
			String opera=SystemConstants.STRINGEMPTY+map.get("OPERA");
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			

			XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.find(accessNum);
			XuyuRecharge xuyuRecharge=new XuyuRecharge();
			xuyuRecharge.setId(createId());
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
			xuyuRecharge.setCreateUser(createUser);
			xuyuRecharge.setCreateDate(calendar.getTime());
			xuyuRecharge.setYc(yc);
			
			xuyuRecharge.setIccid(xuyuContentCardInfo.getIccid());
			xuyuRecharge.setProvider(xuyuContentCardInfo.getProvider());
			xuyuRecharge.setCustomer(xuyuContentCardInfo.getAgency());
			xuyuRecharge.setComeon(SystemConstants.STRINGEMPTY+map.get("COMEON"));
			// 首先判断是否为预充值
			if(SystemConstants.STRING_YES.equals(yc)){
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
						// 计算剩余流量
						xuyuContentCardInfo.setRemainGps(xuyuContentCardInfo.getTotalGprs().subtract(xuyuContentCardInfo.getRemainGps()));
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
				}
			}
			xuyuRechargeDao.insert(xuyuRecharge);
			xuyuContentCardInfoDao.update(xuyuContentCardInfo);
			
			// 记录充值的过程
		}else{
			throw new CustomException("请输入需要充值的卡号");
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

	@Override
	public Map<String, Object> singleGprsInfoQueryIccid(Map paramMap) throws CustomException {
		String agencyCode=(String) paramMap.get("agencyCode");
		String iccid=(String) paramMap.get("iccid");
		String queryDate=(String) paramMap.get("queryDate");
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			sb=new StringBuilder();
			sb.append(" select ");
			sb.append(" ACCESS_NUM  accessNum, ");
			sb.append(" ICCID  iccid, ");
			sb.append(" case when t1.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append("  when t1.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append(" when t1.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
	        sb.append("  end workingCondition, ");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.PROVIDER) as PROVIDER, ");
			sb.append(" '' as   arrea,");
			sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  comboType,");
			sb.append(" t1.COMNO_NAME  comboValue, ");
			sb.append(" IFNULL(floor(TOTAL_GPRS),0)  totalGprs,");
			sb.append(" IFNULL(floor(REMAIN_GPS),0)  remainGprs,");
			sb.append(" IFNULL(floor(USE_GPRS),0)    useGprs,");
			sb.append(" DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') deadLineDate");
			sb.append(" from XUYU_CONTENT_CARD_INFO t1 where t1.ICCID='"+iccid+"'  ");
			map=commonMapper.findOneData(sb.toString());
			return map;
			// 判断是否有叠加包
		}
	}

	@Override
	public List<Map<String, Object>> batchGprsInfoQueryIccid(Map paramMap) throws CustomException {
		String agencyCode=(String) paramMap.get("agencyCode");
		String iccidList=(String) paramMap.get("iccidList");
		String queryDate=(String) paramMap.get("queryDate");
		
		// 首先判断用户信息是否正确
		StringBuilder sb=new StringBuilder();
		sb.append(" select * from SYSTEM_AUTH_USER t1 where t1.USER_NAME='"+agencyCode+"' ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		if(map==null){
			throw new CustomException("供应商代码错误，请联系接口提供方");
		}else{
			// 首先把参数插入临时表
		    List<Map<String, Object>> bacthInsertMap=null;
			Map<String, Object> valuesMap=null;  
			sb=new StringBuilder();
			sb.append(" delete from  FADE_TEMP ");
			commonMapper.executeAction(sb.toString());
			String[]  idsStr=iccidList.split(SystemConstants.STRING_SENT);
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
					params.setTables("FADE_TEMP");
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
				params.setTables("FADE_TEMP");
				params.setInsertMap(insertMap);
				params.setBacthInsertMap(bacthInsertMap);;
				commonMapper.batchAdd(params);
				bacthInsertMap=new ArrayList<Map<String, Object>>();
			}
			// 求一下个数
			sb=new StringBuilder();
			sb.append(" select count(1) as a  from FADE_TEMP a");
			long result=commonMapper.findCount(sb.toString());
			if(result>1000){
				throw new CustomException("目前系统支持批量查询最多为1000");
			}else{
				// 删除非当前代理商的账号
				
				sb=new StringBuilder();
				sb.append(" select ");
				sb.append(" t1.ACCESS_NUM  accessNum, ");
				sb.append(" ICCID  iccid, ");
				sb.append(" case when t1.PROVIDER='1' then  ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append("  when t1.PROVIDER='2' then ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append(" when t1.PROVIDER='3' then ");
		        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
		        sb.append("  end workingCondition, ");
				sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.PROVIDER) as PROVIDER, ");
				sb.append(" ''  AS   arrea,");
				sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  comboType,");
				sb.append(" t1.COMNO_NAME  comboValue, ");
				sb.append(" IFNULL(floor(t1.TOTAL_GPRS),0)  totalGprs,");
				sb.append(" IFNULL(floor(t1.REMAIN_GPS),0)  remainGprs,");
				sb.append(" IFNULL(floor(t1.USE_GPRS),0)    useGprs,");
				sb.append(" DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') deadLineDate");
				sb.append(" from FADE_TEMP t2  left join XUYU_CONTENT_CARD_INFO t1  on t2.ACCESS_NUM=t1.ICCID ");
				List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
				return list;
			}
		}
	}
	

}
