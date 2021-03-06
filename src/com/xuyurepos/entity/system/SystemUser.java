package com.xuyurepos.entity.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author yangfei
 * 系统用户表
 */
@SuppressWarnings("serial")
public class SystemUser implements Serializable{
	private int id;
	private String userName;//登陆名
	private String password;//密码
	private String orgId;
	private String orgName;
	private String deptId;
	private String deptName;
	private String cname;
	private String sex;
	private String email;
	private String mobile;
	
	private String orgCode;
	
	private List<SystemRole> list;// 当前用户默认角色
	
	private Map<String,Object> map;
	
	private Boolean isAdmin; // 是否为系统管理员
	
	private String j_captcha;
	
	private String orgLevel; // 机构层级
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
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
	
	public String getJ_captcha() {
		return j_captcha;
	}
	public void setJ_captcha(String j_captcha) {
		this.j_captcha = j_captcha;
	}
	
	public List<SystemRole> getList() {
		return list;
	}
	public void setList(List<SystemRole> list) {
		this.list = list;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Override
	public String toString() {
		return "SystemUser [id=" + id + ", userName=" + userName + ", password=" + password + ", orgId=" + orgId
				+ ", orgName=" + orgName + ", deptId=" + deptId + ", deptName=" + deptName + ", cname=" + cname
				+ ", sex=" + sex + ", email=" + email + ", mobile=" + mobile + ", orgCode=" + orgCode + ", list=" + list
				+ ", map=" + map + ", isAdmin=" + isAdmin + ", j_captcha=" + j_captcha + ", orgLevel=" + orgLevel + "]";
	}
	
	
}
