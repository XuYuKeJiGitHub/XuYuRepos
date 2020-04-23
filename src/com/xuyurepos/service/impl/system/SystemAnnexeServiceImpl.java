package com.xuyurepos.service.impl.system;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemAnnexeDao;
import com.xuyurepos.entity.system.SystemAnnexe;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.service.system.SystemAnnexeService;
import com.xuyurepos.vo.system.SystemAnnexeVo;
/**
 * 附件相关表查询
 * @author yangfei
 *
 */
@Service
@Transactional
public class SystemAnnexeServiceImpl implements SystemAnnexeService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemAnnexeServiceImpl.class);

	@Autowired
	private SystemAnnexeDao systemAnnexeDao;
	
	
	@Autowired
	LoggerInfoService loggerInfoService;
	
	/**
	 * 查询数据
	 */
	@Override
	public List<SystemAnnexe> getList(SystemAnnexe annexe) {
		return systemAnnexeDao.getList(annexe);
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
					String id=idsStr[i];
					SystemAnnexe systemAnnexe=systemAnnexeDao.find(id);
					if(systemAnnexe!=null){
						systemAnnexeDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存数据
	 */
	@Override
	public SystemAnnexe saveInfo(SystemAnnexeVo systemAnnexeVo) {
		log.info("systemLookUpVo:"+systemAnnexeVo.toString());
		SystemAnnexe systemAnnexe=new SystemAnnexe();
		if(systemAnnexeVo!=null&&systemAnnexeVo.getAnnexeId()==null){
			systemAnnexe=add(systemAnnexeVo);
		}else{
			systemAnnexe=edit(systemAnnexeVo);
		}
		return systemAnnexe;
	}
    
	/**
	 * 更新数据
	 * @param systemAnnexeVo
	 * @return
	 */
	private SystemAnnexe edit(SystemAnnexeVo systemAnnexeVo) {
		SystemAnnexe systemAnnexe=systemAnnexeDao.find(systemAnnexeVo.getAnnexeId());
		if(systemAnnexe!=null){
			BeanUtils.copyProperties(systemAnnexeVo, systemAnnexe);
			systemAnnexeDao.update(systemAnnexe);
		}
		return systemAnnexe;
	}
    
	/**
	 * 添加数据
	 * @param systemAnnexeVo
	 * @return
	 */
	private SystemAnnexe add(SystemAnnexeVo systemAnnexeVo) {
		SystemAnnexe systemAnnexe=new SystemAnnexe();
		BeanUtils.copyProperties(systemAnnexeVo, systemAnnexe);
		String annexeId=loggerInfoService.getSequence("annexeId");
		systemAnnexe.setAnnexeId(annexeId);
		systemAnnexeDao.insert(systemAnnexe);
		return systemAnnexe;
	}
    
	/**
	 * 根据主键查询数据
	 */
	@Override
	public SystemAnnexeVo find(String id) {
		SystemAnnexeVo systemAnnexeVo=new SystemAnnexeVo();
		if(!StringUtil.isEmpty(id)){
			SystemAnnexe systemAnnexe=systemAnnexeDao.find(id);
			BeanUtils.copyProperties(systemAnnexe, systemAnnexeVo);
		}
		return systemAnnexeVo;
	}


}
