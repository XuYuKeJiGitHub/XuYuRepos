package com.xuyurepos.service.impl.system;

import java.util.ArrayList;
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
import com.xuyurepos.common.util.ComboBoxUtils;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.SystemLookUpItemDao;
import com.xuyurepos.entity.system.SystemLookUpItem;
import com.xuyurepos.service.system.SystemLookUpItemService;
import com.xuyurepos.vo.system.SystemLookUpItemVo;
/**
 * 系统数据字典实现方法
 * @author yangfei
 */
@Service
@Transactional
public class SystemLookUpItemServiceImpl implements SystemLookUpItemService{
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemLookUpItemServiceImpl.class);

	@Autowired
	private SystemLookUpItemDao lookUpItemDao;
	
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(lookUpItemDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(lookUpItemDao.selectUserCountWithPage(pageModel));
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
					SystemLookUpItem systemLookUpItem=lookUpItemDao.find(id);
					if(systemLookUpItem!=null){
						lookUpItemDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 查询列表
	 */
	@Override
	public List<ComboBoxUtils> findList(String fLookUpId) {
		List<ComboBoxUtils> list = new ArrayList<ComboBoxUtils>();
		List<SystemLookUpItem> result=lookUpItemDao.getList(fLookUpId);
		try {
			if (result != null && result.size() > 0) {
				boolean selected = false;
				for (int i = 0; i < result.size(); i++) {
					SystemLookUpItem obj = result.get(i);
					String value = obj.getfCode();
					String text = obj.getfValue();
					ComboBoxUtils item = new ComboBoxUtils(i +SystemConstants.STRINGEMPTY,value, text,selected);
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
    
	/**
	 * 保存方法
	 * @throws CustomException 
	 */
	@Override
	public SystemLookUpItem saveInfo(SystemLookUpItemVo systemLookUpVo) throws CustomException {
		log.info("systemLookUpVo:"+systemLookUpVo.toString());
		SystemLookUpItem systemLookUpItem=new SystemLookUpItem();
		try {
			if(systemLookUpVo!=null&&SystemConstants.STRINGEMPTY.equals(systemLookUpVo.getfId())){
				BeanUtils.copyProperties(systemLookUpVo, systemLookUpItem);
				log.info("systemUser:"+systemLookUpItem.toString());
				add(systemLookUpItem);
			}else{
				systemLookUpItem=edit(systemLookUpVo);
			}
		} catch (DuplicateKeyException e) {
			throw new CustomException("操作失败,数据字段编号不能重复");
		} catch (CustomException e) {
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new CustomException("操作失败");
		}
		
		return systemLookUpItem;
	}
    
	/**
	 * 编辑数据
	 * @param systemLookUpVo
	 * @return
	 */
	private SystemLookUpItem edit(SystemLookUpItemVo systemLookUpVo)throws CustomException{
		SystemLookUpItem systemLookUpItem=lookUpItemDao.find(Integer.valueOf(systemLookUpVo.getfId()));
		try {
			if(systemLookUpItem!=null){
				BeanUtils.copyProperties(systemLookUpVo, systemLookUpItem);
				log.info("systemUser:"+systemLookUpItem.toString());
				lookUpItemDao.update(systemLookUpItem);
			}
			return systemLookUpItem;
		} catch (DuplicateKeyException e) {
			throw new CustomException("操作失败,数据字典代码不能重复");
		}
		
		
		
	}
    
	/**
	 * 添加数据
	 * @param systemLookUpItem
	 */
	private void add(SystemLookUpItem systemLookUpItem)throws  Exception {
		lookUpItemDao.insert(systemLookUpItem);
	}
    
	/**
	 * 查询数据
	 */
	@Override
	public SystemLookUpItemVo find(String id) {
		SystemLookUpItemVo systemLookUpVo=new SystemLookUpItemVo();
		if(!StringUtil.isEmpty(id)){
			SystemLookUpItem systemLookUpItem=lookUpItemDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(systemLookUpItem, systemLookUpVo);
			systemLookUpVo.setfId(id);
		}
		return systemLookUpVo;
	}

}
