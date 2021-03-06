package com.xuyurepos.vo.operamanager;

import java.math.BigDecimal;

public class XuyuPackagesVo {
	private String id;
	private String order;
	private Integer gprs;
	private BigDecimal selfPrice;
	private BigDecimal actualPrice;
	private String desc;
	private String opera;
	private String availableType;
	private String agencyId;
	
	private String queryAgencyId;
	private String agencyName;
	private BigDecimal commission;
	
	private String state;
	
	private String discount;
	
	private String comeon;
	
	private String gprsquery;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getGprs() {
		return gprs;
	}

	public void setGprs(Integer gprs) {
		this.gprs = gprs;
	}

	public BigDecimal getSelfPrice() {
		return selfPrice;
	}

	public void setSelfPrice(BigDecimal selfPrice) {
		this.selfPrice = selfPrice;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOpera() {
		return opera;
	}

	public void setOpera(String opera) {
		this.opera = opera;
	}

	public String getAvailableType() {
		return availableType;
	}

	public void setAvailableType(String availableType) {
		this.availableType = availableType;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public String getQueryAgencyId() {
		return queryAgencyId;
	}

	public void setQueryAgencyId(String queryAgencyId) {
		this.queryAgencyId = queryAgencyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getComeon() {
		return comeon;
	}

	public void setComeon(String comeon) {
		this.comeon = comeon;
	}

	public String getGprsquery() {
		return gprsquery;
	}

	public void setGprsquery(String gprsquery) {
		this.gprsquery = gprsquery;
	}
	
	

}
