package com.xuyurepos.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xuyurepos.common.log.LoggerFactory;
/**
 * 切入获取请求action
 * 以及参数
 * @author yangfei
 *
 */
public class ActionLogInterceptor implements HandlerInterceptor{
	Logger logger = LoggerFactory.getInstance().getLogger(ActionLogInterceptor.class);
	
	private long startTime=0;
	private String url=null;
	private String viewName=null;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {
		url=request.getRequestURI();
		if(url.indexOf("js")!=-1
		   ||url.indexOf("css")!=-1
		   ||url.indexOf("images")!=-1
		   ||url.indexOf("js")!=-1){
			
		}else{
			logger.info("Executed action["+url+"] took "+(System.currentTimeMillis()-startTime)+"ms");
			String str="";
			if(handler!=null){
				str=handler.toString().trim();
				int index=str.indexOf(" ");
				if(index!=-1){
					str=str.substring(index, str.length()-1).trim();
				}
				index=str.indexOf(" ");
				if(index!=-1){
					str=str.substring(index+1, str.length()-1).trim();
				}
				index=str.indexOf("(");
				if(index!=-1){
					str=str.substring(0,index);
				}
				logger.info("类名和方法名称："+str);
				Map parameter=request.getParameterMap();
				if(parameter.keySet().size()>0){
					logger.info("参数:");
				}
				for (Object key : parameter.keySet()) {
					String name=(String) key;
					String value=request.getParameter(name);
					if(name.equals("password")){
						value="******************";
					}
					logger.info(name+":"+value);
				}
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView modelAndView)
			throws Exception {
        if (modelAndView != null){  
        	viewName=modelAndView.getViewName();
        	logger.info("viewName:"+viewName);
        }  
		
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		startTime=System.currentTimeMillis();
		return true;
	}
}
