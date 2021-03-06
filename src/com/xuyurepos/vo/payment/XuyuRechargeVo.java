package com.xuyurepos.vo.payment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值相关处理
 * @author yangfei
 *
 */
public class XuyuRechargeVo {

	private String id;

	private String accessNum;

	private String comboType;

	private String comnoName;

	private BigDecimal totalGprs;

	private BigDecimal money;
	
	private BigDecimal price;

	private String isPay;

	private Date createDate;

	private String createUser;

	private String createOrg;

	private String customer;

	private String packagesId;

	private BigDecimal commission;

	private BigDecimal commissionMoney;

	private String payCommission;
	
	private String yesNo;
	
	private String accessNums;
	
	private String accessNumStart;
	
	private String accessNumEnd;
	
	private String chargeCost;
	
	private String createDateStart;
	
	private String createDateEnd;
	
	private String yc;
	
	private String payId;
	
	private String  orderStatus;
	private String  tradeType;
	private String  availableType;
	
	private String numType;
	
	private String comeon;
	
	private String iccid;
	
	private String provider;
	
	private String agencyQuery;
	
	private String iccidStart;
	
	private String iccidEnd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(String accessNum) {
		this.accessNum = accessNum;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public String getComnoName() {
		return comnoName;
	}

	public void setComnoName(String comnoName) {
		this.comnoName = comnoName;
	}

	public BigDecimal getTotalGprs() {
		return totalGprs;
	}

	public void setTotalGprs(BigDecimal totalGprs) {
		this.totalGprs = totalGprs;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPackagesId() {
		return packagesId;
	}

	public void setPackagesId(String packagesId) {
		this.packagesId = packagesId;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getCommissionMoney() {
		return commissionMoney;
	}

	public void setCommissionMoney(BigDecimal commissionMoney) {
		this.commissionMoney = commissionMoney;
	}

	public String getPayCommission() {
		return payCommission;
	}

	public void setPayCommission(String payCommission) {
		this.payCommission = payCommission;
	}

	public String getYesNo() {
		return yesNo;
	}

	public void setYesNo(String yesNo) {
		this.yesNo = yesNo;
	}

	public String getAccessNums() {
		return accessNums;
	}

	public void setAccessNums(String accessNums) {
		this.accessNums = accessNums;
	}

	public String getAccessNumStart() {
		return accessNumStart;
	}

	public void setAccessNumStart(String accessNumStart) {
		this.accessNumStart = accessNumStart;
	}

	public String getAccessNumEnd() {
		return accessNumEnd;
	}

	public void setAccessNumEnd(String accessNumEnd) {
		this.accessNumEnd = accessNumEnd;
	}

	public String getChargeCost() {
		return chargeCost;
	}

	public void setChargeCost(String chargeCost) {
		this.chargeCost = chargeCost;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	
	public String getYc() {
		return yc;
	}

	public void setYc(String yc) {
		this.yc = yc;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAvailableType() {
		return availableType;
	}

	public void setAvailableType(String availableType) {
		this.availableType = availableType;
	}

	public String getNumType() {
		return numType;
	}

	public void setNumType(String numType) {
		this.numType = numType;
	}

	public String getComeon() {
		return comeon;
	}

	public void setComeon(String comeon) {
		this.comeon = comeon;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getAgencyQuery() {
		return agencyQuery;
	}

	public void setAgencyQuery(String agencyQuery) {
		this.agencyQuery = agencyQuery;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getIccidStart() {
		return iccidStart;
	}

	public void setIccidStart(String iccidStart) {
		this.iccidStart = iccidStart;
	}

	public String getIccidEnd() {
		return iccidEnd;
	}

	public void setIccidEnd(String iccidEnd) {
		this.iccidEnd = iccidEnd;
	}

	@Override
	public String toString() {
		return "XuyuRechargeVo [id=" + id + ", accessNum=" + accessNum + ", comboType=" + comboType + ", comnoName="
				+ comnoName + ", totalGprs=" + totalGprs + ", money=" + money + ", price=" + price + ", isPay=" + isPay
				+ ", createDate=" + createDate + ", createUser=" + createUser + ", createOrg=" + createOrg
				+ ", customer=" + customer + ", packagesId=" + packagesId + ", commission=" + commission
				+ ", commissionMoney=" + commissionMoney + ", payCommission=" + payCommission + ", yesNo=" + yesNo
				+ ", accessNums=" + accessNums + ", accessNumStart=" + accessNumStart + ", accessNumEnd=" + accessNumEnd
				+ ", chargeCost=" + chargeCost + ", createDateStart=" + createDateStart + ", createDateEnd="
				+ createDateEnd + ", yc=" + yc + ", payId=" + payId + ", orderStatus=" + orderStatus + ", tradeType="
				+ tradeType + ", availableType=" + availableType + ", numType=" + numType + ", comeon=" + comeon
				+ ", iccid=" + iccid + ", provider=" + provider + ", agencyQuery=" + agencyQuery + ", iccidStart="
				+ iccidStart + ", iccidEnd=" + iccidEnd + "]";
	}

	

	

	
}
