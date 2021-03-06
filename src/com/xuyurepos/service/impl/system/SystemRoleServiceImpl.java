package com.xuyurepos.service.impl.system;

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
import com.xuyurepos.dao.system.SystemRoleDao;
import com.xuyurepos.entity.system.SystemRole;
import com.xuyurepos.service.system.SystemRoleService;
import com.xuyurepos.vo.system.SystemRoleVo;
/**
 * 系统角色实现类
 * @author yangfei
 *
 */
@Service
@Transactional
public class SystemRoleServiceImpl implements SystemRoleService{

	
	Logger log=LoggerFactory.getInstance().getLogger(SystemRoleServiceImpl.class);

	@Autowired
	private SystemRoleDao systemRoleDao;
	
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(systemRoleDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(systemRoleDao.selectUserCountWithPage(pageModel));
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
					SystemRole  systemRole=systemRoleDao.find(id);
					if(systemRole!=null){
						systemRoleDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存数据
	 */
	@Override
	public SystemRole saveInfo(SystemRoleVo systemRoleVo) {
		log.info("systemRoleVo:"+systemRoleVo.toString());
		SystemRole systemRole=new SystemRole();
		if(systemRoleVo!=null&&SystemConstants.STRINGEMPTY.equals(systemRoleVo.getId())){
			BeanUtils.copyProperties(systemRoleVo, systemRole);
			log.info("systemUser:"+systemRoleVo.toString());
			add(systemRole);
		}else{
			systemRole=edit(systemRoleVo);
		}
		return systemRole;
	}
    
	/**
	 * 编辑
	 * @param systemRoleVo
	 * @return
	 */
	private SystemRole edit(SystemRoleVo systemRoleVo) {
		SystemRole systemRole=systemRoleDao.find(Integer.valueOf(systemRoleVo.getId()));
		if(systemRole!=null){
			BeanUtils.copyProperties(systemRoleVo, systemRole);
			log.info("systemUser:"+systemRole.toString());
			systemRoleDao.update(systemRole);
		}
		return systemRole;
	}
    
	/**
	 * 增加
	 * @param systemRole
	 */
	private void add(SystemRole systemRole) {
		systemRoleDao.insert(systemRole);
	}
    
	/**
	 * 查询对象
	 */
	@Override
	public SystemRoleVo find(String id) {
		SystemRoleVo systemRoleVo=new SystemRoleVo();
		if(!StringUtil.isEmpty(id)){
			SystemRole systemRole=systemRoleDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(systemRole, systemRoleVo);
			systemRoleVo.setId(id);
		}
		return systemRoleVo;
	}

	@Override
	public List<SystemRole> getRoleById(String userId) {
		return systemRoleDao.getRoleById(userId);
	}



}
