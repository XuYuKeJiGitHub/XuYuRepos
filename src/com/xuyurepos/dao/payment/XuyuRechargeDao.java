package com.xuyurepos.dao.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.payment.XuyuRecharge;

public interface XuyuRechargeDao {
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public XuyuRecharge find(String id);
    
	/**
	 * 删除
	 * @param id
	 */
	public void del(Integer id);
    
	/**
	 * 添加数据
	 * @param xuyuRecharge
	 */
	public void insert(XuyuRecharge xuyuRecharge);
	
	public void insertAccs(Map<String, Object> map);
    
	/**
	 * 编辑数据
	 * @param xuyuRecharge
	 */
	public void update(XuyuRecharge xuyuRecharge);

	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);

	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);

	public void createOrder(XuyuRecharge xuyuRecharge);

	@SuppressWarnings("rawtypes")
	public List selectCustomerListWithPage(PageModel pageModel);

	@SuppressWarnings("rawtypes")
	public Integer selectCustomerCountWithPage(PageModel pageModel);

	public void insertAccsNew(HashMap map);

}
