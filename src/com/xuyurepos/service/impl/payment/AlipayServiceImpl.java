package com.xuyurepos.service.impl.payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.dao.payment.XuyuPayaddressDao;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.entity.payment.XuyuPayaddress;
import com.xuyurepos.service.payment.AlipayService;
import com.xuyurepos.service.payment.XuyuRechargeService;
import com.xuyurepos.vo.payment.XuyuRechargeResultVo;
import com.xuyurepos.vo.payment.XuyuRechargeVo;
@Service
@Transactional
public class AlipayServiceImpl implements AlipayService{
	
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	@Autowired
	private XuyuRechargeService xuyuRechargeService; 
	
	@Autowired
	private XuyuPayaddressDao xuyuPayaddressDao; 
	
	@javax.annotation.Resource
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private CommonMapper commonMapper;
	
	public Connection getCon() {  
	    Connection connection = null;  
	    SqlSession sqlSession = sqlSessionFactory.openSession();  
	    connection = sqlSession.getConnection();  
	    return connection;  
	} 
    
	/**
	 * 判断值是否正确
	 * @throws CustomException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map checkNums(String accessNums) throws CustomException {
		if(accessNums==null||SystemConstants.STRINGEMPTY.equals(accessNums)){
			throw new CustomException("充值账号错误，请检查");
		}
		StringBuilder sql=new StringBuilder("");
		sql.append(" select t1.ACCESS_NUM,t1.ICCID,t1.PROVIDER from XUYU_CONTENT_CARD_INFO t1 where t1.ACCESS_NUM like'"+accessNums+"%' or t1.ICCID like '"+accessNums+"%' or t1.IMSI like'"+accessNums+"%' ");
		Map<String, Object> map=commonMapper.findOneData(sql.toString());
		return map;
	}
    
	/**
	 * 创建订单信息
	 * @throws CustomException 
	 */
	@Override
	public Map<String, Object> createOrder(String accessNums,String payId) throws CustomException {
		try {
			// 根据代理商id查询当前卡号的流量套餐以及价格
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String id=request.getParameter("id");
			// 预充值
			String yc=request.getParameter("yc");
			XuyuPayaddress payaddress=xuyuPayaddressDao.findByUuid(id);
			if(payaddress==null){
				throw new CustomException("地址错误,请检查");
			}else{
				// 根据查询数据获取主表信息
				XuyuContentCardInfo xuyuContentCardInfo=xuyuContentCardInfoDao.findAny(accessNums);
				if(xuyuContentCardInfo.getOwner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfo.getOwner())){
					throw new CustomException("目前系统暂不支持流量池用户自助充值");
				}
				
				StringBuilder sb=new StringBuilder();
				sb.append("  select * from XUYU_PACKAGES t1 where t1.ID='"+payId+"' ");
				Map<String, Object> map=commonMapper.findOneData(sb.toString());
				
				XuyuRechargeVo xuyuRechargeVo=new XuyuRechargeVo();
				BigDecimal totalGprs=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("GPRS")));
				BigDecimal price=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("ACTUAL_PRICE")));
				BigDecimal money=BigDecimal.valueOf(Double.valueOf(SystemConstants.STRINGEMPTY+map.get("SELF_PRICE")));
				xuyuRechargeVo.setAccessNum(xuyuContentCardInfo.getAccessNum());
				xuyuRechargeVo.setComboType(SystemConstants.STRINGEMPTY+map.get("AVAILABLE_TYPE"));
				xuyuRechargeVo.setComnoName(SystemConstants.STRINGEMPTY+map.get("GPRS"));
				xuyuRechargeVo.setTotalGprs(totalGprs);
				xuyuRechargeVo.setPrice(price);
				xuyuRechargeVo.setMoney(money);
				xuyuRechargeVo.setCustomer(xuyuContentCardInfo.getAgency());
				xuyuRechargeVo.setYc(yc);
				xuyuRechargeVo.setComeon(SystemConstants.STRINGEMPTY+map.get("COMEON"));
				xuyuRechargeVo.setIccid(xuyuContentCardInfo.getIccid());
				XuyuRechargeResultVo xuyuRechargeResultVo=xuyuRechargeService.createOrder(xuyuRechargeVo);
			    Map<String, Object> mapResult=new HashMap<String, Object>();
			    mapResult.put("order", xuyuRechargeResultVo);
				return mapResult;
			}
		}catch(CustomException e){
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new CustomException("创建订单失败，请重试");
		}
		
	}

}
