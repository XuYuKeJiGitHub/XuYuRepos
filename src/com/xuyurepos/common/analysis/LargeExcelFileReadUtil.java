package com.xuyurepos.common.analysis;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
/**
 * Excel解析参考示例
 * @author yangfei
 *
 */
public class LargeExcelFileReadUtil extends LargeExcelFileRead{
    static Pattern pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[\\+-]?[0-9]*");
    
    static DecimalFormat ds = new DecimalFormat("0");
    static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        return pattern.matcher(input).matches();
    }
	 
	public static void main(String[] args) throws Exception {
		Long time=System.currentTimeMillis();
		String filename="C:\\Users\\yangfei\\Desktop\\旭宇科技\\旭宇科技电信.xlsx";
		LargeExcelFileReadUtil excelFileReadUtil=new LargeExcelFileReadUtil();
		excelFileReadUtil.processAllSheets(filename);
		Long endtime=System.currentTimeMillis();
		System.out.println("耗时"+(endtime-time)/1000+"秒");
	}

	@Override
	protected void dealData(LinkedHashMap<String, String> map, int i) {
		try {
			Iterator<Entry<String, String>> it= map.entrySet().iterator();
			int count=0;
		    String prePos="";
		    TestVo orgVo=new TestVo();
			while (it.hasNext()){
	            Map.Entry<String, String> entry=(Map.Entry<String, String>)it.next();
	            String pos=entry.getKey();
	            if(!pos.replaceAll("[^0-9]", "").equals(prePos)){
	                prePos=pos.replaceAll("[^0-9]", "");
	                count++;
	                if(count!=1){
	                	 System.out.println(i+"解析数据"+orgVo.toString());
	                	 orgVo=new TestVo();
	                }
	            }
	            String sPhone =entry.getValue();
	            if (isENum(entry.getValue())) {
	            	  sPhone = ds.format(Double.parseDouble(entry.getValue())).trim();
	            }
	            BeanUtils.setProperty(orgVo,pos.replaceAll("[^a-zA-Z]", "").toLowerCase(),sPhone);
			}
			if(orgVo.toString().equals(new TestVo().toString())){
				
			}else{
				System.out.println(i+"解析数据"+orgVo.toString());
			}
			
			System.out.println(i+"解析数据"+count+"条");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
