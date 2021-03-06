package com.xuyurepos.service.impl.system;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemMenuDao;
import com.xuyurepos.entity.system.SystemMenu;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemFunctionService;
import com.xuyurepos.service.system.SystemMenuService;
import com.xuyurepos.vo.system.SystemFunctionVo;
import com.xuyurepos.vo.system.SystemMenuVo;
/**
 * 系统菜单实现类
 * @author yangfei
 *
 */
@Service
@Transactional
public class SystemMenuServiceImpl implements SystemMenuService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemMenuServiceImpl.class);

	@Autowired
	private SystemMenuDao systemMenuDao;
	
	@Autowired
	private SystemFunctionService systemFunctionService; 

	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		try {
			pageModel.setRows(systemMenuDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(systemMenuDao.selectUserCountWithPage(pageModel));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
	/**
	 * 删除
	 */
	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					SystemMenu  systemMenu=systemMenuDao.find(id);
					if(systemMenu!=null){
						systemMenuDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存
	 */
	@Override
	public SystemMenu saveInfo(SystemMenuVo systemMenuVo) {
		log.info("systemMenuVo:"+systemMenuVo.toString());
		SystemMenu systemMenu=new SystemMenu();
		if(systemMenuVo!=null&&SystemConstants.STRINGEMPTY.equals(systemMenuVo.getId())){
			BeanUtils.copyProperties(systemMenuVo, systemMenu);
			if(systemMenuVo.getParentId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemMenuVo.getParentId())){
				systemMenu.setParentId(Integer.valueOf(systemMenuVo.getParentId()));
			}
			if(systemMenuVo.getModFuncId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemMenuVo.getModFuncId())){
				systemMenu.setModFuncId(Integer.valueOf(systemMenuVo.getModFuncId()));
			}
			systemMenu.setOrder(Integer.valueOf(systemMenuVo.getOrder()));
			log.info("systemMenu:"+systemMenuVo.toString());
			add(systemMenu);
		}else{
			systemMenu=edit(systemMenuVo);
		}
		return systemMenu;
	}
    
	/**
	 * 修改数据
	 * @param systemMenuVo
	 * @return
	 */
	private SystemMenu edit(SystemMenuVo systemMenuVo) {
		SystemMenu systemMenu=systemMenuDao.find(Integer.valueOf(systemMenuVo.getId()));
		if(systemMenu!=null){
			BeanUtils.copyProperties(systemMenuVo, systemMenu);
			if(systemMenuVo.getParentId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemMenuVo.getParentId())){
			    systemMenu.setParentId(Integer.valueOf(systemMenuVo.getParentId()));
			}
			if(systemMenuVo.getModFuncId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemMenuVo.getModFuncId())){
				systemMenu.setModFuncId(Integer.valueOf(systemMenuVo.getModFuncId()));
			}
			systemMenu.setOrder(Integer.valueOf(systemMenuVo.getOrder()));
			log.info("systemMenu:"+systemMenu.toString());
			systemMenuDao.update(systemMenu);
		}
		return systemMenu;
	}
    
	/**
	 * 新增数据
	 * @param systemMenu
	 */
	private void add(SystemMenu systemMenu) {
		systemMenu.setCrtDate(new Date());
		systemMenuDao.insert(systemMenu);
	}

	/**
	 * 查询对象
	 */
	@Override
	public SystemMenuVo find(String id) {
		SystemMenuVo systemMenuVo=new SystemMenuVo();
		if(!StringUtil.isEmpty(id)){
			SystemMenu systemMenu=systemMenuDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(systemMenu, systemMenuVo);
			if(systemMenu.getModFuncId()!=0){
				systemMenuVo.setModFuncId(SystemConstants.STRINGEMPTY+systemMenu.getModFuncId());
			}
			systemMenuVo.setParentId(SystemConstants.STRINGEMPTY+systemMenu.getParentId());
			systemMenuVo.setOrder(SystemConstants.STRINGEMPTY+systemMenu.getOrder());
			systemMenuVo.setId(id);
			if(systemMenu.getModFuncId()!=0){
				SystemFunctionVo systemFunctionVo=systemFunctionService.find(systemMenuVo.getModFuncId());
				systemMenuVo.setModFuncName(systemFunctionVo.getFuncName());
			}
		}
		return systemMenuVo;
	}

	@Override
	public List<SystemMenuVo> loadMenuTree() {
		return systemMenuDao.loadMenuTree();
	}

	@Override
	public List<SystemMenuVo> loadMenuChildrenTree(String fID) {
		 return systemMenuDao.loadMenuChildrenTree(fID);
	}

	@Override
	public List<SystemMenuVo> loadUserFirstMenu(SystemUser systemUser) {
		// 判断是否为系统管理员
		if(systemUser.getIsAdmin()){
			return systemMenuDao.loadMenuTree();
		}else{
			return systemMenuDao.loadUserMenuTree(SystemConstants.STRINGEMPTY+systemUser.getId());
		}
		
	}

	@Override
	public List<SystemMenuVo> loadUserChildrenMenu(SystemUser systemUser,String FID) {
		// 判断是否为系统管理员
		if(systemUser.getIsAdmin()){
			return systemMenuDao.loadMenuChildrenTree(FID);
		}else{
			return systemMenuDao.loadUserMenuChildrenTree(SystemConstants.STRINGEMPTY+systemUser.getId(), FID);
		}
		
	}

}
