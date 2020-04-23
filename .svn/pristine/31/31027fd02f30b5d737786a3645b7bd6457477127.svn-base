/**
 * Project Name:XuYuRepos
 * File Name:SynInfoBatchService.java
 * Package Name:com.xuyurepos.service.intergration.batch
 * Date:2019年4月2日下午1:24:36
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.xuyurepos.service.intergration.batch;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.dao.batch.CardStateChangeDao;
import com.xuyurepos.dao.comm.CommonMapper;
import com.xuyurepos.entity.manager.XuyuContentCardInfo;
import com.xuyurepos.service.intergration.facade.SynInfoFacadeService;
import com.xuyurepos.service.intergration.facade.SynInfoJSFacadeService;

/**
 * ClassName:SynInfoBatchService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2019年4月2日 下午1:24:36 <br/>
 * 
 * @author zhangliang
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional
public class SynCardInfoBatchService {

	static Logger logger = LoggerFactory.getLogger(SynCardInfoBatchService.class);

	@Autowired
	private CardStateChangeDao cardInfoDao;

	private ExecutorService execute = Executors.newFixedThreadPool(50);
	
    private  ScheduledExecutorService service = Executors.newScheduledThreadPool(10);


	private ExecutorService executeTask = Executors.newFixedThreadPool(3);

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private CommonMapper commonMapper;
	
	private SimpleDateFormat sdf=new SimpleDateFormat();

	private String mobileStartFlag = "";
	
	private String uncomStartFlag="";
	
	private String teleStartFlag="";

	
	
	
	public String getMobileStartFlag() {
		return mobileStartFlag;
	}

	public void setMobileStartFlag(String mobileStartFlag) {
		this.mobileStartFlag = mobileStartFlag;
	}

	public String getUncomStartFlag() {
		return uncomStartFlag;
	}

	public void setUncomStartFlag(String uncomStartFlag) {
		this.uncomStartFlag = uncomStartFlag;
	}

	public String getTeleStartFlag() {
		return teleStartFlag;
	}

	public void setTeleStartFlag(String teleStartFlag) {
		this.teleStartFlag = teleStartFlag;
	}

	public void sync() {
		try {
			String address = InetAddress.getLocalHost().getHostAddress();
			logger.info("当前服务器IP地址：" + address);
//				if(!address.equals("172.19.182.120")) {
//					return ;
//				}
			int mobileLimit=120000;
			int uncomLimit=3000;
			int teleLimit=120000;
			updateCardState(mobileLimit,uncomLimit,teleLimit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List queryData(int flag, int  fromIndex,int endIndex) {
		List<XuyuContentCardInfo> cardInfoList = cardInfoDao.queryCardStateResultData(flag, fromIndex,endIndex);
		return cardInfoList;
	}

	public void updateCardState(int mobileLimit,int uncomLimit,int teleLimit) {

		executeTask.execute(new Runnable() {
			
			@Override
			public void run() {
				
				addMobileTask(mobileLimit);
				
			}
		});


		
		executeTask.execute(new Runnable() {
			
			@Override
			public void run() {
				
				addUncomTask(uncomLimit);
				
			}
		});
		

		
       executeTask.execute(new Runnable() {
		
		@Override
		public void run() {
			addTeleTask(teleLimit);
		}
	});

	}
	/**
	 * 
	 * addMobileTask:(添加移动跑批任务). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param limitSize  
	 * @since JDK 1.6
	 */
	public void addMobileTask(int limitSize) {
		sdf.applyPattern("yyyyMMdd");
		String mobileStartFlag=sdf.format(new Date());
		if(this.getMobileStartFlag().equals(mobileStartFlag)) {
			logger.info("！！！！！！！！暂无移动跑批任务！！！！！！！！");
			return;
		}
		
		
		//总记录数
        int totalRecord = limitSize;
        
        //每页显示多小条数据
        int pageSize = 1000;
        
        //总页数
        int totalPage = totalRecord % pageSize == 0?totalRecord/pageSize:totalRecord/pageSize+1;
        
        
		int currentPage=0;
		for (int i = 1; i <= totalPage; i++) {
	        currentPage=i;
			
			//起始索引
			Integer fromIndex = pageSize * (currentPage - 1);
			
			//结束索引
			Integer endIndex = null;
			if(pageSize * currentPage >totalRecord){
				endIndex = totalRecord;
			}else{
				endIndex = pageSize * currentPage;
			}
			
			List<XuyuContentCardInfo> mobileCardInfoList = queryData(1, fromIndex,endIndex);
			
			if(mobileCardInfoList.size()==0) {
				this.setMobileStartFlag(mobileStartFlag);
				return ;
			}
			
			long  delay=0;
			for (XuyuContentCardInfo info : mobileCardInfoList) {
				service.schedule(new Runnable() {
					@Override
					public void run() {
						updateMobileCardInfo(info);
					}
				},delay,TimeUnit.MILLISECONDS);
				delay+=15;
			}
		}
        
		
	}
	
	/**
	 * 
	 * addUncomTask:(添加联通跑批任务). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param limitSize  
	 * @since JDK 1.6
	 */
	public void addUncomTask(int  limitSize) {
		sdf.applyPattern("yyyyMMdd");
		String uncomStartFlag=sdf.format(new Date());
		if(this.getUncomStartFlag().equals(uncomStartFlag)) {
			logger.info("！！！！！！！！！暂无联通跑批任务！！！！！！！！！！");
			return;
		}
		
		
		//总记录数
        int totalRecord = limitSize;
        
        //每页显示多小条数据
        int pageSize = 1000;
        
        //总页数
        int totalPage = totalRecord % pageSize == 0?totalRecord/pageSize:totalRecord/pageSize+1;
        
        
		int currentPage=0;
		 long delay=0;

		for (int i = 1; i <= totalPage; i++) {
	        currentPage=i;
			
			//起始索引
			Integer fromIndex = pageSize * (currentPage - 1);
			
			//结束索引
			Integer endIndex = null;
			if(pageSize * currentPage >totalRecord){
				endIndex = totalRecord;
			}else{
				endIndex = pageSize * currentPage;
			}
		
		List<XuyuContentCardInfo> uncomCardInfoList = queryData(2, fromIndex,endIndex);

		if(uncomCardInfoList.size()==0) {
			this.setUncomStartFlag(uncomStartFlag);
			return ;
		}
		
		
		for (XuyuContentCardInfo info : uncomCardInfoList) {
			service.schedule(new Runnable() {
				@Override
				public void run() {
					updateUncomCardInfo(info);
				}
		}, delay, TimeUnit.MILLISECONDS);
			delay+=500;
		
		}
		
	}
		
	}
	
	/**
	 * 
	 * addTeleTask:(添加电信跑批任务). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param limitSize  
	 * @since JDK 1.6
	 */
	public void addTeleTask(int  limitSize) {
		sdf.applyPattern("yyyyMMdd");
		String teleStartFlag=sdf.format(new Date());
		if(this.getTeleStartFlag().equals(teleStartFlag)) {
			logger.info("！！！！！！！！！暂无电信跑批任务！！！！！！！！！！");
			return ;
		}
		//总记录数
        int totalRecord = limitSize;
        
        //每页显示多小条数据
        int pageSize = 1000;
        
        //总页数
        int totalPage = totalRecord % pageSize == 0?totalRecord/pageSize:totalRecord/pageSize+1;
        
        
		int currentPage=0;
		for (int i = 1; i <= totalPage; i++) {
	        currentPage=i;
			
			//起始索引
			Integer fromIndex = pageSize * (currentPage - 1);
			
			//结束索引
			Integer endIndex = null;
			if(pageSize * currentPage >totalRecord){
				endIndex = totalRecord;
			}else{
				endIndex = pageSize * currentPage;
			}
		List<XuyuContentCardInfo> teleCardInfoList = queryData(3, fromIndex,endIndex);
		
		if(teleCardInfoList.size()==0) {
			this.setTeleStartFlag(teleStartFlag);
			return ;
		}

		long delay=0;
		for (XuyuContentCardInfo info : teleCardInfoList) {
			service.schedule(new Runnable() {
				@Override
				public void run() {
					updateTeleCardInfo(info);
				}
			},delay,TimeUnit.MILLISECONDS);
			delay+=15;
		}
	}
	}
	
	/**
	 * 
	 * updateMobileCardInfo:(更新移动卡状态). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param info  
	 * @since JDK 1.6
	 */
	public void updateMobileCardInfo(XuyuContentCardInfo info) {

		SqlSession sqlSession=sqlSessionFactory.openSession();
		
		try {
			CardStateChangeDao dao=sqlSession.getMapper(CardStateChangeDao.class);

			String id = info.getId();
			String ownerPlace = info.getOwnerPlace();
			boolean result = false;
			String mssidn = info.getAccessNum();
			String workingCondition=info.getOprateType();
//					info.getWorkingCondition();
			String operType="";
			if(StringUtils.isNotBlank(workingCondition)) {
				if(workingCondition.equals("02")) {
					operType="0";
				}else if(workingCondition.equals("03")) {
					operType="1";
				}
			}
			if (ownerPlace.equals("1") || ownerPlace.equals("2")) {
				result = SynInfoJSFacadeService.getInstance().mobileChangeCardState(mssidn, operType,ownerPlace);
			} else {
				result = SynInfoFacadeService.getInstance().mobileChangeCardState(mssidn, operType);
			}
			if (result) {
				
				String status="";
				if(operType.equals("0")) {
					status="02";
				}else if(operType.equals("1")) {
					status="01";
				}
			
				//此处逻辑有问题 id 为 result表id；
				dao.updateDataStateResult(id, "Y");
				StringBuilder sb=new StringBuilder();
				sb.append("update XUYU_CONTENT_CARD_INFO set BILLING_STATUS="+status+" where access_num="+mssidn);
				commonMapper.executeAction(sb.toString());
				 sb.setLength(0);
				 sb=null;
				//dao.updateDataStateByMssidn(mssidn, status);
				sqlSession.commit();
			}
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}

	}

	/**
	 * 
	 * updateUncomCardInfo:(更新联通卡状态). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param info  
	 * @since JDK 1.6
	 */
	public void updateUncomCardInfo(XuyuContentCardInfo info) {

    	SqlSession sqlSession=sqlSessionFactory.openSession();
    	try {
			CardStateChangeDao dao=sqlSession.getMapper(CardStateChangeDao.class);
			
			String id = info.getId();
			String accid = info.getAccessNum();
			String workingCondition=info.getWorkingCondition();
			String oper="";
			boolean result=false;
			if(StringUtils.isNotBlank(workingCondition)) {
				if(workingCondition.equals("RETIRED")) {
					oper="ACTIVATED";
				}else if(workingCondition.equals("ACTIVATED")) {
					oper="RETIRED";
				}
			}
			result = SynInfoFacadeService.getInstance().unicomChangeCardState(accid, oper);
			if (result) {
				dao.updateDataStateResult(id, "Y");
				dao.updateDataStateByNum(accid, oper);
				sqlSession.commit();
			}
		}catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}
    	finally {
			sqlSession.close();
		}  

	}

	/**
	 * 
	 * updateTeleCardInfo:(更新电信卡状态). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author 27569  
	 * @param info  
	 * @since JDK 1.6
	 */
	public void updateTeleCardInfo(XuyuContentCardInfo info) {

		SqlSession sqlSession=sqlSessionFactory.openSession();
		try {
			CardStateChangeDao dao=sqlSession.getMapper(CardStateChangeDao.class);

			String id = info.getId();
			String accessNum = info.getAccessNum();
			String workingCondition=info.getWorkingCondition();
			String ownerPlace = info.getOwnerPlace();
			String oper="";
			boolean result=false;
			result = SynInfoFacadeService.getInstance().telecomChangeCardState(accessNum,oper,ownerPlace);
			
			if(StringUtils.isNotBlank(workingCondition)) {
				if(workingCondition.equals("19")) {
					oper="20";
				}else if(workingCondition.equals("20")) {
					oper="19";
				}
			}
			
			
			
			if (result) {
				dao.updateDataStateResult(id, "Y");
				dao.updateDataStateByNum(accessNum, oper);
				sqlSession.commit();
			}
		}catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}

	}
	
	
	
}
