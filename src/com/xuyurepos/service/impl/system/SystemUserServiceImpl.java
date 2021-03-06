package com.xuyurepos.service.impl.system;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.MD5Util;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemUserDao;
import com.xuyurepos.entity.system.SystemRole;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.service.system.SystemRoleService;
import com.xuyurepos.service.system.SystemUserService;
import com.xuyurepos.vo.system.SystemOrgVo;
import com.xuyurepos.vo.system.SystemUserVo;
/**
 * 用户模块实现方法
 * @author yangfei
 */
@Service
@Transactional
public class SystemUserServiceImpl implements SystemUserService{

	Logger log=LoggerFactory.getInstance().getLogger(SystemUserServiceImpl.class);
	
	@Autowired
	private SystemUserDao systemUserDao;
	@Autowired
	private SystemRoleService  systemUserService;
	
	@Autowired
	private SystemOrgService systemOrgService;
	
	/**
	 * 登陆实现逻辑
	 * @throws Exception 
	 */
	@Override
	public SystemUser login(SystemUser e) throws Exception {
		String password=MD5Util.MD5Encode(e.getPassword(), SystemConstants.CHARSET_UTF_8);
		e.setPassword(password);
		log.info("当前登录用户信息："+e);
		SystemUser systemUser=systemUserDao.login(e);
		log.info("systemUser："+systemUser);
		if(systemUser==null){
			throw new CustomException("账户或者密码错误");
		}else{
			List<SystemRole> list=systemUserService.getRoleById(SystemConstants.STRINGEMPTY+systemUser.getId());
			if(list.size()==0){
				throw new CustomException("当前用户没有角色，请先配置用户角色");
			}else{
				systemUser.setIsAdmin(false);
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getRoleCode().equals("admin")){
						systemUser.setIsAdmin(true);
					}
				}
				systemUser.setList(list);
				SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+systemUser.getOrgId());
				systemUser.setOrgLevel(systemOrgVo.getOrgLevel());
				systemUser.setOrgCode(systemOrgVo.getOrgId());
			}
		}
		return systemUser;
	}
    
	/**
	 * 分页查询数据列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(systemUserDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(systemUserDao.selectUserCountWithPage(pageModel));
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
					SystemUser systemUser=systemUserDao.find(id);
					if(systemUser!=null){
						systemUserDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存用户信息的代码
	 * @throws CustomException 
	 */
	@Override
	public SystemUser saveInfo(SystemUserVo systemUserVo) throws CustomException {
		log.info("systemUserVo:"+systemUserVo.toString());
		SystemUser systemUser=new SystemUser();
		if(systemUserVo!=null&&SystemConstants.STRINGEMPTY.equals(systemUserVo.getId())){
			BeanUtils.copyProperties(systemUserVo, systemUser);
			log.info("systemUser:"+systemUser.toString());
			add(systemUser);
		}else{
			systemUser=edit(systemUserVo);
		}
		return systemUser;
	}
    
	/**
	 * 更新数据
	 * @param systemUserVo
	 * @return
	 */
	private SystemUser edit(SystemUserVo systemUserVo) {
		SystemUser user=systemUserDao.find(Integer.valueOf(systemUserVo.getId()));
		if(user!=null){
			systemUserVo.setUserName(user.getUserName());
			BeanUtils.copyProperties(systemUserVo, user);
			log.info("systemUser:"+user.toString());
			systemUserDao.update(user);
		}
		return user;
	}
    
	/**
	 * 添加数据
	 * @param systemUser
	 * @return
	 * @throws CustomException 
	 */
	private SystemUser add(SystemUser systemUser) throws CustomException {
		try {
			// 默认密码6个1，需要用户登录修改 
			String password=MD5Util.MD5Encode("111111", SystemConstants.CHARSET_UTF_8);
			systemUser.setPassword(password);
			systemUserDao.insert(systemUser);
		} catch (DuplicateKeyException e) {
			throw new CustomException("当前账号已经被注册，请更换");
		}
		
		return systemUser;
	}
    
	/**
	 * 根据id查询用户信息
	 */
	@Override
	public SystemUserVo find(String id) {
		SystemUserVo systemUserVo=new SystemUserVo();
		if(!StringUtil.isEmpty(id)){
			SystemUser user=systemUserDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(user, systemUserVo);
			systemUserVo.setId(id);
		}
		return systemUserVo;
	}
    
	/**
	 * 修改用户信息
	 */
	@Override
	public void resetName(SystemUser systemUser, String cname, String new_password) {
		SystemUser user=systemUserDao.find(systemUser.getId());
		if(!systemUser.getCname().equals(cname)){
			user.setCname(cname);
		}
		if(new_password!=null&&!"".equals(new_password)){
			user.setPassword(new_password);
		}
		systemUserDao.update(user);
	}

}
