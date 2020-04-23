package com.xuyurepos.service.impl.manager;

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
import com.xuyurepos.dao.manager.XuyuCardTypeDao;
import com.xuyurepos.entity.manager.XuyuCardType;
import com.xuyurepos.service.manager.XuyuCardTypeService;
import com.xuyurepos.vo.manager.XuyuCardTypeVo;
@Service
@Transactional
public class XuyuCardTypeServiceImpl implements XuyuCardTypeService{
	Logger log=LoggerFactory.getInstance().getLogger(XuyuCardTypeServiceImpl.class);
	
	@Autowired
	private  XuyuCardTypeDao xuyuCardTypeDao;
    
	/**
	 * 分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(xuyuCardTypeDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuCardTypeDao.selectUserCountWithPage(pageModel));
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
					XuyuCardType systemOrg=xuyuCardTypeDao.find(id);
					if(systemOrg!=null){
						xuyuCardTypeDao.del(id);
					}
				}
			}
		}
	}
    
	/**
	 * 保存机构数据
	 */
	@Override
	public XuyuCardType saveInfo(XuyuCardTypeVo xuyuCardTypeVo) {
		log.info("SystemOrgVo:"+xuyuCardTypeVo.toString());
		XuyuCardType systemOrg=new XuyuCardType();
		if(xuyuCardTypeVo!=null&&SystemConstants.STRINGEMPTY.equals(xuyuCardTypeVo.getId())){
			BeanUtils.copyProperties(xuyuCardTypeVo, systemOrg);
			log.info("systemOrg:"+systemOrg.toString());
			add(systemOrg);
		}else{
			systemOrg=edit(xuyuCardTypeVo);
		}
		return systemOrg;
	}
    
	/**
	 * 编辑
	 * @param systemOrgVo
	 * @return
	 */
	private XuyuCardType edit(XuyuCardTypeVo systemOrgVo) {
		XuyuCardType org=xuyuCardTypeDao.find(Integer.valueOf(systemOrgVo.getId()));
		if(org!=null){
			BeanUtils.copyProperties(systemOrgVo, org);
			log.info("SystemOrg:"+org.toString());
			// 判定
			if("0".equals(org.getCardOrgId())){
				org.setCardLevel("1");
				List<XuyuCardType> list=xuyuCardTypeDao.getList(SystemConstants.STRINGEMPTY+org.getId());
				if(list==null){
					org.setIsLeaf("y");
				}else{
					org.setIsLeaf("n");
				}
				xuyuCardTypeDao.update(org);
			}else{
				xuyuCardTypeDao.update(org);
				// 把父类的是否子节点改成否
				XuyuCardType father=xuyuCardTypeDao.find(Integer.valueOf(org.getCardOrgId()));
				father.setIsLeaf("n");
				xuyuCardTypeDao.update(father);
				org.setCardLevel(SystemConstants.STRINGEMPTY+(Integer.valueOf(father.getCardLevel())+1));
				// 查询当前机构是否有子节点
				List<XuyuCardType> list=xuyuCardTypeDao.getList(SystemConstants.STRINGEMPTY+org.getId());
				if(list==null){
					org.setIsLeaf("y");
				}else{
					org.setIsLeaf("n");
				}
				xuyuCardTypeDao.update(org);
			}
		}
		return org;
	}
    
	/**
	 * 添加
	 * @param systemOrg
	 * @return
	 */
	private XuyuCardType add(XuyuCardType xuyuCardType) {
		// 判定
		if("0".equals(xuyuCardType.getCardOrgId())){
			xuyuCardType.setCardLevel("1");
			xuyuCardType.setIsLeaf("y");
			xuyuCardTypeDao.insert(xuyuCardType);
		}else{
			// 把父类的是否子节点改成否
			XuyuCardType father=xuyuCardTypeDao.find(Integer.valueOf(xuyuCardType.getCardOrgId()));
			father.setIsLeaf("n");
			xuyuCardTypeDao.update(father);
			xuyuCardType.setCardLevel(SystemConstants.STRINGEMPTY+(Integer.valueOf(father.getCardLevel())+1));
			xuyuCardType.setIsLeaf("y");
			xuyuCardTypeDao.insert(xuyuCardType);
		}
		return xuyuCardType;
	}
    
	/**
	 * 查询数据
	 */
	@Override
	public XuyuCardTypeVo find(String id) {
		XuyuCardTypeVo xuyuCardTypeVo=new XuyuCardTypeVo();
		if(!StringUtil.isEmpty(id)){
			XuyuCardType xuyuCardType=xuyuCardTypeDao.find(Integer.valueOf(id));
			BeanUtils.copyProperties(xuyuCardType, xuyuCardTypeVo);
			xuyuCardTypeVo.setId(id);
		}
		return xuyuCardTypeVo;
	}

	@Override
	public List<XuyuCardType> loadOrgTree() {
		 return xuyuCardTypeDao.loadOrgTree();
	}

	@Override
	public List<XuyuCardType> loadOrgChildrenTree(String fID) {
		 return xuyuCardTypeDao.loadOrgChildrenTree(fID);
	}



}
