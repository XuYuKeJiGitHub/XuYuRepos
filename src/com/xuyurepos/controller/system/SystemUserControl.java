package com.xuyurepos.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.CaptchaUtil;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemUserService;
import com.xuyurepos.vo.system.SystemUserVo;
/**
 * 用户管理相关实现入口
 * @author yangfei
 */
@Controller
@RequestMapping(value = "/user")
public class SystemUserControl {
	
	@Autowired
	private SystemUserService systemUserService; 
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(SystemUserControl.class);
	
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		try {
			 String code=request.getParameter("code");
			 CaptchaUtil.outputCaptcha(code, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
       
    }
			
	//登陆
	@RequestMapping(value="/login",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String login(SystemUser e,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String userName=request.getParameter("userName");
			System.out.println(""+userName);
			// 首先校验验证码
			log.info("验证码："+e.getJ_captcha());
			SystemUser systemUser=systemUserService.login(e);
			if(systemUser!=null){
				request.getSession().setAttribute("systemUser", systemUser);
				System.out.println("systemUser="+systemUser.toString());
				log.info("aaaaaaaaaaaaaaaaaaaaa");
				map.put("sucess", true);
			}else{
				map.put("sucess", false);
				map.put("msg", "账户或者密码错误");
			}
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("aaaaaaaaaaaaaaaaaaaaa"+1011);
			bizException.resolveException(request, response, 1011, ex);
			return null;
		}
		
	}
	
	//注销
	@RequestMapping(value="/logout",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String remove(HttpServletRequest request) {
		request.getSession().removeAttribute("systemUser");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sucess", true);
		String result=JSONObject.toJSONString(map);
		return result;
	}
	
	// 保存
	@RequestMapping(value="/resetName",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String resetName(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String cname=request.getParameter("cname");
			String old_password=request.getParameter("old_password");
			String new_password=request.getParameter("new_password");
			SystemUserVo systemUserVo=new SystemUserVo();
			systemUserVo.setCname(cname);
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			if(old_password!=null&&!"".equals(old_password)){
				if(!systemUser.getPassword().equals(old_password)){
					throw new CustomException("原始密码错误，请重新输入");
				}else{
					systemUserService.resetName(systemUser,cname,new_password);
				}
			}else{
				systemUserService.resetName(systemUser,cname,SystemConstants.STRINGEMPTY);
			}
			
			map.put("sucess", true);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("aaaaaaaaaaaaaaaaaaaaa"+1011);
			bizException.resolveException(request, response, 1001, e);
			return null;
		}
	}
	
	
	// 保存
	@RequestMapping(value="/save",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemUserVo systemUserVo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			SystemUser systemUser=systemUserService.saveInfo(systemUserVo);
			map.put("sucess", true);
			map.put("model", systemUser);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			bizException.resolveException(request, response, 1011, ex);
			return null;
		}
	}
	
	/**
	 * 获取用户对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/find",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String find(HttpServletRequest request) throws Exception{
		try {
			String id=request.getParameter("id");
			Map<String,Object> map = new HashMap<String,Object>();
			SystemUserVo systemUserVo=systemUserService.find(id);
			map.put("sucess", true);
			map.put("model", systemUserVo);
			String result=JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 删除数据
	@RequestMapping(value="/delete",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String delete(HttpServletRequest request) throws Exception{
		String ids=request.getParameter("ids");
		systemUserService.delete(ids);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sucess", true);
		String result=JSONObject.toJSONString(map);
		return result;
	}
	
	/**
	 * 分页查询数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/findList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String findList(PageModel pageModel,SystemUserVo user,HttpServletRequest request){
		// 判断是否为总部
		SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		if(!"1000".equals(systemUser.getOrgCode())){
			user.setOrgId(systemUser.getOrgCode());
		}else{
			//user.setOrgId(systemUser.getOrgCode());
		}
		pageModel.setQueryObj(user);
//		String page=request.getParameter("page");
//		String rows=request.getParameter("rows");
//		if(page!=null){
//			pageModel.setPageNumber(Integer.valueOf(page));
//		}
//		if(rows!=null){
//			pageModel.setPageSize(Integer.valueOf(rows));
//		}
		systemUserService.findList(pageModel);
		String result=JSONObject.toJSONString(pageModel);
		return result;
	}
			 

}
