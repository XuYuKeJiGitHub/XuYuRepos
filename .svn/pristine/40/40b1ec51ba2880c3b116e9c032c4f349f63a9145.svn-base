package com.xuyurepos.dao.payment;

import java.util.List;

import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.entity.payment.XuyuPayaddress;
/**
 * 充值链接
 * @author yangfei
 *
 */
public interface XuyuPayaddressDao {
	@SuppressWarnings("rawtypes")
	public List selectUserListWithPage(PageModel pageModel);

	@SuppressWarnings("rawtypes")
	public Integer selectUserCountWithPage(PageModel pageModel);
	
	/**
	 * 添加数据
	 * @param xuyuRecharge
	 */
	public void insert(XuyuPayaddress xuyuPayaddress);
	
	/**
	 * 根据uuid获取地址信息
	 * @param uuid
	 * @return
	 */
	public XuyuPayaddress findByUuid(String uuid);

}
