package com.xuyurepos.service.payment;

import java.util.Map;

import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.vo.payment.XuyuRechargeResultVo;
import com.xuyurepos.vo.payment.XuyuRechargeVo;

public interface XuyuRechargeService {

	public void saveInfo(XuyuRechargeVo xuyuRechargeVo)throws Exception;

	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public XuyuRechargeResultVo createOrder(XuyuRechargeVo xuyuRechargeVo);
	
	public void payNotify(String out_trade_no);

	@SuppressWarnings("rawtypes")
	public void customerFindList(PageModel pageModel);

	public XuyuRechargeResultVo createOrder()throws CustomException;

	public Map<String,Object> checkType(XuyuRechargeVo xuyuRechargeVo) throws CustomException;

	public void backOrder(XuyuRechargeVo xuyuRechargeVo)throws Exception;

	public Map<String, Object> exporCtustomerData(XuyuRechargeVo xuyuRechargeVo);

	public Map<String, Object> exporData(XuyuRechargeVo xuyuRechargeVo);

	public String getDeadlineDate(String dateNow, Integer valueOf, String provider);

}
