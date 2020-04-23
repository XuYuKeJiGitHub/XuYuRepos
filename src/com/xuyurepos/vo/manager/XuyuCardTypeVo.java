package com.xuyurepos.vo.manager;
/**
 * 卡类型vo
 * @author yangfei
 *
 */
public class XuyuCardTypeVo {
    private String id;
	
	private String cardId;// id
	
	private String cardName; // 名称
	
	private String cardOrgId;// 上级id 
	
	private String cardLevel;// 层级

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardOrgId() {
		return cardOrgId;
	}

	public void setCardOrgId(String cardOrgId) {
		this.cardOrgId = cardOrgId;
	}

	public String getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(String cardLevel) {
		this.cardLevel = cardLevel;
	}
}
