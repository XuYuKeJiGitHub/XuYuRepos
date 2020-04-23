package com.xuyurepos.entity.system;

import java.io.Serializable;
/**
 * 系统数据字典类型表
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemLookUp implements Serializable{
	private int id;
	private String fName;
	private String fDesc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getfDesc() {
		return fDesc;
	}
	public void setfDesc(String fDesc) {
		this.fDesc = fDesc;
	}
	
	

}
