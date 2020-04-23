package com.xuyurepos.vo.system;

import java.io.Serializable;

/**
 * 后台模块Vo
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemFunctionVo implements Serializable{
	private String id;
	private String funcName;
	private String funcDesc;
	private String action;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
