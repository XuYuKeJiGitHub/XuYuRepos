package com.xuyurepos.dao.batch;

import java.util.List;

import org.apache.ibatis.annotations.Param;
 

public interface CardStateChangeDao {
	
	public List  queryData(@Param("provider") int provider,@Param("fromIndex")int  fromIndex ,@Param("endIndex") int endIndex);
	
	public void updateDataState(@Param("id") String id, @Param("status") String status);
	
	public void updateDataStateByNum(@Param("accessNum") String accessNum, @Param("status") String status);
	
	public void updateDataStateByMssidn(@Param("mssidn") String mssidn, @Param("status") String status);

	
	public void updateDataStateResult(@Param("id") String id, @Param("status") String status);

	
	public void updateDataGPRS(@Param("gprs") String gprs,@Param("id") String id);
	
	public List queryCardStateResultData(@Param("provider") int provider,@Param("fromIndex")int  fromIndex ,@Param("endIndex") int endIndex);
	
}
