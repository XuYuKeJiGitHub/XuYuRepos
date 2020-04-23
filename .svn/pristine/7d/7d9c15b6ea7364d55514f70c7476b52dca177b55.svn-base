package com.xuyurepos.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.xuyurepos.common.log.LoggerFactory;

/**
 * 时间处理工具类
 * @author yangfei
 *
 */
public class DateUtil {
	private static Logger log=LoggerFactory.getInstance().getLogger(DateUtil.class);
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private static final String[] FORMATS = {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm",
			"HH:mm","HH:mm:ss","yyyy-MM","yyMM","yyyyMMdd"};
	
	public static Date convert(String str){
		if(str!=null&&str.length()>0){
			if(str.length()>10 && str.charAt(10)=='T'){
				str = str.replace('T', ' ');
			}
			for(String format:FORMATS){
				if(str.length()==format.length()){
					try{
						return new SimpleDateFormat(format).parse(str);
					}catch(ParseException e){
						e.printStackTrace();
						log.error(e);
					}
				}
			}
		}
		return null;
	}
	
	public static String convert(Date date,String dateFormat){
		if(date==null)
			return null;
		if(null==dateFormat)
			dateFormat = DATE_FORMAT;
		return new SimpleDateFormat(dateFormat).format(date);
	}
	
	public static Date convert(String str,String format){
		if(str!=null&&!"".equals(str)){
			try{
				Date date = null;
				if(format!=null&&!"".equals(format)){
					date = new SimpleDateFormat(format).parse(str);
				}else{
					date = convert(str);
				}
				return date;
			}catch(ParseException e){
				log.warn(e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 获取传入日期后n个月的最后一天，n包括当月
	 * @param dateNow 当前时间
	 * @param lastMonth 往后月份数字
	 * @return Date
	 * @throws Exception
	 */
	public static Date getLastMonthdate(Date dateNow,int lastMonth)throws Exception{
		if(dateNow==null){
			throw new Exception("日期为必输项");
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(dateNow);
		int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month+(lastMonth-1));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
   }
	
	
	public static void main(String[] args) throws Exception {
		Date result=DateUtil.getLastMonthdate(new Date(), 3);
		System.out.println(new SimpleDateFormat(DATE_FORMAT).format(result));
	}

}
