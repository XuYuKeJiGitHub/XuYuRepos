package com.xuyurepos.service.system;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xuyurepos.common.SpringTool;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.dao.comm.CommonMapper;
/**
 * 获取数据字典的值
 * @author yangfei
 *
 */
public class LookupManager {
    
    private static LookupManager instance;
    
    public static synchronized LookupManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new LookupManager();
        }
        return instance;
    }
    
    /**
     * 通过类型和代码获取数据
     * @param name
     * @param code
     * @return
     */
	public String getValue(String name, String code) {
		CommonMapper commonMapper=(CommonMapper) SpringTool.getBean("commonMapper");
    	StringBuilder sb=new StringBuilder();
    	sb.append(" select * from SYSTEM_LOOKUP_ITEM t1 where ");
    	sb.append(" t1.F_LOOKUP_ID='"+name+"' and t1.F_CODE='"+code+"' ");
    	Map<String, Object> map=commonMapper.findOneData(sb.toString());
    	return SystemConstants.STRINGEMPTY+map.get("F_VALUE");
	}
    
    /**
     * 通过字典名称获取数据字典相关map
     * @param name
     * @return
     */
    public ConcurrentHashMap<String, String> getValues(String name) {
    	ConcurrentHashMap<String, String> lookUp = new ConcurrentHashMap<String, String>();
    	CommonMapper commonMapper=(CommonMapper) SpringTool.getBean("commonMapper");
    	StringBuilder sb=new StringBuilder();
    	sb.append(" select * from SYSTEM_LOOKUP_ITEM t1 where t1.F_LOOKUP_ID='"+name+"' ");
    	List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
    	for(Map<String, Object> item : list){
			lookUp.put(SystemConstants.STRINGEMPTY+item.get("F_CODE"),SystemConstants.STRINGEMPTY+item.get("F_VALUE"));
    	}
    	return lookUp;
    }
    
    /**
     * 通过字典名称获取数据字典相关map
     * @param name
     * @return
     */
    public ConcurrentHashMap<String, String> getKeys(String name) {
    	ConcurrentHashMap<String, String> lookUp = new ConcurrentHashMap<String, String>();
    	CommonMapper commonMapper=(CommonMapper) SpringTool.getBean("commonMapper");
    	StringBuilder sb=new StringBuilder();
    	sb.append(" select * from SYSTEM_LOOKUP_ITEM t1 where t1.F_LOOKUP_ID='"+name+"' ");
    	List<Map<String, Object>> list=commonMapper.findManyData(sb.toString());
    	for(Map<String, Object> item : list){
			lookUp.put(SystemConstants.STRINGEMPTY+item.get("F_VALUE"),SystemConstants.STRINGEMPTY+item.get("F_CODE"));
    	}
    	return lookUp;
    }
  
}
