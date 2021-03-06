package com.alipay.core;

import org.apache.ibatis.session.SqlSession;

import com.xuyurepos.dao.payment.PaymentDao;
import com.xuyurepos.entity.payment.PaymentOrder;

public class AlipayCoreService {

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
	
	    public static void main(String[] args) {
			AlipayCoreService core=new AlipayCoreService();
			PaymentOrder order=new PaymentOrder();
			order.setTradeNo("11111");
			core.findByOrderNo("11111");
			core.createOrder(order);
			core.updateOrderStatus(order);
		}
}
