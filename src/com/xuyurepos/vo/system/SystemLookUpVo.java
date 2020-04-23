package com.xuyurepos.vo.system;

import java.io.Serializable;

/**
 * 数据字典类型vo对象
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class SystemLookUpVo implements Serializable{
	private String id;
	private String fName;
	private String fDesc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	@Override
	public String toString() {
		return "SystemLookUpVo [id=" + id + ", fName=" + fName + ", fDesc=" + fDesc + "]";
	}
	
	

}
