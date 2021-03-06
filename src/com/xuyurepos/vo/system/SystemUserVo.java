package com.xuyurepos.vo.system;

import java.io.Serializable;

/**
 * vo对象
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemUserVo implements Serializable{
	private String id;
	private String userName;
	private String cname;
	private String sex;
	private String email;
	private String mobile;
	private String orgId;
	private String orgName;
	private String deptId;
	private String deptName;
	
	private String queryOrgId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getQueryOrgId() {
		return queryOrgId;
	}
	public void setQueryOrgId(String queryOrgId) {
		this.queryOrgId = queryOrgId;
	}
	@Override
	public String toString() {
		return "SystemUserVo [id=" + id + ", userName=" + userName + ", cname=" + cname + ", sex=" + sex + ", email="
				+ email + ", mobile=" + mobile + ", orgId=" + orgId + ", orgName=" + orgName + ", deptId=" + deptId
				+ ", deptName=" + deptName + "]";
	}
	
}
