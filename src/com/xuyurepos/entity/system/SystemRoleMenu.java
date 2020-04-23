package com.xuyurepos.entity.system;

import java.io.Serializable;
/**
 * 角色菜单
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemRoleMenu implements Serializable{
	private int id;
	private int roleId;
	private int menuId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	
	
	
}
