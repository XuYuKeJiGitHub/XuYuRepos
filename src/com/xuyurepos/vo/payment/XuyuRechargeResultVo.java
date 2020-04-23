package com.xuyurepos.vo.payment;

import java.math.BigDecimal;

/**
 * 订单表返回页面
 * @author yangfei
 *
 */
public class XuyuRechargeResultVo {
	
	private String id;
	 
	private BigDecimal money;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
	
	

}
