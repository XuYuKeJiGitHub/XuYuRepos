package com.xuyurepos.vo.logger;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoggerInfoDto implements Serializable{
	
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
	private String id;
	private String mark;
	private String opreate;
	private String modelId;
	private String modelName;
    private String opreateDesc;
    private String startDate;
    private String endDate;
	public String getOpreateDesc() {
		return opreateDesc;
	}
	public void setOpreateDesc(String opreateDesc) {
		this.opreateDesc = opreateDesc;
	}
	private String createUser;
	private String createDate;
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getOpreate() {
		return opreate;
	}
	public void setOpreate(String opreate) {
		this.opreate = opreate;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "LoggerInfoDto [id=" + id + ", modelId=" + modelId 
				+ ", modelName=" + modelName +", opreate=" + opreate
				+", mark=" + mark+", createUser=" + createUser+", createDate=" + createDate+",opreateDesc="+opreateDesc+"]";
	}
}
