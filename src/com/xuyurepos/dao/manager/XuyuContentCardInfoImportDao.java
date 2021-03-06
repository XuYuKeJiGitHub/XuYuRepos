package com.xuyurepos.dao.manager;

import org.apache.ibatis.annotations.Param;

/**
 * 导入更新数据库接口
 * @author yangfei
 *
 */
public interface XuyuContentCardInfoImportDao {
	
	/**
	 * 获取序号
	 * @param seqName
	 * @return
	 */
	public String getSequence(String seqName);
	
	/**
	 * 数据更新
	 * @param map
	 */
	public void insertMobilInfo(@Param("batchNo")String batchNo);
	
	/**
	 * 插入未更新的内容
	 * @param batchNo
	 */
	public void insertNotExistsMobilInfo(@Param("batchNo")String batchNo);
	
    /**
     * 获取数据是否已经插入
     * @return
     */
	public int getCountYd();
    /**
     * 更新已插入的内容
     * @param batchNo
     */
	public void updateExistsMobilInfo(String batchNo);
    /**
     * 值转换
     */
	public void updateYd();
    /**
     * 值转换
     */
	public void updateLt();
    /**
     * 获取数据是否已经插入
     * @return
     */
	public int getCountLt();

	public void insertUnicomInfo(String batchNo);

	public void updateExistsUnicomInfo(String batchNo);

	public void insertNotExistsUnicomInfo(String batchNo);

	public void updateDx();

	public int getCountDx();

	public void insertTelecomInfo(String batchNo);

	public void updateExistsTelecomInfo(String batchNo);

	public void insertNotExistsTelecomInfo(String batchNo);
    
	/**
	 * 同步更新内容
	 * @param batchNo
	 */
	public void updateBasic(String batchNo);
    /**
     * 计算相对数据
     * @param batchNo
     */
	public void updateSum(String batchNo);

	public void updateSumOwner(String batchNo);

	public void updateActivateNoTest(String batchNo);

	public void updateActivate(String batchNo);

	public void updateDeadline(String batchNo);

}
