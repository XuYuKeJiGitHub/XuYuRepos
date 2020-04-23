/**  
 * Project Name:XuYuRepos  
 * File Name:MailService.java  
 * Package Name:com.xuyurepos.service.mail  
 * Date:2019年7月9日下午1:47:32  
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.xuyurepos.service.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xuyurepos.common.util.Mail;

/**  
 * ClassName:MailService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2019年7月9日 下午1:47:32 <br/>  
 * @author   27569  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
@Service("mailService")
public class MailService {
     /**\
      * 
      * mail:(这里用一句话描述这个方法的作用). <br/>  
      * TODO(这里描述这个方法适用条件 – 可选).<br/>  
      * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
      * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
      * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
      *  
      * @author 27569  
      * @param toAddress  eg: 123@163.com,456@163.com
      * @param content 邮件内容 
      * @param subject  邮件主题
      * @since JDK 1.6
      */
	
	public void sendMail(String toAddress,String content,String subject) {
		
		List addressList=new ArrayList();
		String[] address=toAddress.split(",");
		for (int i = 0; i < address.length; i++) {
			addressList.add(address[i]);
		}
	 Mail.sendEmail( addressList, subject, content);
	}
}
  
