package com.xuyurepos.service.impl.system;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemFunctionDao;
import com.xuyurepos.entity.system.SystemFunction;
import com.xuyurepos.service.system.SystemFunctionService;
import com.xuyurepos.vo.system.SystemFunctionVo;
/**
 * 后台模块实现类
 * @author yangfei
 *
 */
@Service
@Transactional
public class SystemFunctionServiceImpl implements SystemFunctionService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemFunctionServiceImpl.class);

	@Autowired
	private SystemFunctionDao systemFunctionDao;
	
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(systemFunctionDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(systemFunctionDao.selectUserCountWithPage(pageModel));
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
					SystemFunction systemFunction=systemFunctionDao.find(id);
					if(systemFunction!=null){
						systemFunctionDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存数据
	 */
	@Override
	public SystemFunction saveInfo(SystemFunctionVo systemFunctionVo) {
		log.info("systemLookUpVo:"+systemFunctionVo.toString());
		SystemFunction systemFunction=new SystemFunction();
		if(systemFunctionVo!=null&&SystemConstants.STRINGEMPTY.equals(systemFunctionVo.getId())){
			BeanUtils.copyProperties(systemFunctionVo, systemFunction);
			log.info("systemUser:"+systemFunction.toString());
			add(systemFunction);
		}else{
			systemFunction=edit(systemFunctionVo);
		}
		return systemFunction;
	}
    
	/**
	 * 编辑
	 * @param systemFunctionVo
	 * @return
	 */
	private SystemFunction edit(SystemFunctionVo systemFunctionVo) {
		SystemFunction systemFunction=systemFunctionDao.find(Integer.valueOf(systemFunctionVo.getId()));
		if(systemFunction!=null){
			BeanUtils.copyProperties(systemFunctionVo, systemFunction);
			log.info("systemUser:"+systemFunction.toString());
			systemFunctionDao.update(systemFunction);
		}
		return systemFunction;
	}
    
	/**
	 * 增加
	 * @param systemFunction
	 */
	private void add(SystemFunction systemFunction) {
		systemFunctionDao.insert(systemFunction);
	}
    
	/**
	 * 查询对象
	 */
	@Override
	public SystemFunctionVo find(String id) {
		SystemFunctionVo systemLookUpVo=new SystemFunctionVo();
		if(!StringUtil.isEmpty(id)){
			SystemFunction systemFunction=systemFunctionDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(systemFunction, systemLookUpVo);
			systemLookUpVo.setId(id);
		}
		return systemLookUpVo;
	}

}
