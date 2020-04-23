package com.xuyurepos.entity.operamanager;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组管理
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class XuyuOwnerInfo implements Serializable{
	private int id;
	
	private String companyId;
	
	private String companyName;
	
	private String owner;
	
	private String ownerName;
	
	private int memberNum;
	
	private int invalidMember;
	
	private String comboType;
	
	private Double useGprs;
	
	private Double avgGprs;
	
	private int messageCount;
	
	private int  avgMessage;
	
	private String contactPerson;
	
	private String phoneNo;
	
	private String mark;
	
	private Date createDate;
	
	private String createUser;
	
	private String createUserName;
	
	private Date updateDate;
	
	private String updateUser;
	
	private String updateUserName;
	
	private String comboName;
	
	private String payType;
	
	private String silentType;
	
	private String haveTest;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getInvalidMember() {
		return invalidMember;
	}

	public void setInvalidMember(int invalidMember) {
		this.invalidMember = invalidMember;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public Double getUseGprs() {
		return useGprs;
	}

	public void setUseGprs(Double useGprs) {
		this.useGprs = useGprs;
	}

	public Double getAvgGprs() {
		return avgGprs;
	}

	public void setAvgGprs(Double avgGprs) {
		this.avgGprs = avgGprs;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getAvgMessage() {
		return avgMessage;
	}

	public void setAvgMessage(int avgMessage) {
		this.avgMessage = avgMessage;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getSilentType() {
		return silentType;
	}

	public void setSilentType(String silentType) {
		this.silentType = silentType;
	}

	public String getHaveTest() {
		return haveTest;
	}

	public void setHaveTest(String haveTest) {
		this.haveTest = haveTest;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	
	
	

}
