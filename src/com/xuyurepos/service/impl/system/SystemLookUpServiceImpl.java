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
import com.xuyurepos.common.util.ComboBoxUtils;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemLookUpDao;
import com.xuyurepos.entity.system.SystemLookUp;
import com.xuyurepos.service.system.SystemLookUpService;
import com.xuyurepos.vo.system.SystemLookUpVo;
/**
 * 数据字典类别
 * @author yangfei
 */
@Service
@Transactional
public class SystemLookUpServiceImpl implements SystemLookUpService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemLookUpServiceImpl.class);
	
	@Autowired
	private SystemLookUpDao systemLookUpDao;
    
	/**
	 * 分页查询数据列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(systemLookUpDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(systemLookUpDao.selectUserCountWithPage(pageModel));
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
					SystemLookUp systemLookUp=systemLookUpDao.find(id);
					if(systemLookUp!=null){
						systemLookUpDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存信息的代码
	 */
	@Override
	public SystemLookUp saveInfo(SystemLookUpVo systemLookUpVo) {
		log.info("systemLookUpVo:"+systemLookUpVo.toString());
		SystemLookUp systemLookUp=new SystemLookUp();
		if(systemLookUpVo!=null&&SystemConstants.STRINGEMPTY.equals(systemLookUpVo.getId())){
			BeanUtils.copyProperties(systemLookUpVo, systemLookUp);
			log.info("systemUser:"+systemLookUp.toString());
			add(systemLookUp);
		}else{
			systemLookUp=edit(systemLookUpVo);
		}
		return systemLookUp;
	}
    
	/**
	 * 更新数据
	 * @param systemLookUpVo
	 * @return
	 */
	private SystemLookUp edit(SystemLookUpVo systemLookUpVo) {
		SystemLookUp user=systemLookUpDao.find(Integer.valueOf(systemLookUpVo.getId()));
		if(user!=null){
			BeanUtils.copyProperties(systemLookUpVo, user);
			log.info("systemUser:"+user.toString());
			systemLookUpDao.update(user);
		}
		return user;
	}
    
	/**
	 * 添加数据
	 * @param systemLookUp
	 */
	private void add(SystemLookUp systemLookUp) {
		systemLookUpDao.insert(systemLookUp);
	}
    
	/**
	 * 根据id查询信息
	 */
	@Override
	public SystemLookUpVo find(String id) {
		SystemLookUpVo systemLookUpVo=new SystemLookUpVo();
		if(!StringUtil.isEmpty(id)){
			SystemLookUp systemLookUp=systemLookUpDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(systemLookUp, systemLookUpVo);
			systemLookUpVo.setId(id);
		}
		return systemLookUpVo;
	}

	@Override
	public List<ComboBoxUtils> findList() {
		List<ComboBoxUtils> list = new ArrayList<ComboBoxUtils>();
		List<SystemLookUp> result=systemLookUpDao.getList();
		try {
			if (result != null && result.size() > 0) {
				boolean selected = false;
				for (int i = 0; i < result.size(); i++) {
					SystemLookUp obj = result.get(i);
					String value = obj.getfName();
					String text = obj.getfName();
//					if (i == 0) {
//						selected = true;
//					} else {
//						selected = false;
//					}
					ComboBoxUtils item = new ComboBoxUtils(i + SystemConstants.STRINGEMPTY, value, text, selected);
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
