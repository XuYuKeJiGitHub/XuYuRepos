package com.xuyurepos.vo.quartzJob;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuartzJobDto implements Serializable {
	private String id;
	private String asyncBatchNo;
	private String asyncType;
	private String asyncName;
	private String asyncFlag;
	
	private String ip;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsyncBatchNo() {
		return asyncBatchNo;
	}

	public void setAsyncBatchNo(String asyncBatchNo) {
		this.asyncBatchNo = asyncBatchNo;
	}

	public String getAsyncType() {
		return asyncType;
	}

	public void setAsyncType(String asyncType) {
		this.asyncType = asyncType;
	}

	public String getAsyncName() {
		return asyncName;
	}

	public void setAsyncName(String asyncName) {
		this.asyncName = asyncName;
	}

	public String getAsyncFlag() {
		return asyncFlag;
	}

	public void setAsyncFlag(String asyncFlag) {
		this.asyncFlag = asyncFlag;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
