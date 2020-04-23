package com.xuyurepos.dao.manager;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;

/**
 * 物联卡信息数据插入
 * @author yangfei
 *
 */
public interface XuyuContentCardInfoDao {
	
	/**
	 * 添加数据
	 * @param xuyuOwnerInfo
	 */
	public void insert(XuyuContentCardInfo xuyuContentCardInfo);
    
	/**
	 * 编辑数据
	 * @param xuyuOwnerInfo
	 */
	public void update(XuyuContentCardInfo xuyuContentCardInfo);
    
	/**
	 * 批量出库表
	 * @param accessNumStart
	 * @param accessNumEnd
	 */
	@SuppressWarnings("rawtypes")
	public void insertInfo(HashMap map);
	
	/**
	 * 分页查询
	 * @param pageModel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);
	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);
	
	public XuyuContentCardInfo find(String id);
	
	public XuyuContentCardInfo findAny(String id);
	
    
	/**
	 * 出库
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	public void updateInfo(HashMap map);
	/**
	 * 划卡
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	public void updateInfoOwner(HashMap map);
	
    /**
     * 检查数据有效性
     * @param map
     * @return
     */
	@SuppressWarnings("rawtypes")
	public int checkAgency(HashMap map);

	public void truncateTable();

	@SuppressWarnings("rawtypes")
	public void insertInfoOwnerTemp(HashMap map);

	@SuppressWarnings("rawtypes")
	public void updateInfoOwnerDate(HashMap map);
    
	/**
	 * 通过主键查找对象
	 * @param id
	 * @return
	 */
	public XuyuContentCardInfo findById(String id);
    /**
     * 查询数据数量
     * @param accessNumStart
     * @param accessNumEnd
     * @return
     */
	public int findCount(@Param("accessNumStart")String accessNumStart,@Param("accessNumEnd")String accessNumEnd,@Param("numType")String numType);
    /**
     * 查询是否设置套餐
     * @param accessNumStart
     * @param accessNumEnd
     * @return
     */
	public int findComboType(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd,@Param("numType")String numType);
    /**
     * 批量更新套餐总量
     * @param accessNumStart
     * @param accessNumEnd
     * @param chargeCost
     */
	public void updateTotalGprs(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd, @Param("chargeCost")String chargeCost,@Param("numType")String numType);
    /**
     * 更新到期日，如果满足第一个时间的话
     * @param accessNumStart
     * @param accessNumEnd
     * @param chargeCost
     */
	public void updateDeadlineDate(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd,@Param("chargeCost")String chargeCost,@Param("numType")String numType);
    /**
     * 更新套餐总量直接叠加
     * @param accessNumStart
     * @param accessNumEnd
     * @param chargeCost
     */
	public void updateTotalGprsAdd(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd, @Param("chargeCost")String chargeCost,@Param("numType")String numType);
    /**
     * 更新到期时间直接叠加
     * @param accessNumStart
     * @param accessNumEnd
     * @param chargeCost
     */
	public void updateDeadlineDateAdd(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd, @Param("chargeCost")String chargeCost,@Param("numType")String numType);
    
	/**
	 * 查询数据数量
	 * @param accessNumStart
	 * @param accessNumEnd
	 * @return
	 */
	public int findCountIccId(@Param("accessNumStart")String accessNumStart,@Param("accessNumEnd")String accessNumEnd);
    /**
     * 删除数据
     * @param id
     */
	public void del(String id);

	public void updateTotalGprsAddIccId(@Param("accessNumStart")String accessNumStart, @Param("accessNumEnd")String accessNumEnd, @Param("chargeCost")String chargeCost);
    
	/**
	 * 插入输入
	 * @param map
	 */
	public void insertInfoSelF(HashMap map);
    
	/**
	 * 重新划卡
	 * @param map
	 */
	public void updateInfoOwnerNew(HashMap map);

	public void insertInfoOwnerTempNew(HashMap map);

	public void updateTotalGprsAddNew(@Param("chargeCost")String chargeCost, @Param("numType")String numType);

	public void updateTotalGprsNew(@Param("chargeCost")String chargeCost,@Param("numType")String numType);

	public void updateDeadlineDateAddNew(@Param("chargeCost")String chargeCost,@Param("numType")String numType);

	public void updateDeadlineDateNew(@Param("chargeCost")String chargeCost,@Param("numType")String numType);

	public void updateCardState(@Param("accessNum")String accessNum, @Param("state")String state);

}
