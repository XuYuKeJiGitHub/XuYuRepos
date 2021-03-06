package com.xuyurepos.vo.system;

import java.io.Serializable;

/**
 * 数据字典vo对象
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemLookUpItemVo implements Serializable{
	private String fId;
	private String fCode;
	private String fValue;
	private String fDesc;
	private int fOrderId;
	private String fLookUpId;
	public String getfId() {
		return fId;
	}
	public void setfId(String fId) {
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
	@Override
	public String toString() {
		return "SystemLookUpItemVo [fId=" + fId + ", fCode=" + fCode + ", fValue=" + fValue + ", fDesc=" + fDesc
				+ ", fOrderId=" + fOrderId + ", fLookUpId=" + fLookUpId + "]";
	}
	
	

}
