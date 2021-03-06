package com.xuyurepos.vo.autorule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@SuppressWarnings("serial")
public class AutoRuleManagerDTO implements Serializable {
	
	public BigDecimal getDays() {
		return days;
	}
	public void setDays(BigDecimal days) {
		this.days = days;
	}
	public BigDecimal getGprs() {
		return gprs;
	}
	public void setGprs(BigDecimal gprs) {
		this.gprs = gprs;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getRuleTypeName() {
		return ruleTypeName;
	}
	public void setRuleTypeName(String ruleTypeName) {
		this.ruleTypeName = ruleTypeName;
	}
	
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getBindType() {
		return bindType;
	}
	public void setBindType(String bindType) {
		this.bindType = bindType;
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
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOprateType() {
		return oprateType;
	}
	public void setOprateType(String oprateType) {
		this.oprateType = oprateType;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getIsValidate() {
		return isValidate;
	}
	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	private String ruleName;
	private String ruleId;
	private String ruleType;
	private String ruleTypeName;
	private String ruleDesc;
	private BigDecimal days;
	private BigDecimal gprs;
	private BigDecimal rate;
	private String bindType;
	private String accessNums;
	private String accessNumStart;
	private String accessNumEnd;
	private String ownerId;
	private String ownerName;
	private String oprateType;
	private String messageId;
	private String emailId;
	private String isValidate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	private String startDate;
	private String endDate;
}
