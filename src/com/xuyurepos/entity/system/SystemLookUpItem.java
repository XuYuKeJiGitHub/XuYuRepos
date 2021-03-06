package com.xuyurepos.entity.system;

import java.io.Serializable;

/**
 * 系统数据字典表
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemLookUpItem implements Serializable{
	private int fId;
	private String fCode;
	private String fValue;
	private String fDesc;
	private int fOrderId;
	private String fLookUpId;
	
	private String  flfDesc;
	public int getfId() {
		return fId;
	}
	public void setfId(int fId) {
		this.fId = fId;
	}
	public String getfCode() {
		return fCode;
	}
	public void setfCode(String fCode) {
		this.fCode = fCode;
	}
	public String getfValue() {
		return fValue;
	}
	public void setfValue(String fValue) {
		this.fValue = fValue;
	}
	public String getfDesc() {
		return fDesc;
	}
	public void setfDesc(String fDesc) {
		this.fDesc = fDesc;
	}
	public int getfOrderId() {
		return fOrderId;
	}
	public void setfOrderId(int fOrderId) {
		this.fOrderId = fOrderId;
	}
	public String getfLookUpId() {
		return fLookUpId;
	}
	public void setfLookUpId(String fLookUpId) {
		this.fLookUpId = fLookUpId;
	}
	public String getFlfDesc() {
		return flfDesc;
	}
	public void setFlfDesc(String flfDesc) {
		this.flfDesc = flfDesc;
	}
	
}
