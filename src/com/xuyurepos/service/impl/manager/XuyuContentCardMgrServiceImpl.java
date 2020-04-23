package com.xuyurepos.service.impl.manager;

import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.analysis.BigDataExcelOutWrite;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.DateUtil;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.manager.XuyuContentCardMgrDao;
import com.xuyurepos.entity.manager.XuyuContentCardMgr;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.manager.XuyuContentCardMgrService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrVo;
@Service
@Transactional
public class XuyuContentCardMgrServiceImpl implements XuyuContentCardMgrService{

	Logger log=LoggerFactory.getInstance().getLogger(XuyuContentCardMgrServiceImpl.class);

	@Autowired
	private XuyuContentCardMgrDao xuyuContentCardMgrDao;
	
	@javax.annotation.Resource
	private SqlSessionFactory sqlSessionFactory;
	
	public Connection getCon() {  
	    Connection connection = null;  
	    SqlSession sqlSession = sqlSessionFactory.openSession();  
	    connection = sqlSession.getConnection();  
	    return connection;  
	} 
	
	/**
	 * 分页查询物联网卡信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		pageModel.setRows(xuyuContentCardMgrDao.selectUserListWithPage(pageModel));
	    pageModel.setTotal(xuyuContentCardMgrDao.selectUserCountWithPage(pageModel));
	}

	@Override
	public XuyuContentCardMgrVo find(String id) {
		XuyuContentCardMgrVo xuyuContentCardMgrVo=new XuyuContentCardMgrVo();
		if(!StringUtil.isEmpty(id)){
			XuyuContentCardMgr xuyuContentCardMgr=xuyuContentCardMgrDao.find(id);
			BeanUtils.copyProperties(xuyuContentCardMgr, xuyuContentCardMgrVo);
			xuyuContentCardMgrVo.setId(id);
		}
		return xuyuContentCardMgrVo;
	}

	@Override
	public Map<String, Object> exportModel() {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String type=request.getParameter("type");
		    String nowdate = DateUtil.convert(new Date(), "yyyy_MM_dd_HH_mm_ss");
			String nowdate1 = DateUtil.convert(new Date(), "yyyyMMdd");
			
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			String userId=systemUser.getUserName();
			String filename="export_" + userId +"_"+ nowdate +".xlsx";
			String sheetName="Sheet";
			String filepath="";
			String path="";
			// 获取生成文件的路径
	        StringBuilder builder = new StringBuilder();
	        builder.append(ProsUtil.getProperty("exportPath")+File.separator+nowdate1);
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
	        
	        String sql = "";
	        if("1".equals(type)){
	        	String[] colmnsList = {
			  			"接入号"
			  			,"卡号备注"
			  			,"IMSI"
			  			,"ICCID"
			  			,"开户日期"
			  			,"激活日期"
			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);

				map.put("filename", filename);
				map.put("annexeName", "移动数据导入模板.xlsx");
				map.put("filepath", path);
	        }else if("2".equals(type)){
	        	String[] colmnsList = {
	        			"ICCID"
			  			,"MSISDN"
			  			,"SIM卡状态"
			  			,"已激活"
			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);
				map.put("filename", filename);
				map.put("annexeName", "联通数据导入模板.xlsx");
				map.put("filepath", path);
	        }else if("3".equals(type)){
	        	String[] colmnsList = {
			  			"ICCID"
			  			,"接入号码"
			  			,"SIM卡状态"
			  			,"4G IMSI"
			  			,"用户发展时间"
			  			,"激活时间"
			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);
				map.put("filename", filename);
				map.put("annexeName", "电信数据导入模板.xlsx");
				map.put("filepath", path);
	        }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
