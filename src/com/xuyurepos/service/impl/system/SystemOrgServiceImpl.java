package com.xuyurepos.service.impl.system;

import java.util.ArrayList;
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
import com.xuyurepos.dao.system.SystemOrgDao;
import com.xuyurepos.entity.manager.GPRSDosageInfo;
import com.xuyurepos.entity.system.SystemOrg;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.vo.system.SystemOrgVo;

@Service
@Transactional
public class SystemOrgServiceImpl implements SystemOrgService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemOrgServiceImpl.class);
	
	@Autowired
	private SystemOrgDao systemOrgDao;
    
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(systemOrgDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(systemOrgDao.selectUserCountWithPage(pageModel));
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
					SystemOrg systemOrg=systemOrgDao.find(id);
					if(systemOrg!=null){
						systemOrgDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存机构数据
	 */
	@Override
	public SystemOrg saveInfo(SystemOrgVo systemOrgVo) {
		log.info("SystemOrgVo:"+systemOrgVo.toString());
		SystemOrg systemOrg=new SystemOrg();
		if(systemOrgVo!=null&&SystemConstants.STRINGEMPTY.equals(systemOrgVo.getId())){
			BeanUtils.copyProperties(systemOrgVo, systemOrg);
			log.info("systemOrg:"+systemOrg.toString());
			add(systemOrg);
		}else{
			systemOrg=edit(systemOrgVo);
		}
		return systemOrg;
	}
    
	/**
	 * 编辑
	 * @param systemOrgVo
	 * @return
	 */
	private SystemOrg edit(SystemOrgVo systemOrgVo) {
		SystemOrg org=systemOrgDao.find(Integer.valueOf(systemOrgVo.getId()));
		String upOrgId=org.getUpOrgId();
		if(org!=null){
			BeanUtils.copyProperties(systemOrgVo, org);
			org.setUpOrgId(upOrgId);
			log.info("SystemOrg:"+org.toString());
			// 判定
			if("0".equals(org.getUpOrgId())){
				org.setOrgLevel("1");
				List<SystemOrg> list=systemOrgDao.getList(SystemConstants.STRINGEMPTY+org.getId());
				if(list==null){
					org.setIsLeaf("y");
				}else{
					org.setIsLeaf("n");
				}
				systemOrgDao.update(org);
			}else{
				systemOrgDao.update(org);
				// 把父类的是否子节点改成否
				SystemOrg father=systemOrgDao.find(Integer.valueOf(org.getUpOrgId()));
				father.setIsLeaf("n");
				systemOrgDao.update(father);
				org.setOrgLevel(SystemConstants.STRINGEMPTY+(Integer.valueOf(father.getOrgLevel())+1));
				// 查询当前机构是否有子节点
				List<SystemOrg> list=systemOrgDao.getList(SystemConstants.STRINGEMPTY+org.getId());
				if(list==null){
					org.setIsLeaf("y");
				}else{
					org.setIsLeaf("n");
				}
				systemOrgDao.update(org);
			}
		}
		return org;
	}
    
	/**
	 * 添加
	 * @param systemOrg
	 * @return
	 */
	private SystemOrg add(SystemOrg systemOrg) {
		// 判定
		if("0".equals(systemOrg.getUpOrgId())){
			systemOrg.setOrgLevel("1");
			systemOrg.setIsLeaf("y");
			systemOrgDao.insert(systemOrg);
		}else{
			// 把父类的是否子节点改成否
			if(systemOrg.getUpOrgId()!=null&&!SystemConstants.STRINGEMPTY.equals(systemOrg.getUpOrgId())){
				SystemOrg father=systemOrgDao.find(Integer.valueOf(systemOrg.getUpOrgId()));
				father.setIsLeaf("n");
				systemOrgDao.update(father);
				// 获取机构代码
				String newOrgCode=""; 
				if(SystemConstants.STRING_NO.equals(systemOrg.getIsDept())){
					Integer orgLevel=(Integer.valueOf(father.getOrgLevel())+1);
					systemOrg.setOrgLevel(SystemConstants.STRINGEMPTY+orgLevel);
					newOrgCode=getOrgId(orgLevel, father.getOrgId(),SystemConstants.STRINGEMPTY+father.getId(),false);
					systemOrg.setIsDept(SystemConstants.STRING_NO);
				}else{
					Integer orgLevel=2;
					systemOrg.setOrgLevel(SystemConstants.STRINGEMPTY+(orgLevel));
					newOrgCode=getOrgId(orgLevel, systemOrg.getUpOrgId(),SystemConstants.STRINGEMPTY+father.getId(), true);
					systemOrg.setIsDept(SystemConstants.STRING_YES);
				}
				systemOrg.setOrgId(newOrgCode);
				systemOrg.setDelFlag(SystemConstants.STRING_NO);
				systemOrg.setIsLeaf("y");
				systemOrgDao.insert(systemOrg);
			}else{
				systemOrg.setOrgLevel("1");
				systemOrg.setUpOrgId("0");
				systemOrg.setIsLeaf("y");
				systemOrg.setOrgId("1000");
				systemOrg.setIsDept(SystemConstants.STRING_NO);
				systemOrgDao.insert(systemOrg);
			}
		}
		return systemOrg;
	}
	
	/**
	 * 获取机构代码
	 * @param orgLevel
	 * @param upOrgId
	 * @param dept 是否为部门
	 * @return
	 */
	private String getOrgId(Integer orgLevel,String upOrgId,String upId,Boolean dept){
		String result="";
		if(dept){
			String maxOrgId="1000";
			if(systemOrgDao.getMaxOrgId(upId,orgLevel,SystemConstants.STRING_YES)!=null){
				maxOrgId=systemOrgDao.getMaxOrgId(upId,orgLevel,SystemConstants.STRING_YES);
			}
			result=""+(Long.valueOf(maxOrgId)+1);
		}else{
			String maxOrgId="0";
			if(systemOrgDao.getMaxOrgId(upId,orgLevel,SystemConstants.STRING_NO)!=null){
				 maxOrgId=systemOrgDao.getMaxOrgId(upId,orgLevel,SystemConstants.STRING_NO);
			}
			if(maxOrgId.equals("0")){
				result=upOrgId;
				result=result+"001";
			}else{
				result=""+(Long.valueOf(maxOrgId)+1);
			}
		}
		
		return result;
		
	}
    
	/**
	 * 查询数据
	 */
	@Override
	public SystemOrgVo find(String id) {
		SystemOrgVo systemOrgVo=new SystemOrgVo();
		if(!StringUtil.isEmpty(id)){
			SystemOrg org=systemOrgDao.find(Integer.valueOf(id));
			if(org!=null){
				BeanUtils.copyProperties(org, systemOrgVo);
				systemOrgVo.setId(id);
			}
		}
		return systemOrgVo;
	}

	@Override
	public List<SystemOrg> loadOrgTree() {
		 return systemOrgDao.loadOrgTree();
	}

	@Override
	public List<SystemOrg> loadOrgChildrenTree(String fID) {
		 return systemOrgDao.loadOrgChildrenTree(fID);
	}

	@Override
	public SystemOrg findById(String id) {
		if(!StringUtil.isEmpty(id)){
			SystemOrg org=systemOrgDao.find(Integer.valueOf(id));
			if(org!=null){
				return org;
			}
		}
		return null;
	}

}
