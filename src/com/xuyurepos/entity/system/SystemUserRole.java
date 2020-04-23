package com.xuyurepos.entity.system;

import java.io.Serializable;
/**
 * 用户角色
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemUserRole implements Serializable{
	private int id;
	private int userId;
	private int roleId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
