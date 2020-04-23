package com.xuyurepos.dao.manager;

import java.util.HashMap;

import com.xuyurepos.entity.manager.XuyuContentCardInfoRecord;
/**
 * 划卡明细表
 * @author yangfei
 */
public interface XuyuContentCardInfoRecordDao {

	/**
	 * 添加数据
	 * @param xuyuOwnerInfo
	 */
	public void insert(XuyuContentCardInfoRecord xuyuContentCardInfoRecord);
    
	/**
	 * 编辑数据
	 * @param xuyuOwnerInfo
	 */
	public void update(XuyuContentCardInfoRecord xuyuContentCardInfoRecord);
	
	public String getSequence(String seqName);
    
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public XuyuContentCardInfoRecord find(Integer id);
    
	/**
	 * 删除数据
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 批量出库
	 * @param accessNumStart
	 * @param accessNumEnd
	 * @param unitCost
	 */
	@SuppressWarnings("rawtypes")
	public void insertInfo(HashMap map);
    
	/**
	 * 划卡
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	public void insertOwner(HashMap map);
    
	/**
	 * 出库插入明细表
	 * @param map
	 */
	public void insertInfoSel(HashMap map);
    /**
     * 划卡
     * @param map
     */
	public void insertOwnerNew(HashMap map);




}
