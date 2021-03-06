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
import com.xuyurepos.dao.system.SystemUserRoleDao;
import com.xuyurepos.entity.system.SystemUserRole;
import com.xuyurepos.service.system.SystemUserRoleService;
import com.xuyurepos.vo.system.SystemUserRoleVo;

@Service
@Transactional
public class SystemUserRoleServiceImpl implements SystemUserRoleService{
	
    Logger log=LoggerFactory.getInstance().getLogger(SystemUserRoleServiceImpl.class);
	
	@Autowired
	private SystemUserRoleDao systemUserRoleDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> loadUserRoleTree(String userId,String roleType) {
		List<SystemUserRoleVo> OTList = systemUserRoleDao.loadUserRoleTree(userId,roleType);
		List<Map> list=new ArrayList<Map>();
		Map map=null;
		Map attr=null;
	    for (SystemUserRoleVo systemUserRoleVo : OTList) {
        	map=new HashMap<String, Object>();
        	attr=new HashMap<String, Object>();
        	if(systemUserRoleVo.getUserRoleId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemUserRoleVo.getUserRoleId())){
        		attr.put("userRoleId", systemUserRoleVo.getUserRoleId());
        	}else{
        		attr.put("userRoleId", "false");
        	}
            map.put("id", systemUserRoleVo.getId());
            map.put("text", systemUserRoleVo.getName());
            map.put("state", "closed");
            map.put("attributes", attr);
            if(systemUserRoleVo.getUserRoleId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemUserRoleVo.getUserRoleId())){
            	map.put("checked", "true");
            }
            list.add(map);
        }
		return list;
	}

	@Override
	public void saveInfo(String ids, String noids, String userId) {
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
						Integer roleId=Integer.valueOf(id);
						SystemUserRole systemRoleMenu=new SystemUserRole();
						systemRoleMenu.setUserId(Integer.valueOf(userId));
						systemRoleMenu.setRoleId(roleId);
						systemUserRoleDao.insert(systemRoleMenu);
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
					String userRoleId=s2[1];
					if(userRoleId!=null&&"false".equals(userRoleId)){
						log.info("啥也不做"+userRoleId);
					}else{
						if(insertStr.indexOf(userRoleId+SystemConstants.STRING_SENT)!=-1){
							log.info("这个不能删"+userRoleId);
						}else{
							systemUserRoleDao.del(Integer.valueOf(userRoleId));
						}
						
					}
					
				}
			}
		}
	}

}
