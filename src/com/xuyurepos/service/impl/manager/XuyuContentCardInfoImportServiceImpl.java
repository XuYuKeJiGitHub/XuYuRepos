package com.xuyurepos.service.impl.manager;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyurepos.common.analysis.BigDataExcelOutWrite;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.util.DateUtil;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.dao.manager.XuyuContentCardInfoDao;
import com.xuyurepos.entity.comm.Params;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.entity.system.XuyuDown;
import com.xuyurepos.service.manager.XuyuContentCardInfoImportService;
import com.xuyurepos.service.system.SystemOrgService;
import com.xuyurepos.service.system.XuyuDownService;
import com.xuyurepos.vo.manager.XuyuContentCardMgrSelfVo;
import com.xuyurepos.vo.system.SystemOrgVo;
@Service
@Transactional
public class XuyuContentCardInfoImportServiceImpl implements XuyuContentCardInfoImportService{
	
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuContentCardInfoImportServiceImpl.class);
	
	@Autowired
	private XuyuContentCardInfoDao xuyuContentCardInfoDao;
	
	@Autowired
	private SystemOrgService systemOrgService;
	@Autowired
	private XuyuDownService xuyuDownService;
	
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	public Connection getCon() {  
	    Connection connection = null;  
	    SqlSession sqlSession = sqlSessionFactory.openSession();  
	    connection = sqlSession.getConnection();  
	    return connection;  
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
	        if("1".equals(type)){
	        	String[] colmnsList = {
	        			"接入号",
	        			"卡号备注",
	        			"IMSI",
	        			"ICCID",
	        			"企业ID",
	        			"群组",
	        			"物联卡状态",
	        			"工作状态",
	        			"本月累计使用流量（MB）",
	        			"本月累计使用短信（条）",
	        			"开户日期",
	        			"激活日期",
	        			"是否签约短信服务",
	        			"是否签约数据服务"

			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);

				map.put("filename", filename);
				map.put("annexeName", "移动数据更新模板.xlsx");
				map.put("filepath", path);
	        }else if("2".equals(type)){
	        	String[] colmnsList = {
	        			"ICCID"
			  			,"周期累计用量 (MB)"
			  			,"在线"
			  			,"MSISDN"
			  			,"客户"	
			  			,"资费计划"	
			  			,"已达到用量限额"	
			  			,"SIM 卡状态	"
			  			,"已激活"	
			  			,"迁移的 SIM 卡"
			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);
				map.put("filename", filename);
				map.put("annexeName", "联通数据更新模板.xlsx");
				map.put("filepath", path);
	        }else if("3".equals(type)){
	        	String[] colmnsList = {
	        			"ICCID"	
	        			,"接入号码"	
	        			,"SIM卡状态"	
	        			,"最近状态变更时间"	
	        			,"客户编码"	
	        			,"客户名称"	
	        			,"主产品"
	        			,"断网状态"	
	        			,"断网类型"	
	        			,"当月用量"	
	        			,"归属群组"	
	        			,"4G IMSI"	
	        			,"3G IMSI"	
	        			,"行业类型"	
	        			,"用户发展时间"	
	        			,"激活时间"	
	        			,"标签"
			  	};
				// 字段映射
				Map<String, String> fieldLabel=new HashMap<String, String>();
				BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
				bdeo.WriteExcel(true,null,null);
				map.put("filename", filename);
				map.put("annexeName", "电信数据更新模板.xlsx");
				map.put("filepath", path);
	        }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public synchronized void setMark(HttpServletRequest request) throws CustomException {
		String accessNums=request.getParameter("accessNums");
		String mark=request.getParameter("mark");
		if(!StringUtil.isEmpty(accessNums)){
			String[] idsStr=null;
			if(accessNums.indexOf(SystemConstants.STRING_SENT)!=-1){
				accessNums=accessNums.replace(SystemConstants.STRING_HTML_ENTER, SystemConstants.STRINGEMPTY);
				idsStr=accessNums.split(SystemConstants.STRING_SENT);
			}else if(accessNums.indexOf(SystemConstants.STRING_HTML_ENTER)!=-1){
				idsStr=accessNums.split(SystemConstants.STRING_HTML_ENTER);
			}else{
				idsStr=accessNums.split(SystemConstants.STRING_ENTER);
			}
		    List<Map<String, Object>> bacthInsertMap=null;
			Map<String, Object> valuesMap=null;  
			StringBuilder sb=new StringBuilder();
			sb.append(" delete from  MSG_TEMP ");
			commonMapper.executeAction(sb.toString());
			for (int i = 0; i < idsStr.length; i++) {
				if(i==0){
					bacthInsertMap=new ArrayList<Map<String, Object>>();
					valuesMap=new HashMap<String, Object>();
					valuesMap.put("values", idsStr[i]);
					bacthInsertMap.add(valuesMap);
				}else{
					valuesMap=new HashMap<String, Object>();
					valuesMap.put("values", idsStr[i]);
					bacthInsertMap.add(valuesMap);
				}
				if(i!=0&&i%100==0){
					Params params=new Params();
					Map<String, Object> insertMap=new HashMap<String, Object>();
					insertMap.put("ACCESS_NUM", "ACCESS_NUM");
					params.setTables("MSG_TEMP");
					params.setInsertMap(insertMap);
					params.setBacthInsertMap(bacthInsertMap);;
					commonMapper.batchAdd(params);
					bacthInsertMap=new ArrayList<Map<String, Object>>();
				}
			}
			
			// 看是否处理好了
			if(bacthInsertMap!=null&&bacthInsertMap.size()>0){
				Params params=new Params();
				Map<String, Object> insertMap=new HashMap<String, Object>();
				insertMap.put("ACCESS_NUM", "ACCESS_NUM");
				params.setTables("MSG_TEMP");
				params.setInsertMap(insertMap);
				params.setBacthInsertMap(bacthInsertMap);;
				commonMapper.batchAdd(params);
				bacthInsertMap=new ArrayList<Map<String, Object>>();
			}
			// 求一下个数
			sb=new StringBuilder();
			sb.append(" select count(1) as a  from MSG_TEMP a");
			long result=commonMapper.findCount(sb.toString());
			if(result>5000){
				throw new CustomException("目前系统支持输入备注一次最多为5000");
			}
			String numType="01";
			// 查询数据类型
			sb=new StringBuilder();
			sb.append(" select count(1) as a from   MSG_TEMP a1  inner join XUYU_CONTENT_CARD_INFO t1 ");
			sb.append(" on  a1.ACCESS_NUM=t1.ICCID  ");
			long r=commonMapper.findCount(sb.toString());
			if(r>0){
				numType="02";
			}
			// 处理二次出库，只需要调整归属机构
			if("01".equals(numType)){
				sb=new StringBuilder();
				sb.append(" update  XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 ");
				sb.append(" set t1.MARK='"+mark+"'");
				sb.append(" where a1.ACCESS_NUM=t1.ACCESS_NUM  ");
				commonMapper.executeAction(sb.toString());
			}else{
				sb=new StringBuilder();
				sb.append(" update  XUYU_CONTENT_CARD_INFO t1,MSG_TEMP a1 ");
				sb.append(" set t1.MARK='"+mark+"'");
				sb.append(" where a1.ACCESS_NUM=t1.ICCID  ");
				commonMapper.executeAction(sb.toString());
			}
		}
	}
    
	/**
	 * 异步导出
	 */
	@Override
	public Map<String, Object> syncExportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo,Map<String ,Object> mapSet) {
		try {
			String sheetName="Sheet";
			String path=SystemConstants.STRINGEMPTY+mapSet.get("filepath");
			String filename=SystemConstants.STRINGEMPTY+mapSet.get("filename");
	        String filepath = path+filename;
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t2.ID,");
	        sb.append(" t2.ACCESS_NUM,t2.ICCID,t2.IMSI, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='PAY_TYPE'  and a1.F_CODE=t2.PAYMENT_TYPE) as PAYMENT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t2.PROVIDER) as PROVIDER,");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_YD'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_LT'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_DX'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append("  end OWNER_PLACE, ");
	        
	        sb.append(" t4.ORG_NAME, ");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  end WORKING_CONDITION, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t2.COMBO_TYPE) as COMBO_TYPE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_NAME'  and a1.F_CODE=t2.COMNO_NAME) as COMNO_NAME,");
	        sb.append(" t2.USE_GPRS,");
	        sb.append(" t2.TOTAL_GPRS,");
	        sb.append(" t2.REMAIN_GPS,");
	        sb.append(" t2.MESSAGE_COUNT,");
	        
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'SILENT_TYPE' AND a1.F_CODE = t2.WAIT_TYPE ) AS WAIT_TYPE,");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'HAVE_TEST' AND a1.F_CODE = t2.TEST_TYPE ) AS TEST_TYPE,	");
	        sb.append(" t2.DEADLINE_DATE,");
	        sb.append(" t2.ESTABLISH_DATE,");
	        sb.append(" t2.ACTIVATE_DATE,");
	        sb.append(" t2.WAIT_DATE,");
	        
	        
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_WAIT_TYPE'  and a1.F_CODE=t2.REAL_WAIT_TYPE) as REAL_WAIT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_TEST_TYPE'  and a1.F_CODE=t2.REAL_TEST_TYPE) as REAL_TEST_TYPE,	");
	        
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'CARD_TYPE' AND a1.F_CODE = t2.CARD_TYPE ) AS CARD_TYPE, ");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'STANDARD' AND a1.F_CODE = t2.STANDARD ) AS STANDARD,");
	        
	        sb.append(" t2.REAL_ESTABLISH,");
	        sb.append("	t2.REAL_ACTIVATE,");
	        sb.append(" t2.REAL_WAIT_DATE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='ONLINE_STATU'  and a1.F_CODE=t2.BILLING_STATUS) as BILLING_STATUS,");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'OPERATOR_COMBO_TYPE' AND a1.F_CODE = t2.REAL_COMBOTYPE ) AS REAL_COMBOTYPE,");
	        sb.append(" t2.UNIT_COST,");
	        sb.append(" '' as a1,");
	        sb.append(" '' as a2,");
	        sb.append(" '' as a3,");
	        sb.append(" '' as a4,");
	        sb.append(" '' as a5,");
	        sb.append(" '' as a6,");
	        sb.append(" '' as a7,");
	        sb.append(" t2.mark, ");
	        sb.append(" t3.OWNER_NAME");
	        
	        sb.append("  from (select * from XUYU_CONTENT_CARD_INFO WHERE 1=1 ");
	        if(xuyuContentCardMgrSelfVo!=null){
	        	 if(xuyuContentCardMgrSelfVo.getKeywords()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getKeywords())){
	 	        	sb.append(" AND ( ACCESS_NUM like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or ICCID like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or IMSI like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%'  )");
	 	         }
	        	 if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
	        		 SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
	        		 sb.append(" AND EXISTS (SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+systemOrgVo.getOrgId()+"%' and t1.ID=AGENCY) ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getPaymentType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getPaymentType())){
	        		 sb.append(" AND PAYMENT_TYPE='"+xuyuContentCardMgrSelfVo.getPaymentType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getProvider()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getProvider())){
	        		 sb.append(" AND PROVIDER='"+xuyuContentCardMgrSelfVo.getProvider()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwnerPlace()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwnerPlace())){
	        		 sb.append(" AND OWNER_PLACE='"+xuyuContentCardMgrSelfVo.getOwnerPlace()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboType())){
	        		 sb.append(" AND COMBO_TYPE='"+xuyuContentCardMgrSelfVo.getComboType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboName()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboName())){
	        		 sb.append("  AND COMNO_NAME='"+xuyuContentCardMgrSelfVo.getComboName()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRealComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRealComboType())){
	        		 sb.append("  AND REAL_COMBOTYPE='"+xuyuContentCardMgrSelfVo.getRealComboType()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getWorkingCondition()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getWorkingCondition())){
	        		 sb.append("  AND WORKING_CONDITION='"+xuyuContentCardMgrSelfVo.getWorkingCondition()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getBillingStatus()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getBillingStatus())){
	        		 sb.append("  AND BILLING_STATUS='"+xuyuContentCardMgrSelfVo.getBillingStatus()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateStart())){
	        		 sb.append("  AND ESTABLISH_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateEnd())){
	        		 sb.append("  AND ESTABLISH_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateStart())){
	        		 sb.append("  AND ACTIVATE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateEnd())){
	        		 sb.append("  AND ACTIVATE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateStart())){
	        		 sb.append("  AND DEADLINE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateEnd())){
	        		 sb.append("  AND DEADLINE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumStart())){
	        		 sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrSelfVo.getAccessNumStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumEnd())){
	        		 sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrSelfVo.getAccessNumEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidStart())){
	        		 sb.append("  AND ICCID>='"+xuyuContentCardMgrSelfVo.getIccidStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidEnd())){
	        		 sb.append("  AND ICCID<='"+xuyuContentCardMgrSelfVo.getIccidEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiStart())){
	        		 sb.append("  AND IMSI>='"+xuyuContentCardMgrSelfVo.getImsiStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiEnd())){
	        		 sb.append("  AND IMSI<='"+xuyuContentCardMgrSelfVo.getImsiEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getMark()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getMark())){
	        		 sb.append("  AND MARK like '"+xuyuContentCardMgrSelfVo.getMark()+"%'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRemainGpsType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGpsType())){
	        		 if(xuyuContentCardMgrSelfVo.getRemainGps()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGps())){
	        			 sb.append("  AND REMAIN_GPS  "+xuyuContentCardMgrSelfVo.getRemainGpsType()+"  cast('"+xuyuContentCardMgrSelfVo.getRemainGps()+"' as decimal(16,4)) ");
	        		 }
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIsowner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        		 if("y".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND !(ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        		 if("n".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND (ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        	 }
	        	 
	        }
	       
	        sb.append("  ) t2  left JOIN XUYU_OWNER_INFO t3 ON t2.`OWNER` = t3.ID   ");
	        sb.append(" LEFT JOIN SYSTEM_AUTH_ORG t4 on t2.AGENCY=t4.id ");
	        sb.append(" order by t2.ACCESS_NUM,t2.IMSI,t2.ICCID ");
	        logger.info("下载的sql:"+sb.toString());
        	String[] colmnsList = {
        			"ID(不可更改)",
        			"接入号",
        			"iccid",
        			"imsi",
        			"支付类型",
        			"运营商",
        			"二级运营商",
        			"客户名称",
        			"卡状态#",
        			"套餐类型",
        			"套餐",
        			"当月用量(MB)#",
        			"套餐总量(MB)#",
        			"余额",
        			"已发短信",
        			"沉默期类型#",
        			"测试期类型#",
        			"到期日期#",
        			"开户日期 #",
        			"激活日期# ",
        			"沉默期到期时间#",
        			"沉默期类型(实际)",
        			"测试期类型(实际)",
        			"卡种#",
        			"规格#",
        			"开户日期 (实际)",
        			"激活日期 (实际)",
        			"沉默期到期时间(实际)",
        			"在线状态",
        			"实际套餐#",
        			"成本单价#",
        			"当月费用",
        			"理想化成本",
        			"营收",
        			"上月消费额度",
        			"剩余额度",
        			"实际利润",
        			"理想化利润",
        			"备注#",
        			"群组"
		  	};
			// 字段映射
			Map<String, String> fieldLabel=new HashMap<String, String>();
			fieldLabel.put("ID", "ID(不可更改)");
			fieldLabel.put("ACCESS_NUM", "接入号");
			fieldLabel.put("ICCID", "ICCID");
			fieldLabel.put("IMSI", "IMSI");
			fieldLabel.put("PAYMENT_TYPE", "支付类型");
			fieldLabel.put("PROVIDER", "运营商");
			fieldLabel.put("OWNER_PLACE", "二级运营商");
			fieldLabel.put("ORG_NAME", "客户名称");
			fieldLabel.put("WORKING_CONDITION", "卡状态");
			fieldLabel.put("COMBO_TYPE", "套餐类型");
			fieldLabel.put("COMNO_NAME", "套餐");
			fieldLabel.put("USE_GPRS", "当月用量");
			fieldLabel.put("TOTAL_GPRS", "套餐总量(MB)");
			fieldLabel.put("REMAIN_GPS", "余额");
			fieldLabel.put("MESSAGE_COUNT", "已发短信");
			fieldLabel.put("WAIT_TYPE", "沉默期类型");
			fieldLabel.put("TEST_TYPE", "测试期类型");
			fieldLabel.put("DEADLINE_DATE", "到期日期");
			fieldLabel.put("ESTABLISH_DATE", "开户日期");
			fieldLabel.put("ACTIVATE_DATE", "激活日期");
			fieldLabel.put("WAIT_DATE", "沉默期到期时间");
			fieldLabel.put("REAL_WAIT_TYPE", "沉默期类型(实际)");
			fieldLabel.put("CARD_TYPE", "卡种");
			fieldLabel.put("STANDARD", "规格");
			fieldLabel.put("REAL_TEST_TYPE", "测试期类型(实际)");
			fieldLabel.put("REAL_ESTABLISH", "开户日期 (实际)");
			fieldLabel.put("REAL_ACTIVATE", "激活日期 (实际)");
			fieldLabel.put("REAL_WAIT_DATE", "沉默期到期时间(实际)");
			fieldLabel.put("BILLING_STATUS", "在线状态");
			fieldLabel.put("REAL_COMBOTYPE", "实际套餐");
			fieldLabel.put("UNIT_COST", "成本单价");
			fieldLabel.put("a1", "当月费用");
			fieldLabel.put("a2", "理想化成本");
			fieldLabel.put("a3", "营收");
			fieldLabel.put("a4", "上月消费额度");
			fieldLabel.put("a5", "剩余额度");
			fieldLabel.put("a6", "实际利润");
			fieldLabel.put("a7", "理想化利润");
			fieldLabel.put("MARK", "备注");
			fieldLabel.put("OWNER_NAME", "群组");
			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
			bdeo.WriteExcel(false,getCon(),sb.toString());
			
			// 结束以后把异步任务表改回来
			String id=SystemConstants.STRINGEMPTY+mapSet.get("id");
			XuyuDown down=xuyuDownService.find(id);
			down.setStatu(SystemConstants.STRING_YES);
			xuyuDownService.saveInfo(down);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapSet;
	}
    
	/**
	 * 普通下载
	 */
	@Override
	public Map<String, Object> exportData(XuyuContentCardMgrSelfVo xuyuContentCardMgrSelfVo) {
		Map<String ,Object> map=new HashMap<String,Object>();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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
	        String downLoadSize=ProsUtil.getProperty("downLoadSize");
	        StringBuffer sb=new StringBuffer("");
	        sb.append(" SELECT ");
	        sb.append(" t2.ID,");
	        sb.append(" t2.ACCESS_NUM,t2.ICCID,t2.IMSI, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='PAY_TYPE'  and a1.F_CODE=t2.PAYMENT_TYPE) as PAYMENT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR'  and a1.F_CODE=t2.PROVIDER) as PROVIDER,");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_YD'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_LT'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_DX'  and a1.F_CODE=t2.OWNER_PLACE) ");
	        sb.append("  end OWNER_PLACE, ");
	        sb.append(" t4.ORG_NAME, ");
	        sb.append(" case when t2.PROVIDER='1' then  ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='MOBILE_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  when t2.PROVIDER='2' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='UNICOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append(" when t2.PROVIDER='3' then ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='TELECOM_CARD_STATU'  and a1.F_CODE=t2.WORKING_CONDITION) ");
	        sb.append("  end WORKING_CONDITION, ");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_TYPE'  and a1.F_CODE=t2.COMBO_TYPE) as COMBO_TYPE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OWNER_COMBO_NAME'  and a1.F_CODE=t2.COMNO_NAME) as COMNO_NAME,");
	        sb.append(" t2.USE_GPRS,");
	        sb.append(" t2.TOTAL_GPRS,");
	        sb.append(" t2.REMAIN_GPS,");
	        sb.append(" t2.MESSAGE_COUNT,");
	        
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'SILENT_TYPE' AND a1.F_CODE = t2.WAIT_TYPE ) AS WAIT_TYPE,");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'HAVE_TEST' AND a1.F_CODE = t2.TEST_TYPE ) AS TEST_TYPE,	");
	        sb.append(" DATE_FORMAT(t2.DEADLINE_DATE,'%Y-%m-%d')	as DEADLINE_DATE,");
	        sb.append(" DATE_FORMAT(t2.ESTABLISH_DATE,'%Y-%m-%d')	as ESTABLISH_DATE,");
	        sb.append(" DATE_FORMAT(t2.ACTIVATE_DATE,'%Y-%m-%d')	as ACTIVATE_DATE,");
	        sb.append(" DATE_FORMAT(t2.WAIT_DATE,'%Y-%m-%d')		as WAIT_DATE,");
	        
	        
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_WAIT_TYPE'  and a1.F_CODE=t2.REAL_WAIT_TYPE) as REAL_WAIT_TYPE,	");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='OPERATOR_TEST_TYPE'  and a1.F_CODE=t2.REAL_TEST_TYPE) as REAL_TEST_TYPE,	");
	        
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'CARD_TYPE' AND a1.F_CODE = t2.CARD_TYPE ) AS CARD_TYPE, ");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'STANDARD' AND a1.F_CODE = t2.STANDARD ) AS STANDARD,");
	        
	        sb.append(" DATE_FORMAT(t2.REAL_ESTABLISH,'%Y-%m-%d')	as REAL_ESTABLISH,");
	        sb.append("	DATE_FORMAT(t2.REAL_ACTIVATE,'%Y-%m-%d')	as REAL_ACTIVATE,");
	        sb.append(" DATE_FORMAT(t2.REAL_WAIT_DATE,'%Y-%m-%d')	as REAL_WAIT_DATE,");
	        sb.append(" (select a1.F_VALUE from SYSTEM_LOOKUP_ITEM a1 where a1.F_LOOKUP_ID='ONLINE_STATU'  and a1.F_CODE=t2.BILLING_STATUS) as BILLING_STATUS,");
	        sb.append(" ( SELECT a1.F_VALUE FROM SYSTEM_LOOKUP_ITEM a1 WHERE a1.F_LOOKUP_ID = 'OPERATOR_COMBO_TYPE' AND a1.F_CODE = t2.REAL_COMBOTYPE ) AS REAL_COMBOTYPE,");
	        sb.append(" t2.UNIT_COST,");
	        sb.append(" '' as a1,");
	        sb.append(" '' as a2,");
	        sb.append(" '' as a3,");
	        sb.append(" '' as a4,");
	        sb.append(" '' as a5,");
	        sb.append(" '' as a6,");
	        sb.append(" '' as a7,");
	        sb.append(" t2.mark, ");
	        sb.append(" t3.OWNER_NAME");
	        
	        sb.append("  from (select * from XUYU_CONTENT_CARD_INFO WHERE 1=1 ");
	        if(xuyuContentCardMgrSelfVo!=null){
	        	if(xuyuContentCardMgrSelfVo.getKeywords()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getKeywords())){
	 	        	sb.append(" AND ( ACCESS_NUM like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or ICCID like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%' or IMSI like '"+xuyuContentCardMgrSelfVo.getKeywords()+"%'  )");
	 	         }
	        	 if(xuyuContentCardMgrSelfVo.getAgencyQuery()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAgencyQuery())){
	        		 SystemOrgVo systemOrgVo=systemOrgService.find(SystemConstants.STRINGEMPTY+xuyuContentCardMgrSelfVo.getAgencyQuery());
	        		 sb.append(" AND EXISTS (SELECT * FROM SYSTEM_AUTH_ORG t1 where t1.ORG_ID like '"+systemOrgVo.getOrgId()+"%' and t1.ID=AGENCY) ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getPaymentType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getPaymentType())){
	        		 sb.append(" AND PAYMENT_TYPE='"+xuyuContentCardMgrSelfVo.getPaymentType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getProvider()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getProvider())){
	        		 sb.append(" AND PROVIDER='"+xuyuContentCardMgrSelfVo.getProvider()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getOwnerPlace()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getOwnerPlace())){
	        		 sb.append(" AND OWNER_PLACE='"+xuyuContentCardMgrSelfVo.getOwnerPlace()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboType())){
	        		 sb.append(" AND COMBO_TYPE='"+xuyuContentCardMgrSelfVo.getComboType()+"' ");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getComboName()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getComboName())){
	        		 sb.append("  AND COMNO_NAME='"+xuyuContentCardMgrSelfVo.getComboName()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRealComboType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRealComboType())){
	        		 sb.append("  AND REAL_COMBOTYPE='"+xuyuContentCardMgrSelfVo.getRealComboType()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getWorkingCondition()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getWorkingCondition())){
	        		 sb.append("  AND WORKING_CONDITION='"+xuyuContentCardMgrSelfVo.getWorkingCondition()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getBillingStatus()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getBillingStatus())){
	        		 sb.append("  AND BILLING_STATUS='"+xuyuContentCardMgrSelfVo.getBillingStatus()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateStart())){
	        		 sb.append("  AND ESTABLISH_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getEstablishDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getEstablishDateEnd())){
	        		 sb.append("  AND ESTABLISH_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getEstablishDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateStart())){
	        		 sb.append("  AND ACTIVATE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getActivateDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getActivateDateEnd())){
	        		 sb.append("  AND ACTIVATE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getActivateDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateStart())){
	        		 sb.append("  AND DEADLINE_DATE>=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateStart()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getDeadlineDateEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getDeadlineDateEnd())){
	        		 sb.append("  AND DEADLINE_DATE<=str_to_date('"+xuyuContentCardMgrSelfVo.getDeadlineDateEnd()+"','%Y-%m-%d')");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumStart())){
	        		 sb.append("  AND ACCESS_NUM>='"+xuyuContentCardMgrSelfVo.getAccessNumStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getAccessNumEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getAccessNumEnd())){
	        		 sb.append("  AND ACCESS_NUM<='"+xuyuContentCardMgrSelfVo.getAccessNumEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidStart())){
	        		 sb.append("  AND ICCID>='"+xuyuContentCardMgrSelfVo.getIccidStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIccidEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIccidEnd())){
	        		 sb.append("  AND ICCID<='"+xuyuContentCardMgrSelfVo.getIccidEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiStart()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiStart())){
	        		 sb.append("  AND IMSI>='"+xuyuContentCardMgrSelfVo.getImsiStart()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getImsiEnd()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getImsiEnd())){
	        		 sb.append("  AND IMSI<='"+xuyuContentCardMgrSelfVo.getImsiEnd()+"'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getMark()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getMark())){
	        		 sb.append("  AND MARK like '"+xuyuContentCardMgrSelfVo.getMark()+"%'");
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getRemainGpsType()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGpsType())){
	        		 if(xuyuContentCardMgrSelfVo.getRemainGps()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getRemainGps())){
	        			 sb.append("  AND REMAIN_GPS  "+xuyuContentCardMgrSelfVo.getRemainGpsType()+"  cast('"+xuyuContentCardMgrSelfVo.getRemainGps()+"' as decimal(16,4)) ");
	        		 }
	        	 }
	        	 if(xuyuContentCardMgrSelfVo.getIsowner()!=null&&!SystemConstants.STRINGEMPTY.equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        		 if("y".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND !(ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        		 if("n".equals(xuyuContentCardMgrSelfVo.getIsowner())){
	        			 sb.append("  AND (ISNULL(OWNER) || LENGTH(trim(OWNER))<1) ");
	        		 }
	        	 }
	        	 
	        }
	       
	        sb.append("  ) t2  left JOIN XUYU_OWNER_INFO t3 ON t2.`OWNER` = t3.ID   ");
	        sb.append(" LEFT JOIN SYSTEM_AUTH_ORG t4 on t2.AGENCY=t4.id ");
	        sb.append(" order by t2.ACCESS_NUM,t2.IMSI,t2.ICCID limit  "+downLoadSize);
	        logger.info("下载的sql:"+sb.toString());
        	String[] colmnsList = {
        			"ID(不可更改)",
        			"接入号",
        			"iccid",
        			"imsi",
        			"支付类型",
        			"运营商",
        			"二级运营商",
        			"客户名称",
        			"卡状态#",
        			"套餐类型",
        			"套餐",
        			"当月用量(MB)#",
        			"套餐总量(MB)#",
        			"余额",
        			"已发短信",
        			"沉默期类型#",
        			"测试期类型#",
        			"到期日期#",
        			"开户日期 #",
        			"激活日期# ",
        			"沉默期到期时间#",
        			"沉默期类型(实际)",
        			"测试期类型(实际)",
        			"卡种#",
        			"规格#",
        			"开户日期 (实际)",
        			"激活日期 (实际)",
        			"沉默期到期时间(实际)",
        			"在线状态",
        			"实际套餐#",
        			"成本单价#",
        			"当月费用",
        			"理想化成本",
        			"营收",
        			"上月消费额度",
        			"剩余额度",
        			"实际利润",
        			"理想化利润",
        			"备注#",
        			"群组"
		  	};
			// 字段映射
			Map<String, String> fieldLabel=new HashMap<String, String>();
			fieldLabel.put("ID", "ID(不可更改)");
			fieldLabel.put("ACCESS_NUM", "接入号");
			fieldLabel.put("ICCID", "ICCID");
			fieldLabel.put("IMSI", "IMSI");
			fieldLabel.put("PAYMENT_TYPE", "支付类型");
			fieldLabel.put("PROVIDER", "运营商");
			fieldLabel.put("OWNER_PLACE", "二级运营商");
			fieldLabel.put("ORG_NAME", "客户名称");
			fieldLabel.put("WORKING_CONDITION", "卡状态");
			fieldLabel.put("COMBO_TYPE", "套餐类型");
			fieldLabel.put("COMNO_NAME", "套餐");
			fieldLabel.put("USE_GPRS", "当月用量");
			fieldLabel.put("TOTAL_GPRS", "套餐总量(MB)");
			fieldLabel.put("REMAIN_GPS", "余额");
			fieldLabel.put("MESSAGE_COUNT", "已发短信");
			fieldLabel.put("WAIT_TYPE", "沉默期类型");
			fieldLabel.put("TEST_TYPE", "测试期类型");
			fieldLabel.put("DEADLINE_DATE", "到期日期");
			fieldLabel.put("ESTABLISH_DATE", "开户日期");
			fieldLabel.put("ACTIVATE_DATE", "激活日期");
			fieldLabel.put("WAIT_DATE", "沉默期到期时间");
			fieldLabel.put("REAL_WAIT_TYPE", "沉默期类型(实际)");
			fieldLabel.put("CARD_TYPE", "卡种");
			fieldLabel.put("STANDARD", "规格");
			fieldLabel.put("REAL_TEST_TYPE", "测试期类型(实际)");
			fieldLabel.put("REAL_ESTABLISH", "开户日期 (实际)");
			fieldLabel.put("REAL_ACTIVATE", "激活日期 (实际)");
			fieldLabel.put("REAL_WAIT_DATE", "沉默期到期时间(实际)");
			fieldLabel.put("BILLING_STATUS", "在线状态");
			fieldLabel.put("REAL_COMBOTYPE", "实际套餐");
			fieldLabel.put("UNIT_COST", "成本单价");
			fieldLabel.put("a1", "当月费用");
			fieldLabel.put("a2", "理想化成本");
			fieldLabel.put("a3", "营收");
			fieldLabel.put("a4", "上月消费额度");
			fieldLabel.put("a5", "剩余额度");
			fieldLabel.put("a6", "实际利润");
			fieldLabel.put("a7", "理想化利润");
			fieldLabel.put("MARK", "备注");
			fieldLabel.put("OWNER_NAME", "群组");
			BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filepath,sheetName,"",colmnsList,fieldLabel);
			bdeo.WriteExcel(false,getCon(),sb.toString());

			map.put("filename", filename);
			map.put("annexeName", "卡管理导出数据.xlsx");
			map.put("filepath", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
