package com.xuyurepos.service.impl.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.SpringTool;
import com.xuyurepos.common.analysis.LargeExcelFileRead;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.dao.manager.UpdateTempDao;
import com.xuyurepos.entity.manager.UpdateMoreTemp;
import com.xuyurepos.service.system.LookupManager;
/**
 * 导入excel解析类
 * @author yangfei
 */
@Transactional
@Service
public class IccIdManagerExcelService extends LargeExcelFileRead{

    static Pattern pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[\\+-]?[0-9]*");
    
    static DecimalFormat ds = new DecimalFormat("0");
    static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        return pattern.matcher(input).matches();
    }
	
	Logger logger=LoggerFactory.getInstance().getLogger(IccIdManagerExcelService.class);
	
	ConcurrentHashMap<String, String> mapPayType=new ConcurrentHashMap<String, String>();
	
	@Autowired
	private UpdateTempDao updateTempDao;
	
	/**
	 * 解析excel
	 * @param fileName
	 * @return
	 */
	public void anaLysisData(String fileName){
		try {
			logger.info("开始解析"+fileName);
			Long time=System.currentTimeMillis();
			updateTempDao.truncateTable();
			this.processAllSheets(fileName);
			Long endtime=System.currentTimeMillis();
			logger.info("耗时"+(endtime-time)/1000+"秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 解析数据
     */
	@Override
	protected void dealData(LinkedHashMap<String, String> map, int i) {
		try {
			if(updateTempDao==null){
				updateTempDao=(UpdateTempDao) SpringTool.getBean("updateTempDao");
			}
			mapPayType=LookupManager.getInstance().getKeys("PAY_TYPE");
			Iterator<Entry<String, String>> it= map.entrySet().iterator();
			int count=0;
		    String prePos="";
		    UpdateMoreTemp orgVo=new UpdateMoreTemp();
		    List<UpdateMoreTemp> list=new ArrayList<UpdateMoreTemp>();
		    Boolean hasData=false;
			while (it.hasNext()){
	            Map.Entry<String, String> entry=(Map.Entry<String, String>)it.next();
	            String pos=entry.getKey();
	            if(!pos.replaceAll("[^0-9]", "").equals(prePos)){
	                prePos=pos.replaceAll("[^0-9]", "");
	                count++;
	                if(count!=1){
	                	if(orgVo.toString().equals(new UpdateMoreTemp().toString())){
	                	}else{
	                		 // 增加校验类
	                		 String resutl=check(orgVo);
	                		 if(resutl.length()>0){
	                			 orgVo.setResult(resutl);
	                		 }
	                		 list.add(orgVo);
	                	}
	                	orgVo=new UpdateMoreTemp();
	                }
	                if(count!=0&&count%100==0){
	                	updateTempDao.insertByBatch(list);
	                	list.clear();
	                }
	            }
	            String sPhone =entry.getValue();
	            if (isENum(entry.getValue())) {
	            	  sPhone = ds.format(Double.parseDouble(entry.getValue())).trim();
	            }
	            BeanUtils.setProperty(orgVo,pos.replaceAll("[^a-zA-Z]", "").toLowerCase(),sPhone);
	            hasData=true;
	        }
			if(hasData==true){
				if(orgVo.toString().equals(new UpdateMoreTemp().toString())){
				}else{
					// 增加校验类
	           		String resutl=check(orgVo);
	           		if(resutl.length()>0){
	           			 orgVo.setResult(resutl);
	           		}
					list.add(orgVo);
				}
				if(list.size()>0){
					updateTempDao.insertByBatch(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 字段校验
	 * @return
	 */
	private String check(UpdateMoreTemp orgVo){
		StringBuilder sb=new StringBuilder();
		if(orgVo.getE()!=null&&!SystemConstants.STRINGEMPTY.equals(orgVo.getE())){
			if(mapPayType.get(orgVo.getE())==null){
				sb.append("支付类型错误;");
			}
		}
		return sb.toString();
	}
	

}
