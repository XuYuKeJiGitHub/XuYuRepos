package com.xuyurepos.vo.autorule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@SuppressWarnings("serial")
public class AutoRuleLogDTO implements Serializable{
	  public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getOwnerPlaceName() {
		return ownerPlaceName;
	}
	public void setOwnerPlaceName(String ownerPlaceName) {
		this.ownerPlaceName = ownerPlaceName;
	}
	public String getWorkingConditionName() {
		return workingConditionName;
	}
	public void setWorkingConditionName(String workingConditionName) {
		this.workingConditionName = workingConditionName;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public Integer getCardNumTot() {
		return cardNumTot;
	}
	public void setCardNumTot(Integer cardNumTot) {
		this.cardNumTot = cardNumTot;
	}
	public Integer getCardNumActive() {
		return cardNumActive;
	}
	public void setCardNumActive(Integer cardNumActive) {
		this.cardNumActive = cardNumActive;
	}
	public Integer getCardNumTest() {
		return cardNumTest;
	}
	public void setCardNumTest(Integer cardNumTest) {
		this.cardNumTest = cardNumTest;
	}
	public Integer getCardNumWait() {
		return cardNumWait;
	}
	public void setCardNumWait(Integer cardNumWait) {
		this.cardNumWait = cardNumWait;
	}
	public Integer getCardNumStop() {
		return cardNumStop;
	}
	public void setCardNumStop(Integer cardNumStop) {
		this.cardNumStop = cardNumStop;
	}
	public BigDecimal getUseGprsActive() {
		return useGprsActive;
	}
	public void setUseGprsActive(BigDecimal useGprsActive) {
		this.useGprsActive = useGprsActive;
	}
	public BigDecimal getUseGprsTest() {
		return useGprsTest;
	}
	public void setUseGprsTest(BigDecimal useGprsTest) {
		this.useGprsTest = useGprsTest;
	}
	public BigDecimal getUseGprsWait() {
		return useGprsWait;
	}
	public void setUseGprsWait(BigDecimal useGprsWait) {
		this.useGprsWait = useGprsWait;
	}
	public BigDecimal getUseGprsStop() {
		return useGprsStop;
	}
	public void setUseGprsStop(BigDecimal useGprsStop) {
		this.useGprsStop = useGprsStop;
	}
	public String getComboType() {
		return comboType;
	}
	public void setComboType(String comboType) {
		this.comboType = comboType;
	}
	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
	public Integer getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}
	public BigDecimal getUseGprs() {
		return useGprs;
	}
	public void setUseGprs(BigDecimal useGprs) {
		this.useGprs = useGprs;
	}
	public BigDecimal getRemainGprs() {
		return remainGprs;
	}
	public void setRemainGprs(BigDecimal remainGprs) {
		this.remainGprs = remainGprs;
	}
	public BigDecimal getTotalGprs() {
		return totalGprs;
	}
	public void setTotalGprs(BigDecimal totalGprs) {
		this.totalGprs = totalGprs;
	}
	public BigDecimal getUseGprsPercent() {
		return useGprsPercent;
	}
	public void setUseGprsPercent(BigDecimal useGprsPercent) {
		this.useGprsPercent = useGprsPercent;
	}
	public String getWaitDate() {
		return waitDate;
	}
	public void setWaitDate(String waitDate) {
		this.waitDate = waitDate;
	}
	public String getRealWaitDate() {
		return realWaitDate;
	}
	public void setRealWaitDate(String realWaitDate) {
		this.realWaitDate = realWaitDate;
	}
	public String getDeadLineDate() {
		return deadLineDate;
	}
	public void setDeadLineDate(String deadLineDate) {
		this.deadLineDate = deadLineDate;
	}
	public String getStartWDate() {
		return startWDate;
	}
	public void setStartWDate(String startWDate) {
		this.startWDate = startWDate;
	}
	public String getEndWDate() {
		return endWDate;
	}
	public void setEndWDate(String endWDate) {
		this.endWDate = endWDate;
	}
	public String getStartDDate() {
		return startDDate;
	}
	public void setStartDDate(String startDDate) {
		this.startDDate = startDDate;
	}
	public String getEndDDate() {
		return endDDate;
	}
	public void setEndDDate(String endDDate) {
		this.endDDate = endDDate;
	}
	public String getStartRWDate() {
		return startRWDate;
	}
	public void setStartRWDate(String startRWDate) {
		this.startRWDate = startRWDate;
	}
	public String getEndRWDate() {
		return endRWDate;
	}
	public void setEndRWDate(String endRWDate) {
		this.endRWDate = endRWDate;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public BigDecimal getGprsYesterday() {
		return gprsYesterday;
	}
	public void setGprsYesterday(BigDecimal gprsYesterday) {
		this.gprsYesterday = gprsYesterday;
	}
	public String getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccessNum() {
		return accessNum;
	}
	public void setAccessNum(String accessNum) {
		this.accessNum = accessNum;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getOwnerPlace() {
		return ownerPlace;
	}
	public void setOwnerPlace(String ownerPlace) {
		this.ownerPlace = ownerPlace;
	}
	public String getWorkingCondition() {
		return workingCondition;
	}
	public void setWorkingCondition(String workingCondition) {
		this.workingCondition = workingCondition;
	}
	public String getIsDeal() {
		return isDeal;
	}
	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRealComboType() {
		return realComboType;
	}
	public void setRealComboType(String realComboType) {
		this.realComboType = realComboType;
	}

	private String id;//ID 主键
	  private String accessNum;//ACCESS_NUM 接入号
	  private String iccid;//ICCID      长号
	  private String imsi;//IMSI       短号
	  private String provider;//PROVIDER   运营商
	  private String providerName;//PROVIDER   运营商
	  private String ownerPlace;//OWNER_PLACE  地区
	  private String ownerPlaceName;//OWNER_PLACE  地区
	  private String workingCondition;//WORKING_CONDITION  通信状态
	  private String workingConditionName;
	  private String ownerId;//OWNER  群组
	  private String ownerName;//OWNER  群组
	  private String isDeal;//IS_DEAL 处理状态
	  private String operateType;//OPERATE_TYPE 处理方式01仅提醒02停机03复机
	  private String ruleId;//
	  private String ruleName;
	  private String createUser;
	  private Date createDate;
	  private String updateUser;
	  private Date updateDate;
	  private String startDate;
	  private String endDate;
	  //昨日流量使用量
	  private String agency;
	  private String agencyName;
	  private BigDecimal gprsYesterday;
	  private String queryDate;
	  //满期日期沉默期到期日期
	  private String waitDate;
	  private String realWaitDate;
	  private String deadLineDate;
	  private String startWDate;
	  private String endWDate;
	  private String startDDate;
	  private String endDDate;
	  private String startRWDate;
	  private String endRWDate;
	  //流量池信息
	  private BigDecimal useGprs;
	  private BigDecimal remainGprs;
	  private BigDecimal totalGprs;
	  private BigDecimal useGprsPercent;
	  private Integer activeCount;
	  private String comboName;
	  private String comboType;
	  //公司流量池信息
	  //provider,
		//ownerPlace,
		//comboType,
	  private Integer cardNumTot;
	  private Integer cardNumActive;
	  private Integer cardNumTest;
	  private Integer cardNumWait;
	  private Integer cardNumStop;
	  private BigDecimal useGprsActive;
	  private BigDecimal useGprsTest;
	  private BigDecimal useGprsWait;
	  private BigDecimal useGprsStop;
		//useGprs,
		//totalGprs
		//useGprsPercent
	  // 运营商套餐类型
	  private String realComboType;
}
