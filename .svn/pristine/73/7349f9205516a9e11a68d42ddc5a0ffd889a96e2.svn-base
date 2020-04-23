/**
 * Project Name:XuYuRepos
 * File Name:PaymentService.java
 * Package Name:com.xuyurepos.service.payment
 * Date:2019年3月22日下午6:54:10
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.xuyurepos.service.payment;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.alipay.core.DBTools;
import com.xuyurepos.dao.payment.PaymentDao;
import com.xuyurepos.entity.payment.PaymentOrder;

/**
 * ClassName:PaymentService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019年3月22日 下午6:54:10 <br/>
 * @author   zhangliang
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Service
public class PaymentService {


    public   void createOrder(PaymentOrder order) {
         SqlSession session = DBTools.getSession();
         PaymentDao mapper = session.getMapper(PaymentDao.class);
        
         try {
             mapper.createOrder(order);
              session.commit();
         } catch (Exception e) {
             e.printStackTrace();
             session.rollback();
         }finally {
			session.close();
		}
     }
    
    public void updateOrderStatus(PaymentOrder order) {
        SqlSession session = DBTools.getSession();
         PaymentDao mapper = session.getMapper(PaymentDao.class);
         
         try {
			mapper.updateOrderStatus(order);
			 session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			session.close();
		}
    }
    
    public PaymentOrder findByOrderNo(String tradeNo) {

        SqlSession session = DBTools.getSession();
         PaymentDao mapper = session.getMapper(PaymentDao.class);
         PaymentOrder orderModel=new PaymentOrder();
         try {
        	 orderModel=	 mapper.findByOrderNo(tradeNo);
			 session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			session.close();
		}
    return  orderModel;
    }
    

	
	
}

