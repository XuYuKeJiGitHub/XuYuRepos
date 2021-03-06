package com.xuyurepos.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.entity.system.SystemUser;

public class CommonInterceptor1 extends HandlerInterceptorAdapter {
	Logger logger = LoggerFactory.getInstance().getLogger(ActionLogInterceptor.class);
	//配置不需要拦截的url地址
    private static final String[] IGNORE_URI = {"register","captcha","views","login","logout","remove","select","delect","update1","wxpay","wappay","alipay","facade","iccidmanager"};
    //在执行调用类方法之前执行的
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    		throws Exception {
    	boolean flag = false;
    	//得到用户请求的url地址
    	String url=request.getRequestURI().toString();
    	for (String s : IGNORE_URI) {
			if(url.contains(s)){
				logger.info("这是不需要拦截的请求");
				flag = true;
                break;
			}
		}
	    if(!flag){
			SystemUser employee1=(SystemUser) request.getSession().getAttribute("systemUser");
			logger.info("这是需要拦截的请求");
			logger.info("url:"+url);
    		if(null==employee1){
    			logger.info("回话过期请重新登录"+employee1);
    			if(request.getHeader("x-requested-with")!=null){
    				response.setHeader("sessionstatus", "timeout");
    				
    			}else{
    				throw new SessionTimeoutException();
    			}
    			return false;
    		}
    		return true;
		}
    	
    	return true; 


    	}
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
    		ModelAndView modelAndView) throws Exception {
    	super.postHandle(request, response, handler, modelAndView);
    }
}
