package com.xuyurepos.common.log;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

//import com.xuyurepos.common.util.ProsUtil;
/**
 * 日志类工厂，防止替换日志类影响现有代码
 * @author yangfei
 *
 */
public class LoggerFactory {
	private static LoggerFactory instance;
	
	private LoggerFactory(){};
	
	/**
	 * 获取日志实现类工厂
	 * @return
	 */
	public static synchronized LoggerFactory getInstance(){
		if(instance!=null){
			return instance;
		}else{
			instance=new LoggerFactory();
		}
		return instance;
	}
	
	/**
	 * 获取日志类
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Logger getLogger(Class clazz){
//		try {
//			PropertyConfigurator.configure(ProsUtil.getProperty("log4j.path"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		Logger logger=Logger.getLogger(clazz);
		return logger;
	}
	

}
