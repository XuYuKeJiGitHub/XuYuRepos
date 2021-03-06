package com.xuyurepos.dao.manager;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.XuyuContentCardMgr;

public interface XuyuContentCardMgrDao {
	/**
	 * 将导入临时表的数据插入目标表
	 * @param operator 运营商
	 * @param batchNo  关联id
	 */
	public void insertYd(@Param("operator")String operator,@Param("ownerPlace")String ownerPlace,@Param("batchNo")String batchNo ,@Param("comboType")String comboType,@Param("waitType")String waitType,@Param("testType")String testType,@Param("cardType")String cardType,@Param("standard")String standard,@Param("unitCost")String unitCost);
	/**
	 * 将导入临时表的数据插入目标表
	 * @param operator 运营商
	 * @param batchNo  关联id
	 */
	public void insertLt(@Param("operator")String operator,@Param("ownerPlace")String ownerPlace,@Param("batchNo")String batchNo,@Param("comboType")String comboType,@Param("waitType")String waitType,@Param("testType")String testType,@Param("cardType")String cardType,@Param("standard")String standard,@Param("unitCost")String unitCost);
	/**
	 * 将导入临时表的数据插入目标表
	 * @param operator 运营商
	 * @param batchNo  关联id
	 */
	public void insertDx(@Param("operator")String operator,@Param("ownerPlace")String ownerPlace,@Param("batchNo")String batchNo,@Param("comboType")String comboType,@Param("waitType")String waitType,@Param("testType")String testType,@Param("cardType")String cardType,@Param("standard")String standard,@Param("unitCost")String unitCost);
	
	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);
	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);
	
	public XuyuContentCardMgr find(String id);
	
	/**
	 * 卡出库的查询
	 * @param pageModel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List selectListWithPage(PageModel pageModel);
	@SuppressWarnings("rawtypes")
	public Integer selectCountWithPage(PageModel pageModel);
	
    
	/**
	 * 编辑数据
	 * @param systemUser
	 */
	public void update(XuyuContentCardMgr xuyuContentCardMgr);
	/**
	 * 批量出库
	 * @param accessNumStart
	 * @param accessNumEnd
	 */
	@SuppressWarnings("rawtypes")
	public void updateInfo(HashMap map);
	/**
	 * 查询出库数量
	 * @param accessNumStart
	 * @param accessNumEnd
	 * @return
	 */
	public int findCount(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd);
	/**
	 * 查询出库数量
	 * @param accessNumStart
	 * @param accessNumEnd
	 * @return
	 */
	public int findCountIccId(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd);
	/**
	 * 删除数据
	 * @param id
	 */
	public void del(String id);
	
	public XuyuContentCardMgr findAny(String id);
	
	/**
	 * 修改出库标志位
	 * @param map
	 */
	public void updateInfoManager(HashMap map);
	
}
