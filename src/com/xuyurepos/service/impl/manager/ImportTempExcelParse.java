package com.xuyurepos.service.impl.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.SpringTool;
import com.xuyurepos.common.analysis.LargeExcelFileRead;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.dao.manager.QuartzImportDao;
import com.xuyurepos.entity.manager.QuartzImport;

/**
 * excel解析类
 * 首次导入使用
 * @author yangfei
 */
@Transactional
@Service
public class ImportTempExcelParse extends LargeExcelFileRead{
	
    static Pattern pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[\\+-]?[0-9]*");
    
    static DecimalFormat ds = new DecimalFormat("0");
    static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        return pattern.matcher(input).matches();
    }
	
	Logger logger=LoggerFactory.getInstance().getLogger(ImportTempServiceImpl.class);
	
	@Autowired
	private QuartzImportDao quartzImportDao;
	
	public void anaLysisData(String fileName) {
		try {
			logger.info("开始解析");
			Long time=System.currentTimeMillis();
			quartzImportDao.truncateTable();
			this.processAllSheets(fileName);
			Long endtime=System.currentTimeMillis();
			logger.info("耗时"+(endtime-time)/1000+"秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void dealData(LinkedHashMap<String, String> map, int i) {
		try {
			if(quartzImportDao==null){
				quartzImportDao=(QuartzImportDao) SpringTool.getBean("quartzImportDao");
			}
			Iterator<Entry<String, String>> it= map.entrySet().iterator();
			int count=0;
		    String prePos="";
		    QuartzImport orgVo=new QuartzImport();
		    List<QuartzImport> list=new ArrayList<QuartzImport>();
		    Boolean hasData=false;
			while (it.hasNext()){
	            Map.Entry<String, String> entry=(Map.Entry<String, String>)it.next();
	            String pos=entry.getKey();
	            if(!pos.substring(1).equals(prePos)){
	                prePos=pos.substring(1);
	                count++;
	                if(count!=1){
	                	if(orgVo.toString().equals(new QuartzImport().toString())){
	                	}else{
	                		 list.add(orgVo);
	                	}
	                	orgVo=new QuartzImport();
	                }
	                if(count!=0&&count%100==0){
	                	quartzImportDao.insertByBatch(list);
	                	list.clear();
	                }
	            }
	            String sPhone =entry.getValue();
	            if (isENum(entry.getValue())) {
		            	try {
		            		sPhone = ds.format(Double.parseDouble(entry.getValue())).trim();
						} catch (Exception e) {
							logger.info("数字转换异常，忽略"+sPhone);
						}
	            }
	            BeanUtils.setProperty(orgVo,pos.substring(0,1).toLowerCase(),sPhone);
	            hasData=true;
	        }
			if(hasData==true){
				if(orgVo.toString().equals(new QuartzImport().toString())){
				}else{
					list.add(orgVo);
				}
				if(list.size()>0){
					quartzImportDao.insertByBatch(list);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(100%100);
	}



}
