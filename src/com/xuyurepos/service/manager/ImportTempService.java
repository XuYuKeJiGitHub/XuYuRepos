package com.xuyurepos.service.manager;

/**
 * 临时表导入的service
 * @author yangfei
 *
 */
public interface ImportTempService {
	
	public void anaLysisData(String batchNo);

	public void anaLysisUpdateData(String asyncBatchNo);

}
