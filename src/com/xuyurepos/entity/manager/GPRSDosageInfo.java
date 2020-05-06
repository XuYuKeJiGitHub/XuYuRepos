package com.xuyurepos.entity.manager;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class GPRSDosageInfo implements Serializable{

	private String accessNum;//	String	20	Y  				接入号				ACCESS_NUM
	private String comboType;//	String	4	Y				套餐种类				COMBO_TYPE
	private BigDecimal comboValue;//	bigdecimal	(12,4)	Y	套餐规模			COMNO_NAME
	private BigDecimal flowRemain;//	bigdecimal	(12,4)	Y	剩余流量			REMAIN_GPS
	private BigDecimal flowUsed;//	bigdecimal	(12,4)	Y		已用流量			USE_GPRS
	private String provider;//	String	2	Y				运营商				PROVIDER
	private String expireDate;//	datetime	8	N		满期日				DEADLINE_DATE
	private String iccid;//	String	64	Y					长号					ICCID
	private BigDecimal flowTotal;//	bigdecimal	(12,4)	Y		套餐总量			TOTAL_GPRS
	private String workingCondition;//	String	20	N		卡状态				WORKING_CONDITION
	//private Integer onlineStatus;//	Int	4	N				 联网状态，1在线 0 离线		WORKING_CONDITION
	public String getAccessNum() {
		return accessNum;
	}
	public void setAccessNum(String accessNum) {
		this.accessNum = accessNum;
	}
	public String getComboType() {
		return comboType;
	}
	public void setComboType(String comboType) {
		this.comboType = comboType;
	}
	public BigDecimal getComboValue() {
		return comboValue;
	}
	public void setComboValue(BigDecimal comboValue) {
		this.comboValue = comboValue;
	}
	public BigDecimal getFlowRemain() {
		return flowRemain;
	}
	public void setFlowRemain(BigDecimal flowRemain) {
		this.flowRemain = flowRemain;
	}
	public BigDecimal getFlowUsed() {
		return flowUsed;
	}
	public void setFlowUsed(BigDecimal flowUsed) {
		this.flowUsed = flowUsed;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public BigDecimal getFlowTotal() {
		return flowTotal;
	}
	public void setFlowTotal(BigDecimal flowTotal) {
		this.flowTotal = flowTotal;
	}
	public String getWorkingCondition() {
		return workingCondition;
	}
	public void setWorkingCondition(String workingCondition) {
		this.workingCondition = workingCondition;
	}
	@Override
	public String toString() {
		return "GPRSDosageInfo [accessNum=" + accessNum + ", comboType=" + comboType + ", comboValue=" + comboValue
				+ ", flowRemain=" + flowRemain + ", flowUsed=" + flowUsed + ", provider=" + provider + ", expireDate="
				+ expireDate + ", iccid=" + iccid + ", flowTotal=" + flowTotal + ", workingCondition="
				+ workingCondition + "]";
	}
	
	
}
