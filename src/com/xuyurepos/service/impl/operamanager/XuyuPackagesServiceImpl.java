package com.xuyurepos.service.impl.operamanager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.operamanager.XuyuPackagesDao;
import com.xuyurepos.dao.payment.XuyuPayaddressDao;
import com.xuyurepos.entity.operamanager.XuyuPackages;
import com.xuyurepos.entity.payment.XuyuPayaddress;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.operamanager.XuyuPackagesService;
import com.xuyurepos.vo.operamanager.XuyuPackagesVo;
@Service
@Transactional
public class XuyuPackagesServiceImpl implements XuyuPackagesService{
	
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuPackagesServiceImpl.class);

	@Autowired
	private XuyuPackagesDao xuyuPackagesDao;
	@Autowired
	private XuyuPayaddressDao xuyuPayaddressDao;
	
	@Autowired
	private CommonMapper commonMapper;
    
	/**
	 * 分页查询
	 * @throws CustomException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findList(PageModel pageModel){
		pageModel.setRows(xuyuPackagesDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuPackagesDao.selectUserCountWithPage(pageModel));
	}
	
	/**
	 * 页面H5查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findListH5(PageModel pageModel) throws CustomException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		XuyuPayaddress payaddress=xuyuPayaddressDao.findByUuid(id);
		if(payaddress==null){
			throw new CustomException("地址错误,请检查");
		}else{
			XuyuPackagesVo queryObj=(XuyuPackagesVo) pageModel.getQueryObj();
			queryObj.setAgencyId(payaddress.getAgencyId());
			// 判定类型是否是多类型
			if(type!=null&&!"".equals(type)){
				if(type.indexOf(";")!=-1){
					String[] types=type.split(";");
					queryObj.setOpera(types[0]);
					queryObj.setAvailableType(types[1]);
				}else{
					queryObj.setOpera(type);
				}
			}
			pageModel.setQueryObj(queryObj);
			pageModel.setRows(xuyuPackagesDao.selectH5Page(pageModel));
		    pageModel.setTotal(xuyuPackagesDao.selectH5CountPage(pageModel));
		}
	}
    
	/**
	 * 批量删除
	 */
	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuPackages xuyuPackages=xuyuPackagesDao.find(id);
					if(xuyuPackages!=null){
						xuyuPackagesDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存数据
	 */
	@Override
	public XuyuPackages saveInfo(XuyuPackagesVo xuyuPackagesVo) {
		log.info("xuyuPackagesVo:"+xuyuPackagesVo.toString());
		XuyuPackages xuyuPackages=new XuyuPackages();
		if(xuyuPackagesVo!=null&&SystemConstants.STRINGEMPTY.equals(xuyuPackagesVo.getId())){
			xuyuPackages=add(xuyuPackagesVo);
		}else{
			xuyuPackages=edit(xuyuPackagesVo);
		}
		return xuyuPackages;
	}
    
	/**
	 * 编辑数据
	 * @param xuyuPackagesVo
	 * @return
	 */
	private XuyuPackages edit(XuyuPackagesVo xuyuPackagesVo) {
		XuyuPackages xuyuPackages=xuyuPackagesDao.find(Integer.valueOf(xuyuPackagesVo.getId()));
		xuyuPackagesVo.setState(xuyuPackages.getState());
		if(xuyuPackages!=null){
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(systemUser.getOrgCode().equals("1000")){
				BeanUtils.copyProperties(xuyuPackagesVo, xuyuPackages);
				log.info("xuyuPackages:"+xuyuPackages.toString());
				xuyuPackages.setUpdateTime(new Date());
				
				String order=request.getParameter("order");
				if(order!=null&&!SystemConstants.STRINGEMPTY.equals(order)){
					xuyuPackages.setOrder(Integer.valueOf(order));
				}
				BigDecimal comnoName=BigDecimal.valueOf(xuyuPackages.getGprs()/Integer.valueOf(xuyuPackages.getAvailableType()));
				xuyuPackages.setComnoName(comnoName);
			}else{
                xuyuPackages.setUpdateTime(new Date());
				String order=request.getParameter("order");
				if(order!=null&&!SystemConstants.STRINGEMPTY.equals(order)){
					xuyuPackages.setOrder(Integer.valueOf(order));
				}
				xuyuPackages.setSelfPrice(xuyuPackagesVo.getSelfPrice());
				xuyuPackages.setDesc(xuyuPackagesVo.getDesc());
				BigDecimal comnoName=BigDecimal.valueOf(xuyuPackages.getGprs()/Integer.valueOf(xuyuPackages.getAvailableType()));
				xuyuPackages.setComnoName(comnoName);
			}
			xuyuPackagesDao.update(xuyuPackages);
		}
		return xuyuPackages;
	} 
    
	/**
	 * 添加数据
	 * @param xuyuPackagesVo
	 * @return
	 */
	private XuyuPackages add(XuyuPackagesVo xuyuPackagesVo) {
		XuyuPackages xuyuPackages=new XuyuPackages();
		BeanUtils.copyProperties(xuyuPackagesVo, xuyuPackages);
		xuyuPackages.setUpdateTime(new Date());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String order=request.getParameter("order");
		if(order!=null&&!SystemConstants.STRINGEMPTY.equals(order)){
			xuyuPackages.setOrder(Integer.valueOf(order));
		}
		// 设置代理机构名称
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		xuyuPackages.setAgencyId(systemUser.getOrgId());
		// 设置默认上架状态
		xuyuPackages.setState("02");
		xuyuPackagesDao.insert(xuyuPackages);
		return xuyuPackages;
	}
    
	/**
	 * 查询数据
	 */
	@Override
	public XuyuPackagesVo find(String id) {
		XuyuPackagesVo xuyuPackagesVo=new XuyuPackagesVo();
		if(!StringUtil.isEmpty(id)){
			XuyuPackages xuyuPackages=xuyuPackagesDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(xuyuPackages, xuyuPackagesVo);
			xuyuPackagesVo.setId(id);
			xuyuPackagesVo.setOrder(SystemConstants.STRINGEMPTY+xuyuPackages.getOrder());
		}
		return xuyuPackagesVo;
	}

	@Override
	public void setState(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			String state="01";
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuPackages xuyuPackages=xuyuPackagesDao.find(id);
					if(i==0){
						if(state.equals(xuyuPackages.getState())){
							state="02";
						}
					}
					if(xuyuPackages!=null){
						xuyuPackages.setState(state);
						xuyuPackagesDao.update(xuyuPackages);
					}
				}
			}
		}
	}
    
	/**
	 * 发送套餐给客户
	 */
	@Override
	public void packageSend(String ids, String agency) {
		ids=ids.replace("\r\n", "");
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuPackages xuyuPackages=xuyuPackagesDao.find(id);
					xuyuPackages.setId(null);
					xuyuPackages.setUpdateTime(new Date());
					// 设置代理机构名称
					xuyuPackages.setAgencyId(agency);
					xuyuPackagesDao.insert(xuyuPackages);
				}
			}
		}
	}

	@Override
	public Map<String, Object> select(String accessNum) {
		StringBuilder sb=new StringBuilder();
		sb.append(" select ");
		sb.append(" ACCESS_NUM  accessNum, ");
		sb.append(" ICCID  iccid, ");
		sb.append(" OWNER_PLACE ownerPlace,");
		sb.append(" case when t1.PROVIDER='1' then  ");
        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
        sb.append("  when t1.PROVIDER='2' then ");
        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
        sb.append(" when t1.PROVIDER='3' then ");
        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t1.WORKING_CONDITION) ");
        sb.append("  end workingCondition, ");
		sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t1.PROVIDER) as PROVIDER, ");
		sb.append(" '' as   arrea,");
		sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t1.COMBO_TYPE)  comboType,");
		sb.append(" t1.COMNO_NAME  comboValue, ");
		sb.append(" IFNULL(floor(TOTAL_GPRS),0)  totalGprs,");
		sb.append(" IFNULL(floor(REMAIN_GPS),0)  remainGprs,");
		sb.append(" IFNULL(floor(USE_GPRS),0)    useGprs,");
		sb.append(" DATE_FORMAT(t1.DEADLINE_DATE,'%Y-%m-%d') deadLineDate");
		sb.append(" from XUYU_CONTENT_CARD_INFO t1 where (t1.ACCESS_NUM like '"+accessNum+"%' or t1.ICCID like '"+accessNum+"%')  ");
		Map<String, Object> map=commonMapper.findOneData(sb.toString());
		// 判断是否有叠加包
		return map;
	}

}
