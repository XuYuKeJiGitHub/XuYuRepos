package com.xuyurepos.entity.system;

import java.io.Serializable;
/**
 * 系统机构表
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemOrg implements Serializable{
	
	private int id;
	
	private String orgId;// 机构id
	
	private String orgName; // 机构名称
	
	private String upOrgId;// 上级机构id 
	
	private String orgLevel;// 机构层级
	
	private String isLeaf;
	
	private String delFlag; //删除
	
	private String isDept; // 是否为部门

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUpOrgId() {
		return upOrgId;
	}

	public void setUpOrgId(String upOrgId) {
		this.upOrgId = upOrgId;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getIsDept() {
		return isDept;
	}

	public void setIsDept(String isDept) {
		this.isDept = isDept;
	}

	@Override
	public String toString() {
		return "SystemOrg [id=" + id + ", orgId=" + orgId + ", orgName=" + orgName + ", upOrgId=" + upOrgId
				+ ", orgLevel=" + orgLevel + ", isLeaf=" + isLeaf + ", delFlag=" + delFlag + ", isDept=" + isDept + "]";
	}
	
	

}
