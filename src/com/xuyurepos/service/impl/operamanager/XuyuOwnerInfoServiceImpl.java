package com.xuyurepos.service.impl.operamanager;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.operamanager.XuyuOwnerInfoDao;
import com.xuyurepos.entity.operamanager.XuyuOwnerInfo;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.operamanager.XuyuOwnerInfoService;
import com.xuyurepos.vo.operamanager.XuyuOwnerInfoVo;
/**
 * 群组管理
 * @author yangfei
 */
@Service
@Transactional
public class XuyuOwnerInfoServiceImpl implements XuyuOwnerInfoService{
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuOwnerInfoServiceImpl.class);

	@Autowired
	private XuyuOwnerInfoDao xuyuOwnerInfoDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(xuyuOwnerInfoDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuOwnerInfoDao.selectUserCountWithPage(pageModel));
	}
    
	/**
	 * 删除数据
	 */
	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuOwnerInfo xuyuOwnerInfo=xuyuOwnerInfoDao.find(Integer.valueOf(id));
					if(xuyuOwnerInfo!=null){
						xuyuOwnerInfoDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存数据
	 */
	@Override
	public XuyuOwnerInfo saveInfo(XuyuOwnerInfoVo xuyuOwnerInfoVo) {
		log.info("systemLookUpVo:"+xuyuOwnerInfoVo.toString());
		XuyuOwnerInfo xuyuOwnerInfo=new XuyuOwnerInfo();
		if(xuyuOwnerInfoVo!=null&&SystemConstants.STRINGEMPTY.equals(xuyuOwnerInfoVo.getId())){
			xuyuOwnerInfo=add(xuyuOwnerInfoVo);
		}else{
			xuyuOwnerInfo=edit(xuyuOwnerInfoVo);
		}
		return xuyuOwnerInfo;
	}
    
	/**
	 * 修改数据
	 * @param xuyuOwnerInfoVo
	 * @return
	 */
	private XuyuOwnerInfo edit(XuyuOwnerInfoVo xuyuOwnerInfoVo) {
		XuyuOwnerInfo xuyuOwnerInfo=xuyuOwnerInfoDao.find(Integer.valueOf(xuyuOwnerInfoVo.getId()));
		if(xuyuOwnerInfo!=null){
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			BeanUtils.copyProperties(xuyuOwnerInfoVo, xuyuOwnerInfo);
			xuyuOwnerInfo.setUpdateUser(systemUser.getUserName());
			xuyuOwnerInfo.setUpdateUserName(systemUser.getCname());
			xuyuOwnerInfo.setUpdateDate(new Date());
			log.info("systemUser:"+xuyuOwnerInfo.toString());
			xuyuOwnerInfoDao.update(xuyuOwnerInfo);
		}
		return xuyuOwnerInfo;
	}
    
	/**
	 * 添加数据
	 * @param xuyuOwnerInfoVo
	 * @return
	 */
	private XuyuOwnerInfo add(XuyuOwnerInfoVo xuyuOwnerInfoVo) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		XuyuOwnerInfo xuyuOwnerInfo=new XuyuOwnerInfo();
		BeanUtils.copyProperties(xuyuOwnerInfoVo, xuyuOwnerInfo);
		xuyuOwnerInfo.setCreateUser(systemUser.getUserName());
		xuyuOwnerInfo.setCreateUserName(systemUser.getCname());
		xuyuOwnerInfo.setCreateDate(new Date());
		xuyuOwnerInfoDao.insert(xuyuOwnerInfo);
		return xuyuOwnerInfo;
	}
    
	/**
	 * 根据id查询对象
	 */
	@Override
	public XuyuOwnerInfoVo find(String id) {
		XuyuOwnerInfoVo xuyuOwnerInfoVo=new XuyuOwnerInfoVo();
		if(!StringUtil.isEmpty(id)){
			XuyuOwnerInfo xuyuOwnerInfo=xuyuOwnerInfoDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(xuyuOwnerInfo, xuyuOwnerInfoVo);
			xuyuOwnerInfoVo.setId(id);
		}
		return xuyuOwnerInfoVo;
	}

}
