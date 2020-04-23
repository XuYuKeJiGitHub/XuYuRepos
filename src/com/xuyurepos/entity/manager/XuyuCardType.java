package com.xuyurepos.entity.manager;

import java.io.Serializable;
/**
 * 卡类型表
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class XuyuCardType implements Serializable{
	
	private int id;
	
	private String cardId;// id
	
	private String cardName; // 名称
	
	private String cardOrgId;// 上级id 
	
	private String cardLevel;// 层级
	
	private String isLeaf;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

}
