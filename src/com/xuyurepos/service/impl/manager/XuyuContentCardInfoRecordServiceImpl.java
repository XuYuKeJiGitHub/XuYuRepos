package com.xuyurepos.service.impl.manager;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.dao.manager.XuyuContentCardInfoRecordDao;
import com.xuyurepos.entity.manager.XuyuContentCardInfoRecord;
import com.xuyurepos.service.manager.XuyuContentCardInfoRecordService;
import com.xuyurepos.vo.manager.XuyuContentCardInfoRecordVo;
/**
 * @author yangfei
 * 物联卡信息明细表实现类
 */
@Service
@Transactional
public class XuyuContentCardInfoRecordServiceImpl implements XuyuContentCardInfoRecordService{
	
	
	Logger log=LoggerFactory.getInstance().getLogger(XuyuContentCardInfoRecordServiceImpl.class);

	@Autowired
	private XuyuContentCardInfoRecordDao xuyuContentCardInfoRecordDao;

	@Override
	public void delete(String ids) {
		if(!StringUtil.isEmpty(ids)){
			String[] idsStr=ids.split(SystemConstants.STRING_SENT);
			for (int i = 0; i < idsStr.length; i++) {
				if(!StringUtil.isEmpty(idsStr[i])){
					Integer id=Integer.valueOf(idsStr[i]);
					XuyuContentCardInfoRecord xuyuContentCardInfoRecord=xuyuContentCardInfoRecordDao.find(id);
					if(xuyuContentCardInfoRecord!=null){
						xuyuContentCardInfoRecordDao.del(id);
					}
				}
			}
		}
	}

	@Override
	public XuyuContentCardInfoRecord saveInfo(XuyuContentCardInfoRecordVo xuyuContentCardInfoRecordVo) {
		log.info("xuyuContentCardInfoRecordVo:"+xuyuContentCardInfoRecordVo.toString());
		XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
		if(xuyuContentCardInfoRecordVo!=null&&SystemConstants.STRINGEMPTY.equals(xuyuContentCardInfoRecordVo.getId())){
			xuyuContentCardInfoRecord=add(xuyuContentCardInfoRecordVo);
		}else{
			xuyuContentCardInfoRecord=edit(xuyuContentCardInfoRecordVo);
		}
		return xuyuContentCardInfoRecord;
	}
    
	/**
	 * 编辑数据
	 * @param xuyuContentCardInfoRecordVo
	 * @return
	 */
	private XuyuContentCardInfoRecord edit(XuyuContentCardInfoRecordVo xuyuContentCardInfoRecordVo) {
		XuyuContentCardInfoRecord xuyuContentCardInfoRecord=xuyuContentCardInfoRecordDao.find(Integer.valueOf(xuyuContentCardInfoRecordVo.getId()));
		if(xuyuContentCardInfoRecord!=null){
			BeanUtils.copyProperties(xuyuContentCardInfoRecordVo, xuyuContentCardInfoRecord);
			xuyuContentCardInfoRecordDao.update(xuyuContentCardInfoRecord);
		}
		return xuyuContentCardInfoRecord;
	}
    
	/**
	 * 添加数据
	 * @param xuyuContentCardInfoRecordVo
	 * @return
	 */
	private XuyuContentCardInfoRecord add(XuyuContentCardInfoRecordVo xuyuContentCardInfoRecordVo) {
		XuyuContentCardInfoRecord xuyuContentCardInfoRecord=new XuyuContentCardInfoRecord();
		BeanUtils.copyProperties(xuyuContentCardInfoRecordVo, xuyuContentCardInfoRecord);
		xuyuContentCardInfoRecord.setId(xuyuContentCardInfoRecordDao.getSequence("cardInfoRecord"));
		xuyuContentCardInfoRecordDao.insert(xuyuContentCardInfoRecord);
		return xuyuContentCardInfoRecord;
	}

}
