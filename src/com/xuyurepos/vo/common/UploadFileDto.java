package com.xuyurepos.vo.common;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UploadFileDto implements Serializable {
	private String annexeId;// ANNEXE_ID,
	private String annexeName;// ANNEXE_NAME,
	private Date createTime;// CREATE_TIME,
	private String annexeType;// ANNEXE_TYPE,
	private Double annexeSize;// ANNEXE_SIZE,
	private String relationInfo;// RELATION_INFO,
	private String relationMod;// RELATION_MOD,
	private String uploadUser;// UPLOAD_USER,
	private String uploadUserName;// UPLOAD_USER_NAME,
	private String uploadPath;// UPLOAD_PATH,
	private String uploadBatchNo;// UPLOAD_BATCHNO
	
	private String comboType;
	private String waitType;
	private String testType;
	private String cardType;
	private String standard;
	private String unitCost;
	
	private String ip;

	public String getAnnexeId() {
		return annexeId;
	}

	public void setAnnexeId(String annexeId) {
		this.annexeId = annexeId;
	}

	public String getAnnexeName() {
		return annexeName;
	}

	public void setAnnexeName(String annexeName) {
		this.annexeName = annexeName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAnnexeType() {
		return annexeType;
	}

	public void setAnnexeType(String annexeType) {
		this.annexeType = annexeType;
	}

	public Double getAnnexeSize() {
		return annexeSize;
	}

	public void setAnnexeSize(Double annexeSize) {
		this.annexeSize = annexeSize;
	}

	public String getRelationInfo() {
		return relationInfo;
	}

	public void setRelationInfo(String relationInfo) {
		this.relationInfo = relationInfo;
	}

	public String getRelationMod() {
		return relationMod;
	}

	public void setRelationMod(String relationMod) {
		this.relationMod = relationMod;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUploadBatchNo() {
		return uploadBatchNo;
	}

	public void setUploadBatchNo(String uploadBatchNo) {
		this.uploadBatchNo = uploadBatchNo;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public String getWaitType() {
		return waitType;
	}

	public void setWaitType(String waitType) {
		this.waitType = waitType;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
}
