package com.xuyurepos.entity.system;

import java.io.Serializable;
/**
 * 后台模块
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemFunction implements Serializable{
	private int id;
	private String funcName;
	private String funcDesc;
	private String action;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFuncDesc() {
		return funcDesc;
	}
	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
