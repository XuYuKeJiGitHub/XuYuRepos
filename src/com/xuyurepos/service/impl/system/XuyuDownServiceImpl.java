package com.xuyurepos.service.impl.system;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.DateUtil;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.system.XuyuDownDao;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.entity.system.XuyuDown;
import com.xuyurepos.service.system.XuyuDownService;
@Service
@Transactional
public class XuyuDownServiceImpl implements XuyuDownService {
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuDownServiceImpl.class);
	
	@Autowired
	private XuyuDownDao xuyuDownDao;
    
	/**
	 * 页面分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		// 处理条件数据
		pageModel.setRows(xuyuDownDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuDownDao.selectUserCountWithPage(pageModel));
	}
    
	/**
	 * 批量删除
	 */
	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuDown xuyuDown=xuyuDownDao.find(idsStr[i]);
					if(xuyuDown!=null){
						xuyuDownDao.del(id);
					}
				}
			}
		}
	}

	@Override
	public XuyuDown saveInfo(XuyuDown xuyuDown) {
		log.info("xuyuDown:"+xuyuDown.toString());
		XuyuDown down=new XuyuDown();
		if(xuyuDown==null||xuyuDown.getId()==null){
			down=add(xuyuDown);
		}else{
			down=edit(xuyuDown);
		}
		return down;
	}
    
	/**
	 * 编辑数据
	 * @param xuyuDown
	 * @return
	 */
	private XuyuDown edit(XuyuDown xuyuDown) {
		xuyuDownDao.update(xuyuDown);
		return xuyuDown;
	}

	/**
	 * 新增异步任务表
	 * @param xuyuDown
	 * @return XuyuDown
	 */
	private XuyuDown add(XuyuDown xuyuDown) {
		xuyuDownDao.insert(xuyuDown);
		return xuyuDown;
	
	}

	@Override
	public XuyuDown find(String id) {
		XuyuDown xuyuDown=xuyuDownDao.find(id);
		return xuyuDown;
	}

	@Override
	public Map<String, Object> exportData(String downType) {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		    String nowdate = DateUtil.convert(new Date(), "yyyy_MM_dd_HH_mm_ss");
			String nowdate1 = DateUtil.convert(new Date(), "yyyyMMdd");
			
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			String userId=systemUser.getUserName();
			String filename="export_" + userId +"_"+ nowdate +".xlsx";
			String filepath="";
			String path="";
			// 获取生成文件的路径
	        StringBuilder builder = new StringBuilder();
	        builder.append(ProsUtil.getProperty("syncexportPath")+File.separator+nowdate1);
	        // 查看目录是否存在
	        File file = new File(builder.toString());
			if (!file.exists()) {
				file.mkdir();
			}
	        if (!builder.toString().endsWith(File.separator)) {
	            builder.append(File.separator);
	        }
	        path=builder.toString();
	        filepath = path+filename;
			
			XuyuDown down=new XuyuDown();
			down.setCreateUser(userId);
			down.setCreateDate(new Date());
			down.setOrgId(systemUser.getOrgId());
			down.setDownType(downType);
			down.setFilename(filename);
			down.setFilepath(path);
			down.setAnnexeName("卡管理导出数据.xlsx");
			// 导出是否成功
			down.setStatu(SystemConstants.STRING_NO);
			saveInfo(down);
			
			map.put("filename", filename);
			map.put("annexeName", "卡管理导出数据.xlsx");
		    map.put("filepath", path);
		    map.put("id", down.getId());
	  } catch (Exception e) {
			e.printStackTrace();
	  }
	  return map;
	}

}
