package com.xuyurepos.dao.manager;

import java.util.List;

import com.xuyurepos.entity.manager.QuartzImport;

/**
 * 批量导入使用的dao类
 * @author yangfei
 *
 */
public interface QuartzImportDao {

	/**
	 * 查询数据在目标表中有没有
	 * 
	 * @return
	 */
	public int getCountYd();

	/**
	 * 查询数据在目标表中有没有
	 * 
	 * @return
	 */
	public int getCountLt();

	/**
	 * 查询数据在目标表中有没有
	 * 
	 * @return
	 */
	public int getCountDx();

	/**
	 * 删除不符合要求的数据
	 */
	public void delYd();

	/**
	 * 删除不符合要求的数据
	 */
	public void delLt();

	/**
	 * 删除不符合要求的数据
	 */
	public void delDx();
	
	/**
	 * 批量添加
	 * @param list
	 */
	public void insertByBatch(List<QuartzImport> list);
	
	/**
	 * 批量删除
	 */
   public void truncateTable();

}
