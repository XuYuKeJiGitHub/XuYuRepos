package com.xuyurepos.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.log.LoggerFactory;
/**
 * 统一异常处理
 * @author yangfei
 *
 */
public class BizException implements HandlerExceptionResolver{
	
	
	Logger logger=LoggerFactory.getInstance().getLogger(BizException.class);
	
    @SuppressWarnings("unchecked")
	@Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object code, Exception ex) {
        @SuppressWarnings("unused")
		CustomException customException = null;
        //向前台返回错误信息
        ModelAndView modelAndView = new ModelAndView();
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache，must-revalidate");
        //如果抛出的是系统自定义的异常则直接转换
        if(ex instanceof CustomException) {
            customException = (CustomException) ex;
    		logger.error("业务异常：",ex);
    		try {
    			@SuppressWarnings("rawtypes")
    			Map map=new HashMap();
    			map.put("sucess", false);
    			map.put("msg", ex.getMessage());
    			String result=JSONObject.toJSONString(map);
    			response.getWriter().write(result);
    			response.getWriter().flush();
    			response.getWriter().close();
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("与客户端通讯异常");
    		}
        } else {
            //如果抛出的不是系统自定义的异常则重新构造一个未知错误异常
            //这里我就也有CustomException省事了，实际中应该要再定义一个新的异常
            customException = new CustomException("系统未知错误");
            try {
            	@SuppressWarnings("rawtypes")
				Map map=new HashMap();
                map.put("sucess", false);
     			map.put("msg", "操作失败");
     			String result=JSONObject.toJSONString(map);
     			response.getWriter().write(result);
     			response.getWriter().flush();
     			response.getWriter().close();
			} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("与客户端通讯异常");
			}
        }
        return modelAndView;
    }

}
