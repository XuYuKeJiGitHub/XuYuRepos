package com.xuyurepos.vo.operamanager;
/**
 * 群组数据
 * @author yangfei
 *
 */
public class XuyuOwnerInfoVo {
    private String id;
	
	private String companyId;
	
	private String companyName;
	
	private String comboType;
	
    private String comboName;
    
     private String owner;
	
	private String ownerName;
	
	private String payType;
	
	private String silentType;
	
	private String haveTest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getSilentType() {
		return silentType;
	}

	public void setSilentType(String silentType) {
		this.silentType = silentType;
	}

	public String getHaveTest() {
		return haveTest;
	}

	public void setHaveTest(String haveTest) {
		this.haveTest = haveTest;
	}

	@Override
	public String toString() {
		return "XuyuOwnerInfoVo [id=" + id + ", companyId=" + companyId + ", companyName=" + companyName
				+ ", comboType=" + comboType + ", comboName=" + comboName + ", owner=" + owner + ", ownerName="
				+ ownerName + ", payType=" + payType + ", silentType=" + silentType + ", haveTest=" + haveTest + "]";
	}
	
}
