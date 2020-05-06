/**  
 * Project Name:XuYuRepos  
 * File Name:ProviderSupportService.java  
 * Package Name:com.xuyurepos.service.intergration  
 * Date:2019年4月5日上午11:46:21  
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.  
 *  
*/

package com.xuyurepos.service.intergration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.service.intergration.facade.SynInfoFacadeService;
import com.xuyurepos.service.intergration.facade.SynInfoJSFacadeService;

/**
 * ClassName:ProviderSupportService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2019年4月5日 上午11:46:21 <br/>
 * 
 * @author 27569
 * @version
 * @since JDK 1.6
 * @see
 */

@Service
public class ProviderSupportService {

	static Logger logger = LoggerFactory.getLogger(SynInfoJSFacadeService.class);

	/**
	 * 
	 * downTimeByMobile:(移动物联网卡停复机). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param cardNo     移动 mssidn 联通 accid 电信 access_number
	 * @param operType    0-暂停 1-恢复
	 * @param provider    运营商 1:移动 2：联通 3：电信
	 * @param ownerPlace 1淮安 2盐城 3 上海 null无
	 * 
	 * @return
	 * @since JDK 1.8
	 */

	public boolean changeCardState(String cardNo, String operType, String provider, String ownerPlace) {
		boolean result = false;
		try {

			if (StringUtils.isNotBlank(provider) && provider.equals("1")) {
				if (StringUtils.isNotBlank(ownerPlace)) {
					if (ownerPlace.equals("1") || ownerPlace.equals("2")) {
						String status = SynInfoJSFacadeService.getInstance().mobileChangeCardState(cardNo, operType,ownerPlace);
						if(SystemConstants.STRING_YES.equals(status)){
							result = true;
						}else {
							result = false;
						}
					
					} else if (ownerPlace.equals("3")) {
						result = SynInfoFacadeService.getInstance().mobileChangeCardState(cardNo, operType);
					}
				}

			} else if (StringUtils.isNotBlank(provider) && provider.equals("2")) {
				result = SynInfoFacadeService.getInstance().unicomChangeCardState(cardNo, operType);
			} else if (StringUtils.isNotBlank(provider) && provider.equals("3")) {
				result = SynInfoFacadeService.getInstance().telecomChangeCardState(cardNo, operType,ownerPlace);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	/**
	 * 
	 * cardStatusService:(同卡用量). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * 
	 * @param provider    运营商 1:移动 2：联通 3：电信
	 * @param ownerPlace 1淮安 2盐城 3 上海 null无
	 * @param cardNo     移动 mssidn 联通 accid 电信 access_number 单位 MB
	 * @author zhangliang
	 * @param msisdn
	 * @since JDK 1.8
	 */
	public String gprsQueryService(String cardNo, String ownerPlace, String provider) {

		String gprsUsed = "";

		try {

			if (StringUtils.isNotBlank(provider) && provider.equals("1")) {
				if (StringUtils.isNotBlank(ownerPlace)) {
					if (ownerPlace.equals("1") || ownerPlace.equals("2")) {
						gprsUsed = SynInfoJSFacadeService.getInstance().mobileGPRSQueryService(cardNo, ownerPlace);
					} else if (ownerPlace.equals("3")) {
						gprsUsed = SynInfoFacadeService.getInstance().mobileGPRSQueryService(cardNo);
					}
				}

			} else if (StringUtils.isNotBlank(provider) && provider.equals("2")) {
				gprsUsed = SynInfoFacadeService.getInstance().unicomGPRSQueryService(cardNo);
			} else if (StringUtils.isNotBlank(provider) && provider.equals("3")) {
				gprsUsed = SynInfoFacadeService.getInstance().telecomGPRSQueryService(cardNo,ownerPlace);
			}
			return gprsUsed.replace("MB", "");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return gprsUsed;
	}

	/**
	 * 
	 * messageSendService:(提供发送本地号码API。). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhangliang
	 * @param msisdn
	 * @param content
	 * @return
	 * @since JDK 1.8
	 */

	public boolean mobileMessageSendService(String cardNo, String content, String provider, String ownerPlace) {
		boolean result = false;

		try {

			if (StringUtils.isNotBlank(provider) && provider.equals("1")) {
				if (StringUtils.isNotBlank(ownerPlace)) {
					if (ownerPlace.equals("1") || ownerPlace.equals("2")) {
						result = SynInfoJSFacadeService.getInstance().mobileMessageSendService(cardNo, content,
								ownerPlace);
					} else if (ownerPlace.equals("3")) {
						result = SynInfoFacadeService.getInstance().mobileMessageSendService(cardNo, content);
					}
				}

			} else if (StringUtils.isNotBlank(provider) && provider.equals("2")) {
				result = SynInfoFacadeService.getInstance().unicomMessageSendService(cardNo, content);
			}

			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 
	 * userStatusQuery:(卡工作状态). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @param provider    运营商 1:移动 2：联通 3：电信
	 * @param ownerPlace 1淮安 2盐城 3 上海 null无
	 * @param cardNo     移动 mssidn 联通 accid 电信 access_number
	 * 
	 * @author zhangliang
	 * @param msisdn
	 * @return
	 * @since JDK 1.8
	 */
	public String mobileUserStatusQuery(String cardNo, String ownerPlace, String provider) {

		String status = "";

		try {

			if (StringUtils.isNotBlank(provider) && provider.equals("1")) {
				if (StringUtils.isNotBlank(ownerPlace)) {
					if (ownerPlace.equals("1") || ownerPlace.equals("2")) {
						status = SynInfoJSFacadeService.getInstance().mobileUserStatusQuery(cardNo, ownerPlace);
					} else if (ownerPlace.equals("3")) {
						status = SynInfoFacadeService.getInstance().mobileUserStatusQuery(cardNo);
					}
				}

			} else if (StringUtils.isNotBlank(provider) && provider.equals("2")) {
				status = SynInfoFacadeService.getInstance().unicomUserStatusQuery(cardNo);
			} else if (StringUtils.isNotBlank(provider) && provider.equals("3")) {
				status = SynInfoFacadeService.getInstance().telecomUserStatusQuery(cardNo,ownerPlace);
			}
			return status;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;

	}

}
