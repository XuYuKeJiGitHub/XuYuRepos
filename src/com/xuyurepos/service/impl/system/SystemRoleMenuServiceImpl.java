package com.xuyurepos.service.impl.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemRoleMenuDao;
import com.xuyurepos.entity.system.SystemRoleMenu;
import com.xuyurepos.service.system.SystemRoleMenuService;
import com.xuyurepos.vo.system.SystemRoleMenuVo;
@Service
@Transactional
public class SystemRoleMenuServiceImpl implements SystemRoleMenuService{

	
	Logger log=LoggerFactory.getInstance().getLogger(SystemRoleMenuServiceImpl.class);
	
	@Autowired
	private SystemRoleMenuDao systemRoleMenuDao;
    
	/**
	 * 查询当前用户的菜单角色
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> loadRoleMenuTree(String roleId) {
		List<SystemRoleMenuVo> OTList = systemRoleMenuDao.loadRoleMenuTree(roleId);
		List<Map> list=new ArrayList<Map>();
		Map map=null;
		Map attr=null;
	    for (SystemRoleMenuVo systemRoleMenuVo : OTList) {
        	map=new HashMap<String, Object>();
        	attr=new HashMap<String, Object>();
        	if(systemRoleMenuVo.getRoleMenuId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemRoleMenuVo.getRoleMenuId())){
        		attr.put("roleMenuId", systemRoleMenuVo.getRoleMenuId());
        	}else{
        		attr.put("roleMenuId", "false");
        	}
            map.put("id", systemRoleMenuVo.getId());
            map.put("text", systemRoleMenuVo.getName());
            map.put("state", "closed");
            map.put("attributes", attr);
            if(systemRoleMenuVo.getRoleMenuId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemRoleMenuVo.getRoleMenuId())){
            	 map.put("indeterminate", "true");
            }else{}
            list.add(map);
        }
		return list;
	}
	
	/**
	 * 查询当前用户的菜单角色
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> loadRoleMenuChildrenTree(String roleId,String fID) {
		List<SystemRoleMenuVo> OTList = systemRoleMenuDao.loadRoleMenuChildrenTree(roleId,fID);
		List<Map> list=new ArrayList<Map>();
		Map map=null;
		Map attr=null;
	    for (SystemRoleMenuVo systemRoleMenuVo : OTList) {
        	map=new HashMap<String, Object>();
        	attr=new HashMap<String, Object>();
        	if(systemRoleMenuVo.getRoleMenuId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemRoleMenuVo.getRoleMenuId())){
        		attr.put("roleMenuId", systemRoleMenuVo.getRoleMenuId());
        	}else{
        		attr.put("roleMenuId", "false");
        	}
            map.put("id", systemRoleMenuVo.getId());
            map.put("text", systemRoleMenuVo.getName());
            map.put("state", "closed");
            map.put("attributes", attr);
            if(systemRoleMenuVo.getRoleMenuId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemRoleMenuVo.getRoleMenuId())){
            	 map.put("checked", "true");
            }else{}
            list.add(map);
        }
		return list;
	}
    
	/**
	 * 查询所有数据
	 */
	@Override
	public void saveInfo(String ids,String noids,String roleId){
		String insertStr="";
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_COMM);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					String s1=idsStr[i];
					String[] s2=s1.split(SystemConstants.STRING_SENT);
					String id=s2[0];
					String roleMenuId=s2[1];
					if(roleMenuId!=null&&"false".equals(roleMenuId)){
						Integer menuId=Integer.valueOf(id);
						SystemRoleMenu systemRoleMenu=new SystemRoleMenu();
						systemRoleMenu.setMenuId(menuId);
						systemRoleMenu.setRoleId(Integer.valueOf(roleId));
						systemRoleMenuDao.insert(systemRoleMenu);
						insertStr=insertStr+systemRoleMenu.getId()+SystemConstants.STRING_SENT;
					}else{
						log.info("啥也不做"+roleMenuId);
						insertStr=insertStr+roleMenuId+SystemConstants.STRING_SENT;
					}
					
				}
			}
			
			String[] noidsStr=noids.split(SystemConstants.STRING_COMM);
			for (int i = 0; i < noidsStr.length; i++) {
				if(!StringUtil.isEmpty(noidsStr[i])){
					String s1=noidsStr[i];
					String[] s2=s1.split(SystemConstants.STRING_SENT);
					String roleMenuId=s2[1];
					if(roleMenuId!=null&&"false".equals(roleMenuId)){
						log.info("啥也不做"+roleMenuId);
					}else{
						if(insertStr.indexOf(roleMenuId+SystemConstants.STRING_SENT)!=-1){
							log.info("这个不能删"+roleMenuId);
						}else{
							systemRoleMenuDao.del(Integer.valueOf(roleMenuId));
						}
						
					}
					
				}
			}
		}
	}

}
