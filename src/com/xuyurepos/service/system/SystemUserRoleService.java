package com.xuyurepos.service.system;

import java.util.List;
import java.util.Map;

public interface SystemUserRoleService {
    /**
     * 加载用户角色
     * @param userId
     * @return
     */
	@SuppressWarnings("rawtypes")
	public List<Map> loadUserRoleTree(String userId,String roleType);

	public void saveInfo(String ids,String noids,String userId);

}
